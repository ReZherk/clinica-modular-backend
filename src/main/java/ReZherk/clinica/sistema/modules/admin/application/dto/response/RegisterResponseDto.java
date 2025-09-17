package ReZherk.clinica.sistema.modules.admin.application.dto.response;

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
public class RegisterResponseDto {
 private Integer id;
 private String message;
 private MedicalCreationResponseDto usuario;

}
