package com.centillion.productservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductWithoutDescDto
{
    private String name;
    private double price;
    private String imageUrl;
    private String category;
}