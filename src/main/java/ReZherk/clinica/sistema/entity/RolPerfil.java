package ReZherk.clinica.sistema.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Roles_Perfil")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolPerfil {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "Id_Perfil")
 private Integer id;

 @Column(name = "Nombre", nullable = false, length = 50)
 private String nombre;

 @Column(name = "Descripcion")
 private String descripcion;

 @Builder.Default
 @Column(name = "EstadoRegistro", nullable = false)
 private Boolean estadoRegistro = true;

 @Builder.Default
 @ManyToMany(mappedBy = "perfiles")
 private Set<Usuario> usuarios = new HashSet<>();
}
