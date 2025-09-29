package ReZherk.clinica.sistema.modules.admin.application.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponseDto {

 private boolean success;
 private String message;
 private Integer id;
 private String nombre;
 private String descripcion;
 private Boolean estadoRegistro;

 public RoleResponseDto(boolean success, String message) {
  this.success = success;
  this.message = message;
 }

}
