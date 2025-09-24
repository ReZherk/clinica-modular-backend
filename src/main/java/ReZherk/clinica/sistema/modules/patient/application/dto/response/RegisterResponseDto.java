package ReZherk.clinica.sistema.modules.patient.application.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponseDto {

 private boolean success;
 private String message;

 private Integer id;
 private PatientCreationResponseDto usuario;

 public RegisterResponseDto(boolean success, String message) {
  this.success = success;
  this.message = message;
 }
}
