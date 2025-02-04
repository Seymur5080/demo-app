package az.bankrespublika.demoapp.dao.repository;


import az.bankrespublika.demoapp.dao.entity.Currency;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    @Query(value = "SELECT c.* FROM currencies c " +
            "JOIN currency_types ct ON c.currency_type_id = ct.id " +
            "JOIN currency_reports cr ON ct.currency_report_id = cr.id " +
            "WHERE c.code = :code " +
            "AND TO_DATE(cr.date, 'DD.MM.YYYY') <= CURRENT_DATE " +
            "ORDER BY cr.date DESC " +
            "LIMIT 1", nativeQuery = true)
    Optional<Currency> findByCode(@Param("code") String code);
}
