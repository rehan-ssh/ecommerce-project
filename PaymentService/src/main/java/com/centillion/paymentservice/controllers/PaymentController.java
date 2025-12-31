package com.centillion.paymentservice.controllers;


import com.centillion.paymentservice.services.PaymentService;
import com.centillion.paymentservice.services.PaymentServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payments")
    public String initiatePayment(){
        return paymentService.initiatePayment();
    }
}
