package az.bankrespublika.demoapp.dao.repository;


import az.bankrespublika.demoapp.dao.entity.CurrencyReport;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyReportRepository extends JpaRepository<CurrencyReport, Long> {
    Optional<CurrencyReport> findByDate(String date);

    boolean existsByDate(String date);

    @Transactional
    void deleteByDate(String date);
}
