package ReZherk.clinica.sistema.modules.future_modules.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import ReZherk.clinica.sistema.modules.patient.application.dto.response.UsuarioResponseDto;
import ReZherk.clinica.sistema.modules.patient.application.service.UsuarioService;

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
