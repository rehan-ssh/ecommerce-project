package com.centillion.productservice.services;

import com.centillion.productservice.exceptions.ProductNotFoundException;
import com.centillion.productservice.models.Category;
import com.centillion.productservice.models.Product;
import com.centillion.productservice.repositories.CategoryRepository;
import com.centillion.productservice.repositories.ProductRepository;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductDBService implements ProductService, ProductAIService {

    private CategoryRepository categoryRepository;
    ProductRepository productRepository;
    ChatClient chatClient;
    RedisTemplate<String, Object> redisTemplate;


    public ProductDBService(ProductRepository productRepository,
                            CategoryRepository categoryRepository,
                            RedisTemplate<String, Object> redisTemplate,
                            ChatClient chatClient) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.redisTemplate = redisTemplate;
        this.chatClient = chatClient;

    }

    @Override
    public Product getProductById(Long id) throws ProductNotFoundException {
        Product product = (Product)redisTemplate.opsForValue().get("products:" + id);
        if(product != null){
            return product;
        }
        product =  productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product Not Found with id: " + id));
        redisTemplate.opsForValue().set("products:" + id, product);
        return product;
    }


    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(String name, String description, double price,
                                 String imageUrl, String category) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setImageUrl(imageUrl);

        Category categoryObj = getCategoryFromDB(category);

        product.setCategory(categoryObj);
        return productRepository.save(product);
    }

    private Category getCategoryFromDB(String name){

        Optional<Category> optionalCategory = categoryRepository.findByName(name);
        if(optionalCategory.isPresent()){
            return optionalCategory.get();
        }

        Category categoryObj = new Category();
        categoryObj.setName(name);
        return categoryRepository.save(categoryObj);
    }


    @Override
    public Product createProductWithAIDescription(String name, double price, String imageUrl, String category)
    {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setImageUrl(imageUrl);

        Category categoryObj = getCategoryFromDB(category);
        product.setCategory(categoryObj);

        String description = getDescriptionFromAI(product);
        product.setDescription(description);
        System.out.println(description);

        return productRepository.save(product);
    }

    //TODO: read prompts from resources
    private String getDescriptionFromAI(Product product)
    {
        String userPrompt = String.format(
                "Generate a 250 characters professional marketing description for a %s product named '%s'. " +
                        "Key features: Priced at $%.2f, Category: %s. " +
                        "Focus on benefits and unique selling points. Avoid technical jargon. Use markdown formatting.",
                product.getCategory().getName().toLowerCase(),
                product.getName(),
                product.getPrice(),
                product.getCategory().getName()
        );

        return chatClient.prompt()
                .system("You are a professional marketing copywriter. Write clear, engaging, persuasive content in markdown format. Stay concise and avoid technical jargon.")
                .user(userPrompt)
                .call()
                .content();

    }
}
