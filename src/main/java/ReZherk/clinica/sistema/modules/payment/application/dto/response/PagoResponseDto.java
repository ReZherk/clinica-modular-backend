package ReZherk.clinica.sistema.modules.payment.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import ReZherk.clinica.sistema.core.shared.enums.EstadoPago;
import ReZherk.clinica.sistema.core.shared.enums.MetodoPago;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagoResponseDto {

 private Integer idPago;
 private Integer idCita;
 private BigDecimal monto;
 private MetodoPago metodoPago;
 private EstadoPago estadoPago;
 private String numeroReferencia;
 private LocalDateTime fechaPago;
 private String datosPago; // JSON String

 // Informacion de la cita
 private CitaInfoDto cita;

 @Data
 @Builder
 @NoArgsConstructor
 @AllArgsConstructor
 public static class CitaInfoDto {
  private Integer idCita;
  private String nombreMedico;
  private String especialidad;
  private String fecha;
  private String hora;
 }
}
