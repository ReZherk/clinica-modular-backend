package ReZherk.clinica.sistema.modules.patient.application.dto.request;

import lombok.Data;

@Data
public class ChangePasswordDto {
 private String passwordActual;
 private String passwordNueva;
}
