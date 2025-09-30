package ReZherk.clinica.sistema.modules.admin.application.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicoDto {

 private Integer idUsuario;
 private String nombres;
 private String apellidos;
 private String cmp;
 private Boolean activo;

}
