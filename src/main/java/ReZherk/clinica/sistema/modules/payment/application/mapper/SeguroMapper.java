package ReZherk.clinica.sistema.modules.payment.application.mapper;

import org.springframework.stereotype.Component;

import ReZherk.clinica.sistema.core.domain.entity.Seguro;
import ReZherk.clinica.sistema.modules.payment.application.dto.response.SeguroResponseDto;

@Component
public class SeguroMapper {

 public SeguroResponseDto toResponseDto(Seguro seguro) {
  return SeguroResponseDto.builder()
    .idSeguro(seguro.getIdSeguro())
    .nombreSeguro(seguro.getNombreSeguro())
    .descripcion(seguro.getDescripcion())
    .cubreCostoTotal(seguro.getCubreCostoTotal())
    .build();
 }
}
