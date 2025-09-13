package ReZherk.clinica.sistema.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitaResponseDTO {
 private Integer id;
 private LocalDateTime fechaHora;
 private String nombrePaciente;
 private String nombreMedico;
 private String especialidad;
 private String sede;
 private Double tarifa;
 private Boolean estado;
}
