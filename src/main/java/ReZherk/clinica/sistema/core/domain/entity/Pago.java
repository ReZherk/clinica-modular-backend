package ReZherk.clinica.sistema.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import ReZherk.clinica.sistema.core.shared.enums.EstadoPago;
import ReZherk.clinica.sistema.core.shared.enums.MetodoPago;

@Entity
@Table(name = "pago")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "id_pago")
 private Integer idPago;

 @ManyToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "id_cita", nullable = false)
 private Cita cita;

 @ManyToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "id_usuario", nullable = false)
 private Usuario paciente;

 @Column(name = "monto", nullable = false, precision = 10, scale = 2)
 private BigDecimal monto;

 @Enumerated(EnumType.STRING)
 @Column(name = "metodo_pago", nullable = false)
 private MetodoPago metodoPago;

 @Enumerated(EnumType.STRING)
 @Column(name = "estado_pago", nullable = false)
 @Builder.Default
 private EstadoPago estadoPago = EstadoPago.PENDIENTE;

 @Column(name = "numero_referencia", length = 100)
 private String numeroReferencia;

 @Column(name = "fecha_pago", nullable = false, updatable = false, insertable = false)
 private LocalDateTime fechaPago;

 @Column(name = "fecha_actualizacion")
 private LocalDateTime fechaActualizacion;

 @Column(name = "datos_pago", columnDefinition = "JSON")
 private String datosPago;

 @ManyToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "id_paciente_seguro")
 private PacienteSeguro pacienteSeguro;

 @PrePersist
 protected void onCreate() {
  if (numeroReferencia == null) {
   numeroReferencia = generarNumeroReferencia();
  }
 }

 @PreUpdate
 protected void onUpdate() {
  fechaActualizacion = LocalDateTime.now();
 }

 private String generarNumeroReferencia() {
  long timestamp = System.currentTimeMillis();
  String metodo = metodoPago.name().substring(0, 3);
  return String.format("%s-%d-%d", metodo, timestamp, (cita != null ? cita.getIdCita() : 0));
 }

}
