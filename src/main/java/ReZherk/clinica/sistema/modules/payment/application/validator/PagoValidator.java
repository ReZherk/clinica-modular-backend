package ReZherk.clinica.sistema.modules.payment.application.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import ReZherk.clinica.sistema.core.domain.entity.Cita;
import ReZherk.clinica.sistema.core.domain.entity.PacienteSeguro;
import ReZherk.clinica.sistema.core.domain.repository.CitaRepository;
import ReZherk.clinica.sistema.core.domain.repository.PacienteSeguroRepository;
import ReZherk.clinica.sistema.core.domain.repository.PagoRepository;
import ReZherk.clinica.sistema.core.shared.enums.EstadoCita;
import ReZherk.clinica.sistema.core.shared.enums.EstadoPago;
import ReZherk.clinica.sistema.core.shared.exception.BusinessException;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class PagoValidator {

 private final CitaRepository citaRepository;
 private final PagoRepository pagoRepository;
 private final PacienteSeguroRepository pacienteSeguroRepository;

 /**
  * Valida que la cita exista y no tenga pago completado
  */
 public Cita validateCitaParaPago(Integer idCita) {
  Cita cita = citaRepository.findByIdWithDetails(idCita)
    .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));

  // Verificar que la cita este en estado RESERVADA
  if (cita.getEstado() != EstadoCita.RESERVADA) {
   throw new BusinessException("Solo se pueden pagar citas en estado RESERVADA");
  }

  // Verificar que no tenga un pago completado
  if (pagoRepository.existsByCitaIdCitaAndEstadoPago(idCita, EstadoPago.COMPLETADO)) {
   throw new BusinessException("Esta cita ya tiene un pago completado");
  }

  return cita;
 }

 /**
  * Valida datos de tarjeta
  */
 public void validateDatosTarjeta(String numeroTarjeta, String titular, String fechaExpiracion, String cvv) {
  // Validar numero de tarjeta (Algoritmo de Luhn simplificado)
  if (!Pattern.matches("^[0-9]{16}$", numeroTarjeta)) {
   throw new BusinessException("Numero de tarjeta invalido");
  }

  // Validar titular
  if (titular == null || titular.trim().length() < 3) {
   throw new BusinessException("Titular de tarjeta invalido");
  }

  // Validar fecha de expiracion
  if (!validateFechaExpiracion(fechaExpiracion)) {
   throw new BusinessException("Tarjeta expirada o fecha invalida");
  }

  // Validar CVV
  if (!Pattern.matches("^[0-9]{3,4}$", cvv)) {
   throw new BusinessException("CVV invalido");
  }
 }

 /**
  * Valida fecha de expiracion MM/YY
  */
 private boolean validateFechaExpiracion(String fechaExpiracion) {
  try {
   String[] parts = fechaExpiracion.split("/");
   if (parts.length != 2)
    return false;

   int mes = Integer.parseInt(parts[0]);
   int anio = 2000 + Integer.parseInt(parts[1]);

   if (mes < 1 || mes > 12)
    return false;

   YearMonth expiracion = YearMonth.of(anio, mes);
   YearMonth actual = YearMonth.now();

   return !expiracion.isBefore(actual);
  } catch (Exception e) {
   return false;
  }
 }

 /**
  * Valida datos de Yape
  */
 public void validateDatosYape(String numeroTelefono, String codigoOperacion) {
  if (!Pattern.matches("^9[0-9]{8}$", numeroTelefono)) {
   throw new BusinessException("Numero de telefono Yape invalido");
  }

  if (codigoOperacion == null || codigoOperacion.trim().length() < 6) {
   throw new BusinessException("Codigo de operacion Yape invalido");
  }
 }

 /**
  * Valida que el paciente tenga seguro vigente
  */
 public PacienteSeguro validateSeguroVigente(Integer idPaciente) {
  return pacienteSeguroRepository.findSeguroVigente(idPaciente, LocalDate.now())
    .orElseThrow(() -> new BusinessException(
      "El paciente no tiene un seguro vigente o activo"));
 }

 /**
  * Valida vincular seguro
  */
 public void validateVincularSeguro(Integer idPaciente, LocalDate fechaInicio, LocalDate fechaFin) {
  if (fechaFin.isBefore(fechaInicio)) {
   throw new BusinessException("La fecha de fin debe ser posterior a la fecha de inicio");
  }

  if (fechaFin.isBefore(LocalDate.now())) {
   throw new BusinessException("La fecha de fin no puede ser anterior a la fecha actual");
  }

  // Verificar si ya tiene un seguro activo
  pacienteSeguroRepository.findByPacienteIdAndEstadoActivoTrue(idPaciente)
    .ifPresent(ps -> {
     throw new BusinessException("El paciente ya tiene un seguro activo. " +
       "Desactive el seguro actual antes de vincular uno nuevo.");
    });
 }
}