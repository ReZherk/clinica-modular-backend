package ReZherk.clinica.sistema.modules.admin.application.dto.response;

import lombok.Data;

@Data
public class SpecialtyResponseDto {

 private boolean success;
 private String message;

 public SpecialtyResponseDto(boolean success, String message) {
  this.success = success;
  this.message = message;
 }

}
