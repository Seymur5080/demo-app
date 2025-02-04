package az.bankrespublika.demoapp.dao.repository;


import az.bankrespublika.demoapp.dao.entity.CurrencyType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyTypeRepository extends JpaRepository<CurrencyType, Long> {

}