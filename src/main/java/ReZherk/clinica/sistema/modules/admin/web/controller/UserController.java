package ReZherk.clinica.sistema.modules.admin.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ReZherk.clinica.sistema.core.application.dto.ApiResponse;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.UserResponseDto;
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

 @GetMapping("/active")
 public ResponseEntity<ApiResponse<Page<UserResponseDto>>> getActiveUser(
   @RequestParam(required = false) String search,
   @RequestParam(required = false, defaultValue = "documento") String searchType,
   @RequestParam(defaultValue = "0") int page,
   @RequestParam(defaultValue = "10") int size,
   @RequestParam(defaultValue = "id") String sortBy,
   @RequestParam(defaultValue = "ASC") String sortDirection,
   @RequestParam(required = true) String rol) {

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

}
