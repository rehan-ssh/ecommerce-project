package com.centillion.productservice.services;


import com.centillion.productservice.models.Product;

public interface ProductAIService
{
    Product createProductWithAIDescription(String name, double price,
                                           String imageUrl, String category);
}