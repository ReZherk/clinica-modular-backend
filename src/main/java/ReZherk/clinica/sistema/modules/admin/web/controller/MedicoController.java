package ReZherk.clinica.sistema.modules.admin.web.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ReZherk.clinica.sistema.modules.admin.application.dto.request.RegisterMedicoDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.MedicoCountResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.MedicoResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.RegisterResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/admin/medicos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MedicoController {
 private final AdminService adminService;

 @PostMapping("/register")
 public ResponseEntity<RegisterResponseDto> registerDoctors(@Valid @RequestBody RegisterMedicoDto registerDto) {

  RegisterResponseDto response = adminService.registrarMedico(registerDto);

  return new ResponseEntity<>(response, HttpStatus.CREATED);
 }

 @GetMapping
 public ResponseEntity<List<MedicoResponseDto>> listDoctors() {
  List<MedicoResponseDto> medicos = adminService.listarMedicos();
  return ResponseEntity.ok(medicos);
 }

 // Contar médicos activos e inactivos
 @GetMapping("/count")
 public ResponseEntity<MedicoCountResponseDto> tellDoctors() {
  MedicoCountResponseDto conteo = adminService.contarMedicosActivosInactivos();
  return ResponseEntity.ok(conteo);
 }

}
