package ReZherk.clinica.sistema.modules.patient.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ReZherk.clinica.sistema.core.application.dto.ApiResponse;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.CitaCancelRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.CitaCreateRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.HorariosDisponiblesRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.CitaResponseDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.HorariosDisponiblesResponseDto;
import ReZherk.clinica.sistema.modules.patient.application.dto.request.ChangePasswordDto;
import ReZherk.clinica.sistema.modules.patient.application.dto.request.RegisterPacienteDto;
import ReZherk.clinica.sistema.modules.patient.application.dto.response.RegisterResponseDto;
import ReZherk.clinica.sistema.modules.patient.application.service.PacienteService;
import ReZherk.clinica.sistema.modules.payment.application.dto.request.VincularSeguroRequestDto;
import ReZherk.clinica.sistema.modules.payment.application.dto.response.PacienteSeguroResponseDto;
import ReZherk.clinica.sistema.modules.payment.application.dto.response.SeguroResponseDto;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PatientController {

  private final PacienteService pacienteService;

  @PostMapping("/register")
  public ResponseEntity<RegisterResponseDto> registerPaciente(
      @Valid @RequestBody RegisterPacienteDto registerDto) {
    try {
      RegisterResponseDto response = pacienteService.registerPaciente(registerDto);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(new RegisterResponseDto(false, "Error en registro: " + e.getMessage()));
    }
  }

  @PostMapping("/change-password")
  public String cambiarPassword(@RequestBody ChangePasswordDto request,
      @RequestAttribute("userId") Integer userId) {

    pacienteService.cambiarPassword(userId, request.getPasswordActual(), request.getPasswordNueva());
    return "Contraseña actualizada correctamente";
  }

  /////////////////////////////////////////////////////////////////////

  @PostMapping("/create-appointment")
  @Operation(summary = "Crear nueva cita", description = "Permite al paciente crear una nueva cita médica")
  public ResponseEntity<ApiResponse<CitaResponseDto>> crearCita(@Valid @RequestBody CitaCreateRequestDto request) {
    CitaResponseDto cita = pacienteService.crearCita(request);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>(true, "Se creo la cita satisfactoriamente", cita));
  }

  @PutMapping("/cancelar-appointment")
  @Operation(summary = "Cancelar cita", description = "Permite al paciente cancelar una cita reservada")
  public ResponseEntity<ApiResponse<CitaResponseDto>> cancelarCita(@Valid @RequestBody CitaCancelRequestDto request) {
    CitaResponseDto cita = pacienteService.cancelarCita(request);
    return ResponseEntity.ok(new ApiResponse<>(true, "Cita cancelada exitosamente", cita));
  }

  @GetMapping("/mis-citas/{idPaciente}")
  @Operation(summary = "Obtener mis citas", description = "Lista todas las citas del paciente autenticado")
  public ResponseEntity<ApiResponse<Page<CitaResponseDto>>> obtenerMisCitas(
      @PathVariable Integer idPaciente,
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer size) {

    Page<CitaResponseDto> citas = pacienteService.listarCitas(idPaciente, page, size);
    return ResponseEntity.ok(new ApiResponse<>(true, "Citas obtenidas exitosamente", citas));
  }

  @GetMapping("/{idCita}/paciente/{idPaciente}")
  @Operation(summary = "Obtener detalle de cita", description = "Obtiene el detalle completo de una cita específica")
  public ResponseEntity<ApiResponse<CitaResponseDto>> obtenerDetalleCita(
      @PathVariable Integer idCita,
      @PathVariable Integer idPaciente) {

    CitaResponseDto cita = pacienteService.detalleCita(idCita, idPaciente);
    return ResponseEntity.ok(new ApiResponse<>(true, "Detalle de cita obtenido exitosamente", cita));
  }

  @PostMapping("/horarios-disponibles")
  @Operation(summary = "Listar horarios disponibles", description = "Lista todos los horarios libres de un médico en una fecha específica")
  public ResponseEntity<ApiResponse<HorariosDisponiblesResponseDto>> listarHorariosDisponibles(
      @Valid @RequestBody HorariosDisponiblesRequestDto request) {

    HorariosDisponiblesResponseDto horarios = pacienteService.listarHorariosDisponibles(request);
    return ResponseEntity.ok(new ApiResponse<>(true, "Horarios disponibles obtenidos exitosamente", horarios));
  }

  @GetMapping("/convenios")
  @Operation(summary = "Listar seguros con convenio", description = "Lista todos los seguros que tienen convenio con la clinica y cubren el costo total")
  public ResponseEntity<ApiResponse<List<SeguroResponseDto>>> listarSegurosConConvenio() {
    List<SeguroResponseDto> seguros = pacienteService.listarSegurosConConvenio();
    return ResponseEntity.ok(new ApiResponse<>(true, "Se listo los seguros  con cobertura completa", seguros));
  }

  @PostMapping("/vincular")
  @Operation(summary = "Vincular seguro", description = "Vincula un seguro al paciente con su numero de poliza y fechas de vigencia")
  public ResponseEntity<ApiResponse<PacienteSeguroResponseDto>> vincularSeguro(
      @Valid @RequestBody VincularSeguroRequestDto request) {

    PacienteSeguroResponseDto seguro = pacienteService.vincularSeguro(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>(true, "Seguro vinculado exitosamente", seguro));
  }

}
