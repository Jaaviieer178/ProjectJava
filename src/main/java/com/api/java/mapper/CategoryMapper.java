package com.api.java.mapper;

import com.api.java.dto.CategoryDTO;
import com.api.java.models.CategoryModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO categoryToCategoryDto(CategoryModel categoryModel);
    CategoryModel categoryDtoToCategory (CategoryDTO categoryDTO);
}
