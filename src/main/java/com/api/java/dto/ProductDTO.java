package com.api.java.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/** DTO que representa los datos de un producto transferidos entre cliente y servidor. */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    /** ID único del producto. */
    @Schema(description = "ID del producto. Se genera automáticamente", example = "101", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    /** Nombre del producto. */
    @Schema(description = "Nombre del producto", example = "Auriculares Bluetooth")
    @NotBlank(message = "El nombre del producto no puede estar vacío")
    private String nameProduct;

    /** Descripción del producto. */
    @Schema(description = "Descripción breve del producto", example = "Auriculares inalámbricos con cancelación de ruido")
    @NotBlank(message = "La descripción no puede estar vacía")
    private String descriptionProduct;

    /** Precio unitario del producto. */
    @Schema(description = "Precio del producto en pesos argentinos", example = "18999.50")
    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser un valor positivo")
    private Float priceProduct;

    /** ID de la categoría a la que pertenece el producto. */
    @Schema(description = "ID de la categoría asociada al producto", example = "3")
    @NotNull(message = "La categoría es obligatoria")
    private Long categoryId;

    /** Cantidad de unidades disponibles.*/
    @Schema(description = "Cantidad de productos disponibles en stock", example = "25")
    @NotNull(message = "El stock no puede ser nulo")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stockProduct;

    /** Indica si el producto está activo y disponible para la venta.
     * Se utiliza para aplicar borrado lógico.*/
    @Schema(description = "Indica si el producto está activo y disponible para la venta", example = "true")
    private boolean activo;

}