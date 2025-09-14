package ReZherk.clinica.sistema.dto;

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
