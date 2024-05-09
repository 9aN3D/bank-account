package com.techbank.account.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ErrorResponse extends BaseResponse {

    private Date timestamp;

    private int status;

    private String error;

    public ErrorResponse(String message, Date timestamp, int status, String error) {
        super(message);
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
    }

}
