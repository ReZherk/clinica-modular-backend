package ReZherk.clinica.sistema.mapper;

import ReZherk.clinica.sistema.dto.CitaRequestDTO;
import ReZherk.clinica.sistema.dto.CitaResponseDTO;
import ReZherk.clinica.sistema.entity.Cita;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CitaMapper {

 // Mapeo Entity -> ResponseDTO (con propiedades anidadas)
 @Mapping(source = "paciente.usuario.nombres", target = "nombrePaciente")
 @Mapping(source = "medico.usuario.nombres", target = "nombreMedico")
 @Mapping(source = "especialidad.nombreEspecialidad", target = "especialidad")
 @Mapping(source = "sede.nombre", target = "sede")
 @Mapping(source = "tarifa.monto", target = "tarifa")
 CitaResponseDTO toResponseDto(Cita entity);

 // Mapeo RequestDTO -> Entity (ignorando relaciones que se setean manualmente)
 @Mapping(target = "paciente", ignore = true)
 @Mapping(target = "medico", ignore = true)
 @Mapping(target = "especialidad", ignore = true)
 @Mapping(target = "sede", ignore = true)
 @Mapping(target = "tarifa", ignore = true)
 @Mapping(target = "id", ignore = true)
 @Mapping(target = "estado", ignore = true) // Se usa @Builder.Default = true
 Cita toEntity(CitaRequestDTO dto);
}