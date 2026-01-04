package com.centillion.productservice.services;

import com.centillion.productservice.exceptions.ProductNotFoundException;
import com.centillion.productservice.models.Category;
import com.centillion.productservice.models.Product;
import com.centillion.productservice.repositories.CategoryRepository;
import com.centillion.productservice.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// method_conditionname_expectedoutput this format is used
@ExtendWith(MockitoExtension.class)
class ProductDBServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @Mock
    private ChatClient chatClient;

    @InjectMocks
    private ProductDBService productDBService;

    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        product = new Product();
        product.setId(1L);
        product.setName("Phone");
        product.setDescription("Smartphone");
        product.setPrice(50000);
        product.setImageUrl("img.png");
        product.setCategory(category);

        // Mock Redis operations with lenient to avoid UnnecessaryStubbingException
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    // ---------- getProductById ----------

    @Test
    void getProductById_success() throws ProductNotFoundException {
        // given
        when(valueOperations.get("products:1")).thenReturn(null); // Cache miss
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        // when
        Product result = productDBService.getProductById(1L);

        // then
        assertNotNull(result);
        assertEquals("Phone", result.getName());
        verify(valueOperations).get("products:1");
        verify(productRepository).findById(1L);
        verify(valueOperations).set("products:1", product);
    }

    @Test
    void getProductById_fromCache_success() throws ProductNotFoundException {
        // given
        when(valueOperations.get("products:1")).thenReturn(product); // Cache hit

        // when
        Product result = productDBService.getProductById(1L);

        // then
        assertNotNull(result);
        assertEquals("Phone", result.getName());
        verify(valueOperations).get("products:1");
        verify(productRepository, never()).findById(any()); // Should not hit DB
        verify(valueOperations, never()).set(anyString(), any()); // Should not update cache
    }

    @Test
    void getProductById_notFound_throwsException() {
        // given
        when(valueOperations.get("products:1")).thenReturn(null); // Cache miss
        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        // when + then
        ProductNotFoundException ex = assertThrows(
                ProductNotFoundException.class,
                () -> productDBService.getProductById(1L)
        );

        assertEquals("Product Not Found with id: 1", ex.getMessage());
        verify(valueOperations).get("products:1");
        verify(productRepository).findById(1L);
    }

    // ---------- getAllProducts ----------

    @Test
    void getAllProducts_returnsProducts() {
        // given
        when(productRepository.findAll())
                .thenReturn(List.of(product));

        // when
        List<Product> products = productDBService.getAllProducts();

        // then
        assertEquals(1, products.size());
        assertEquals("Phone", products.get(0).getName());
        verify(productRepository).findAll();
    }

    // ---------- createProduct ----------

    @Test
    void createProduct_usesExistingCategory() {
        // given
        when(categoryRepository.findByName("Electronics"))
                .thenReturn(Optional.of(category));

        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Product saved = productDBService.createProduct(
                "Laptop",
                "Gaming Laptop",
                90000,
                "laptop.png",
                "Electronics"
        );

        // then
        assertEquals("Laptop", saved.getName());
        assertEquals(category, saved.getCategory());

        verify(categoryRepository).findByName("Electronics");
        verify(categoryRepository, never()).save(any());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void createProduct_createsNewCategoryIfNotExists() {
        // given
        when(categoryRepository.findByName("Books"))
                .thenReturn(Optional.empty());

        when(categoryRepository.save(any(Category.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Product saved = productDBService.createProduct(
                "Book",
                "Spring Boot Guide",
                999,
                "book.png",
                "Books"
        );

        // then
        assertEquals("Books", saved.getCategory().getName());

        verify(categoryRepository).findByName("Books");
        verify(categoryRepository).save(any(Category.class));
        verify(productRepository).save(any(Product.class));
    }
}