package com.api.java.mapper;
import com.api.java.dto.CategoryDTO;
import com.api.java.models.CategoryModel;
import org.mapstruct.Mapper;

/** Mapper de MapStruct encargado de convertir entre la entidad {@link CategoryModel} y el DTO {@link CategoryDTO}.
 * Este mapper se utiliza para separar la lógica de conversión entre capas,
 * manteniendo la arquitectura limpia y desacoplada. */

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    /** Convierte una entidad {@link CategoryModel} a un DTO {@link CategoryDTO}.
     * @param categoryModel la entidad de categoría proveniente de la base de datos
     * @return una instancia de {@link CategoryDTO} con los datos equivalentes */

    CategoryDTO categoryToCategoryDto(CategoryModel categoryModel);

    /** Convierte un DTO {@link CategoryDTO} a una entidad {@link CategoryModel}, normalmente para operaciones de creación o actualización.
     * @param categoryDTO el objeto recibido desde el cliente
     * @return una instancia de {@link CategoryModel} lista para persistencia */

    CategoryModel categoryDtoToCategory(CategoryDTO categoryDTO);
}