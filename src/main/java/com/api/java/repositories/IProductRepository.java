package com.api.java.repositories;
import com.api.java.models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Repositorio JPA para la entidad ProductModel.
 * Extiende JpaRepository para proporcionar operaciones CRUD estándar, además de consultas personalizadas definidas por nombre de metodo. */
@Repository
public interface IProductRepository extends JpaRepository<ProductModel, Long> {

    /**  Busca productos que coincidan exactamente con el nombre proporcionado.
     * @param nameProduct nombre exacto del producto a buscar
     * @return lista de productos cuyo nombre coincide*/

    List<ProductModel> findByNameProduct(String nameProduct);

    /** Busca todos los productos pertenecientes a una categoría específica, identificada por su ID.
     * @param categoryId ID de la categoría asociada
     * @return lista de productos pertenecientes a esa categoría */

    List<ProductModel> findByCategoryProduct_Id(Long categoryId);
}
