package ReZherk.clinica.sistema.infrastructure.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

 @Value("${jwt.secret}")
 private String secretkey;

 @Value("${jwt.expiration}")
 private long expirationTime;

 // Este metodo recoge la clase de secretkey y lo uso par generar una clave
 // criptográfica .
 private Key getSigningKey() {

  return Keys.hmacShaKeyFor(secretkey.getBytes());

 }

 // Generare el token.

 public String generateToken(String username, String role) {
  return Jwts.builder()
    .setSubject(username)
    .claim("role", role)
    .setIssuedAt(new Date())
    .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
    .compact();
 }

 // Verifica y decodifica el token JWT, devolviendo sus claims si la firma es
 // válida.
 private Jws<Claims> parseClaims(String token) {
  return Jwts.parserBuilder()
    .setSigningKey(getSigningKey())
    .build()
    .parseClaimsJws(token);
 }

 // Devuelve el nombre de usuario guardado como "subject" en el token JWT.
 public String extractUsername(String token) {
  return parseClaims(token).getBody().getSubject();
 }

 // Extraer rol
 public String extractRole(String token) {
  return parseClaims(token).getBody().get("role", String.class);
 }

 // Validar token
 public boolean validateToken(String token) {
  try {
   parseClaims(token);
   return true;
  } catch (JwtException e) {
   return false;
  }
 }
}
