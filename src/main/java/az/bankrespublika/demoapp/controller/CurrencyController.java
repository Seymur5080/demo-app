package az.bankrespublika.demoapp.controller;

import az.bankrespublika.demoapp.model.BaseResponse;
import az.bankrespublika.demoapp.service.CurrencyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/currency")
public class CurrencyController {
    CurrencyService currencyService;

    @GetMapping("/by-date/{date}")
    public ResponseEntity<BaseResponse<String>> getCurrenciesByDate(@PathVariable("date") String date) {
        return currencyService.fetchAndSaveCurrencies(date);
    }

    @GetMapping("/convert")
    public ResponseEntity<BaseResponse<Map<String, BigDecimal>>> convertCurrency(
            @RequestHeader("Authorization") String token,
            @RequestParam String fromCode,
            @RequestParam String toCode,
            @RequestParam Double amount) {
        return currencyService.convertCurrency(token,fromCode, toCode, amount);
    }
}

