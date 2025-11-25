package ReZherk.clinica.sistema.modules.medico.application.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import ReZherk.clinica.sistema.core.domain.entity.Cita;
import ReZherk.clinica.sistema.core.domain.repository.CitaRepository;
import ReZherk.clinica.sistema.core.domain.repository.DetalleCitaRepository;
import ReZherk.clinica.sistema.core.shared.enums.EstadoCita;
import ReZherk.clinica.sistema.core.shared.exception.BusinessException;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;

@Component
@RequiredArgsConstructor
public class MedicoCitaValidator {

 private final CitaRepository citaRepository;
 private final DetalleCitaRepository detalleCitaRepository;

 /**
  * Valida que la cita exista y pueda registrarse detalle
  */
 public Cita validateCitaParaDetalle(Integer idCita) {
  Cita cita = citaRepository.findByIdWithDetails(idCita)
    .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));

  if (cita.getEstado() == EstadoCita.CANCELADA) {
   throw new BusinessException("No se puede registrar detalle en una cita cancelada");
  }

  return cita;
 }

 /**
  * Valida que el detalle no exista ya
  */
 public void validateDetalleNoExiste(Integer idCita) {
  if (detalleCitaRepository.existsByCitaIdCita(idCita)) {
   throw new BusinessException("Esta cita ya tiene un detalle registrado. Use la opcion de actualizacion.");
  }
 }

 /**
  * Valida formato de URL
  */
 public void validateEnlaceReunion(String enlace) {
  if (enlace == null || enlace.trim().isEmpty()) {
   throw new BusinessException("El enlace de reunion no puede estar vacio");
  }

  if (!enlace.matches("^https?://.*")) {
   throw new BusinessException("El enlace debe ser una URL valida que comience con http:// o https://");
  }

  if (enlace.length() > 500) {
   throw new BusinessException("El enlace no puede exceder 500 caracteres");
  }
 }
}
