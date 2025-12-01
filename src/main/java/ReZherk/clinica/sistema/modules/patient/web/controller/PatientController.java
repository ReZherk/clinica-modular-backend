package ReZherk.clinica.sistema.modules.patient.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Arrays;
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
import ReZherk.clinica.sistema.core.shared.enums.TipoTarjeta;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.CitaCancelRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.CitaCreateRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.request.HorariosDisponiblesRequestDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.CitaDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.CitaResponseDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.HorariosDisponiblesResponseDto;
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.SpecialtyResponseDto;
import ReZherk.clinica.sistema.modules.patient.application.dto.request.ChangePasswordDto;
import ReZherk.clinica.sistema.modules.patient.application.dto.request.PatientDataRequestDto;
import ReZherk.clinica.sistema.modules.patient.application.dto.response.DoctorBySpecialtyResponseDto;
import ReZherk.clinica.sistema.modules.patient.application.dto.response.PatientDataResponseDto;
import ReZherk.clinica.sistema.modules.patient.application.service.PacienteService;
import ReZherk.clinica.sistema.modules.payment.application.dto.request.PagoSeguroRequestDto;
import ReZherk.clinica.sistema.modules.payment.application.dto.request.PagoTarjetaRequestDto;
import ReZherk.clinica.sistema.modules.payment.application.dto.request.PagoYapeRequestDto;
import ReZherk.clinica.sistema.modules.payment.application.dto.request.VincularSeguroRequestDto;
import ReZherk.clinica.sistema.modules.payment.application.dto.response.PacienteSeguroResponseDto;
import ReZherk.clinica.sistema.modules.payment.application.dto.response.PagoResponseDto;
import ReZherk.clinica.sistema.modules.payment.application.dto.response.ResumenPagoDto;
import ReZherk.clinica.sistema.modules.payment.application.dto.response.SeguroResponseDto;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class PatientController {

  private final PacienteService pacienteService;

  @PostMapping("/register")
  @Operation(summary = "Registrar un nuevo paciente", description = "Permite crear un nuevo paciente en el sistema. Valida los datos enviados y retorna la información del paciente registrado.")
  public ResponseEntity<ApiResponse<PatientDataResponseDto>> registerPaciente(
      @Valid @RequestBody PatientDataRequestDto registerDto) {

    PatientDataResponseDto response = pacienteService.registerPaciente(registerDto);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>(true, "Paciente registrado exitosamente", response));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Obtener datos del paciente", description = "Devuelve toda la información registrada del paciente según su ID")
  public ResponseEntity<ApiResponse<PatientDataResponseDto>> obtenerPaciente(@PathVariable Integer id) {

    PatientDataResponseDto response = pacienteService.obtenerPaciente(id);

    return ResponseEntity.ok(
        new ApiResponse<>(true, "Datos del paciente obtenidos exitosamente", response));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> updateUsuario(@PathVariable Integer id,
      @RequestBody PatientDataRequestDto data) {

    pacienteService.modificarPaciente(id, data);

    return ResponseEntity.ok(new ApiResponse<>(true, "Se actualizo satisfactoriamente el paciente", null));
  }

  @PostMapping("/users/{userId}/change-password")
  public String cambiarPassword(@PathVariable Integer userId, @RequestBody ChangePasswordDto request) {

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
  public ResponseEntity<ApiResponse<Page<CitaDto>>> obtenerMisCitas(
      @PathVariable Integer idPaciente,
      @RequestParam(required = false) String nombrePaciente,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
      @RequestParam(required = false) String estado,
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer size) {

    Page<CitaDto> citas = pacienteService.listarCitas(idPaciente, nombrePaciente, fecha, estado, page, size);
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

  @GetMapping("/specialty")
  @Operation(summary = "Listar las especialidades activas", description = "Lista todas las especialidades activas para luego listas los medicos de estas especialidades.")
  public ResponseEntity<ApiResponse<List<SpecialtyResponseDto>>> listActiveSpecialties() {
    List<SpecialtyResponseDto> result = pacienteService.listarEspecialidades(true);
    return ResponseEntity.ok(new ApiResponse<>(true, "Se obtuvo exitosamente las especialidades activas", result));
  }

  @GetMapping("/{idSpecialty}/medicos")
  @Operation(summary = "Listar los medicos para la especialidad", description = "Lista los medicos de  la especialidad mediante el id de la  especialidad.")
  public ResponseEntity<ApiResponse<Page<DoctorBySpecialtyResponseDto>>> getDoctors(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "ASC") String sorDirection,
      @PathVariable Integer idSpecialty) {

    log.info("GET /api/patient/medico/{} - Obteniendo los médicos de esta especialidad", idSpecialty);

    try {
      Sort.Direction direction = sorDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;

      Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

      Page<DoctorBySpecialtyResponseDto> Doctors = pacienteService.getActiveMedicos(pageable,
          idSpecialty);

      log.info("Respuesta exitosa: {} medicos por especialidad obtenidos encontrados de {} totales",
          Doctors.getNumberOfElements(), Doctors.getTotalElements());

      return ResponseEntity.ok(
          new ApiResponse<>(true, "Se obtuvo medicos por especialidad.", Doctors));

    } catch (Exception e) {
      log.error("Error al obtener medicos por especialidad", e);
      return ResponseEntity.internalServerError()
          .body(new ApiResponse<>(false, "Error al obtener medicos por especialidad: " + e.getMessage(), null));
    }
  }

  ////////

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

  ////////
  @GetMapping("/tipos-tarjeta")
  @Operation(summary = "Listar tipos de tarjeta", description = "Devuelve todos los tipos de tarjeta que acepta el sistema")
  public ResponseEntity<ApiResponse<List<String>>> listarTiposTarjeta() {
    List<String> tipos = Arrays.stream(TipoTarjeta.values())
        .map(Enum::name)
        .toList();

    return ResponseEntity.ok(
        new ApiResponse<>(true, "Tipos de tarjeta obtenidos exitosamente", tipos));
  }

  @PostMapping("/tarjeta")
  @Operation(summary = "Pagar con tarjeta", description = "Simula el proceso de pago de una cita usando tarjeta de credito o debito")
  public ResponseEntity<ApiResponse<PagoResponseDto>> pagarConTarjeta(
      @Valid @RequestBody PagoTarjetaRequestDto request) {

    PagoResponseDto pago = pacienteService.procesarPagoConTarjeta(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>(true, "Pago procesado exitosamente", pago));
  }

  @PostMapping("/yape")
  @Operation(summary = "Pagar con Yape", description = "Simula el proceso de pago de una cita usando Yape")
  public ResponseEntity<ApiResponse<PagoResponseDto>> pagarConYape(
      @Valid @RequestBody PagoYapeRequestDto request) {

    PagoResponseDto pago = pacienteService.procesarPagoConYape(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>(true, "Pago con Yape procesado exitosamente", pago));
  }

  @PostMapping("/seguro")
  @Operation(summary = "Pagar con seguro", description = "Registra el pago de una cita cubierto por seguro medico")
  public ResponseEntity<ApiResponse<PagoResponseDto>> pagarConSeguro(
      @Valid @RequestBody PagoSeguroRequestDto request) {

    PagoResponseDto pago = pacienteService.procesarPagoConSeguro(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>(true, "Pago con seguro registrado exitosamente", pago));
  }

  @GetMapping("/resumen/{idCita}")
  @Operation(summary = "Obtener resumen de pago", description = "Obtiene el resumen del pago de una cita con monto y datos del medico")
  public ResponseEntity<ApiResponse<ResumenPagoDto>> obtenerResumenPago(
      @PathVariable Integer idCita) {

    ResumenPagoDto resumen = pacienteService.obtenerResumenPago(idCita);
    return ResponseEntity.ok(new ApiResponse<>(true, "Resumen de pago obtenido  correctamente", resumen));
  }

  @GetMapping("/pago/{idPago}")
  @Operation(summary = "Obtener detalle de pago", description = "Obtiene el detalle completo de un pago especifico")
  public ResponseEntity<ApiResponse<PagoResponseDto>> obtenerDetallePago(@PathVariable Integer idPago) {
    PagoResponseDto pago = pacienteService.obtenerDetallePago(idPago);
    return ResponseEntity.ok(new ApiResponse<>(true, "Detalle de pago obtenido exitosamente", pago));
  }

  @GetMapping("/cita/{idCita}")
  @Operation(summary = "Obtener pago de cita", description = "Obtiene el pago asociado a una cita especifica")
  public ResponseEntity<ApiResponse<PagoResponseDto>> obtenerPagoPorCita(
      @PathVariable Integer idCita) {

    PagoResponseDto pago = pacienteService.obtenerPagoPorCita(idCita);
    return ResponseEntity.ok(new ApiResponse<>(true, "Pago de cita obtenido exitosamente", pago));
  }

  @GetMapping("/historial/{idPaciente}")
  @Operation(summary = "Historial de pagos", description = "Lista todos los pagos realizados por el paciente")
  public ResponseEntity<ApiResponse<Page<PagoResponseDto>>> obtenerHistorialPagos(
      @PathVariable Integer idPaciente,
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer size) {

    Page<PagoResponseDto> pagos = pacienteService.obtenerHistorialPagos(idPaciente, page, size);
    return ResponseEntity.ok(new ApiResponse<>(true, "Historial de pagos obtenido exitosamente", pagos));
  }
}
