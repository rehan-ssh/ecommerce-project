package com.centillion.productservice.commons;


import com.centillion.productservice.repositories.CategoryRepository;
import com.centillion.productservice.repositories.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ApplicationCommons {
    RestTemplate restTemplate;
    public ApplicationCommons(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

    }
    public void validateToken(String token) {
        if(token == null || token.isEmpty()) {
            throw new RuntimeException("Token is null or empty");
        }

        String url = "http://UserService/users/validate/" + token;
        Boolean tokenIsValid = restTemplate.getForObject(url, Boolean.class);

        if(Boolean.FALSE.equals(tokenIsValid)) {
            throw new RuntimeException("Invalid token");
        }

    }
}
