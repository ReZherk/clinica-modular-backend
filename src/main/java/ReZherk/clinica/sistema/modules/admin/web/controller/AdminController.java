package ReZherk.clinica.sistema.modules.admin.web.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ReZherk.clinica.sistema.modules.admin.application.dto.request.AssignAdminToUserRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.AdminBaseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.AdminResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.service.AdminService;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {

 private final AdminService adminService;

 @GetMapping
 public ResponseEntity<List<AdminResponseDto>> getAllAdmins() {
  return ResponseEntity.ok(adminService.listarAdministradores());
 }

 @GetMapping("/{id}")
 public ResponseEntity<AdminBaseDto> getAdminById(@PathVariable Integer id) {
  return ResponseEntity.ok(adminService.obtenerAdminPorId(id));
 }

 @PutMapping("/{id}")
 public ResponseEntity<AdminResponseDto> updateAdmin(
   @PathVariable Integer id,
   @RequestBody AssignAdminToUserRequestDto data) {
  return ResponseEntity.ok(adminService.modificarAdministrador(id, data));
 }

 @PutMapping("/{id}/activate")
 public ResponseEntity<AdminResponseDto> activateAdmin(@PathVariable Integer id) {
  return ResponseEntity.ok(adminService.activarAdministrador(id));
 }

 @PutMapping("/{id}/deactivate")
 public ResponseEntity<AdminResponseDto> deactivateAdmin(@PathVariable Integer id) {
  return ResponseEntity.ok(adminService.desactivarAdministrador(id));
 }
}