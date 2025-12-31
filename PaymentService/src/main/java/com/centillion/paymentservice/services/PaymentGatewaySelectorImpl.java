package com.centillion.paymentservice.services;

import org.springframework.stereotype.Service;

@Service
public class PaymentGatewaySelectorImpl implements PaymentGatewaySelector{

    private final PaymentGateway stripeStrategy;
    private final PaymentGateway razorpayStrategy;
//    private final GatewayHealthCheck stripeHealth;
//    private final GatewayHealthCheck razorpayHealth;

    public PaymentGatewaySelectorImpl(
            PaymentGateway stripeStrategy,
            PaymentGateway razorpayStrategy
    ) {
        this.stripeStrategy = stripeStrategy;
        this.razorpayStrategy = razorpayStrategy;
    }

    public PaymentGateway getPaymentGateway() {
//        if (stripeHealth.isHealthy()) {
//            return stripeStrategy;
//        }
//        if (razorpayHealth.isHealthy()) {
//            return razorpayStrategy;
//        }
//        throw new RuntimeException("No payment gateway available");
        return stripeStrategy;
    }
}
