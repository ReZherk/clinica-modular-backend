package ReZherk.clinica.sistema.modules.admin.application.dto.response;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalCreationResponseDto {
 private Integer id;
 private String nombres;
 private String apellidos;
 private String email;
 private String telefono;
 private Boolean estadoRegistro;
 private Set<String> roles;

}
