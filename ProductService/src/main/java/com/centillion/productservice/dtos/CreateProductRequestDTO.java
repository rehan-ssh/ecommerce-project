package com.centillion.productservice.dtos;

import com.centillion.productservice.models.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductRequestDTO {
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private String category;

}
