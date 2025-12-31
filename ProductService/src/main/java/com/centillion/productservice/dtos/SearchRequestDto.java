package com.centillion.productservice.dtos;


import lombok.Data;

@Data
public class SearchRequestDto {
    private String query;
    private int pageNumber;
    private int pageSize;
    private String sortBy;
}
