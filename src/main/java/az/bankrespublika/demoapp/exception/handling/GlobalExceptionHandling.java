package az.bankrespublika.demoapp.exception.handling;

import az.bankrespublika.demoapp.exception.BadRequestException;
import az.bankrespublika.demoapp.exception.NotFoundException;
import az.bankrespublika.demoapp.model.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandling {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return BaseResponse.buildResponse(HttpStatus.BAD_REQUEST,
                "Validation error", errors).toResponseEntity();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<BaseResponse<Void>> handleException(NotFoundException exception) {
        return BaseResponse.<Void>buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), null)
                .toResponseEntity();
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BaseResponse<Void>> handleException(BadRequestException exception) {
        return BaseResponse.<Void>buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), null)
                .toResponseEntity();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse<Void>> handleException(IllegalArgumentException exception) {
        return BaseResponse.<Void>buildResponse(HttpStatus.CONFLICT, exception.getMessage(), null)
                .toResponseEntity();
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<BaseResponse<Void>> handleException(IllegalStateException exception) {
        return BaseResponse.<Void>buildResponse(HttpStatus.CONFLICT, exception.getMessage(), null)
                .toResponseEntity();
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<BaseResponse<Void>> handleException(SQLException exception) {
        return BaseResponse.<Void>buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), null)
                .toResponseEntity();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Void>> handleException(Exception exception) {
        return BaseResponse.<Void>buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), null)
                .toResponseEntity();
    }
}


