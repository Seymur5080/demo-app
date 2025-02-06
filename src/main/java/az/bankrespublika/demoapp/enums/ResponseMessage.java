package az.bankrespublika.demoapp.enums;

import lombok.Getter;

@Getter
public enum ResponseMessage {
    SUCCESS("success");

    private final String message;

    ResponseMessage(String message) {
        this.message = message;
    }
}
