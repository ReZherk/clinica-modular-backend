package ReZherk.clinica.sistema.modules.patient.application.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolPerfilDto {
 private Integer id;
 private String nombre;
 private String descripcion;
 private Boolean estadoRegistro;
}
