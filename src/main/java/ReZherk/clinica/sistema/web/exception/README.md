# 📌 Guía de Excepciones en Spring Boot (GlobalExceptionHandler)

Este clase centraliza el manejo de errores para **todos los controladores** del sistema, evitando tener `try/catch` repetidos en cada endpoint.

Gracias a `@RestControllerAdvice`, podemos capturar excepciones lanzadas en el **Service** o **Repository** y devolver una respuesta uniforme con nuestro modelo `ApiResponse<T>`.

---

## 🔹 Excepciones comunes, su uso y su captura global

### 1. `EntityNotFoundException`

- **Qué significa:** recurso no encontrado.
- **Status HTTP:** `404 Not Found`.
- **Dónde usar (Service):**

  ```java
  return adminRepository.findById(id)
      .orElseThrow(() -> new EntityNotFoundException("Admin con ID " + id + " no existe"));
  ```

- **En el GlobalExceptionHandler:**
  ```java
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ApiResponse<String>> handleEntityNotFound(EntityNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse<>(false, "Recurso no encontrado", ex.getMessage()));
  }
  ```

---

### 2. `IllegalArgumentException`

- **Qué significa:** argumentos inválidos.
- **Status HTTP:** `400 Bad Request`.
- **Dónde usar (Service):**

  ```java
  if (especialidad == null) {
      throw new IllegalArgumentException("La especialidad no puede ser nula");
  }
  ```

- **En el GlobalExceptionHandler:**
  ```java
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiResponse<String>> handleIllegalArgument(IllegalArgumentException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ApiResponse<>(false, "Argumento inválido", ex.getMessage()));
  }
  ```

---

### 3. `BusinessException` (Custom)

- **Qué significa:** violación de una regla de negocio.
- **Status HTTP:** `409 Conflict`.
- **Dónde usar (Service):**

  ```java
  if (medico.getPacientes().size() > 100) {
      throw new BusinessException("Un médico no puede tener más de 100 pacientes asignados");
  }
  ```

- **En el GlobalExceptionHandler:**
  ```java
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ApiResponse<String>> handleBusiness(BusinessException ex) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(new ApiResponse<>(false, "Error de negocio", ex.getMessage()));
  }
  ```

---

### 4. `ValidationException` (Custom o Jakarta)

- **Qué significa:** datos inválidos en validación manual.
- **Status HTTP:** `400 Bad Request`.
- **Dónde usar (Service):**

  ```java
  if (dto.getPassword().length() < 6) {
      throw new ValidationException("La contraseña debe tener al menos 6 caracteres");
  }
  ```

- **En el GlobalExceptionHandler:**
  ```java
  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ApiResponse<String>> handleValidation(ValidationException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ApiResponse<>(false, "Error de validación", ex.getMessage()));
  }
  ```

---

### 5. `MethodArgumentNotValidException`

- **Qué significa:** validación automática de `@RequestBody` con `@Valid` fallida.
- **Status HTTP:** `400 Bad Request`.
- **Spring la lanza automáticamente.**
- **Ejemplo de error de DTO:**

  ```java
  @NotBlank(message = "El nombre no puede estar vacío")
  private String nombre;
  ```

- **En el GlobalExceptionHandler:**

  ```java
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<List<String>>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
      List<String> errores = ex.getBindingResult().getFieldErrors()
          .stream()
          .map(err -> err.getField() + ": " + err.getDefaultMessage())
          .toList();

      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ApiResponse<>(false, "Errores de validación", errores));
  }
  ```

---

### 6. `DataIntegrityViolationException`

- **Qué significa:** violación de integridad de BD (duplicados, FK, unique).
- **Status HTTP:** `409 Conflict`.
- **Spring la lanza automáticamente.**
- **Ejemplo:** insertar un email duplicado en una columna `unique`.
- **En el GlobalExceptionHandler:**
  ```java
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiResponse<String>> handleDataIntegrity(DataIntegrityViolationException ex) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(new ApiResponse<>(false, "Error de integridad de datos", ex.getMostSpecificCause().getMessage()));
  }
  ```

---

### 7. `AccessDeniedException`

- **Qué significa:** falta de permisos.
- **Status HTTP:** `403 Forbidden`.
- **Dónde usar (Service):**

  ```java
  if (!usuario.isAdmin()) {
      throw new AccessDeniedException("No tienes permisos para esta operación");
  }
  ```

- **En el GlobalExceptionHandler:**
  ```java
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiResponse<String>> handleAccessDenied(AccessDeniedException ex) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body(new ApiResponse<>(false, "Acceso denegado", ex.getMessage()));
  }
  ```

---

### 8. `Exception` (genérica)

- **Qué significa:** cualquier error inesperado no cubierto.
- **Status HTTP:** `500 Internal Server Error`.
- **En el GlobalExceptionHandler:**
  ```java
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<String>> handleGeneral(Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse<>(false, "Error inesperado", ex.getMessage()));
  }
  ```

---

### 9. BadCredentialsException

- **Qué significa:** credenciales de autenticación incorrectas (usuario/contraseña inválidos).
- **Status HTTP:** 401 Unauthorized.
  Spring Security la lanza automáticamente.
- **Cuándo se lanza:** cuando AuthenticationManager no puede autenticar al usuario.
- **Ejemplo en Service:**

```java
// AuthenticationManager internamente lanza esta excepción
Authentication authentication = authenticationManager.authenticate(
new UsernamePasswordAuthenticationToken(
loginDto.getDni(),
loginDto.getPassword()));
```

- **En el GlobalExceptionHandler:**

```java
@ExceptionHandler(BadCredentialsException.class)
public ResponseEntity<ApiResponse<String>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
           .body(new ApiResponse<>(false, "Credenciales inválidas", "DNI o contraseña incorrectos"));
}

```

## 📌 Resumen rápido

| Excepción                         | HTTP Status | Cuándo usar                            |
| --------------------------------- | ----------- | -------------------------------------- |
| `EntityNotFoundException`         | 404         | Recurso no encontrado                  |
| `IllegalArgumentException`        | 400         | Argumento inválido                     |
| `BusinessException` (Custom)      | 409         | Regla de negocio violada               |
| `ValidationException`             | 400         | Validación manual fallida              |
| `MethodArgumentNotValidException` | 400         | Validación automática `@Valid` fallida |
| `DataIntegrityViolationException` | 409         | Restricción de BD violada              |
| `AccessDeniedException`           | 403         | Sin permisos                           |
| `Exception`                       | 500         | Error inesperado                       |
| `BadCredentialsException`         | 401         | Credenciales inválidas                 |

---

## ✅ Buenas prácticas

- No usar `try/catch` en cada controlador.
- Lanzar excepciones específicas en el `Service`.
- Dejar que el `GlobalExceptionHandler` capture y devuelva la respuesta uniforme.
- Todas las respuestas de error siguen el formato de `ApiResponse<T>`.

---
