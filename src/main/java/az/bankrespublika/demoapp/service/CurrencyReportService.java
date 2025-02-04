package az.bankrespublika.demoapp.service;


import az.bankrespublika.demoapp.dao.entity.CurrencyReport;
import az.bankrespublika.demoapp.dao.repository.CurrencyReportRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CurrencyReportService {
    CurrencyReportRepository currencyReportRepository;

    @Transactional
    public void saveOrUpdate(CurrencyReport currencyReport) {
        if (currencyReportRepository.existsByDate(currencyReport.getDate())) {
            currencyReportRepository.deleteByDate(currencyReport.getDate());
        }

        currencyReportRepository.save(currencyReport);
    }
}
