package ReZherk.clinica.sistema.modules.patient.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FotoPerfilResponseDto {

 private Integer idUsuario;
 private String nombreArchivo;
 private String urlFoto;
 private String mensaje;
}