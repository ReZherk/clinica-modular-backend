package ReZherk.clinica.sistema.controller;

import ReZherk.clinica.sistema.dto.RegisterPacienteDto;
import ReZherk.clinica.sistema.dto.RegisterResponseDto;
import ReZherk.clinica.sistema.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

 private final UsuarioService usuarioService;

 @PostMapping("/register")
 public ResponseEntity<RegisterResponseDto> registerPaciente(
   @Valid @RequestBody RegisterPacienteDto registerDto) {

  RegisterResponseDto response = usuarioService.registerPaciente(registerDto);
  return new ResponseEntity<>(response, HttpStatus.CREATED);
 }
}
