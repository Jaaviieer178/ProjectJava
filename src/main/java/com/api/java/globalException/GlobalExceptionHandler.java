package com.api.java.globalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import java.nio.file.AccessDeniedException;

/** Manejador global de excepciones para toda la aplicación.
 * Intercepta y transforma diferentes tipos de excepciones en respuestas HTTP estandarizadas. */

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /** Maneja excepciones de tipo {@link ResponseStatusException}.
     * @param e excepción recibida
     * @return respuesta con detalles del error y código correspondiente */

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException e) {
        HttpStatus status = HttpStatus.resolve(e.getStatusCode().value());
        ErrorResponse error = buildErrorResponse(e, status != null ? status : HttpStatus.INTERNAL_SERVER_ERROR);
        logError("ResponseStatus", e, error.getErrorId());
        return ResponseEntity.status(e.getStatusCode()).body(error);
    }

    /** Maneja errores de validación en parámetros anotados con {@code @Valid}.
     * @param ex excepción generada por errores de validación
     * @return lista de errores específicos de campos con código 400*/

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        ValidationErrorResponse errorResponse = new ValidationErrorResponse("Error de validación", HttpStatus.BAD_REQUEST.value());
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage())
        );
        logError("Validación", ex, errorResponse.getErrorId());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /** Maneja errores de formato JSON mal enviado al backend.
     * @param ex excepción que indica que el cuerpo del request no se pudo parsear
     * @return error 400 con mensaje de formato inválido */

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonFormatException(HttpMessageNotReadableException ex) {
        ErrorResponse error = buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
        logError("Formato JSON inválido", ex, error.getErrorId());
        return ResponseEntity.badRequest().body(error);
    }

    /** Maneja errores de acceso denegado cuando el usuario no tiene los permisos necesarios.
     * @param ex excepción lanzada por Spring Security
     * @return respuesta 403 con detalles del acceso denegado */

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorResponse error = buildErrorResponse(ex, HttpStatus.FORBIDDEN);
        logError("AccessDenied", ex, error.getErrorId());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    /** Maneja todas las excepciones genéricas no contempladas explícitamente.
     * @param ex excepción inesperada
     * @return respuesta 500 con información técnica del error */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        logError("Error inesperado", ex, error.getErrorId());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /*** Genera una estructura {@link ErrorResponse} con metadatos comunes para un error.
     * @param ex      excepción que se desea representar
     * @param status  código HTTP asociado
     * @return instancia de {@code ErrorResponse} */

    private ErrorResponse buildErrorResponse(Exception ex, HttpStatus status) {
        return new ErrorResponse(
                ex.getMessage(),
                status.value(),
                ex.getClass().getSimpleName()
        );
    }

    /** Realiza el log enriquecido de una excepción capturada.
     * @param contexto  descripción textual del contexto del error
     * @param ex        excepción ocurrida
     * @param errorId   identificador único del error para trazabilidad*/

    private void logError(String contexto, Exception ex, String errorId) {
        log.error("Contexto: {}, ID Error: {}, Mensaje: {}", contexto, errorId, ex.getMessage(), ex);
    }
}