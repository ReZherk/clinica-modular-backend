package ReZherk.clinica.sistema.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Intento_Login")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntentoLogin {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 @Column(name = "Id_Intento")
 private Integer id;

 @ManyToOne
 @JoinColumn(name = "Id_Usuario")
 private Usuario usuario;

 @Column(name = "Token", length = 100)
 private String token;

 @Column(name = "FechaIntento")
 private LocalDateTime fechaIntento;

 @Column(name = "Exitoso")
 private Boolean exitoso;

 @Column(name = "IpOrigen", length = 50)
 private String ipOrigen;
}
