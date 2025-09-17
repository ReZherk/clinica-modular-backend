package ReZherk.clinica.sistema.modules.patient.application.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponseDto {
 private Integer id;
 private String message;
 private PatientCreationResponseDto usuario;
}
