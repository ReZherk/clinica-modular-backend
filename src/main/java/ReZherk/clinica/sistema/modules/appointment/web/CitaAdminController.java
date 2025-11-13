package ReZherk.clinica.sistema.modules.appointment.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import ReZherk.clinica.sistema.core.application.dto.ApiResponse;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.CitaCreateRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.CitaFiltroRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.CitaReprogramRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.CitaListadoResult;
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.CitaResponseDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.HorariosDisponiblesResponseDto;
import ReZherk.clinica.sistema.modules.appointment.application.service.CitaService;

@RestController
@RequestMapping("/api/admin/citas")
@RequiredArgsConstructor
@Tag(name = "Citas - Administrador", description = "Endpoints para gestión de citas por administradores")
public class CitaAdminController {

 private final CitaService citaService;

 @PostMapping
 @Operation(summary = "Crear cita", description = "Permite al administrador crear una cita para un paciente")
 public ResponseEntity<ApiResponse<CitaResponseDto>> crearCita(@Valid @RequestBody CitaCreateRequestDto request) {

  CitaResponseDto cita = citaService.crearCitaPaciente(request);

  return ResponseEntity.status(HttpStatus.CREATED)
    .body(new ApiResponse<>(true, "Cita creada exitosamente", cita));
 }

 @PutMapping("/{idCita}/cancelar")
 @Operation(summary = "Cancelar cita", description = "Permite al administrador cancelar cualquier cita")
 public ResponseEntity<ApiResponse<CitaResponseDto>> cancelarCita(@PathVariable Integer idCita) {
  CitaResponseDto cita = citaService.cancelarCitaAdmin(idCita);
  return ResponseEntity.ok(new ApiResponse<>(true, "Cita cancelada exitosamente", cita));
 }

 @PutMapping("/{idCita}/completar")
 @Operation(summary = "Completar cita", description = "Marca una cita como completada")
 public ResponseEntity<ApiResponse<CitaResponseDto>> completarCita(@PathVariable Integer idCita) {
  CitaResponseDto cita = citaService.completarCita(idCita);
  return ResponseEntity.ok(new ApiResponse<>(true, "Cita completada exitosamente", cita));
 }

 @PutMapping("/reprogramar")
 @Operation(summary = "Reprogramar cita", description = "Permite cambiar fecha, hora y/o médico de una cita (mismo especialidad)")
 public ResponseEntity<ApiResponse<CitaResponseDto>> reprogramarCita(
   @Valid @RequestBody CitaReprogramRequestDto request) {

  CitaResponseDto cita = citaService.reprogramarCita(request);
  return ResponseEntity.ok(new ApiResponse<>(true, "Cita reprogramada exitosamente", cita));
 }

 @GetMapping
 @Operation(summary = "Listar todas las citas", description = "Lista citas con filtros opcionales usando Stored Procedure. "
   +
   "Búsqueda independiente por CMP, DNI o nombre completo del médico.")
 public ResponseEntity<ApiResponse<Page<CitaListadoResult>>> listarCitas(
   @RequestParam(required = false) String cmpMedico,
   @RequestParam(required = false) String dniMedico,
   @RequestParam(required = false) String nombreMedico,
   @RequestParam(required = false) Integer idEspecialidad,
   @RequestParam(required = false) String estado,
   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
   @RequestParam(defaultValue = "0") Integer page,
   @RequestParam(defaultValue = "10") Integer size) {

  CitaFiltroRequestDto filtros = CitaFiltroRequestDto.builder()
    .cmpMedico(cmpMedico)
    .dniMedico(dniMedico)
    .nombreMedico(nombreMedico)
    .idEspecialidad(idEspecialidad)
    .estado(estado)
    .fecha(fecha)
    .fechaInicio(fechaInicio)
    .fechaFin(fechaFin)
    .page(page)
    .size(size)
    .build();

  Page<CitaListadoResult> citas = citaService.listarCitasConFiltros(filtros);
  return ResponseEntity.ok(new ApiResponse<>(true, "Citas obtenidas exitosamente", citas));
 }

 @GetMapping("/horarios-medico/{idMedico}")
 @Operation(summary = "Ver horarios completos del médico", description = "Lista todos los horarios del médico (libres y ocupados).El formato para la fecha es 2025-MES-DIA")
 public ResponseEntity<ApiResponse<HorariosDisponiblesResponseDto>> listarHorariosCompletos(
   @PathVariable Integer idMedico,
   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

  HorariosDisponiblesResponseDto horarios = citaService.listarHorariosMedicoCompleto(idMedico, fecha);
  return ResponseEntity.ok(new ApiResponse<>(true, "Horarios obtenidos exitosamente", horarios));
 }

 @PutMapping("/{idCita}/no-atendida")
 @Operation(summary = "Marcar cita como NO_ATENDIDA", description = "Marca una cita como NO_ATENDIDA cuando el paciente no asistió")
 public ResponseEntity<ApiResponse<CitaResponseDto>> marcarNoAtendida(@PathVariable Integer idCita) {
  CitaResponseDto cita = citaService.marcarNoAtendida(idCita);
  return ResponseEntity.ok(new ApiResponse<>(true, "Horarios obtenidos exitosamente", cita));
 }

}
