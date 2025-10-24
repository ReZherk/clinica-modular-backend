package ReZherk.clinica.sistema.modules.admin.web.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ReZherk.clinica.sistema.core.application.dto.ApiResponse;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.MedicoCreationDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.CountResponse;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.MedicoResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.service.MedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/admin/medicos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MedicoController {
 private final MedicoService medicoService;

 @PostMapping("/create-medico")
 public ResponseEntity<ApiResponse<Void>> createDoctors(@Valid @RequestBody MedicoCreationDto dto) {
  medicoService.registrarMedico(dto);

  return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Medico creado exitosamente", null));
 }

 @GetMapping
 public ResponseEntity<List<MedicoResponseDto>> listDoctors() {
  List<MedicoResponseDto> medicos = medicoService.listarMedicos();
  return ResponseEntity.ok(medicos);
 }

 // Contar médicos activos e inactivos
 @GetMapping("/count")
 public ResponseEntity<ApiResponse<CountResponse>> tellDoctors() {
  CountResponse conteo = medicoService.contarMedicosActivosInactivos();
  return ResponseEntity.ok(new ApiResponse<>(true, "Cantidad de medicos activos e inactivos", conteo));
 }

}
