package ReZherk.clinica.sistema.core.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 // Nombre descriptivo del permiso (ej: "Lectura de Roles")
 @Column(unique = true, nullable = false)
 private String name;

 // Descripción detallada de lo que permite hacer (ej: "Permite consultar la
 // lista de roles")
 private String description;

 // CLAVE DE ACCIÓN: usada en @PreAuthorize (ej: "ROLE_READ")
 @Column(name = "action_key", unique = true, nullable = false, length = 100)
 private String actionKey;
}
