package ReZherk.clinica.sistema.web.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ReZherk.clinica.sistema.core.application.dto.ApiResponse;
import ReZherk.clinica.sistema.core.shared.exception.BusinessException;
import ReZherk.clinica.sistema.core.shared.exception.ValidationException;
import jakarta.persistence.EntityNotFoundException;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // 1. Recurso no encontrado
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ApiResponse<String>> handleEntityNotFound(EntityNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ApiResponse<>(false, "Recurso no encontrado", ex.getMessage()));
  }

  // 2. Argumentos inválidos
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiResponse<String>> handleIllegalArgument(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ApiResponse<>(false, "Argumento inválido", ex.getMessage()));
  }

  // 3. Regla de negocio violada
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ApiResponse<String>> handleBusiness(BusinessException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(new ApiResponse<>(false, "Error de negocio", ex.getMessage()));
  }

  // 4. Validación manual
  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ApiResponse<String>> handleValidation(ValidationException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ApiResponse<>(false, "Error de validación", ex.getMessage()));
  }

  // 5. Validación automática de @Valid en DTOs
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<List<String>>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
    List<String> errores = ex.getBindingResult().getFieldErrors()
        .stream()
        .map(err -> err.getField() + ": " + err.getDefaultMessage())
        .toList();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ApiResponse<>(false, "Errores de validación", errores));
  }

  // 6. Restricciones de BD (duplicados, unique, FK)
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiResponse<String>> handleDataIntegrity(DataIntegrityViolationException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(new ApiResponse<>(false, "Error de integridad de datos",
            ex.getMostSpecificCause().getMessage()));
  }

  // 7. Falta de permisos
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiResponse<String>> handleAccessDenied(AccessDeniedException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(new ApiResponse<>(false, "Acceso denegado", ex.getMessage()));
  }

  // 8. Error inesperado (fallback)
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<String>> handleGeneral(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ApiResponse<>(false, "Error inesperado", ex.getMessage()));
  }

  // 9. Credenciales incorrectas(Para la contraseña)
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ApiResponse<String>> handleBadCredentials(BadCredentialsException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(new ApiResponse<>(false, "Credenciales inválidas", ex.getMessage()));
  }

}