package az.bankrespublika.demoapp.dao.repository;

import az.bankrespublika.demoapp.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Optional -> To avoid getting zero
    Optional<User> findByUsername(String username);

    Optional<User> findByToken(String token);
}
