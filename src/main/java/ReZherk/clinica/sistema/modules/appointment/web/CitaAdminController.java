package ReZherk.clinica.sistema.modules.appointment.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ReZherk.clinica.sistema.core.application.dto.ApiResponse;
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.SpecialtyResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.MedicoResponseDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.CitaCreateRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.CitaFiltroRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.CitaReprogramRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.HorariosDisponiblesRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.CitaListadoResult;
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.CitaResponseDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.HorariosDisponiblesResponseDto;
import ReZherk.clinica.sistema.modules.appointment.application.service.CitaService;

@RestController
@RequestMapping("/api/admin/citas")
@RequiredArgsConstructor
@Tag(name = "Citas - Administrador", description = "Endpoints para gestión de citas por administradores")
@Slf4j
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

 @GetMapping("/specialty")
 public ResponseEntity<ApiResponse<List<SpecialtyResponseDto>>> listActiveSpecialties() {

  List<SpecialtyResponseDto> result = citaService.listarEspecialidades(true);
  return ResponseEntity.ok(new ApiResponse<>(true, "Se obtuvo exitosamente las especialidades activas", result));
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

 @PostMapping("/horarios-disponibles")
 @Operation(summary = "Listar horarios disponibles", description = "Lista todos los horarios libres de un médico en una fecha específica")
 public ResponseEntity<ApiResponse<HorariosDisponiblesResponseDto>> listarHorariosDisponibles(
   @Valid @RequestBody HorariosDisponiblesRequestDto request) {

  HorariosDisponiblesResponseDto horarios = citaService.listarHorariosDisponibles(request);
  return ResponseEntity.ok(new ApiResponse<>(true, "Horarios disponibles obtenidos exitosamente", horarios));
 }

 @GetMapping("/doctors")
 @Operation(summary = "Listar medicos buscados", description = "Lista todos los medicos que deben ser de la misma especialidad")
 public ResponseEntity<ApiResponse<Page<MedicoResponseDto>>> getDoctors(
   @RequestParam(required = false) String search,
   @RequestParam(required = false, defaultValue = "documento") String searchType,
   @RequestParam(defaultValue = "0") int page,
   @RequestParam(defaultValue = "10") int size,
   @RequestParam(defaultValue = "id") String sortBy,
   @RequestParam(defaultValue = "ASC") String sorDirection,
   @RequestParam(required = false) String especialidad) {

  log.info(
    "GET /api/cita/doctors -search: '{}', searchType: '{}',page: '{}', size:'{}', sortBy: '{}', sortDirection: {}",
    search != null ? search : "Sin busqueda", searchType, page, size, sortBy, sorDirection);
  try {
   Sort.Direction direction = sorDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;

   Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

   Page<MedicoResponseDto> activeDoctors = citaService.getMedicos(search, searchType, pageable, especialidad);

   log.info("Respuesta exitosa: {} medicos  encontrados de {} totales",
     activeDoctors.getNumberOfElements(), activeDoctors.getTotalElements());

   return ResponseEntity.ok(
     new ApiResponse<>(true, "Medicos obtenidos exitosamente.", activeDoctors));

  } catch (Exception e) {
   log.error("Error al obtener medicos activos", e);
   return ResponseEntity.internalServerError()
     .body(new ApiResponse<>(false, "Error al obtener medicos activos: " + e.getMessage(), null));
  }
 }

}
