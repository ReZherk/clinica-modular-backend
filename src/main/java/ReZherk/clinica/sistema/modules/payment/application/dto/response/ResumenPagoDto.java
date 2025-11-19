package ReZherk.clinica.sistema.modules.payment.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumenPagoDto {

 private Integer idCita;
 private String nombreMedico;
 private String especialidad;
 private String fecha;
 private String hora;
 private BigDecimal monto;
 private Boolean requierePago; // false si tiene seguro con convenio
 private String motivoSinPago; // "Cubierto por seguro ,aqui va el seguro que elgio"
}
