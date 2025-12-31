package com.centillion.paymentservice.services;

import com.centillion.paymentservice.services.PaymentGateway;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Primary
public class StripeGateway implements PaymentGateway
{
    @Value("${stripe.api.key}")
    private String apiKey;

    @Override
    public String generatePaymentLink()
    {
        try {
            Stripe.apiKey = this.apiKey;
            // Generate an idempotency key (for example: UUID)
            // this key should have order id and user id hash
            String idempotencyKey = UUID.randomUUID().toString();

            // Pass idempotency key in RequestOptions
            RequestOptions requestOptions = RequestOptions.builder()
                    .setIdempotencyKey(idempotencyKey)
                    .build();

            Price price = getPrice();


            PaymentLinkCreateParams params =
                    PaymentLinkCreateParams.builder()
                            .addLineItem(
                                    PaymentLinkCreateParams.LineItem.builder()
                                            .setPrice(price.getId())
                                            .setQuantity(1L)
                                            .build()
                            ).setAfterCompletion(PaymentLinkCreateParams.AfterCompletion.builder()
                                    .setType(PaymentLinkCreateParams.AfterCompletion.Type.REDIRECT)
                                    .setRedirect(PaymentLinkCreateParams.AfterCompletion.Redirect.builder()
                                            .setUrl("https://google.com/?trx_id=" + "abcd1234").build()).build())
                            .build();
            PaymentLink paymentLink = PaymentLink.create(params, requestOptions);
            return paymentLink.getUrl();
        }catch (StripeException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    private Price getPrice() {
        try {
            String idempotencyKey = UUID.randomUUID().toString();

            // Pass idempotency key in RequestOptions
            RequestOptions requestOptions = RequestOptions.builder()
                    .setIdempotencyKey(idempotencyKey)
                    .build();
            PriceCreateParams params =
                    PriceCreateParams.builder()
                            .setCurrency("inr")
                            .setUnitAmount(200000L)
                            .setProductData(
                                    PriceCreateParams
                                            .ProductData
                                            .builder()
                                            .setName("iPhone")
                                            .build()
                            )
                            .build();
            return Price.create(params, requestOptions);
        }catch (StripeException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }
}