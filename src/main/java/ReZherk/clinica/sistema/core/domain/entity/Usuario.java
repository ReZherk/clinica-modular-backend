package ReZherk.clinica.sistema.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
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

 // === Relación con tipo de documento ===
 @ManyToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "Id_TipoDocumento")
 private TipoDocumento tipoDocumento;

 @Column(name = "Nombres", nullable = false, length = 100)
 private String nombres;

 @Column(name = "Apellidos", nullable = false, length = 100)
 private String apellidos;

 @Column(name = "NumeroDocumento", columnDefinition = "CHAR(12)")
 private String numeroDocumento;

 @Column(name = "PasswordHash", nullable = false)
 private String passwordHash;

 @Column(name = "Email", unique = true, length = 150)
 private String email;

 @Column(name = "Telefono", length = 20)
 private String telefono;

 @Column(name = "EstadoRegistro", nullable = false)
 @Builder.Default
 private Boolean estadoRegistro = true;

 @Column(name = "Fecha_Creacion", updatable = false, insertable = false)
 private LocalDateTime fechaCreacion;

 // Relación con perfiles
 @ManyToMany(fetch = FetchType.EAGER)
 @JoinTable(name = "usuario_perfil", joinColumns = @JoinColumn(name = "Id_Usuario"), inverseJoinColumns = @JoinColumn(name = "Id_Perfil"))
 @Builder.Default
 private Set<RolPerfil> perfiles = new HashSet<>();

 // ======================
 // MÉTODOS HELPER OPCIONALES
 // ======================

 /**
  * Agregar un rol al usuario
  */
 public void addRol(RolPerfil rol) {
  this.perfiles.add(rol);
 }

 /**
  * Obtener todos los permisos del usuario (a través de sus roles/perfiles)
  */
 public Set<String> getAllPermissions() {
  Set<String> permisos = new HashSet<>();
  for (RolPerfil rol : this.perfiles) {
   rol.getPermisos().forEach(p -> permisos.add(p.getActionKey()));
  }
  return permisos;
 }

 /**
  * Verificar si el usuario tiene un permiso específico
  */
 public boolean hasPermission(String actionKey) {
  return getAllPermissions().contains(actionKey);
 }
}
