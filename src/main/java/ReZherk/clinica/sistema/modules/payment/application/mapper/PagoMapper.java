package ReZherk.clinica.sistema.modules.payment.application.mapper;

import org.springframework.stereotype.Component;

import ReZherk.clinica.sistema.core.domain.entity.Cita;
import ReZherk.clinica.sistema.core.domain.entity.MedicoDetalle;
import ReZherk.clinica.sistema.core.domain.entity.Pago;
import ReZherk.clinica.sistema.modules.payment.application.dto.response.PagoResponseDto;

@Component
public class PagoMapper {

 public PagoResponseDto toResponseDto(Pago pago) {
  Cita cita = pago.getCita();
  MedicoDetalle medicoDetalle = cita.getMedicoHorario().getMedico().getMedicoDetalle();

  return PagoResponseDto.builder()
    .idPago(pago.getIdPago())
    .idCita(cita.getIdCita())
    .monto(pago.getMonto())
    .metodoPago(pago.getMetodoPago())
    .estadoPago(pago.getEstadoPago())
    .numeroReferencia(pago.getNumeroReferencia())
    .fechaPago(pago.getFechaPago())
    .datosPago(pago.getDatosPago())
    .cita(PagoResponseDto.CitaInfoDto.builder()
      .idCita(cita.getIdCita())
      .nombreMedico(cita.getMedicoHorario().getMedico().getNombres() + " " +
        cita.getMedicoHorario().getMedico().getApellidos())
      .especialidad(medicoDetalle.getEspecialidad().getNombreEspecialidad())
      .fecha(cita.getFecha().toString())
      .hora(cita.getHora().toString())
      .build())
    .build();
 }
}
