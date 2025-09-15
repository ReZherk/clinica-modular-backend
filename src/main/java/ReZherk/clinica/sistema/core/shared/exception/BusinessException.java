package ReZherk.clinica.sistema.core.shared.exception;

public class BusinessException extends RuntimeException {
 public BusinessException(String message) {
  super(message);
 }

 public BusinessException(String message, Throwable cause) {
  super(message, cause);
 }
}