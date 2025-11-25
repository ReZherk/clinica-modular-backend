package ReZherk.clinica.sistema.modules.medico.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ReZherk.clinica.sistema.core.application.dto.ApiResponse;
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.CitaResponseDto;
import ReZherk.clinica.sistema.modules.medico.application.dto.request.BuscarCitasFiltrosRequestDto;
import ReZherk.clinica.sistema.modules.medico.application.dto.request.DetalleCitaRequestDto;
import ReZherk.clinica.sistema.modules.medico.application.dto.request.MeetingLinkRequestDto;
import ReZherk.clinica.sistema.modules.medico.application.dto.response.CitaHistorialResponseDto;
import ReZherk.clinica.sistema.modules.medico.application.dto.response.CitaMedicoResponseDto;
import ReZherk.clinica.sistema.modules.medico.application.dto.response.DetalleCitaResponseDto;
import ReZherk.clinica.sistema.modules.medico.application.dto.response.MeetingLinkResponseDto;
import ReZherk.clinica.sistema.modules.medico.application.service.MedicoCitaService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/medico/citas")
@RequiredArgsConstructor
@Tag(name = "Médico - Citas", description = "Endpoints para gestión de citas por médicos")
public class MedicoCitaController {

 private final MedicoCitaService medicoCitaService;

 @GetMapping("/del-dia/{idMedico}")
 @Operation(summary = "Listar citas del día", description = "Si no se especifica una fecha, se utilizará automáticamente la fecha actual.")
 public ResponseEntity<ApiResponse<List<CitaMedicoResponseDto>>> listarCitasDelDia(
   @PathVariable Integer idMedico,
   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

  List<CitaMedicoResponseDto> citas = medicoCitaService.listarCitasDelDia(idMedico, fecha);
  return ResponseEntity.ok(new ApiResponse<>(true, "Citas del día obtenidas exitosamente", citas));
 }

 @PatchMapping("/{idCita}/ausente")
 @Operation(summary = "Cancelar cita", description = "Permite al médico cancelar cualquier cita")
 public ResponseEntity<ApiResponse<CitaResponseDto>> cancelarCita(@PathVariable Integer idCita) {
  CitaResponseDto cita = medicoCitaService.cancelarCitaAdmin(idCita);
  return ResponseEntity.ok(new ApiResponse<>(true, "Cita cancelada exitosamente", cita));
 }

 @PatchMapping("/{idCita}/presente")
 @Operation(summary = "Completar cita", description = "Marca una cita como completada")
 public ResponseEntity<ApiResponse<CitaResponseDto>> completarCita(@PathVariable Integer idCita) {
  CitaResponseDto cita = medicoCitaService.completarCita(idCita);
  return ResponseEntity.ok(new ApiResponse<>(true, "Cita completada exitosamente", cita));
 }

 @GetMapping("/{idCita}/detalle")
 @Operation(summary = "Detalle de cita")
 public ResponseEntity<ApiResponse<CitaMedicoResponseDto>> obtenerDetalleCita(
   @PathVariable Integer idCita) {

  CitaMedicoResponseDto cita = medicoCitaService.obtenerDetalleCita(idCita);
  return ResponseEntity.ok(new ApiResponse<>(true, "Detalle de cita obtenido exitosamente", cita));
 }

 @PostMapping("/detalle")
 @Operation(summary = "Registrar detalle de cita")
 public ResponseEntity<ApiResponse<DetalleCitaResponseDto>> registrarDetalleCita(
   @Valid @RequestBody DetalleCitaRequestDto request) {

  DetalleCitaResponseDto detalle = medicoCitaService.registrarDetalleCita(request);
  return ResponseEntity.status(HttpStatus.CREATED)
    .body(new ApiResponse<>(true, "Detalle de cita registrado exitosamente", detalle));
 }

 @PutMapping("/detalle/{idDetalleCita}")
 @Operation(summary = "Actualizar detalle de cita")
 public ResponseEntity<ApiResponse<DetalleCitaResponseDto>> actualizarDetalleCita(
   @PathVariable Integer idDetalleCita,
   @Valid @RequestBody DetalleCitaRequestDto request) {

  DetalleCitaResponseDto detalle = medicoCitaService.actualizarDetalleCita(idDetalleCita, request);
  return ResponseEntity.ok(new ApiResponse<>(true, "Detalle de cita actualizado exitosamente", detalle));
 }

 @PostMapping("/enlace-reunion")
 @Operation(summary = "Enviar enlace de reunión")
 public ResponseEntity<ApiResponse<MeetingLinkResponseDto>> enviarEnlaceReunion(
   @Valid @RequestBody MeetingLinkRequestDto request) {

  MeetingLinkResponseDto resultado = medicoCitaService.enviarEnlaceReunion(request);
  return ResponseEntity.ok(new ApiResponse<>(true, "Enlace de reunión enviado exitosamente", resultado));
 }

 @GetMapping("/historial-paciente/{idPaciente}")
 @Operation(summary = "Historial del paciente")
 public ResponseEntity<ApiResponse<List<CitaHistorialResponseDto>>> obtenerHistorialPaciente(
   @PathVariable Integer idPaciente) {

  List<CitaHistorialResponseDto> historial = medicoCitaService.obtenerHistorialPaciente(idPaciente);
  return ResponseEntity.ok(new ApiResponse<>(true, "Historial del paciente obtenido exitosamente", historial));
 }

 @GetMapping("/buscar")
 @Operation(summary = "Buscar citas con filtros")
 public ResponseEntity<ApiResponse<Page<CitaMedicoResponseDto>>> buscarCitasConFiltros(
   @RequestParam(required = false) String dniPaciente,
   @RequestParam(required = false) String nombrePaciente,
   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
   @RequestParam(required = false) String estado,
   @RequestParam(defaultValue = "0") Integer page,
   @RequestParam(defaultValue = "10") Integer size) {

  BuscarCitasFiltrosRequestDto filtros = BuscarCitasFiltrosRequestDto.builder()
    .dniPaciente(dniPaciente)
    .nombrePaciente(nombrePaciente)
    .fecha(fecha)
    .estado(estado != null ? ReZherk.clinica.sistema.core.shared.enums.EstadoCita.valueOf(estado) : null)
    .page(page)
    .size(size)
    .build();

  Page<CitaMedicoResponseDto> citas = medicoCitaService.buscarCitasConFiltros(filtros);

  return ResponseEntity.ok(new ApiResponse<>(true, "Búsqueda completada exitosamente", citas));
 }
}
