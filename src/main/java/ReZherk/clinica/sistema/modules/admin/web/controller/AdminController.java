package ReZherk.clinica.sistema.modules.admin.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
// Importa la clase Sort para definir criterios de ordenamiento dinámico en consultas paginadas
import org.springframework.data.domain.Sort;

import ReZherk.clinica.sistema.core.application.dto.ApiResponse;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.AssignAdminToUserRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.ChangePasswordRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.AdminBaseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.AdminResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.service.AdminService;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

  private final AdminService adminService;

  @PostMapping("/create-admin")
  public ResponseEntity<ApiResponse<Void>> createAdmin(@Validated @RequestBody AssignAdminToUserRequestDto dto) {
    adminService.createAdminUser(dto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>(true, "Usuario administrador creado exitosamente", null));

  }

  @GetMapping("/active")
  public ResponseEntity<ApiResponse<Page<AdminResponseDto>>> getActiveAdmins(
      @RequestParam(required = false) String search,
      @RequestParam(required = false, defaultValue = "documento") String searchType,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "ASC") String sortDirection) {

    log.info(
        "GET /api/admins/active - search: '{}', searchType: '{}', page: {}, size: {}, sortBy: {}, sortDirection: {}",
        search != null ? search : "sin filtro", searchType, page, size, sortBy, sortDirection);

    try {
      Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC")
          ? Sort.Direction.DESC
          : Sort.Direction.ASC;

      Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

      Page<AdminResponseDto> activeAdmins = adminService.getActiveAdministrators(search, searchType, pageable);

      log.info("Respuesta exitosa: {} administradores activos encontrados de {} totales",
          activeAdmins.getNumberOfElements(), activeAdmins.getTotalElements());

      return ResponseEntity.ok(
          new ApiResponse<>(true, "Administradores activos obtenidos exitosamente.", activeAdmins));
    } catch (Exception e) {
      log.error("Error al obtener administradores activos", e);
      return ResponseEntity.internalServerError()
          .body(new ApiResponse<>(false, "Error al obtener administradores activos: " + e.getMessage(), null));
    }
  }

  @GetMapping("/inactive")
  public ResponseEntity<ApiResponse<Page<AdminResponseDto>>> getInactiveAdmins(
      @RequestParam(required = false) String search,
      @RequestParam(required = false, defaultValue = "documento") String searchType,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "ASC") String sortDirection) {

    log.info("GET /api/admins/inactive -search: '{}',page: {},size: {}, sortBy: {},sortDirection: {}",
        search != null ? search : "Sin filtro", page, size, sortBy, sortDirection);

    try {

      Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;

      Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

      Page<AdminResponseDto> inactiveAdmins = adminService.getInactiveAdministrators(search, searchType, pageable);

      log.info("Respuesta exitosa: {} administradores inactivos encontrados de {} totales",
          inactiveAdmins.getNumberOfElements(), inactiveAdmins.getTotalElements());

      return ResponseEntity
          .ok(new ApiResponse<>(true, "Administradores inactivos obtenidos exitosamente.", inactiveAdmins));

    } catch (Exception e) {
      log.error("Error al obtener administradores inactivos", e);
      return ResponseEntity.internalServerError()
          .body(new ApiResponse<>(false, "Error al obtener administradores inactivos" + e.getMessage(), null));
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<AdminBaseDto>> getAdminById(@PathVariable Integer id) {
    AdminBaseDto admin = adminService.obtenerAdminPorId(id);

    return ResponseEntity.ok(new ApiResponse<>(true, "Se obtuvo satisfactoriamente el administrador", admin));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<AdminResponseDto>> updateAdmin(
      @PathVariable Integer id,
      @RequestBody AssignAdminToUserRequestDto data) {

    AdminResponseDto update = adminService.modificarAdministrador(id, data);
    return ResponseEntity.ok(new ApiResponse<>(true, "Se actualizo satisfactoriamente", update));
  }

  @PutMapping("/{id}/change-password")
  public ResponseEntity<ApiResponse<Void>> changePassword(@PathVariable Integer id,
      @RequestBody ChangePasswordRequestDto dto) {

    adminService.cambiarPassword(id, dto);

    return ResponseEntity.ok(
        new ApiResponse<>(true, "Contraseña actualizada exitosamente", null));
  }

  @PatchMapping("/{id}/activate")
  public ResponseEntity<ApiResponse<AdminResponseDto>> activateAdmin(@PathVariable Integer id) {
    AdminResponseDto activated = adminService.activarAdministrador(id);
    return ResponseEntity.ok(new ApiResponse<>(true, "Se activo el usuario correctamente", activated));
  }

  @PatchMapping("/{id}/deactivate")
  public ResponseEntity<ApiResponse<AdminResponseDto>> deactivateAdmin(@PathVariable Integer id) {
    AdminResponseDto disabled = adminService.desactivarAdministrador(id);
    return ResponseEntity.ok(new ApiResponse<>(true, "Administrador desactivado", disabled));
  }
}