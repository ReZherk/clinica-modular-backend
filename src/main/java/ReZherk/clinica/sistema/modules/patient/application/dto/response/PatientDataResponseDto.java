package ReZherk.clinica.sistema.modules.patient.application.dto.response;

import java.util.Set;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDataResponseDto {

 // Identificación principal
 private Integer id;
 private String tipoDocumento;
 private String dni;

 // Datos personales
 private String nombres;
 private String apellidos;
 private String fechaNacimiento;

 // Contacto
 private String telefono;
 private String email;

 // Ubicación
 private String departamento;
 private String provincia;
 private String distrito;
 private String direccion;

 // Estado y roles
 private Boolean estadoRegistro;
 private Set<String> roles;
}