package ReZherk.clinica.sistema.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "Id_Usuario")
 private Integer id;

 @Column(name = "Nombres", nullable = false, length = 100)
 private String nombres;

 @Column(name = "Apellidos", nullable = false, length = 100)
 private String apellidos;

 @Column(name = "PasswordHash", nullable = false)
 private String passwordHash;

 @Column(name = "Salt")
 private String salt;

 @Column(name = "Email", unique = true, length = 150)
 private String email;

 @Column(name = "Telefono", length = 20)
 private String telefono;

 @Column(name = "EstadoRegistro", nullable = false)
 @Builder.Default
 private Boolean estadoRegistro = true;

 // Relación con perfiles
 @ManyToMany(fetch = FetchType.LAZY)
 @JoinTable(name = "Usuario_Perfil", joinColumns = @JoinColumn(name = "Id_Usuario"), inverseJoinColumns = @JoinColumn(name = "Id_Perfil"))
 @Builder.Default
 private Set<RolPerfil> perfiles = new HashSet<>();
}
