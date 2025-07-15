package com.api.java.dto;
import com.api.java.models.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class DetailOrderDTO {

    @Schema(description = "ID único del detalle de la orden", example = "1001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "ID del usuario que realizó la orden", example = "42")
    @NotNull(message = "El ID del usuario no puede ser nulo")
    private Long UserOrder;

    @Schema(description = "Producto incluido en la orden")
    @NotNull(message = "El producto no puede ser nulo")
    private ProductDTO productOrder;

    @Schema(description = "Cantidad de unidades solicitadas del producto", example = "3")
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "Debe solicitarse al menos una unidad")
    private Integer amount;

    @Schema(description = "Estado de la orden", example = "PENDING")
    @NotNull(message = "El estado de la orden es obligatorio")
    private OrderStatus status;

    @Schema(description = "Nombre del producto en el momento de la compra", example = "Auriculares Bluetooth")
    private String nombreProductoSnapshot;

    @Schema(description = "Precio unitario del producto en el momento de la compra", example = "18999.50")
    private BigDecimal precioUnitarioSnapshot;
}
