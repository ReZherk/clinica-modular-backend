package ReZherk.clinica.sistema.modules.admin.application.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicoResponseDto {
 private Integer id;
 private String numeroDocumento;
 private Integer tipoDocumentoId;
 private String nombresCompleto;
 private String email;
 private String telefono;
 private String cmp;
 private String especialidad;
 private Integer horasSemanales;
 private boolean estadoRegistro;
}
