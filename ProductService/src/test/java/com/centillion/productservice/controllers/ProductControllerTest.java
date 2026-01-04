package com.centillion.productservice.controllers;

import com.centillion.productservice.commons.ApplicationCommons;
import com.centillion.productservice.dtos.CreateProductRequestDTO;
import com.centillion.productservice.models.Category;
import com.centillion.productservice.models.Product;
import com.centillion.productservice.services.ProductAIService;
import com.centillion.productservice.services.ProductService;
import com.centillion.productservice.exceptions.ProductNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @Mock
    private ApplicationCommons applicationCommons;

    @Mock
    private ProductAIService productAIService;

    @InjectMocks
    private ProductController productController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(productController)
                .setControllerAdvice(productController) // include exception handler
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetProductByIdSuccess() throws Exception {
        // Given
        Product product = new Product();
        product.setId(1L);
        product.setName("Book");
        product.setDescription("A book");
        product.setPrice(100.0);
        product.setImageUrl("url");

        Category category = new Category();
        category.setId(11L);
        category.setName("education");
        product.setCategory(category);

        when(productService.getProductById(1L)).thenReturn(product);

        // When & Then
        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Book"))
                .andExpect(jsonPath("$.description").value("A book"))
                .andExpect(jsonPath("$.price").value(100.0))
                .andExpect(jsonPath("$.imageUrl").value("url"));
        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    void testGetProductByIdNotFound() throws Exception {
        // Given
        when(productService.getProductById(2L))
                .thenThrow(new ProductNotFoundException("Product not found"));

        // When & Then
        mockMvc.perform(get("/products/2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product not found"))
                .andExpect(jsonPath("$.status").value(404));

        verify(productService).getProductById(2L);
    }

    @Test
    void testGetAllProducts() throws Exception {
        // Given
        Product p1 = new Product();
        p1.setId(1L);
        p1.setName("Book");
        Category c1 = new Category();
        c1.setId(1L);
        c1.setName("education");
        p1.setCategory(c1);

        Product p2 = new Product();
        p2.setId(2L);
        p2.setName("Pen");
        Category c2 = new Category();
        c2.setId(2L);
        c2.setName("stationery");
        p2.setCategory(c2);

        when(productService.getAllProducts()).thenReturn(Arrays.asList(p1, p2));

        // When & Then
        mockMvc.perform(get("/products/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));

        verify(productService).getAllProducts();
    }

    @Test
    void testCreateProductSuccess() throws Exception {
        // Given
        CreateProductRequestDTO request = new CreateProductRequestDTO();
        request.setName("Book");
        request.setDescription("A book");
        request.setPrice(100.0);
        request.setImageUrl("url");
        request.setCategory("education");


        Category category = new Category();
        category.setId(10L);
        category.setName("education");

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("Book");
        savedProduct.setDescription("A book");
        savedProduct.setPrice(100.0);
        savedProduct.setImageUrl("url");
        savedProduct.setCategory(category);

        when(productService.createProduct(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getImageUrl(),
                request.getCategory()
        )).thenReturn(savedProduct);

        // Mock the token validation to do nothing (valid token)
        doNothing().when(applicationCommons).validateToken(anyString());

        // When & Then
        mockMvc.perform(post("/products/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer valid-token")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Book"))
                .andExpect(jsonPath("$.category").value("education"));

        verify(productService, times(1))
                .createProduct(
                        request.getName(),
                        request.getDescription(),
                        request.getPrice(),
                        request.getImageUrl(),
                        request.getCategory()
                );
    }
}
