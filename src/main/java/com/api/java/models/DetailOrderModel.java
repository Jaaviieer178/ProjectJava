package com.api.java.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

/** Entidad JPA que representa un detalle de una orden.
 * Cada instancia corresponde a un producto solicitado por un usuario, con su cantidad y estado de procesamiento.*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "detailOrder")
public class DetailOrderModel {
    /** Identificador único del detalle de la orden.
     * Se genera automáticamente por la base de datos.*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Usuario que realizó la orden.
     * Relación many-to-one, ya que un usuario puede tener múltiples órdenes.
     * Se carga de forma perezosa para eficiencia. */

    @NotNull(message = "El usuario de la orden no puede ser nulo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserModel userOrder;

    /** Producto asociado a esta orden.
     * Relación many-to-one; un producto puede estar en múltiples órdenes.*/

    @NotNull(message = "El producto no puede ser nulo")
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private ProductModel productOrder;

    /** Cantidad solicitada del producto.
     * Debe ser al menos 1.*/

    @Column
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "Debe ordenar al menos una unidad")
    private Integer amount;

    /** Estado actual de este ítem en la orden.
     * Puede ser PENDING, PAID o CANCELLED. */

    @Column
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    /** Nombre del producto en el momento de la compra (desnormalizado). */

    @Column(nullable = false)
    private String nameProductSnapshot;

    /** Precio unitario del producto en el momento de la compra (desnormalizado). */

    @Column(nullable = false)
    private BigDecimal priceUnitSnapshot;


}