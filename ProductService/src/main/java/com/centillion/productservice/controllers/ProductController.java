package com.centillion.productservice.controllers;

import com.centillion.productservice.commons.ApplicationCommons;
import com.centillion.productservice.dtos.CreateProductRequestDTO;
import com.centillion.productservice.dtos.ErrorDTO;
import com.centillion.productservice.dtos.ProductResponseDTO;
import com.centillion.productservice.exceptions.ProductNotFoundException;
import com.centillion.productservice.models.Product;
import com.centillion.productservice.services.ProductService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {

    ProductService productService;
    ApplicationCommons applicationCommons;

    ProductController(ProductService productService, ApplicationCommons applicationCommons) {
        this.productService = productService;
        this.applicationCommons = applicationCommons;
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById
            (@PathVariable Long id,
             @RequestHeader("Authorization") String token) throws ProductNotFoundException {

        applicationCommons.validateToken(token);

        
        ProductResponseDTO productResponseDTO = ProductResponseDTO.from(productService.getProductById(id));
        return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/products/")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> productDTOs = new ArrayList<>();

        productService.getAllProducts().forEach(p -> {
            productDTOs.add(
                    ProductResponseDTO.from(p)
            );
        });
        return new ResponseEntity<>(productDTOs, HttpStatus.OK);
    }

    @PostMapping("/products/")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody
                                                CreateProductRequestDTO createProductRequestDTO)
    {
        Product product = productService.createProduct(
                createProductRequestDTO.getName(),
                createProductRequestDTO.getDescription(),
                createProductRequestDTO.getPrice(),
                createProductRequestDTO.getImageUrl(),
                createProductRequestDTO.getCategory()
        );

        ProductResponseDTO productResponseDto = ProductResponseDTO.from(product);

        return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleProductNotFoundException(
            ProductNotFoundException ex) {

       ErrorDTO errorDTO = new ErrorDTO();
       errorDTO.setMessage(ex.getMessage());
       errorDTO.setStatus(HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }




}
