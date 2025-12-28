package com.centillion.productservice.services;

import com.centillion.productservice.exceptions.ProductNotFoundException;
import com.centillion.productservice.models.Category;
import com.centillion.productservice.models.Product;
import com.centillion.productservice.repositories.CategoryRepository;
import com.centillion.productservice.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductDBService implements ProductService {

    private CategoryRepository categoryRepository;
    ProductRepository productRepository;

    public ProductDBService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product getProductById(Long id) throws ProductNotFoundException {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product Not Found with id: " + id));
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


}
