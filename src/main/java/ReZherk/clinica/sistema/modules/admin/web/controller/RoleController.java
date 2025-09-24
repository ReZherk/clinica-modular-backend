package ReZherk.clinica.sistema.modules.admin.web.controller;

import ReZherk.clinica.sistema.modules.admin.application.dto.request.AssignRoleToUserRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.CreateRoleRequestDto;
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

  /**
   * Crear un rol dinámico.
   * Solo lo permite:
   * - SUPERADMIN: puede crear rol "ADMINISTRADOR".
   * - ADMINISTRADOR: puede crear cualquier otro rol.
   */
  @PostMapping
  public ResponseEntity<RoleResponseDto> createRole(
      @Validated @RequestBody CreateRoleRequestDto dto) {

    RoleResponseDto response = roleService.createRole(dto);

    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  /**
   * Asignar un rol existente a un usuario.
   * SuperAdmin o Administrador.
   */
  @PostMapping("/assign")
  public ResponseEntity<Void> assignRoleToUser(
      @Validated @RequestBody AssignRoleToUserRequestDto dto) {
    roleService.assignRoleToUser(dto);
    return ResponseEntity.noContent().build();
  }

  /**
   * Listar todos los roles disponibles.
   */
  @GetMapping
  public ResponseEntity<List<RoleResponseDto>> getAllRoles() {
    return ResponseEntity.ok(roleService.getAllRoles());
  }
}
