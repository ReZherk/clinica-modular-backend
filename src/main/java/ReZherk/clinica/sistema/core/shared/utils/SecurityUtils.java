package ReZherk.clinica.sistema.core.shared.utils;

import java.security.SecureRandom;
import java.util.Base64;

public final class SecurityUtils {

 // evitar instanciación
 private SecurityUtils() {
  throw new UnsupportedOperationException("Utility class");
 }

 public static String generateSalt() {
  SecureRandom random = new SecureRandom();
  byte[] salt = new byte[16];
  random.nextBytes(salt);
  return Base64.getEncoder().encodeToString(salt);
 }
}