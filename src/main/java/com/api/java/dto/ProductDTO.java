package com.api.java.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductDTO {
    private Long id;
    private String nameProduct;
    private String descriptionProduct;
    private Float priceProduct;
    private Long categoryId;
    private Integer stockProduct;
}
