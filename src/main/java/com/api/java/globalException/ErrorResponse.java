package com.api.java.globalException;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

/** Representa una respuesta genérica para errores en la aplicación.
 * Puede utilizarse para excepciones del sistema, validación o de negocio. */

@AllArgsConstructor
@Data
public class ErrorResponse {
    private String message;     // Mensaje descriptivo del error para el cliente.
    private int code;       // Código HTTP correspondiente al tipo de error.
    private String exceptionType;   // Tipo de excepción (nombre simple de la clase).
    private String errorId;     // Identificador único del error, útil para trazabilidad en logs.
    private LocalDateTime timestamp;    //Fecha y hora en que ocurrió el error.

    /** Constructor principal que genera automáticamente el ID de error y timestamp.
     * @param message        mensaje explicativo del error
     * @param code           código HTTP (ej.: 400, 500)
     * @param exceptionType  tipo de excepción (por ejemplo, {@code IllegalArgumentException}) */

    public ErrorResponse(String message, int code, String exceptionType) {
        this.message = message;
        this.code = code;
        this.exceptionType = exceptionType;
        this.errorId = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
    }
}
