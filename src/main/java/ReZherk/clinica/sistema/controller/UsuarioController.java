package ReZherk.clinica.sistema.controller;

import ReZherk.clinica.sistema.dto.UsuarioResponseDto;
import ReZherk.clinica.sistema.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioController {

 private final UsuarioService usuarioService;

 @GetMapping("/{id}")
 @PreAuthorize("hasRole('SUPERADMIN') or hasRole('ADMIN_MEDICOS') or @usuarioService.isOwner(authentication.name, #id)")
 public ResponseEntity<UsuarioResponseDto> getUserById(@PathVariable Integer id) {
  UsuarioResponseDto usuario = usuarioService.getUserById(id);
  return ResponseEntity.ok(usuario);
 }

 @GetMapping("/email/{email}")
 @PreAuthorize("hasRole('SUPERADMIN') or hasRole('ADMIN_MEDICOS') or authentication.name == #email")
 public ResponseEntity<UsuarioResponseDto> getUserByEmail(@PathVariable String email) {
  UsuarioResponseDto usuario = usuarioService.getUserByEmail(email);
  return ResponseEntity.ok(usuario);
 }
}
