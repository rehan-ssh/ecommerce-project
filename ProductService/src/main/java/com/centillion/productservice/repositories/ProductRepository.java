package com.centillion.productservice.repositories;

import com.centillion.productservice.models.Category;
import com.centillion.productservice.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// Table name and type of key is the argument JpaRepository takes
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product save(Product product);

    @Override
    List<Product> findAll();
}
