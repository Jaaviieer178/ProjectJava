package com.api.java.models;

import jakarta.persistence.*;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/** Entidad JPA que representa un usuario en la base de datos. */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class UserModel implements UserDetails {
    /** ID único autogenerado del usuario. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Número de DNI, debe ser único y no nulo.   */
    @Column(unique = true, nullable = false, name = "dni")
    @NotNull(message = "El DNI es obligatorio")
    @Digits(integer = 8, fraction = 0, message = "El DNI debe contener hasta 8 dígitos sin decimales")
    private Integer dni;

    /** Alias del usuario dentro del sistema. Debe ser único y obligatorio. */
    @Column(unique = true, nullable = false, name = "username")
    private String username;

    /** Apellido del usuario, obligatorio. */
    @Column(nullable = false, name = "lastname")
    private String lastname;

    /** Nombre del usuario, obligatorio. */
    @Column(nullable = false, name = "firstname")
    private String firstname;

    /** Correo electrónico válido y único del usuario. */
    @Email(message = "El correo debe tener un formato válido")
    @NotBlank(message = "El email es obligatorio")
    @Column(unique = true, nullable = false, name = "email")
    private String email;

    /** Contraseña del usuario (idealmente encriptada).*/
    @Column(unique = true, nullable = false)
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    /** Pais del usuario, obligatorio. */
    @Column(nullable = false, name = "country")
    @NotBlank(message = "El pais no puede estar vacío")
    private String country;

    /** Conjunto de roles asociados al usuario (muchos a muchos).
     * Se cargan con EAGER porque suelen ser necesarios al autenticar. */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "roles_id"))
    private Set<RoleModel> role;

    @OneToMany(mappedBy = "userOrder", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<DetailOrderModel> detailOrders = new ArrayList<>();

    /** Devuelve las autoridades (permisos) del usuario para el sistema de seguridad.
     * Cada rol del usuario se convierte en una instancia de SimpleGrantedAuthority. */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName().name()))     // Se transforma cada rol en una autoridad utilizando SimpleGrantedAuthority y "ROLE_" es un prefijo obligatorio que Spring Security espera por convención
                .collect(Collectors.toSet());   // Se recolectan las autoridades en un Set para evitar duplicados
    }

    // Métodos requeridos por la interfaz UserDetails, asumidos como siempre válidos:

    @Override
    public boolean isAccountNonExpired() {
        return true; // La cuenta no expira
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // La cuenta no está bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Las credenciales no expiran
    }

    @Override
    public boolean isEnabled() {
        return true; // La cuenta está activa
    }

}