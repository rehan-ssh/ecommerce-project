package com.centillion.productservice.controllers;

import com.centillion.productservice.dtos.ProductResponseDTO;
import com.centillion.productservice.dtos.SearchRequestDto;
import com.centillion.productservice.models.Product;
import com.centillion.productservice.services.SearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class SearchController {
    SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/search")
    public Page<ProductResponseDTO> search(@RequestBody SearchRequestDto searchRequestDto) {
        Page<Product> productPage =   searchService.search(searchRequestDto.getQuery(),
                searchRequestDto.getPageNumber(),
                searchRequestDto.getPageSize(),
                searchRequestDto.getSortBy());

        List<ProductResponseDTO> productResponseDTOS = productPage.getContent()
                .stream().map(ProductResponseDTO::from).collect(Collectors.toList());

        return new PageImpl<>(productResponseDTOS, productPage.getPageable(), productPage.getTotalElements());
    }

}
