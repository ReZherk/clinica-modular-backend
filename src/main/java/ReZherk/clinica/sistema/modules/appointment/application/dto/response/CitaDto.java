package ReZherk.clinica.sistema.modules.appointment.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import ReZherk.clinica.sistema.core.shared.enums.EstadoCita;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitaDto {

    private Integer idCita;
    private LocalDate fecha;
    private LocalTime hora;
    private EstadoCita estado;
    private String motivo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private String linkReunion;

    private PacienteDto paciente;
    private MedicoDto medico;
    private EspecialidadDto especialidad;
    private DetalleDto detalle; // Puede ser null si no hay DetalleCita

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PacienteDto {
        private Integer id;
        private String nombres;
        private String apellidos;
        private String numeroDocumento;
        private String email;
        private String telefono;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MedicoDto {
        private Integer id;
        private String nombres;
        private String apellidos;
        private String cmp;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EspecialidadDto {
        private Integer id;
        private String nombreEspecialidad;
        private BigDecimal tarifa;
        private Byte duracion;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetalleDto {
        private String diagnostico;
        private String receta;
        private String observaciones;
    }
}
