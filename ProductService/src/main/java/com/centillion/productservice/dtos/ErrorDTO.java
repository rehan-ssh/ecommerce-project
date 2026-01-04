package com.centillion.productservice.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDTO {
    private int status;
    private String message;

    public ErrorDTO(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
