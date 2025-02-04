package az.bankrespublika.demoapp.service;

import az.bankrespublika.demoapp.client.CurrencyFeignClient;
import az.bankrespublika.demoapp.dao.entity.Currency;
import az.bankrespublika.demoapp.dao.entity.CurrencyReport;
import az.bankrespublika.demoapp.dao.repository.CurrencyReportRepository;
import az.bankrespublika.demoapp.dao.repository.CurrencyRepository;
import az.bankrespublika.demoapp.model.BaseResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CurrencyService {
    static final String BASE_CURRENCY = "AZN";

    AuthenticationService authenticationService;
    CurrencyFeignClient currencyFeignClient;
    CurrencyReportRepository currencyReportRepository;
    CurrencyRepository currencyRepository;

    @Transactional
    public ResponseEntity<BaseResponse<String>> fetchAndSaveCurrencies(String date) {
        try {
            // Step 1: Fetch and parse XML (получаем XML с курсами валют и парсим его)
            String xmlResponse = currencyFeignClient.getCurrencies(date);
            XmlMapper xmlMapper = new XmlMapper();
            CurrencyReport currencyReport = xmlMapper.readValue(xmlResponse, CurrencyReport.class);

            // Step 2: Establish relationships (устанавливаем связи между объектами)
            currencyReport.getCurrencyTypes().forEach(currencyType -> {
                currencyType.setCurrencyReport(currencyReport);
                currencyType.getCurrencies().forEach(currency -> currency.setCurrencyType(currencyType));
            });

            // Step 3: Replace existing data (удаляем старые данные за эту дату и сохраняем новые)
//            replaceExistingData(date, currencyReport);

            // Step 4: Return success message (возвращаем успешное сообщение)
            return BaseResponse.ok("Currencies for " + date + " successfully saved to the database with " +
                    currencyReport.getCurrencyTypes().size() + " currency types!");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to fetch and save currencies for " + date, e);
        }
    }


//    private void replaceExistingData(String date, CurrencyReport currencyReport) {
//        currencyReportRepository.findByDate(date).ifPresent(currencyReportRepository::delete);
//        currencyReportRepository.save(currencyReport);
//    }

    public ResponseEntity<BaseResponse<Map<String, BigDecimal>>> convertCurrency(String token, String fromCode, String toCode, Double amount) {
        // check login user token
        authenticationService.validateToken(token);

        Map<String, BigDecimal> response = new HashMap<>();
        response.put("convertedAmount", convert(fromCode, toCode, amount));

        return BaseResponse.ok(response);
    }

    public BigDecimal convert(String fromCode, String toCode, Double amount) {
        if (fromCode.equalsIgnoreCase(toCode)) {
            throw new IllegalArgumentException("Source currency cannot be the same as target currency.");
        }

        BigDecimal convertedAmount;
        if (BASE_CURRENCY.equalsIgnoreCase(fromCode)) {
            // Converting from base currency (AZN -> USD)
            Currency toCurrency = getCurrencyByCode(toCode);
            convertedAmount = BigDecimal.valueOf(amount / toCurrency.getValue());
        } else if (BASE_CURRENCY.equalsIgnoreCase(toCode)) {
            // Converting to base currency (USD -> AZN)
            Currency fromCurrency = getCurrencyByCode(fromCode);
            convertedAmount = BigDecimal.valueOf(amount * fromCurrency.getValue());
        } else {
            // Converting between two non-base currencies (USD -> EUR)
            Currency fromCurrency = getCurrencyByCode(fromCode);
            Currency toCurrency = getCurrencyByCode(toCode);

            Double fromCurrencyToAzn = amount * fromCurrency.getValue();
            convertedAmount = BigDecimal.valueOf((fromCurrencyToAzn / toCurrency.getValue()));
        }

        return convertedAmount;
    }

    public Currency getCurrencyByCode(String code) {
        return currencyRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid currency code: " + code));
    }
}