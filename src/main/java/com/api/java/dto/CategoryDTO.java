package com.api.java.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/** DTO que representa una categoría de productos.
 *  Se utiliza para transportar datos entre cliente y servidor sin exponer la entidad completa. */
@Data
@AllArgsConstructor
public class CategoryDTO {

    @Schema(description = "Identificador único de la categoría", example = "1")
    private Long id;

    @Schema(description = "Nombre de la categoría que agrupa productos similares", example = "Electrónica")
    private String categoryProducts;
}
