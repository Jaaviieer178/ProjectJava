package com.api.java.models;

import jakarta.persistence.*;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.Set;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable =false, name = "dni")
    @Digits(integer = 8, fraction = 0)
    private Integer dni;

    @Column(nullable = false, name = "username")
    private String userName;

    @Email
    @Column(unique = true, nullable = false, name = "email")
    private String email;

    @Column(unique = true)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = RoleModel.class, cascade = CascadeType.PERSIST)
    @JoinTable( name = "user_roles",
                joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "roles_id"))
    private Set<RoleModel> role;
}