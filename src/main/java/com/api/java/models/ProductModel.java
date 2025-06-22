package com.api.java.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="product")
public class ProductModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column( unique = true)
    private String nameProduct;

    @Column
    @Size(max = 600)
    private String descriptionProduct;

    @Column
    @NotNull
    @DecimalMin(value = "0.00")
    private BigDecimal priceProduct;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)//Como product esta relacionado a 1 de las varias categorias que tiene se usa ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private CategoryModel categoryProduct;

    @Column
    @NotNull
    @Min(0)
    private Integer stockProduct;

}
