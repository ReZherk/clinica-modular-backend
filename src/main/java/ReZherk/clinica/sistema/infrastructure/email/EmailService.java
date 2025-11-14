package ReZherk.clinica.sistema.infrastructure.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

 private final JavaMailSender mailSender;

 @Value("${app.base-url}")
 private String baseUrl;

 public void enviarLinkRecuperacion(String emailDestino, String token) {
  String url = baseUrl + "/reset-password?token=" + token;

  SimpleMailMessage msg = new SimpleMailMessage();
  msg.setFrom("patrickcomeresbueno@gmail.com");
  msg.setTo(emailDestino);
  msg.setSubject("🔐 Recuperación de contraseña - Clínica Aurora");
  msg.setText("""
    Hola,

    Recibimos una solicitud para restablecer tu contraseña en Clínica Aurora.
    Usa este enlace para continuar:

    %s

    Este enlace es válido por 15 minutos.

    Si no solicitaste esto, ignora este mensaje y tu contraseña permanecerá sin cambios.

    ---
    Equipo de Clínica Aurora
    """.formatted(url));

  mailSender.send(msg);
 }
}