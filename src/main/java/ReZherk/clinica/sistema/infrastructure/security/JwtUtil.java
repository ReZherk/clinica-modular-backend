package ReZherk.clinica.sistema.infrastructure.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utilidad para la gestión de JWT (JSON Web Tokens).
 *
 * Esta clase se encarga de:
 * - Generar tokens JWT con información del usuario.
 * - Validar tokens existentes para comprobar su integridad y expiración.
 * - Extraer información contenida en los tokens (claims).
 *
 * Funcionamiento general:
 * 1. El usuario inicia sesión → se genera un JWT.
 * 2. El JWT contiene: header.payload.signature.
 * 3. El cliente envía el JWT en el encabezado Authorization: Bearer {token}.
 * 4. El servidor valida la firma y extrae los datos del token.
 */
@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secret; // Clave secreta usada para firmar y validar JWT

  @Value("${jwt.expiration}")
  private Long expiration; // Tiempo de expiración del token en milisegundos

  // =====================
  // MÉTODOS DE EXTRACCIÓN
  // =====================

  /**
   * Obtiene el nombre de usuario (subject) del token JWT.
   *
   * @param token Token JWT
   * @return Nombre de usuario
   */
  public String getDniFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  /**
   * Obtiene la fecha de expiración del token.
   *
   * @param token Token JWT
   * @return Fecha de expiración
   */
  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  /**
   * Extrae un claim específico del token mediante un resolver.
   *
   * @param token          Token JWT
   * @param claimsResolver Función que indica qué claim extraer
   * @param <T>            Tipo de retorno
   * @return Claim solicitado
   */
  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  /**
   * Decodifica el token y obtiene todos los claims.
   *
   * @param token Token JWT
   * @return Claims contenidos en el token
   */
  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser()
        .setSigningKey(secret) // Verifica la firma con la clave secreta
        .parseClaimsJws(token)
        .getBody();
  }

  /**
   * Verifica si el token ha expirado.
   *
   * @param token Token JWT
   * @return true si está expirado, false en caso contrario
   */
  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  // =====================
  // MÉTODOS DE GENERACIÓN
  // =====================

  /**
   * Genera un token JWT para un usuario.
   *
   * @param userDetails Información del usuario autenticado
   * @return Token JWT generado
   */
  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    return doGenerateToken(claims, userDetails.getUsername());
  }

  /**
   * Construye y firma un token JWT.
   *
   * @param claims  Datos adicionales que se incluyen en el payload
   * @param subject Nombre de usuario
   * @return Token JWT firmado
   */
  private String doGenerateToken(Map<String, Object> claims, String subject) {
    return Jwts.builder()
        .setClaims(claims) // Datos adicionales
        .setSubject(subject) // Usuario dueño del token
        .setIssuedAt(new Date(System.currentTimeMillis())) // Fecha de creación
        .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Fecha de expiración
        .signWith(SignatureAlgorithm.HS512, secret) // Firma con algoritmo HS512
        .compact();
  }

  // =====================
  // MÉTODOS DE VALIDACIÓN
  // =====================

  /**
   * Valida el token JWT verificando:
   * - Que el username del token coincida con el del usuario autenticado.
   * - Que el token no esté expirado.
   *
   * @param token       Token JWT
   * @param userDetails Datos del usuario autenticado
   * @return true si el token es válido, false en caso contrario
   */
  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = getDniFromToken(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }
}