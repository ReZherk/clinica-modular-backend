# Sistema Clínico Modular

Sistema integral de gestión clínica desarrollado con Spring Boot que permite administrar citas médicas, pacientes, médicos, especialidades, horarios y pagos.

## 📋 Tabla de Contenidos

- [Características](#características)
- [Tecnologías](#tecnologías)
- [Requisitos Previos](#requisitos-previos)
- [Instalación](#instalación)
- [Configuración](#configuración)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Módulos del Sistema](#módulos-del-sistema)
- [Base de Datos](#base-de-datos)
- [Seguridad](#seguridad)
- [Documentación API](#documentación-api)
- [Ejecución](#ejecución)

## ✨ Características

- **Gestión de Usuarios**: Administradores, médicos y pacientes con roles y permisos
- **Gestión de Citas**: Creación, cancelación, reprogramación y seguimiento de citas médicas
- **Gestión de Horarios**: Asignación de horarios a médicos por día de la semana
- **Gestión de Especialidades**: CRUD de especialidades médicas con tarifas
- **Sistema de Pagos**: Múltiples métodos (tarjeta, Yape, seguros médicos)
- **Autenticación JWT**: Sistema seguro de autenticación y autorización
- **Recuperación de Contraseña**: Sistema de tokens temporales vía email
- **Gestión de Fotos de Perfil**: Subida y almacenamiento de imágenes
- **Historial Médico**: Detalles de consultas y diagnósticos
- **Reuniones Virtuales**: Enlaces para videoconsultas

## 🛠 Tecnologías

- **Framework**: Spring Boot 3.5.5
- **Java**: 17
- **Base de Datos**: MySQL 8.0
- **ORM**: Hibernate / JPA
- **Migraciones**: Flyway
- **Seguridad**: Spring Security + JWT
- **Pool de Conexiones**: HikariCP
- **Validaciones**: Spring Validation
- **Documentación**: SpringDoc OpenAPI (Swagger)
- **Email**: Spring Mail (Gmail SMTP)
- **Build Tool**: Maven
- **Otros**: Lombok, Jackson

## 📦 Requisitos Previos

- Java 17 o superior
- Maven 3.6+
- MySQL 8.0+
- IDE recomendado: IntelliJ IDEA o VS Code

## 🚀 Instalación

1. **Clonar el repositorio**

```bash
git clone <url-del-repositorio>
cd sistema
```

2. **Crear la base de datos**

```sql
CREATE DATABASE clinica_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. **Configurar las credenciales**

Editar `src/main/resources/application.properties`:

```properties
# Configurar MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/clinica_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password

# Configurar Email
spring.mail.username=tu_email@gmail.com
spring.mail.password=tu_app_password

# JWT Secret (generar uno propio en producción)
jwt.secret=tu_secret_key_seguro
```

4. **Instalar dependencias**

```bash
mvn clean install
```

5. **Ejecutar migraciones**

```bash
mvn flyway:migrate
```

## ⚙️ Configuración

### Variables de Entorno Clave

| Variable               | Descripción                  | Default               |
| ---------------------- | ---------------------------- | --------------------- |
| `server.port`          | Puerto del servidor          | 8080                  |
| `jwt.secret`           | Clave secreta JWT            | (requerido)           |
| `jwt.expiration`       | Tiempo expiración token (ms) | 3600000 (1h)          |
| `spring.mail.username` | Email SMTP                   | (requerido)           |
| `spring.mail.password` | Password SMTP                | (requerido)           |
| `app.upload.dir`       | Directorio fotos perfil      | uploads/fotos-perfil  |
| `app.base-url`         | URL frontend                 | http://localhost:5173 |

### Pool de Conexiones HikariCP

```properties
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=1
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.max-lifetime=1800000
```

## 📁 Estructura del Proyecto

```
sistema/
├── src/main/java/ReZherk/clinica/sistema/
│   ├── core/                          # Núcleo del sistema
│   │   ├── application/               # DTOs y mappers compartidos
│   │   ├── domain/                    # Entidades y repositorios
│   │   └── shared/                    # Utilidades, excepciones, enums
│   ├── infrastructure/                # Configuraciones técnicas
│   │   ├── config/                    # Configuración de storage
│   │   ├── email/                     # Servicio de correo
│   │   ├── security/                  # JWT, UserDetails, SecurityConfig
│   │   └── storage/                   # Gestión de archivos
│   ├── modules/                       # Módulos funcionales
│   │   ├── admin/                     # Gestión administrativa
│   │   ├── appointment/               # Gestión de citas
│   │   ├── auth/                      # Autenticación
│   │   ├── medico/                    # Funciones del médico
│   │   ├── patient/                   # Funciones del paciente
│   │   └── payment/                   # Gestión de pagos
│   └── web/                           # Configuración web
│       ├── config/                    # CORS, Swagger
│       └── exception/                 # Manejo global de errores
└── src/main/resources/
    ├── application.properties         # Configuración principal
    └── db/migration/                  # Scripts Flyway
```

## 🧩 Módulos del Sistema

### 1. **Admin Module**

Gestión completa del sistema por administradores:

- CRUD de usuarios, roles y permisos
- Gestión de médicos y especialidades
- Asignación de horarios a médicos
- Estadísticas del sistema

### 2. **Auth Module**

Sistema de autenticación:

- Login con JWT
- Registro de usuarios
- Recuperación de contraseña
- Cambio de contraseña

### 3. **Patient Module**

Funcionalidades para pacientes:

- Gestión de perfil
- Búsqueda de médicos por especialidad
- Subida de foto de perfil
- Cambio de contraseña

### 4. **Appointment Module**

Gestión de citas médicas:

- Crear citas
- Consultar horarios disponibles
- Cancelar citas
- Reprogramar citas
- Filtros avanzados

### 5. **Medico Module**

Funcionalidades para médicos:

- Ver agenda de citas
- Registrar detalles de consulta
- Gestionar enlaces de reunión
- Historial de pacientes

### 6. **Payment Module**

Sistema de pagos:

- Pago con tarjeta
- Pago con Yape
- Pago con seguro médico
- Vinculación de seguros
- Historial de pagos

## 🗄️ Base de Datos

### Tablas Principales

- `Usuario` - Usuarios del sistema
- `RolPerfil` - Roles (Admin, Médico, Paciente)
- `UsuarioPerfil` - Relación usuario-rol
- `Permission` - Permisos del sistema
- `PacienteDetalle` - Información adicional de pacientes
- `MedicoDetalle` - Información adicional de médicos
- `Especialidad` - Especialidades médicas
- `Horario` - Horarios base del sistema
- `MedicoHorario` - Horarios asignados a médicos
- `Cita` - Citas médicas
- `DetalleCita` - Detalles de consulta médica
- `Pago` - Transacciones de pago
- `Seguro` - Seguros médicos disponibles
- `PacienteSeguro` - Seguros vinculados a pacientes
- `TokenSesion` - Tokens de recuperación de contraseña
- `IntentoLogin` - Registro de intentos de login

### Migraciones Flyway

Las migraciones se ejecutan automáticamente al iniciar la aplicación. Se encuentran en `src/main/resources/db/migration/`.

Comandos útiles:

```bash
mvn flyway:info      # Ver estado de migraciones
mvn flyway:migrate   # Ejecutar migraciones pendientes
mvn flyway:clean     # Limpiar base de datos (¡cuidado!)
```

## 🔒 Seguridad

### Autenticación JWT

El sistema utiliza JWT (JSON Web Tokens) para autenticación:

1. Usuario hace login en `/api/auth/login`
2. Sistema devuelve token JWT
3. Cliente incluye token en header: `Authorization: Bearer <token>`
4. Token válido por 1 hora (configurable)

### Roles y Permisos

- **ADMIN**: Acceso completo al sistema
- **MEDICO**: Gestión de citas y pacientes asignados
- **PACIENTE**: Gestión de perfil y citas propias

### Contraseñas

- Encriptación con BCrypt
- Validación de fortaleza en registro
- Sistema de recuperación con tokens temporales

## 📚 Documentación API

La documentación interactiva está disponible con Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON:

```
http://localhost:8080/api-docs
```

## ▶️ Ejecución

### Desarrollo

```bash
# Ejecutar con Maven
mvn spring-boot:run

# O ejecutar la clase principal
mvn clean package
java -jar target/sistema-0.0.1-SNAPSHOT.jar
```

### Producción

```bash
# Compilar
mvn clean package -DskipTests

# Ejecutar JAR
java -jar target/sistema-0.0.1-SNAPSHOT.jar
```

### Docker (Opcional)

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/sistema-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

```bash
docker build -t sistema-clinica .
docker run -p 8080:8080 sistema-clinica
```

## 🧪 Testing

```bash
# Ejecutar todos los tests
mvn test

# Ejecutar un test específico
mvn test -Dtest=PasswordCheckTest
```

## 📝 Logs

Configuración de logs en `application.properties`:

```properties
logging.level.com.clinica=DEBUG
logging.level.org.springframework=INFO
```

Los logs se muestran en consola por defecto.

## 🤝 Contribución

1. Fork el proyecto
2. Crear rama feature (`git checkout -b feature/AmazingFeature`)
3. Commit cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir Pull Request

## 📄 Licencia

Este proyecto está bajo licencia privada.

## 👥 Autores

- **ReZherk** - Desarrollo inicial

## 📧 Soporte

Para soporte, contactar a: patrickcomeresbueno@gmail.com

---

**Nota**: Este es un sistema en desarrollo. Asegúrate de cambiar todas las credenciales y secretos antes de desplegar en producción.
