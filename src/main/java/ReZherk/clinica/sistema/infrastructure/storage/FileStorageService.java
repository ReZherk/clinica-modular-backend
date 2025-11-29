package ReZherk.clinica.sistema.infrastructure.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import ReZherk.clinica.sistema.core.shared.exception.BusinessException;
import ReZherk.clinica.sistema.infrastructure.config.FileStorageProperties;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageService {

 private final Path fileStorageLocation;
 private final String baseUrl;

 public FileStorageService(FileStorageProperties fileStorageProperties) {
  this.fileStorageLocation = Paths.get(fileStorageProperties.getDir())
    .toAbsolutePath().normalize();
  this.baseUrl = fileStorageProperties.getBaseUrl();

  try {
   Files.createDirectories(this.fileStorageLocation);
   log.info("Directorio de almacenamiento creado: {}", this.fileStorageLocation);
  } catch (Exception ex) {
   throw new BusinessException("No se pudo crear el directorio de almacenamiento", ex);
  }
 }

 /**
  * Guarda un archivo en el sistema de archivos
  */
 public String guardarArchivo(MultipartFile file, Integer idUsuario) {
  // Validar archivo
  validarArchivo(file);

  // Generar nombre unico para el archivo
  String extension = obtenerExtension(file.getOriginalFilename());
  String nombreArchivo = String.format("user_%d_%s.%s",
    idUsuario,
    UUID.randomUUID().toString(),
    extension);

  try {
   Path targetLocation = this.fileStorageLocation.resolve(nombreArchivo);
   Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

   log.info("Archivo guardado exitosamente: {}", nombreArchivo);
   return nombreArchivo;
  } catch (IOException ex) {
   log.error("Error al guardar archivo", ex);
   throw new BusinessException("No se pudo guardar el archivo", ex);
  }
 }

 /**
  * Elimina un archivo del sistema de archivos
  */
 public void eliminarArchivo(String nombreArchivo) {
  if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
   return;
  }

  try {
   Path filePath = this.fileStorageLocation.resolve(nombreArchivo).normalize();
   Files.deleteIfExists(filePath);
   log.info("Archivo eliminado: {}", nombreArchivo);
  } catch (IOException ex) {
   log.error("Error al eliminar archivo: {}", nombreArchivo, ex);
   // No lanzar excepcion, solo registrar
  }
 }

 /**
  * Carga un archivo como recurso
  */
 public Resource cargarArchivo(String nombreArchivo) {
  try {
   Path filePath = this.fileStorageLocation.resolve(nombreArchivo).normalize();
   Resource resource = new UrlResource(filePath.toUri());

   if (resource.exists()) {
    return resource;
   } else {
    throw new BusinessException("Archivo no encontrado: " + nombreArchivo);
   }
  } catch (MalformedURLException ex) {
   throw new BusinessException("Archivo no encontrado: " + nombreArchivo, ex);
  }
 }

 /**
  * Obtiene la URL completa del archivo
  */
 public String obtenerUrlArchivo(String nombreArchivo) {
  if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
   return null;
  }
  return baseUrl + "/" + nombreArchivo;
 }

 /**
  * Valida el archivo subido
  */
 private void validarArchivo(MultipartFile file) {
  if (file.isEmpty()) {
   throw new BusinessException("El archivo esta vacio");
  }

  // Validar extension
  String extension = obtenerExtension(file.getOriginalFilename());
  if (!esExtensionPermitida(extension)) {
   throw new BusinessException("Tipo de archivo no permitido. Solo se permiten: JPG, JPEG, PNG");
  }

  // Validar tamano (5MB)
  long maxSize = 5 * 1024 * 1024; // 5MB en bytes
  if (file.getSize() > maxSize) {
   throw new BusinessException("El archivo excede el tamano maximo de 5MB");
  }
 }

 /**
  * Obtiene la extension del archivo
  */
 private String obtenerExtension(String nombreArchivo) {
  String fileName = StringUtils.cleanPath(nombreArchivo);
  int lastDotIndex = fileName.lastIndexOf('.');

  if (lastDotIndex == -1) {
   throw new BusinessException("El archivo no tiene extension");
  }

  return fileName.substring(lastDotIndex + 1).toLowerCase();
 }

 /**
  * Verifica si la extension esta permitida
  */
 private boolean esExtensionPermitida(String extension) {
  return extension.equals("jpg") ||
    extension.equals("jpeg") ||
    extension.equals("png");
 }
}