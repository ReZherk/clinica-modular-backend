package ReZherk.clinica.sistema.modules.patient.application.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import ReZherk.clinica.sistema.modules.patient.application.dto.response.FotoPerfilResponseDto;

public interface FotoPerfilService {

 FotoPerfilResponseDto subirFotoPerfil(Integer idUsuario, MultipartFile foto);

 FotoPerfilResponseDto cambiarFotoPerfil(Integer idUsuario, MultipartFile foto);

 void eliminarFotoPerfil(Integer idUsuario);

 Resource obtenerFotoPerfil(Integer idUsuario);

 String obtenerUrlFotoPerfil(Integer idUsuario);
}
