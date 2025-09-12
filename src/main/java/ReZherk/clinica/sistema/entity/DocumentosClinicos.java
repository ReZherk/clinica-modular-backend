package ReZherk.clinica.sistema.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Documentos_Clinicos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentosClinicos {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "Id_Documento")
 private Integer id;

 @ManyToOne
 @JoinColumn(name = "Id_Detalle")
 private DetalleCita detalle;

 @Column(name = "Documento", length = 255)
 private String documento;
}