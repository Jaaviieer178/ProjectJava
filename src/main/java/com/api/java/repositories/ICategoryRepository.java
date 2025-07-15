package com.api.java.repositories;
import com.api.java.models.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Repositorio de acceso a datos para la entidad {@link CategoryModel}.
 *
 * Extiende {@link JpaRepository} para proporcionar operaciones CRUD y búsqueda por campos específicos. */

@Repository
public interface ICategoryRepository extends JpaRepository<CategoryModel, Long> {

    /** Busca una o más categorías cuyo nombre o etiqueta coincida exactamente con el valor proporcionado.
     *
     * @param categoryProducts nombre o descripción de la categoría a buscar
     * @return lista de categorías que coinciden con el texto especificado */

    List<CategoryModel> findByCategoryProducts(String categoryProducts);
}
