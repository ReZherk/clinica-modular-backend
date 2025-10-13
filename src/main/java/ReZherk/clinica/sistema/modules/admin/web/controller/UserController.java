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

   Page<UserResponseDto> activeAdmins = userService.getActiveUser(search, searchType, pageable, rol);

   log.info("Respuesta exitosa: {} usuarios activos encontrados de {} totales",
     activeAdmins.getNumberOfElements(), activeAdmins.getTotalElements());

   return ResponseEntity.ok(
     new ApiResponse<>(true, "Usuarios activos obtenidos exitosamente.", activeAdmins));
  } catch (Exception e) {
   log.error("Error al obtener usuarios activos", e);
   return ResponseEntity.internalServerError()
     .body(new ApiResponse<>(false, "Error al obtener usuarios activos: " + e.getMessage(), null));
  }
 }

}
