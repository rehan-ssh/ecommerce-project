package com.centillion.paymentservice.services;


// this can be named as PaymentGatewayStrategy and then StripeGatewayStrategy
// kept the name short here
public interface PaymentGateway {
    String generatePaymentLink();
}
