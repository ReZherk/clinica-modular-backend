package ReZherk.clinica.sistema.modules.admin.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ReZherk.clinica.sistema.core.application.dto.ApiResponse;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.AssignRoleToUserRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.ChangePasswordRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.RoleResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.UserResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.UsuarioWithRoleResponse;
import ReZherk.clinica.sistema.modules.admin.application.service.RoleService;
import ReZherk.clinica.sistema.modules.admin.application.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

  private final UserService userService;
  private final RoleService roleService;

  @GetMapping("/active")
  public ResponseEntity<ApiResponse<Page<UserResponseDto>>> getActiveUser(
      @RequestParam(required = false) String search,
      @RequestParam(required = false, defaultValue = "documento") String searchType,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "ASC") String sortDirection,
      @RequestParam(required = false) String rol) {

    log.info(
        "GET /api/user/active - search: '{}', searchType: '{}', page: {}, size: {}, sortBy: {}, sortDirection: {}",
        search != null ? search : "sin busqueda", searchType, page, size, sortBy, sortDirection);

    try {
      Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC")
          ? Sort.Direction.DESC
          : Sort.Direction.ASC;

      Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

      Page<UserResponseDto> activeUser = userService.getActiveUser(search, searchType, pageable, rol);

      log.info("Respuesta exitosa: {} usuarios activos encontrados de {} totales",
          activeUser.getNumberOfElements(), activeUser.getTotalElements());

      return ResponseEntity.ok(
          new ApiResponse<>(true, "Usuarios activos obtenidos exitosamente.", activeUser));
    } catch (Exception e) {
      log.error("Error al obtener usuarios activos", e);
      return ResponseEntity.internalServerError()
          .body(new ApiResponse<>(false, "Error al obtener usuarios activos: " + e.getMessage(), null));
    }
  }

  @GetMapping("/inactive")
  public ResponseEntity<ApiResponse<Page<UserResponseDto>>> getInactiveUser(
      @RequestParam(required = false) String search,
      @RequestParam(required = false, defaultValue = "documento") String searchType,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "ASC") String sortDirection,
      @RequestParam(required = true) String rol) {

    log.info("GET /api/user/inactive -search: '{}',page: {},size: {}, sortBy: {},sortDirection: {}",
        search != null ? search : "Sin busqueda", page, size, sortBy, sortDirection);

    try {

      Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;

      Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

      Page<UserResponseDto> inactiveUser = userService.getInactiveUser(search, searchType, pageable, rol);

      log.info("Respuesta exitosa: {} usuarios inactivos encontrados de {} totales",
          inactiveUser.getNumberOfElements(), inactiveUser.getTotalElements());

      return ResponseEntity
          .ok(new ApiResponse<>(true, "usuarios inactivos obtenidos exitosamente.", inactiveUser));

    } catch (Exception e) {
      log.error("Error al obtener usuarios inactivos", e);
      return ResponseEntity.internalServerError()
          .body(new ApiResponse<>(false, "Error al obtener usuarios inactivos" + e.getMessage(), null));
    }
  }

  @PostMapping("/assign")
  public ResponseEntity<ApiResponse<Void>> assignRoleToUser(
      @Validated @RequestBody AssignRoleToUserRequestDto dto) {
    userService.assignRoleToUser(dto);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>(true, "Usuario creado correctamente", null));
  }

  @GetMapping("/roles")
  public ResponseEntity<ApiResponse<Page<RoleResponseDto>>> getActiveRolesForUserCreation() {
    log.info("GET /api/user/active - sin filtros, para creación de usuario");

    try {
      Pageable pageable = PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "id"));

      Page<RoleResponseDto> activeRoles = roleService.getActiveRoles(null, pageable);

      log.info("Roles activos obtenidos: {} de {}", activeRoles.getNumberOfElements(), activeRoles.getTotalElements());

      return ResponseEntity.ok(
          new ApiResponse<>(true, "Roles activos obtenidos exitosamente.", activeRoles));
    } catch (Exception e) {
      log.error("Error al obtener roles activos", e);
      return ResponseEntity.internalServerError()
          .body(new ApiResponse<>(false, "Error al obtener roles activos: " + e.getMessage(), null));
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<UsuarioWithRoleResponse>> getUserById(@PathVariable Integer id) {
    UsuarioWithRoleResponse result = userService.obtenerUsuarioPorId(id);

    return ResponseEntity.ok(new ApiResponse<>(true, "Se obtuvo satisfactoriamente el usuario", result));
  }

  @PutMapping("/{id}/change-password")
  public ResponseEntity<ApiResponse<Void>> changePassword(@PathVariable Integer id,
      @RequestBody ChangePasswordRequestDto dto) {

    userService.cambiarPassword(id, dto);

    return ResponseEntity.ok(
        new ApiResponse<>(true, "Contraseña actualizada exitosamente", null));
  }

  @PatchMapping("/{id}/activate")
  public ResponseEntity<ApiResponse<UserResponseDto>> activateAdmin(@PathVariable Integer id) {
    UserResponseDto activated = userService.activeUser(id);
    return ResponseEntity.ok(new ApiResponse<>(true, "Se activo el usuario correctamente", activated));
  }

  @PatchMapping("/{id}/deactivate")
  public ResponseEntity<ApiResponse<UserResponseDto>> deactivateAdmin(@PathVariable Integer id) {
    UserResponseDto disabled = userService.desactiveUser(id);
    return ResponseEntity.ok(new ApiResponse<>(true, "Administrador desactivado", disabled));
  }

}
