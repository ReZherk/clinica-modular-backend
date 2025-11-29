package ReZherk.clinica.sistema.modules.patient.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioRepository;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import ReZherk.clinica.sistema.infrastructure.storage.FileStorageService;
import ReZherk.clinica.sistema.modules.patient.application.dto.response.FotoPerfilResponseDto;
import ReZherk.clinica.sistema.modules.patient.application.service.FotoPerfilService;
import ReZherk.clinica.sistema.modules.patient.application.validator.FotoPerfilValidator;

@Service
@RequiredArgsConstructor
@Slf4j
public class FotoPerfilServiceImpl implements FotoPerfilService {

 private final UsuarioRepository usuarioRepository;
 private final FileStorageService fileStorageService;
 private final FotoPerfilValidator fotoPerfilValidator;

 @Override
 @Transactional
 public FotoPerfilResponseDto subirFotoPerfil(Integer idUsuario, MultipartFile foto) {
  log.info("Subiendo foto de perfil para usuario ID: {}", idUsuario);

  // Validar usuario
  Usuario usuario = fotoPerfilValidator.validateUsuarioExiste(idUsuario);

  // Validar foto
  fotoPerfilValidator.validateFoto(foto);

  // Guardar archivo
  String nombreArchivo = fileStorageService.guardarArchivo(foto, idUsuario);

  // Actualizar usuario
  usuario.setFotoPerfil(nombreArchivo);
  usuarioRepository.save(usuario);

  log.info("Foto de perfil subida exitosamente: {}", nombreArchivo);

  return FotoPerfilResponseDto.builder()
    .idUsuario(idUsuario)
    .nombreArchivo(nombreArchivo)
    .urlFoto(fileStorageService.obtenerUrlArchivo(nombreArchivo))
    .mensaje("Foto de perfil subida exitosamente")
    .build();
 }

 @Override
 @Transactional
 public FotoPerfilResponseDto cambiarFotoPerfil(Integer idUsuario, MultipartFile foto) {
  log.info("Cambiando foto de perfil para usuario ID: {}", idUsuario);

  // Validar usuario
  Usuario usuario = fotoPerfilValidator.validateUsuarioExiste(idUsuario);

  // Validar foto
  fotoPerfilValidator.validateFoto(foto);

  // Eliminar foto anterior si existe
  if (usuario.getFotoPerfil() != null) {
   fileStorageService.eliminarArchivo(usuario.getFotoPerfil());
  }

  // Guardar nueva foto
  String nombreArchivo = fileStorageService.guardarArchivo(foto, idUsuario);

  // Actualizar usuario
  usuario.setFotoPerfil(nombreArchivo);
  usuarioRepository.save(usuario);

  log.info("Foto de perfil actualizada exitosamente: {}", nombreArchivo);

  return FotoPerfilResponseDto.builder()
    .idUsuario(idUsuario)
    .nombreArchivo(nombreArchivo)
    .urlFoto(fileStorageService.obtenerUrlArchivo(nombreArchivo))
    .mensaje("Foto de perfil actualizada exitosamente")
    .build();
 }

 @Override
 @Transactional
 public void eliminarFotoPerfil(Integer idUsuario) {
  log.info("Eliminando foto de perfil para usuario ID: {}", idUsuario);

  Usuario usuario = fotoPerfilValidator.validateUsuarioExiste(idUsuario);

  if (usuario.getFotoPerfil() == null) {
   log.warn("El usuario no tiene foto de perfil");
   return;
  }

  // Eliminar archivo
  fileStorageService.eliminarArchivo(usuario.getFotoPerfil());

  // Actualizar usuario
  usuario.setFotoPerfil(null);
  usuarioRepository.save(usuario);

  log.info("Foto de perfil eliminada exitosamente");
 }

 @Override
 @Transactional(readOnly = true)
 public Resource obtenerFotoPerfil(Integer idUsuario) {
  log.info("Obteniendo foto de perfil para usuario ID: {}", idUsuario);

  Usuario usuario = usuarioRepository.findById(idUsuario)
    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

  if (usuario.getFotoPerfil() == null) {
   throw new ResourceNotFoundException("El usuario no tiene foto de perfil");
  }

  return fileStorageService.cargarArchivo(usuario.getFotoPerfil());
 }

 @Override
 @Transactional(readOnly = true)
 public String obtenerUrlFotoPerfil(Integer idUsuario) {
  Usuario usuario = usuarioRepository.findById(idUsuario)
    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

  if (usuario.getFotoPerfil() == null) {
   return null;
  }

  return fileStorageService.obtenerUrlArchivo(usuario.getFotoPerfil());
 }
}