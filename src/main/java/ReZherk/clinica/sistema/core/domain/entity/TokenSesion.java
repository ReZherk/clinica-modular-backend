package ReZherk.clinica.sistema.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Token_Sesion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenSesion {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "Id_Token")
 private Integer id;

 @ManyToOne
 @JoinColumn(name = "Id_Usuario")
 private Usuario usuario;

 @Column(name = "Token", length = 100)
 private String token;

 @Column(name = "FechaExpiracion")
 private LocalDateTime fechaExpiracion;

 @Column(name = "Activo")
 private Boolean activo;
}
