package ReZherk.clinica.sistema.mapper;

import ReZherk.clinica.sistema.dto.UsuarioRequestDTO;
import ReZherk.clinica.sistema.dto.UsuarioResponseDTO;
import ReZherk.clinica.sistema.entity.Usuario;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

 Usuario toEntity(UsuarioRequestDTO dto);

 UsuarioResponseDTO toResponseDto(Usuario entity);
}
