package com.centillion.productservice.services;

import com.centillion.productservice.dtos.SearchRequestDto;
import com.centillion.productservice.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface SearchService {
    public Page<Product> search(String query, int pageNumber, int pageSize, String sortBy);
}
