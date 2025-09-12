package ReZherk.clinica.sistema.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Especialidad")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Especialidad {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "Id_Especialidad")
 private Integer id;

 @Column(name = "NombreEspecialidad", nullable = false, length = 100)
 private String nombreEspecialidad;

 @Column(name = "Descripcion")
 private String descripcion;
}
