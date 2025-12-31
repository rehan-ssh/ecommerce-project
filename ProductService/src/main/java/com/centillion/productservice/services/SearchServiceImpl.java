package com.centillion.productservice.services;

import com.centillion.productservice.dtos.SearchRequestDto;
import com.centillion.productservice.models.Product;
import com.centillion.productservice.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    ProductRepository productRepository;
    @Autowired
    public SearchServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<Product> search(String query, int pageNumber, int pageSize, String sortParam) {

        Sort sort = Sort.by(sortParam).descending()
                .and(Sort.by("price").ascending());
// if in case we accept multiple sort params
//        for(int i = 1; i <= sortParam.size(); i++){
//            sort.and(Sort.by(sortParam.get(i)).ascending());
//        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return productRepository.findByNameContaining(query, pageable);
    }
}
