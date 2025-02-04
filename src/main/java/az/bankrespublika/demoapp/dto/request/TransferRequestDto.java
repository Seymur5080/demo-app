package az.bankrespublika.demoapp.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransferRequestDto {
    String fromAccountNo;
    String toAccountNo;
    Double amount;
}
