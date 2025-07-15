package com.api.java.repositories;

import com.api.java.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/** Repositorio JPA para operaciones con la entidad UserModel.
 * Extiende JpaRepository para proveer funcionalidades CRUD automáticas.*/
@Repository
public interface IUserRepository extends JpaRepository<UserModel, Long> {

    /** Busca un usuario por su email.
     * @param email correo electrónico del usuario
     * @return un Optional con el usuario, si existe
     * Este caso sirve por si deseamos agregar como tipo de Usuario al correo ademas del alias creado*/

    Optional<UserModel> findByEmail(String email);

    /** Busca un usuario por su nombre de usuario.
     * @param username nombre de usuario a buscar
     * @return un Optional con el usuario, si existe*/

    Optional<UserModel> findByUsername(String username);
}
