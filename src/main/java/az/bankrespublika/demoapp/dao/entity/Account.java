package az.bankrespublika.demoapp.dao.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "account_no", unique = true, length = 16)
    String accountNo;

    @Column(name = "currency", nullable = false)
    String currency;

    @Column(name = "balance", precision = 19, scale = 2)
    BigDecimal balance;

    @Column(name = "creation_date")
    LocalDateTime creationDate;

    @Column(name = "expiration_date")
    LocalDateTime expirationDate;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @PrePersist
    private void prePersist() {
        accountNo = generateUniqueAccountNo();
        active = true;
        balance = BigDecimal.valueOf(0.0);
        creationDate = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
    }

    private String generateUniqueAccountNo() {
        Random random = new Random();
        StringBuilder accountNumber = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            accountNumber.append(random.nextInt(10));
        }

        return accountNumber.toString();
    }
}
