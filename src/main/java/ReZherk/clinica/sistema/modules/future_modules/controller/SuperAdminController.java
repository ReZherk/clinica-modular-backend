package ReZherk.clinica.sistema.modules.future_modules.controller;

import ReZherk.clinica.sistema.modules.future_modules.dto.*;
import ReZherk.clinica.sistema.modules.future_modules.service.RolPerfilService;
import ReZherk.clinica.sistema.modules.patient.application.dto.request.CreateRolPerfilDto;
import ReZherk.clinica.sistema.modules.patient.application.dto.response.RegisterResponseDto;
import ReZherk.clinica.sistema.modules.patient.application.dto.response.RolPerfilDto;
import ReZherk.clinica.sistema.modules.patient.application.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/super")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('SUPERADMIN')")
public class SuperAdminController {

  private final UsuarioService usuarioService;
  private final RolPerfilService rolPerfilService;

  // ===== GESTIÓN DE ROLES =====
  @PostMapping("/roles")
  public ResponseEntity<RolPerfilDto> createRol(@Valid @RequestBody CreateRolPerfilDto createDto) {
    RolPerfilDto response = rolPerfilService.createRol(createDto);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping("/roles")
  public ResponseEntity<List<RolPerfilDto>> getAllRoles() {
    List<RolPerfilDto> roles = rolPerfilService.getAllActiveRoles();
    return ResponseEntity.ok(roles);
  }

  @GetMapping("/roles/{id}")
  public ResponseEntity<RolPerfilDto> getRolById(@PathVariable Integer id) {
    RolPerfilDto rol = rolPerfilService.getRolById(id);
    return ResponseEntity.ok(rol);
  }

  @PutMapping("/roles/{id}")
  public ResponseEntity<RolPerfilDto> updateRol(
      @PathVariable Integer id,
      @Valid @RequestBody CreateRolPerfilDto updateDto) {
    RolPerfilDto response = rolPerfilService.updateRol(id, updateDto);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/roles/{id}")
  public ResponseEntity<Void> deleteRol(@PathVariable Integer id) {
    rolPerfilService.deleteRol(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/roles/initialize")
  public ResponseEntity<String> initializeDefaultRoles() {
    rolPerfilService.initializeDefaultRoles();
    return ResponseEntity.ok("Roles por defecto inicializados correctamente");
  }

  // ===== REGISTRO DE USUARIOS CON MÚLTIPLES ROLES =====
  @PostMapping("/register")
  public ResponseEntity<RegisterResponseDto> registerUser(
      @Valid @RequestBody RegisterSuperAdminDto registerDto) {

    RegisterResponseDto response = usuarioService.registerUserBySuperAdmin(registerDto);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }
}
