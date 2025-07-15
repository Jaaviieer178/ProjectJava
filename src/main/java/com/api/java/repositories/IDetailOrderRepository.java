package com.api.java.repositories;

import com.api.java.models.DetailOrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Repositorio JPA para la entidad DetailOrderModel.
 * Proporciona operaciones CRUD estándar y una consulta personalizada
 * para obtener los detalles de órdenes realizadas por un usuario específico.*/
@Repository
public interface IDetailOrderRepository extends JpaRepository<DetailOrderModel, Long> {

    /** Obtiene una lista de detalles de orden filtrados por el ID del usuario que realizó la compra.
     *
     * @param userOrderId ID del usuario
     * @return lista de detalles de orden asociados al usuario especificado */

    List<DetailOrderModel> findByUserOrder_Id(Long userOrderId);
}
