package ReZherk.clinica.sistema;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordCheckTest {

 @Test
 void checkPassword() {
  String rawPassword = "1234567"; // lo que ingresas en login
  String hashedPassword = "$10$jEY1vqUPB2XWd7LMBjrzUe32j//ODxOKWqxbmJeOsP7Q02MhPZJ1m"; // hash en la BD

  BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
  boolean matches = encoder.matches(rawPassword, hashedPassword);

  System.out.println("¿Coincide?: " + matches);
 }
}
