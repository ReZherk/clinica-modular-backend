package ReZherk.clinica.sistema.controller;

import ReZherk.clinica.sistema.dto.*;
import ReZherk.clinica.sistema.service.RolPerfilService;
import ReZherk.clinica.sistema.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
// ⚠️ ESTE CONTROLADOR ES SOLO PARA PRUEBAS - ELIMINAR EN PRODUCCIÓN
public class TestController {

 private final UsuarioService usuarioService;
 private final RolPerfilService rolPerfilService;

 // Inicializar roles por defecto
 @PostMapping("/init-roles")
 public ResponseEntity<String> initRoles() {
  rolPerfilService.initializeDefaultRoles();
  return ResponseEntity.ok("Roles inicializados");
 }

 // Listar roles
 @GetMapping("/roles")
 public ResponseEntity<List<RolPerfilDto>> getRoles() {
  return ResponseEntity.ok(rolPerfilService.getAllActiveRoles());
 }

 // Registro de paciente sin autenticación
 @PostMapping("/register/paciente")
 public ResponseEntity<RegisterResponseDto> testRegisterPaciente(
   @Valid @RequestBody RegisterPacienteDto registerDto) {
  RegisterResponseDto response = usuarioService.registerPaciente(registerDto);
  return new ResponseEntity<>(response, HttpStatus.CREATED);
 }

 // Registro por superadmin sin autenticación
 @PostMapping("/register/superadmin")
 public ResponseEntity<RegisterResponseDto> testRegisterBySuperAdmin(
   @Valid @RequestBody RegisterSuperAdminDto registerDto) {
  RegisterResponseDto response = usuarioService.registerUserBySuperAdmin(registerDto);
  return new ResponseEntity<>(response, HttpStatus.CREATED);
 }

 // Registro de médico sin autenticación
 @PostMapping("/register/medico")
 public ResponseEntity<RegisterResponseDto> testRegisterMedico(
   @Valid @RequestBody RegisterMedicoDto registerDto) {
  RegisterResponseDto response = usuarioService.registerMedicoByAdmin(registerDto);
  return new ResponseEntity<>(response, HttpStatus.CREATED);
 }
}