package az.bankrespublika.demoapp.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponseDto {
    String accountNo;
    String currency;
    BigDecimal balance;
    LocalDateTime creationDate;
    LocalDateTime expirationDate;
    Boolean active;
    String username;
}
