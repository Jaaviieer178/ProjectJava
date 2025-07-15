package com.api.java.mapper;
import com.api.java.dto.ProductDTO;
import com.api.java.models.ProductModel;
import org.mapstruct.*;


/** Mapper de MapStruct responsable de convertir entre entidades ProductModel y DTOs ProductDTO.
 *  Está registrado como componente Spring y reutiliza el CategoryMapper para conversiones anidadas si fuese necesario.*/

@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface ProductMapper {

    /** Convierte una entidad ProductModel a su representación DTO.
     * Extrae el ID de la categoría relacionada y lo asigna al campo categoryId del DTO.
     * @param productModel entidad JPA del producto
     * @return DTO con los datos del producto*/

    @Mapping(source = "categoryProduct.id", target = "categoryId")
    ProductDTO productToProductDto(ProductModel productModel);

    /** Convierte un DTO en una entidad ProductModel.
     * Asigna el campo categoryId del DTO a la relación categoryProduct.id de la entidad.
     * Se utiliza también como configuración base para el metodo de actualización.
     * @param productDTO DTO de entrada
     * @return entidad JPA construida a partir del DTO */

    @Named("productDtoToProduct")
    @Mapping(source = "categoryId", target = "categoryProduct.id")
    ProductModel productDtoToProduct(ProductDTO productDTO);

    /** Actualiza una entidad ProductModel existente con los datos del DTO.
     * Reutiliza la configuración de mapeo definida en "productDtoToProduct" para mantener consistencia.
     * Ignora el campo ID para evitar modificar la clave primaria accidentalmente.
     * @param dto     DTO con los nuevos valores
     * @param entity  entidad que será modificada (target) */

    @InheritConfiguration(name = "productDtoToProduct")
    @Mapping(target = "id", ignore = true)
    void updateProductFromDto(ProductDTO dto, @MappingTarget ProductModel entity);
}