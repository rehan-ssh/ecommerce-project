package com.centillion.paymentservice.services;

import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    PaymentGatewaySelector paymentGatewaySelector;

    public PaymentServiceImpl(PaymentGatewaySelector paymentGatewaySelector) {
        this.paymentGatewaySelector = paymentGatewaySelector;
    }

    @Override
    public String initiatePayment() {
        return paymentGatewaySelector.getPaymentGateway().generatePaymentLink();
    }
}
