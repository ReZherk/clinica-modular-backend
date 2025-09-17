package ReZherk.clinica.sistema.modules.patient.application.dto.response;

import lombok.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientCreationResponseDto {
    private Integer id;
    private String nombres;
    private String apellidos;
    private String email;
    private String telefono;
    private Boolean estadoRegistro;
    private Set<String> roles;
    private String dni;
}
