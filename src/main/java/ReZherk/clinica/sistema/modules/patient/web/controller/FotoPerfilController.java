package ReZherk.clinica.sistema.modules.patient.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ReZherk.clinica.sistema.core.application.dto.ApiResponse;
import ReZherk.clinica.sistema.modules.patient.application.dto.response.FotoPerfilResponseDto;
import ReZherk.clinica.sistema.modules.patient.application.service.FotoPerfilService;

@RestController
@RequestMapping("/api/paciente/foto-perfil")
@RequiredArgsConstructor
@Tag(name = "Foto de Perfil", description = "Endpoints para gestión de foto de perfil de usuarios. " +
  "⚠️ IMPORTANTE: Los endpoints de subida usan multipart/form-data. " +
  "En Swagger, selecciona 'form-data' y usa key = foto.")
public class FotoPerfilController {

 private final FotoPerfilService fotoPerfilService;

 // ---------------------------------------------------------
 // SUBIR FOTO
 // ---------------------------------------------------------
 @PostMapping(value = "/{idUsuario}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
 @Operation(summary = "Subir foto de perfil", description = "Permite subir una foto de perfil.\n\n" +
   "📌 **Cómo enviar la imagen en Swagger:**\n" +
   "- Selecciona: `multipart/form-data`\n" +
   "- Campo: **foto**\n" +
   "- Tipo: **file**\n" +
   "- No enviar JSON\n\n" +
   "Ejemplo desde Postman o frontend:\n" +
   "```\n" +
   "formData.append('foto', archivo);\n" +
   "```\n")
 public ResponseEntity<ApiResponse<FotoPerfilResponseDto>> subirFotoPerfil(
   @PathVariable Integer idUsuario,

   @Parameter(description = "Archivo de imagen (jpg, jpeg, png).\n" +
     "Debe enviarse como multipart/form-data.\n" +
     "Ejemplo (FormData):\n" +
     "```\n" +
     "foto: <archivo>\n" +
     "```", required = true, content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @RequestParam("foto") MultipartFile foto) {

  FotoPerfilResponseDto response = fotoPerfilService.subirFotoPerfil(idUsuario, foto);

  ApiResponse<FotoPerfilResponseDto> api = new ApiResponse<>(
    true,
    "Foto de perfil subida exitosamente",
    response);

  return ResponseEntity.status(HttpStatus.CREATED).body(api);
 }

 // ---------------------------------------------------------
 // CAMBIAR FOTO
 // ---------------------------------------------------------
 @PutMapping(value = "/{idUsuario}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
 @Operation(summary = "Cambiar foto de perfil", description = "Cambia la foto de perfil existente.\n\n" +
   "📌 **Enviar como multipart/form-data** usando la key 'foto'.\n")
 public ResponseEntity<ApiResponse<FotoPerfilResponseDto>> cambiarFotoPerfil(
   @PathVariable Integer idUsuario,

   @Parameter(description = "Archivo de imagen (jpg, jpeg, png)", required = true) @RequestParam("foto") MultipartFile foto) {

  FotoPerfilResponseDto response = fotoPerfilService.cambiarFotoPerfil(idUsuario, foto);

  ApiResponse<FotoPerfilResponseDto> api = new ApiResponse<>(
    true,
    "Foto de perfil actualizada exitosamente",
    response);

  return ResponseEntity.ok(api);
 }

 // ---------------------------------------------------------
 // ELIMINAR FOTO
 // ---------------------------------------------------------
 @DeleteMapping("/{idUsuario}")
 @Operation(summary = "Eliminar foto de perfil")
 public ResponseEntity<ApiResponse<Void>> eliminarFotoPerfil(@PathVariable Integer idUsuario) {

  fotoPerfilService.eliminarFotoPerfil(idUsuario);

  ApiResponse<Void> api = new ApiResponse<>(
    true,
    "Foto de perfil eliminada exitosamente",
    null);

  return ResponseEntity.ok(api);
 }

 // ---------------------------------------------------------
 // OBTENER FOTO
 // ---------------------------------------------------------
 @GetMapping("/{idUsuario}")
 @Operation(summary = "Obtener foto de perfil", description = "Devuelve la imagen del usuario como recurso binario (image/jpeg).")
 public ResponseEntity<Resource> obtenerFotoPerfil(@PathVariable Integer idUsuario) {

  Resource resource = fotoPerfilService.obtenerFotoPerfil(idUsuario);

  return ResponseEntity.ok()
    .contentType(MediaType.IMAGE_JPEG)
    .header(HttpHeaders.CONTENT_DISPOSITION,
      "inline; filename=\"" + resource.getFilename() + "\"")
    .body(resource);
 }

 // ---------------------------------------------------------
 // OBTENER URL
 // ---------------------------------------------------------
 @GetMapping("/{idUsuario}/url")
 @Operation(summary = "Obtener URL pública de la foto")
 public ResponseEntity<ApiResponse<String>> obtenerUrlFotoPerfil(@PathVariable Integer idUsuario) {

  String url = fotoPerfilService.obtenerUrlFotoPerfil(idUsuario);

  ApiResponse<String> api = new ApiResponse<>(
    true,
    "URL obtenida exitosamente",
    url);

  return ResponseEntity.ok(api);
 }
}
