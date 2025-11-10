# 🏥 Sistema Clínica - Spring Boot Application

## 📋 Descripción

Sistema clínico modular desarrollado con Spring Boot que gestiona pacientes, médicos, citas y documentación clínica. Incluye autenticación JWT, seguridad robusta y una API RESTful completa.

## 🚀 Características

- **Autenticación JWT** segura con Spring Security
- **API RESTful** documentada con Swagger/OpenAPI
- **Gestión de usuarios** con roles y permisos
- **Sistema de citas** médicas completo
- **Mapeo automático** entre entidades y DTOs con MapStruct
- **Migraciones de base de datos** con Flyway
- **Validación de datos** integrada

## 🛠️ Tecnologías Utilizadas

### Frameworks Principales

- **Spring Boot 3.5.5** - Framework principal
- **Spring Data JPA** - Persistencia de datos
- **Spring Security** - Autenticación y autorización
- **Spring Validation** - Validación de datos
- **Spring Web MVC** - API REST

### Base de Datos

- **MySQL** - Sistema de base de datos
- **Flyway** - Control de versiones de base de datos

### Utilidades

- **Lombok** - Reducción de código boilerplate
- **JJWT** - Tokens JWT
- **SpringDoc OpenAPI** - Documentación API

### Desarrollo

- **Spring Boot DevTools** - Herramientas de desarrollo
- **JUnit & Spring Security Test** - Testing

## 📦 Instalación y Configuración

### Prerrequisitos

- Java 17 o superior
- Maven 3.6+
- MySQL 8.0+

### Pasos de instalación

1. **Clonar el repositorio**

   ```bash
   git clone <url-del-repositorio>
   cd sistema
   ```

2. **Configurar base de datos**

   ```bash
   # Crear base de datos en MySQL
   CREATE DATABASE clinica_db;
   ```

3. **Configurar variables de entorno**
   Editar `src/main/resources/application.properties`:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/clinica_db
   spring.datasource.username=tu_usuario
   spring.datasource.password=tu_contraseña
   spring.jpa.hibernate.ddl-auto=validate
   ```

4. **Compilar y ejecutar**

   ```bash
   # Compilar con Maven
   ./mvnw clean compile

   # Ejecutar la aplicación
   ./mvnw spring-boot:run
   ```

## 🏃‍♂️ Uso Rápido

### Acceder a la documentación API

Una vez ejecutada la aplicación, accede a:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## 🗂️ Estructura del Proyecto

```
src/main/java/ReZherk/clinica/sistema/
├── SistemaApplication.java          # Clase principal de Spring Boot
├── core/                           # Núcleo compartido del sistema
├── infrastructure/                 # Capa de infraestructura
├── modules/                        # Módulos de dominio
└── web/                           # Configuración web global
```

- [Arquitectura Modular por Dominios](./ARQUITECTURA.md)  
  Detalles completos de la estructura, capas, módulos y principios aplicados.

## 🔐 Seguridad

El sistema implementa:

- Autenticación JWT stateless
- Roles y permisos (SuperAdmin, Médico, Paciente)
- Encriptación de contraseñas con BCrypt
- Protección contra ataques comunes
- Validación de tokens expirados

## 📊 Base de Datos

Las migraciones se encuentran en:

```
src/main/resources/db/migration/
```

- Flyway ejecuta automáticamente las migraciones al iniciar la aplicación
- El esquema de base de datos está versionado.

## 🚀 Despliegue

### Empaquetado para producción

```bash
mvn clean package -DskipTests
```

### Ejecutar JAR

```bash
java -jar target/sistema-0.0.1-SNAPSHOT.jar
```

### Variables de entorno para producción

```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://servidor:3306/clinica_db
export SPRING_DATASOURCE_USERNAME=usuario_prod
export SPRING_DATASOURCE_PASSWORD=password_seguro
export JWT_SECRET=clave-secreta-segura-production
```

## 🤝 Contribución

1. Fork el proyecto
2. Crear una rama para la feature (`git checkout -b feature/AmazingFeature`)
3. Commit de los cambios (`git commit -m 'Add AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request.

## 📄 Licencia

Este proyecto está bajo licencia. Ver el archivo `LICENSE` para más detalles.

## 🆘 Soporte

Si encuentras algún problema o tienes preguntas:

1. Revisa la documentación en Swagger UI
2. Verifica los logs de la aplicación
3. Abre un issue en el repositorio.

---
