package az.bankrespublika.demoapp.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static az.bankrespublika.demoapp.enums.ResponseMessage.SUCCESS;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseResponse<T> {
    HttpStatus status;
    Integer code;
    String message;
    T data;
    LocalDateTime timestamp;
    String traceId;

    public static <T> BaseResponse<T> buildResponse(HttpStatus status, String message, T data) {
        return buildResponse(status.value(), status, message, data);
    }

    public static <T> BaseResponse<T> buildResponse(Integer statusCode, String message, T data) {
        return buildResponse(statusCode, HttpStatus.valueOf(statusCode), message, data);
    }

    private static <T> BaseResponse<T> buildResponse(Integer statusCode, HttpStatus status, String message, T data) {
        return BaseResponse.<T>builder()
                .status(status)
                .code(statusCode)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS))
                .traceId(UUID.randomUUID() + "-" + System.currentTimeMillis())
                .build();
    }

    public ResponseEntity<BaseResponse<T>> toResponseEntity() {
        return new ResponseEntity<>(this, this.status);
    }

    public static ResponseEntity<BaseResponse<Void>> ok() {
        return BaseResponse.<Void>buildResponse(HttpStatus.OK, SUCCESS, null).toResponseEntity();
    }

    public static <T> ResponseEntity<BaseResponse<T>> ok(T data) {
        return BaseResponse.buildResponse(HttpStatus.OK, SUCCESS, data).toResponseEntity();
    }

    public static ResponseEntity<BaseResponse<Void>> success(HttpStatus status) {
        return BaseResponse.<Void>buildResponse(status, SUCCESS, null).toResponseEntity();
    }

    public static <T> ResponseEntity<BaseResponse<T>> success(HttpStatus status, T data) {
        return BaseResponse.buildResponse(status, SUCCESS, data).toResponseEntity();
    }
}