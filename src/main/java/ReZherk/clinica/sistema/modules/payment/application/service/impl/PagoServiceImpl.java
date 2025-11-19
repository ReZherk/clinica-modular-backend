package ReZherk.clinica.sistema.modules.payment.application.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

import ReZherk.clinica.sistema.core.domain.entity.Cita;
import ReZherk.clinica.sistema.core.domain.entity.Especialidad;
import ReZherk.clinica.sistema.core.domain.entity.MedicoDetalle;
import ReZherk.clinica.sistema.core.domain.entity.PacienteSeguro;
import ReZherk.clinica.sistema.core.domain.entity.Pago;
import ReZherk.clinica.sistema.core.domain.repository.CitaRepository;
import ReZherk.clinica.sistema.core.domain.repository.PacienteSeguroRepository;
import ReZherk.clinica.sistema.core.domain.repository.PagoRepository;
import ReZherk.clinica.sistema.core.shared.enums.EstadoPago;
import ReZherk.clinica.sistema.core.shared.enums.MetodoPago;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import ReZherk.clinica.sistema.modules.payment.application.dto.request.PagoSeguroRequestDto;
import ReZherk.clinica.sistema.modules.payment.application.dto.request.PagoTarjetaRequestDto;
import ReZherk.clinica.sistema.modules.payment.application.dto.request.PagoYapeRequestDto;
import ReZherk.clinica.sistema.modules.payment.application.dto.response.PagoResponseDto;
import ReZherk.clinica.sistema.modules.payment.application.dto.response.ResumenPagoDto;
import ReZherk.clinica.sistema.modules.payment.application.mapper.PagoMapper;
import ReZherk.clinica.sistema.modules.payment.application.service.PagoService;
import ReZherk.clinica.sistema.modules.payment.application.validator.PagoValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PagoServiceImpl implements PagoService {

 private final PagoRepository pagoRepository;
 private final CitaRepository citaRepository;
 private final PacienteSeguroRepository pacienteSeguroRepository;
 private final PagoMapper pagoMapper;
 private final PagoValidator pagoValidator;
 private final ObjectMapper objectMapper;

 @Override
 @Transactional(readOnly = true)
 public ResumenPagoDto obtenerResumenPago(Integer idCita) {
  log.info("Obteniendo resumen de pago para cita ID: {}", idCita);

  Cita cita = citaRepository.findByIdWithDetails(idCita)
    .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));

  MedicoDetalle medicoDetalle = cita.getMedicoHorario().getMedico().getMedicoDetalle();
  Especialidad especialidad = medicoDetalle.getEspecialidad();

  // Verificar si el paciente tiene seguro vigente con convenio
  boolean tieneSeguros = pacienteSeguroRepository
    .findSeguroVigente(cita.getPaciente().getId(), LocalDate.now())
    .isPresent();

  return ResumenPagoDto.builder()
    .idCita(cita.getIdCita())
    .nombreMedico(cita.getMedicoHorario().getMedico().getNombres() + " " +
      cita.getMedicoHorario().getMedico().getApellidos())
    .especialidad(especialidad.getNombreEspecialidad())
    .fecha(cita.getFecha().toString())
    .hora(cita.getHora().toString())
    .monto(especialidad.getTarifa())
    .requierePago(!tieneSeguros)
    .motivoSinPago(tieneSeguros ? "Cubierto por seguro" : null)
    .build();
 }

 @Override
 @Transactional
 public PagoResponseDto procesarPagoConTarjeta(PagoTarjetaRequestDto request) {
  log.info("Procesando pago con tarjeta para cita ID: {}", request.getIdCita());

  // Validar cita
  Cita cita = pagoValidator.validateCitaParaPago(request.getIdCita());

  // Validar datos de tarjeta
  pagoValidator.validateDatosTarjeta(
    request.getNumeroTarjeta(),
    request.getTitular(),
    request.getFechaExpiracion(),
    request.getCvv());

  // Simular procesamiento de pago
  boolean pagoExitoso = simularPagoTarjeta();

  // Crear datos de pago en JSON
  Map<String, String> datosPago = new HashMap<>();
  datosPago.put("tipo_tarjeta", request.getTipoTarjeta().name());
  datosPago.put("ultimos_digitos", request.getNumeroTarjeta().substring(12));
  datosPago.put("titular", request.getTitular());
  datosPago.put("fecha_expiracion", request.getFechaExpiracion());

  String datosPagoJson = convertToJson(datosPago);

  // Crear registro de pago
  Pago pago = Pago.builder()
    .cita(cita)
    .paciente(cita.getPaciente())
    .monto(cita.getMedicoHorario().getMedico().getMedicoDetalle().getEspecialidad().getTarifa())
    .metodoPago(MetodoPago.TARJETA)
    .estadoPago(pagoExitoso ? EstadoPago.COMPLETADO : EstadoPago.FALLIDO)
    .datosPago(datosPagoJson)
    .build();

  pago = pagoRepository.save(pago);

  // Vincular pago a cita
  if (pagoExitoso) {
   cita.setPago(pago);
   citaRepository.save(cita);
  }

  log.info("Pago procesado: {}, Referencia: {}", pago.getEstadoPago(), pago.getNumeroReferencia());

  // Recargar con relaciones
  pago = pagoRepository.findById(pago.getIdPago())
    .orElseThrow(() -> new ResourceNotFoundException("Error al recargar pago"));

  return pagoMapper.toResponseDto(pago);
 }

 @Override
 @Transactional
 public PagoResponseDto procesarPagoConYape(PagoYapeRequestDto request) {
  log.info("Procesando pago con Yape para cita ID: {}", request.getIdCita());

  // Validar cita
  Cita cita = pagoValidator.validateCitaParaPago(request.getIdCita());

  // Validar datos de Yape
  pagoValidator.validateDatosYape(request.getNumeroTelefono(), request.getCodigoOperacion());

  // Simular procesamiento de pago
  boolean pagoExitoso = simularPagoYape();

  // Crear datos de pago en JSON
  Map<String, String> datosPago = new HashMap<>();
  datosPago.put("numero_telefono", request.getNumeroTelefono());
  datosPago.put("codigo_operacion", request.getCodigoOperacion());

  String datosPagoJson = convertToJson(datosPago);

  // Crear registro de pago
  Pago pago = Pago.builder()
    .cita(cita)
    .paciente(cita.getPaciente())
    .monto(cita.getMedicoHorario().getMedico().getMedicoDetalle().getEspecialidad().getTarifa())
    .metodoPago(MetodoPago.YAPE)
    .estadoPago(pagoExitoso ? EstadoPago.COMPLETADO : EstadoPago.FALLIDO)
    .datosPago(datosPagoJson)
    .build();

  pago = pagoRepository.save(pago);

  // Vincular pago a cita
  if (pagoExitoso) {
   cita.setPago(pago);
   citaRepository.save(cita);
  }

  log.info("Pago Yape procesado: {}, Referencia: {}", pago.getEstadoPago(), pago.getNumeroReferencia());

  // Recargar con relaciones
  pago = pagoRepository.findById(pago.getIdPago())
    .orElseThrow(() -> new ResourceNotFoundException("Error al recargar pago"));

  return pagoMapper.toResponseDto(pago);
 }

 @Override
 @Transactional
 public PagoResponseDto procesarPagoConSeguro(PagoSeguroRequestDto request) {
  log.info("Procesando pago con seguro para cita ID: {}", request.getIdCita());

  // Validar cita
  Cita cita = pagoValidator.validateCitaParaPago(request.getIdCita());

  // Validar que el paciente tenga seguro vigente
  PacienteSeguro pacienteSeguro = pagoValidator.validateSeguroVigente(request.getIdPaciente());

  // Crear datos de pago en JSON
  Map<String, String> datosPago = new HashMap<>();
  datosPago.put("nombre_seguro", pacienteSeguro.getSeguro().getNombreSeguro());
  datosPago.put("numero_poliza", pacienteSeguro.getNumeroPoliza());
  datosPago.put("cobertura", "100%");

  String datosPagoJson = convertToJson(datosPago);

  // Crear registro de pago (monto 0 porque esta cubierto)
  Pago pago = Pago.builder()
    .cita(cita)
    .paciente(cita.getPaciente())
    .monto(BigDecimal.ZERO) // Cubierto por seguro
    .metodoPago(MetodoPago.SEGURO)
    .estadoPago(EstadoPago.COMPLETADO)
    .datosPago(datosPagoJson)
    .pacienteSeguro(pacienteSeguro)
    .build();

  pago = pagoRepository.save(pago);

  // Vincular pago a cita
  cita.setPago(pago);
  citaRepository.save(cita);

  log.info("Pago con seguro completado, Referencia: {}", pago.getNumeroReferencia());

  // Recargar con relaciones
  pago = pagoRepository.findById(pago.getIdPago())
    .orElseThrow(() -> new ResourceNotFoundException("Error al recargar pago"));

  return pagoMapper.toResponseDto(pago);
 }

 @Override
 @Transactional(readOnly = true)
 public PagoResponseDto obtenerDetallePago(Integer idPago) {
  log.info("Obteniendo detalle de pago ID: {}", idPago);

  Pago pago = pagoRepository.findById(idPago)
    .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado"));

  return pagoMapper.toResponseDto(pago);
 }

 @Override
 @Transactional(readOnly = true)
 public PagoResponseDto obtenerPagoPorCita(Integer idCita) {
  log.info("Obteniendo pago de cita ID: {}", idCita);

  Pago pago = pagoRepository.findByCitaId(idCita)
    .orElseThrow(() -> new ResourceNotFoundException("No se encontro pago para esta cita"));

  return pagoMapper.toResponseDto(pago);
 }

 @Override
 @Transactional(readOnly = true)
 public Page<PagoResponseDto> obtenerHistorialPagos(Integer idPaciente, Integer page, Integer size) {
  log.info("Obteniendo historial de pagos del paciente ID: {}", idPaciente);

  Pageable pageable = PageRequest.of(page, size);
  Page<Pago> pagos = pagoRepository.findByPacienteId(idPaciente, pageable);

  return pagos.map(pagoMapper::toResponseDto);
 }

 // Metodos auxiliares de simulacion
 private boolean simularPagoTarjeta() {
  // Simula un 95% de exito
  // return Math.random() < 0.95;
  return true;
 }

 private boolean simularPagoYape() {
  // Simula un 98% de exito
  // return Math.random() < 0.98;
  return true;
 }

 private String convertToJson(Map<String, String> data) {
  try {
   return objectMapper.writeValueAsString(data);
  } catch (Exception e) {
   log.error("Error al convertir datos a JSON", e);
   return "{}";
  }
 }
}
