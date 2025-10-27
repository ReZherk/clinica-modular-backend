package ReZherk.clinica.sistema.modules.admin.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import ReZherk.clinica.sistema.core.domain.entity.Especialidad;
import ReZherk.clinica.sistema.core.domain.entity.MedicoDetalle;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.SpecialtyUpdateDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.MedicoDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.SpecialtyResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.SpecialtyUpdateResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.SpecialtyWithDoctorsResponseDto;

public class EspecialidadMapper {

  public static SpecialtyWithDoctorsResponseDto toDto(Especialidad especialidad, List<MedicoDetalle> medicos) {

    List<MedicoDto> activos = medicos.stream()
        .filter(m -> Boolean.TRUE.equals(m.getUsuario().getEstadoRegistro()))
        .map(m -> MedicoDto.builder()
            .idUsuario(m.getUsuario().getId())
            .nombres(m.getUsuario().getNombres())
            .apellidos(m.getUsuario().getApellidos())
            .cmp(m.getCmp())
            .activo(true)
            .build())
        .collect(Collectors.toList());

    List<MedicoDto> inactivos = medicos.stream()
        .filter(m -> Boolean.FALSE.equals(m.getUsuario().getEstadoRegistro()))
        .map(m -> MedicoDto.builder()
            .idUsuario(m.getUsuario().getId())
            .nombres(m.getUsuario().getNombres())
            .apellidos(m.getUsuario().getApellidos())
            .cmp(m.getCmp())
            .activo(false)
            .build())
        .collect(Collectors.toList());

    return SpecialtyWithDoctorsResponseDto.builder()
        .id(especialidad.getId())
        .nombreEspecialidad(especialidad.getNombreEspecialidad())
        .descripcion(especialidad.getDescripcion())
        .medicosActivos(activos)
        .medicosInactivos(inactivos)
        .build();
  }

  public static SpecialtyResponseDto toSimpleDto(Especialidad especialidad) {
    return SpecialtyResponseDto.builder()
        .id(especialidad.getId())
        .nombreEspecialidad(especialidad.getNombreEspecialidad())
        .descripcion(especialidad.getDescripcion())
        .costo(especialidad.getTarifa())
        .activo(especialidad.getEstadoRegistro())
        .duracion(especialidad.getDuracion())
        .build();
  }

  public static void updateEntity(Especialidad entity, SpecialtyUpdateDto dto) {
    if (dto.getNombreEspecialidad() != null) {
      entity.setNombreEspecialidad(dto.getNombreEspecialidad());
    }
    if (dto.getDescripcion() != null) {
      entity.setDescripcion(dto.getDescripcion());
    }
    if (dto.getTarifa() != null) {
      entity.setTarifa(dto.getTarifa());
    }

    if (dto.getDuracion() != null) {
      entity.setDuracion(dto.getDuracion());
    }

    if (dto.getEstadoRegistro() != null) {
      entity.setEstadoRegistro(dto.getEstadoRegistro());
    }

  }

  public static SpecialtyUpdateResponseDto toUpdateResponseDto(Especialidad entity) {
    return SpecialtyUpdateResponseDto.builder()
        .id(entity.getId())
        .nombreEspecialidad(entity.getNombreEspecialidad())
        .descripcion(entity.getDescripcion())
        .tarifa(entity.getTarifa())
        .duracion(entity.getDuracion())
        .estadoRegistro(entity.getEstadoRegistro())
        .build();
  }

}
