package com.api.java.repositories;

import com.api.java.models.ERole;
import com.api.java.models.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/** Repositorio JPA para la entidad RoleModel.
 * Gestiona el acceso a roles definidos en el sistema */
@Repository
public interface IRoleRepository extends JpaRepository<RoleModel, Long> {

    /** Busca un rol por su enumeración ERole.
     * @param name nombre del rol (ej: ADMIN, USER)
     * @return un Optional con el rol correspondiente*/

    Optional<RoleModel> findByName(ERole name);
}
