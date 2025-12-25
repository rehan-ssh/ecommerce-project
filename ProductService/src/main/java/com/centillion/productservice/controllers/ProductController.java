package com.centillion.productservice.controllers;

import com.centillion.productservice.dtos.ProductResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {


    @GetMapping("/products/{id}")
    public ProductResponseDTO getProductById(long id) {

        ProductResponseDTO dummyProductDTO = new ProductResponseDTO();
        dummyProductDTO.setId(id);
        dummyProductDTO.setCategory("category");
        dummyProductDTO.setName("name");
        dummyProductDTO.setDescription("description");
        dummyProductDTO.setImageUrl("imageUrl");

        return dummyProductDTO;
    }
}
