package ReZherk.clinica.sistema.modules.admin.web.controller;

import ReZherk.clinica.sistema.core.application.dto.ApiResponse;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.RoleRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.PermissionResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.RoleResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/roles")
@RequiredArgsConstructor
@Slf4j
public class RoleController {

  private final RoleService roleService;

  @PostMapping("/create")
  public ResponseEntity<ApiResponse<RoleResponseDto>> createRole(
      @Validated @RequestBody RoleRequestDto dto) {
    RoleResponseDto response = roleService.createRole(dto);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>(true, "Rol creado correctamente", response));
  }

  @GetMapping("/permissions")
  public ResponseEntity<ApiResponse<List<PermissionResponseDto>>> getAllPermissions() {
    List<PermissionResponseDto> result = roleService.getAllPermissions();
    return ResponseEntity.ok(new ApiResponse<>(true, "Se obtuvieron los permisos disponibles", result));
  }

  @GetMapping("/active")
  public ResponseEntity<ApiResponse<Page<RoleResponseDto>>> getActiveRoles(
      @RequestParam(required = false) String search,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "100") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "ASC") String sortDirection) {

    log.info("GET /api/roles/active - search: '{}', page: {}, size: {}, sortBy: {}, sortDirection: {}",
        search != null ? search : "sin filtro", page, size, sortBy, sortDirection);

    try {
      Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC")
          ? Sort.Direction.DESC
          : Sort.Direction.ASC;

      Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

      Page<RoleResponseDto> activeRoles = roleService.getActiveRoles(search, pageable);

      log.info("Respuesta exitosa: {} roles activos encontrados de {} totales",
          activeRoles.getNumberOfElements(), activeRoles.getTotalElements());

      return ResponseEntity.ok(
          new ApiResponse<>(true, "Roles activos obtenidos exitosamente.", activeRoles));
    } catch (Exception e) {
      log.error("Error al obtener roles activos", e);
      return ResponseEntity.internalServerError()
          .body(new ApiResponse<>(false, "Error al obtener roles activos: " + e.getMessage(), null));
    }
  }

  @GetMapping("/inactive")
  public ResponseEntity<ApiResponse<Page<RoleResponseDto>>> getInactiveRoles(
      @RequestParam(required = false) String search,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "100") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "ASC") String sortDirection) {

    log.info("GET /api/roles/inactive - search: '{}', page: {}, size: {}, sortBy: {}, sortDirection: {}",
        search != null ? search : "sin filtro", page, size, sortBy, sortDirection);

    try {
      Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC")
          ? Sort.Direction.DESC
          : Sort.Direction.ASC;

      Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

      Page<RoleResponseDto> activeRoles = roleService.getInactiveRoles(search, pageable);

      log.info("Respuesta exitosa: {} roles inactive encontrados de {} totales",
          activeRoles.getNumberOfElements(), activeRoles.getTotalElements());

      return ResponseEntity.ok(
          new ApiResponse<>(true, "Roles inactive obtenidos exitosamente.", activeRoles));
    } catch (Exception e) {
      log.error("Error al obtener roles inactive", e);
      return ResponseEntity.internalServerError()
          .body(new ApiResponse<>(false, "Error al obtener roles inactive: " + e.getMessage(), null));
    }
  }

  @PatchMapping("/{id}/deactivate")
  public ResponseEntity<ApiResponse<Void>> deactivateRole(@PathVariable Integer id) {
    roleService.deactivateRole(id);
    return ResponseEntity.ok(new ApiResponse<>(true, "Se desacivo exitosamente el rol", null));
  }

  @PatchMapping("/{id}/activate")
  public ResponseEntity<ApiResponse<Void>> activateRole(@PathVariable Integer id) {
    roleService.activateRole(id);
    return ResponseEntity.ok(new ApiResponse<>(true, "Se activo exitosamente al rol solicitado", null));
  }

  @PutMapping("/{id}/update")
  public ResponseEntity<ApiResponse<Void>> updateRole(
      @PathVariable Integer id,
      @Validated @RequestBody RoleRequestDto dto) {
    roleService.updateRole(id, dto);
    return ResponseEntity.ok(new ApiResponse<>(true, "Actualizacion exitosa", null));
  }

  @GetMapping("/{id}/role")
  public ResponseEntity<ApiResponse<RoleResponseDto>> getRole(
      @PathVariable Integer id) {
    RoleResponseDto result = roleService.getRole(id);
    return ResponseEntity.ok(new ApiResponse<>(true, "Se obtubo el rol:" + result.getNombre(), result));
  }

}
