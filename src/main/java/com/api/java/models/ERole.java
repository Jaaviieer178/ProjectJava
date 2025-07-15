package com.api.java.models;

/**Enumeración que representa los diferentes roles que puede tener un usuario.*/
public enum ERole {
    ADMIN,   // Administrador del sistema
    USER,    // Usuario con permisos estándar
    INVITED  // Invitado con acceso limitado o de solo lectura
}
