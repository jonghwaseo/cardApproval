package com.jjong.cardApproval.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {

    private String message;
    private String code;
    private int status;
    private String detail;

    public ErrorResponse(ErrorCode code, String eMessage) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.code = code.getCode();
        this.detail = eMessage;
    }

    public static ErrorResponse of(ErrorCode code, String eMessage) {
        return new ErrorResponse(code, eMessage);
    }
}
