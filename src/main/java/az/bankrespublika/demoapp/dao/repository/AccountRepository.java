package az.bankrespublika.demoapp.dao.repository;

import az.bankrespublika.demoapp.dao.entity.Account;
import az.bankrespublika.demoapp.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUser(User user);

    Optional<Account> findByAccountNoAndActiveTrue(String accountNo);
}
