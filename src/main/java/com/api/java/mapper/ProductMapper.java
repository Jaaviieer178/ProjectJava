package com.api.java.mapper;
import com.api.java.dto.ProductDTO;
import com.api.java.models.ProductModel;
import org.mapstruct.*;


@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface ProductMapper {
    @Mapping(source = "categoryProduct.id", target = "categoryId")
    ProductDTO productToProductDto(ProductModel productModel);

    @Named("productDtoToProduct")
    @Mapping(source = "categoryId", target = "categoryProduct.id")
    ProductModel productDtoToProduct(ProductDTO productDTO);

    @InheritConfiguration(name = "productDtoToProduct")
    @Mapping(target = "id", ignore = true)
    void updateProductFromDto(ProductDTO dto, @MappingTarget ProductModel entity);


}