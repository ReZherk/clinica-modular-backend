package ReZherk.clinica.sistema.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles_perfil")
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

 /**
  * Relación con usuarios
  */
 @Builder.Default
 @ManyToMany(mappedBy = "perfiles")
 private Set<Usuario> usuarios = new HashSet<>();

 /**
  * Relación con permisos (tabla intermedia roles_perfil_permisos)
  */
 @Builder.Default
 @ManyToMany(fetch = FetchType.EAGER)
 @JoinTable(name = "roles_perfil_permisos", joinColumns = @JoinColumn(name = "Id_Perfil"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
 private Set<Permission> permisos = new HashSet<>();

 // Métodos helper para manejar permisos
 public void addPermission(Permission permiso) {
  this.permisos.add(permiso);
 }

 public void removePermission(Permission permiso) {
  this.permisos.remove(permiso);
 }

 public boolean hasPermission(String actionKey) {
  return this.permisos.stream()
    .anyMatch(permission -> permission.getActionKey().equals(actionKey));
 }
}
