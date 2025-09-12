package ReZherk.clinica.sistema.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Tarifario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tarifario {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "Id_Tarifario")
 private Integer id;

 @ManyToOne
 @JoinColumn(name = "Id_Especialidad")
 private Especialidad especialidad;

 @Column(name = "Monto", precision = 10, scale = 2)
 private BigDecimal monto;

 @Column(name = "FechaVigencia")
 private LocalDate fechaVigencia;

 @Column(name = "Activo")
 private Boolean activo;
}
