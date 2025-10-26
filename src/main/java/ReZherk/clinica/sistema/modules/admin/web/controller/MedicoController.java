package ReZherk.clinica.sistema.modules.admin.web.controller;

import org.springframework.data.domain.Pageable;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ReZherk.clinica.sistema.core.application.dto.ApiResponse;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.ChangePasswordRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.MedicoCreationDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.CountResponse;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.MedicoResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.SpecialtyResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.service.MedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/admin/medicos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class MedicoController {
 private final MedicoService medicoService;

 @PostMapping("/create-medico")
 public ResponseEntity<ApiResponse<Void>> createDoctors(@Valid @RequestBody MedicoCreationDto dto) {
  medicoService.registrarMedico(dto);

  return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Medico creado exitosamente", null));
 }

 @GetMapping("/active")
 public ResponseEntity<ApiResponse<Page<MedicoResponseDto>>> getActiveDoctors(
   @RequestParam(required = false) String search,
   @RequestParam(required = false, defaultValue = "documento") String searchType,
   @RequestParam(defaultValue = "0") int page,
   @RequestParam(defaultValue = "10") int size,
   @RequestParam(defaultValue = "id") String sortBy,
   @RequestParam(defaultValue = "ASC") String sorDirection,
   @RequestParam(required = false) String especialidad) {

  log.info(
    "GET /api/medicos/active -search: '{}', searchType: '{}',page: '{}', size:'{}', sortBy: '{}', sortDirection: {}",
    search != null ? search : "Sin busqueda", searchType, page, size, sortBy, sorDirection);
  try {
   Sort.Direction direction = sorDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;

   Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

   Page<MedicoResponseDto> activeDoctors = medicoService.getActiveMedicos(search, searchType, pageable, especialidad);

   log.info("Respuesta exitosa: {} medicos activos encontrados de {} totales",
     activeDoctors.getNumberOfElements(), activeDoctors.getTotalElements());

   return ResponseEntity.ok(
     new ApiResponse<>(true, "Medicos activos obtenidos exitosamente.", activeDoctors));

  } catch (Exception e) {
   log.error("Error al obtener medicos activos", e);
   return ResponseEntity.internalServerError()
     .body(new ApiResponse<>(false, "Error al obtener medicos activos: " + e.getMessage(), null));
  }
 }

 @GetMapping("/inactive")
 public ResponseEntity<ApiResponse<Page<MedicoResponseDto>>> getInactiveDoctors(
   @RequestParam(required = false) String search,
   @RequestParam(required = false, defaultValue = "documento") String searchType,
   @RequestParam(defaultValue = "0") int page,
   @RequestParam(defaultValue = "10") int size,
   @RequestParam(defaultValue = "id") String sortBy,
   @RequestParam(defaultValue = "ASC") String sorDirection,
   @RequestParam(required = false) String especialidad) {

  log.info(
    "GET /api/medicos/inactive -search: '{}', searchType: '{}',page: '{}', size:'{}', sortBy: '{}', sortDirection: {}",
    search != null ? search : "Sin busqueda", searchType, page, size, sortBy, sorDirection);
  try {
   Sort.Direction direction = sorDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;

   Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

   Page<MedicoResponseDto> inactiveDoctors = medicoService.getInactiveMedicos(search, searchType, pageable,
     especialidad);

   log.info("Respuesta exitosa: {} medicos activos encontrados de {} totales",
     inactiveDoctors.getNumberOfElements(), inactiveDoctors.getTotalElements());

   return ResponseEntity.ok(
     new ApiResponse<>(true, "Medicos Inactivos obtenidos exitosamente.", inactiveDoctors));

  } catch (Exception e) {
   log.error("Error al obtener medicos Inactivos", e);
   return ResponseEntity.internalServerError()
     .body(new ApiResponse<>(false, "Error al obtener medicos inactivos: " + e.getMessage(), null));
  }
 }

 @GetMapping("/{id}")
 public ResponseEntity<ApiResponse<MedicoResponseDto>> getMedicoById(@PathVariable Integer id) {
  MedicoResponseDto admin = medicoService.obtenerMedicoPorId(id);

  return ResponseEntity.ok(new ApiResponse<>(true, "Se obtuvo satisfactoriamente el medico", admin));
 }

 @PutMapping("/{id}")
 public ResponseEntity<ApiResponse<MedicoResponseDto>> updateMedico(
   @PathVariable Integer id,
   @RequestBody MedicoCreationDto data) {

  MedicoResponseDto update = medicoService.modificarMedico(id, data);
  return ResponseEntity.ok(new ApiResponse<>(true, "Se actualizo satisfactoriamente", update));
 }

 @PutMapping("/{id}/change-password")
 public ResponseEntity<ApiResponse<Void>> changePassword(@PathVariable Integer id,
   @RequestBody ChangePasswordRequestDto dto) {

  medicoService.cambiarPassword(id, dto);

  return ResponseEntity.ok(
    new ApiResponse<>(true, "Contraseña actualizada exitosamente", null));
 }

 @PatchMapping("/{id}/activate")
 public ResponseEntity<ApiResponse<MedicoResponseDto>> activateAdmin(@PathVariable Integer id) {
  MedicoResponseDto activated = medicoService.activarMedico(id);
  return ResponseEntity.ok(new ApiResponse<>(true, "Se activo el Medico correctamente", activated));
 }

 @PatchMapping("/{id}/deactivate")
 public ResponseEntity<ApiResponse<MedicoResponseDto>> deactivateAdmin(@PathVariable Integer id) {
  MedicoResponseDto disabled = medicoService.desactivarMedico(id);
  return ResponseEntity.ok(new ApiResponse<>(true, "Medico desactivado", disabled));
 }

 @GetMapping("/specialty")
 public ResponseEntity<ApiResponse<List<SpecialtyResponseDto>>> listActiveSpecialties() {

  List<SpecialtyResponseDto> result = medicoService.listarEspecialidades(true);
  return ResponseEntity.ok(new ApiResponse<>(true, "Se obtuvo exitosamente las especialidades activas", result));
 }

 // Contar médicos activos e inactivos
 @GetMapping("/count")
 public ResponseEntity<ApiResponse<CountResponse>> tellDoctors() {
  CountResponse conteo = medicoService.contarMedicosActivosInactivos();
  return ResponseEntity.ok(new ApiResponse<>(true, "Cantidad de medicos activos e inactivos", conteo));
 }

}
