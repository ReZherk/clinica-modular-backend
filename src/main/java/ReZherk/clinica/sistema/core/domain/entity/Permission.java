package ReZherk.clinica.sistema.core.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;

/**
 * ENTIDAD PERMISO: Representa una acción específica en el sistema
 * 
 * EJEMPLOS DE PERMISOS USADOS EN ESTE PROYECTO:
 * - ROLE_READ: Consultar lista de roles
 * - ROLE_CREATE: Crear nuevos roles
 * - ROLE_ASSIGN: Asignar roles a usuarios
 * - MEDICO_REGISTER: Registrar médicos
 * - ESPECIALIDAD_REGISTER: Registrar especialidades
 * 
 * Estos permisos se utilizan en las anotaciones @PreAuthorize
 * para restringir el acceso a los endpoints según el rol del usuario.
 */
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
