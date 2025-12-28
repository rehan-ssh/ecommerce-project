package com.centillion.productservice.dtos;

import com.centillion.productservice.models.Category;
import com.centillion.productservice.models.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDTO {
    private long id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private String category;

    public static ProductResponseDTO from(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.id = product.getId();
        dto.name = product.getName();
        dto.description = product.getDescription();
        dto.price = product.getPrice();
        dto.imageUrl = product.getImageUrl();
        dto.category = product.getCategory().getName();
        return dto;
    }
}
