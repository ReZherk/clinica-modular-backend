package ReZherk.clinica.sistema.modules.payment.application.mapper;

import org.springframework.stereotype.Component;

import ReZherk.clinica.sistema.core.domain.entity.PacienteSeguro;
import ReZherk.clinica.sistema.modules.payment.application.dto.response.PacienteSeguroResponseDto;

@Component
public class PacienteSeguroMapper {

 public PacienteSeguroResponseDto toResponseDto(PacienteSeguro pacienteSeguro) {
  return PacienteSeguroResponseDto.builder()
    .idPacienteSeguro(pacienteSeguro.getIdPacienteSeguro())
    .nombreSeguro(pacienteSeguro.getSeguro().getNombreSeguro())
    .numeroPoliza(pacienteSeguro.getNumeroPoliza())
    .fechaVigenciaInicio(pacienteSeguro.getFechaVigenciaInicio())
    .fechaVigenciaFin(pacienteSeguro.getFechaVigenciaFin())
    .estadoActivo(pacienteSeguro.getEstadoActivo())
    .vigente(pacienteSeguro.isVigente())
    .build();
 }
}
