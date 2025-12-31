package com.centillion.productservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
public class Category extends BaseModel implements Serializable {
    private String description;
    @OneToMany(mappedBy = "category")
    @JsonIgnore // so that when someone searches for products we don't get infinite loop
    private List<Product> products;
    @OneToMany
    @JsonIgnore
    private List<Product> featuredProducts;
}
