package ReZherk.clinica.sistema.modules.admin.web.controller;

import ReZherk.clinica.sistema.modules.admin.application.dto.request.AssignAdminToUserRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.AssignRoleToUserRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.RoleRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.PermissionResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.RoleResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.service.RoleService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/roles")
@RequiredArgsConstructor
public class RoleController {

  private final RoleService roleService;

  @PostMapping("/create")
  public ResponseEntity<RoleResponseDto> createRole(
      @Validated @RequestBody RoleRequestDto dto) {
    try {
      RoleResponseDto response = roleService.createRole(dto);

      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new RoleResponseDto(false, " Error creando rol: " + e.getMessage()));

    }

  }

  @PostMapping("/create-admin")
  public ResponseEntity<String> createAdmin(@Validated @RequestBody AssignAdminToUserRequestDto dto) {
    try {
      roleService.createAdminUser(dto);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body("Usuario administrador creado exitosamente");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Error al crear administrador: " + e.getMessage());
    }
  }

  @GetMapping("/permissions")
  public ResponseEntity<List<PermissionResponseDto>> getAllPermissions() {
    return ResponseEntity.ok(roleService.getAllPermissions());
  }

  @GetMapping
  public ResponseEntity<List<RoleResponseDto>> getAllRoles() {

    return ResponseEntity.ok(roleService.getAllRoles());
  }

  @PostMapping("/assign")
  public ResponseEntity<Void> assignRoleToUser(
      @Validated @RequestBody AssignRoleToUserRequestDto dto) {
    roleService.assignRoleToUser(dto);

    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{id}/deactivate")
  public ResponseEntity<RoleResponseDto> deactivateRole(@PathVariable Integer id) {

    return ResponseEntity.ok(roleService.deactivateRole(id));
  }

  @PatchMapping("/{id}/activate")
  public ResponseEntity<RoleResponseDto> activateRole(@PathVariable Integer id) {

    return ResponseEntity.ok(roleService.activateRole(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<RoleResponseDto> updateRole(
      @PathVariable Integer id,
      @Validated @RequestBody RoleRequestDto dto) {

    return ResponseEntity.ok(roleService.updateRole(id, dto));
  }

}
