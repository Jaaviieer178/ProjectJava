package com.api.java.globalException;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

/** Representa una respuesta especializada para errores de validación.
 * Contiene una lista de errores de campo, junto con detalles estándar del error. */

@Getter
@Setter
public class ValidationErrorResponse extends ErrorResponse {
    private final List<FieldValidationError> errors = new ArrayList<>();    //Lista de errores de validación individuales por campo.

    /** Constructor base con mensaje y código HTTP.
     * Inicializa con tipo de excepción "ValidationError".
     * @param message mensaje general de validación
     * @param code    código HTTP (usualmente 400) */

    public ValidationErrorResponse(String message, int code) {
        super(message, code, "ValidationError");
    }

    /** Agrega un nuevo error de validación relacionado con un campo específico.
     * @param field         nombre del campo inválido
     * @param errorMessage  descripción del error */

    public void addValidationError(String field, String errorMessage) {
        errors.add(new FieldValidationError(field, errorMessage));
    }

    /** Representa un error de validación en un campo específico.
     * @param field   nombre del campo inválido
     * @param message descripción del error encontrado */

    public record FieldValidationError(String field, String message) {}
}
