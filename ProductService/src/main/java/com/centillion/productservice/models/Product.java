package com.centillion.productservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Product {
    private long id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private Category category;
}
