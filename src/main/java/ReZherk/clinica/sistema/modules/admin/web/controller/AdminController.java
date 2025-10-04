package ReZherk.clinica.sistema.modules.admin.web.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ReZherk.clinica.sistema.modules.admin.application.dto.request.AssignAdminToUserRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.ChangePasswordRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.AdminBaseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.AdminResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.ApiResponse;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.ChangePasswordResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.service.AdminService;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {

  private final AdminService adminService;

  @PostMapping("/create-admin")
  public ResponseEntity<String> createAdmin(@Validated @RequestBody AssignAdminToUserRequestDto dto) {
    try {
      adminService.createAdminUser(dto);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body("Usuario administrador creado exitosamente");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Error al crear administrador: " + e.getMessage());
    }
  }

  @GetMapping("/all")
  public ResponseEntity<ApiResponse<List<AdminResponseDto>>> getAllAdmins() {
    List<AdminResponseDto> allAdmins = adminService.listarAdministradores();
    return ResponseEntity
        .ok(new ApiResponse<>(true, "Se muestran los administradores activos e inactivos.", allAdmins));
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
  public ResponseEntity<ChangePasswordResponseDto> changePassword(@PathVariable Integer id,
      @RequestBody ChangePasswordRequestDto dto) {

    return ResponseEntity.ok(adminService.cambiarPassword(id, dto));
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