package com.api.java.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** Entidad JPA que representa un producto en la base de datos.
 * Cada instancia corresponde a un registro en la tabla "product".*/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class ProductModel {
    /**  Identificador único del producto (clave primaria).
     * Se genera automáticamente por la base de datos.*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre del producto.
     * No puede estar en blanco y debe ser único.*/

    @NotBlank(message = "El nombre del producto no puede estar vacío")
    @Column(unique = true)
    private String nameProduct;

    /** Descripción opcional del producto.
     * Se limita a 600 caracteres como máximo. */

    @Size(max = 600, message = "La descripción no puede superar los 600 caracteres")
    @Column
    private String descriptionProduct;

    /** Precio del producto.
     * No puede ser nulo y debe ser un valor positivo igual o mayor a 0.00.*/

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.00", message = "El precio debe ser igual o mayor a 0.00")
    @Column
    private BigDecimal priceProduct;

    /** Categoría a la que pertenece el producto.
     * Relación Many-to-One con CategoryModel.
     * Se carga de forma perezosa para optimizar el rendimiento.*/

    @NotNull(message = "La categoría es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private CategoryModel categoryProduct;

    /** Cantidad disponible del producto en stock.
     *  No puede ser nulo ni tener un valor negativo. */

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column
    private Integer stockProduct;

    /** Indica si el producto está disponible para la venta.
     * Se usa para aplicar borrado lógico y preservar integridad referencial. */

    @Column(nullable = false)
    private boolean activo = true;

    /** Lista de detalles de orden en los que aparece este producto.
     * Se elimina la cascada y el orphanRemoval para preservar el historial de pedidos. */

    @OneToMany(mappedBy = "productOrder")
    private List<DetailOrderModel> detailOrders = new ArrayList<>();
}
