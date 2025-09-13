package ReZherk.clinica.sistema.controller;

import ReZherk.clinica.sistema.dto.UsuarioRequestDTO;
import ReZherk.clinica.sistema.dto.UsuarioResponseDTO;
import ReZherk.clinica.sistema.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

 private final UsuarioService usuarioService;

 @PostMapping
 public ResponseEntity<UsuarioResponseDTO> registrarUsuario(
   @Valid @RequestBody UsuarioRequestDTO dto) {
  UsuarioResponseDTO response = usuarioService.registrarUsuario(dto);
  return ResponseEntity.ok(response);
 }

 @GetMapping
 public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
  return ResponseEntity.ok(usuarioService.listarUsuarios());
 }

 @GetMapping("/{email}")
 public ResponseEntity<UsuarioResponseDTO> buscarPorEmail(@PathVariable String email) {
  UsuarioResponseDTO response = usuarioService.buscarPorEmail(email);
  return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
 }
}
