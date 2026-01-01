package com.centillion.emailservice.dtos;

import lombok.Data;

@Data
public class SendEmailDto {
    private String from;
    private String to;
    private String subject;
    private String body;
}
