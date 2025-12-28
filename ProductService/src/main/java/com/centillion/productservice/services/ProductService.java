package com.centillion.productservice.services;

import com.centillion.productservice.exceptions.ProductNotFoundException;
import com.centillion.productservice.models.Category;
import com.centillion.productservice.models.Product;

import java.util.List;

public interface ProductService {
    Product getProductById(Long id) throws ProductNotFoundException;
    List<Product> getAllProducts();
    Product createProduct(String name, String description, double price, String imageUrl, String category);
}
