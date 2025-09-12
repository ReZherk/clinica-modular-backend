# 🗄️ Configuración de DataSource para Spring Boot con MySQL

## 📋 Dependencia Maven

Esta configuración requiere las siguientes dependencias en tu `pom.xml`:

```xml
<dependencies>
    <!-- Spring Boot Starter Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- MySQL Connector -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- HikariCP Connection Pool (incluido por defecto en Spring Boot) -->
    <dependency>
        <groupId>com.zaxxer</groupId>
        <artifactId>HikariCP</artifactId>
    </dependency>
</dependencies>
```

## ⚙️ Configuración application.properties

```properties
# 🔗 CONFIGURACIÓN DE CONEXIÓN A BASE DE DATOS
spring.datasource.url=jdbc:mysql://localhost:3306/clinica_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
# 📝 URL de conexión JDBC a MySQL:
#    - localhost:3306 → servidor local en puerto por defecto
#    - clinica_db → nombre de la base de datos
#    - useSSL=false → desactiva SSL para desarrollo local
#    - allowPublicKeyRetrieval=true → permite autenticación con clave pública
#    - serverTimezone=UTC → establece zona horaria UTC para evitar conflictos

spring.datasource.username=tu_usuario
# 👤 Usuario de la base de datos MySQL

spring.datasource.password=tu_contraseña
# 🔒 Contraseña del usuario de la base de datos

spring.jpa.open-in-view=false
# 🧩 Desactiva el patrón Open Session in View (OSIV), que mantiene abierta la sesión de Hibernate durante toda la petición HTTP.
# ✅ Con esta propiedad en false, todas las operaciones de acceso a datos deben completarse en el servicio antes de devolver la respuesta.

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
# 🚗 Driver JDBC para MySQL 8.0+ (versión moderna del conector)

# 🏊‍♂️ CONFIGURACIÓN DEL POOL DE CONEXIONES HIKARI
spring.datasource.hikari.maximum-pool-size=20
# 🔝 Máximo número de conexiones simultáneas en el pool
#    Valor recomendado: 10-30 para aplicaciones típicas

spring.datasource.hikari.minimum-idle=1
# 💤 Mínimo número de conexiones inactivas mantenidas
#    Reduce latencia al tener conexiones listas para usar

spring.datasource.hikari.idle-timeout=300000
# ⏱️ Tiempo máximo de inactividad antes de cerrar conexión (300 seg = 5 min)
#    Libera recursos cuando no hay actividad

spring.datasource.hikari.connection-timeout=30000
# ⌛ Tiempo máximo de espera para obtener una conexión (30 seg)
#    Lanza excepción si no se puede obtener conexión en este tiempo

spring.datasource.hikari.max-lifetime=1800000
# 🧬 Tiempo máximo de vida de una conexión (1800 seg = 30 min)
#    Previene problemas con conexiones que permanecen abiertas mucho tiempo
#    MySQL cierra conexiones inactivas por defecto después de 8 horas

# 📊 CONFIGURACIÓN ADICIONAL RECOMENDADA
spring.jpa.hibernate.ddl-auto=update
# 🔄 Actualiza automáticamente el esquema de BD basado en entidades JPA

spring.jpa.show-sql=true
# 👁️ Muestra las consultas SQL en los logs (útil para desarrollo)

spring.jpa.properties.hibernate.format_sql=true
# 🎨 Formatea las consultas SQL para mejor legibilidad

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
# 🗣️ Dialecto específico para MySQL 8.0+
```

## Dependencias Spring Boot Relacionadas

Esta configuración hace referencia principalmente a:

**`spring-boot-starter-data-jpa`** - Starter principal que incluye:

- Spring Data JPA
- Hibernate ORM
- **HikariCP (incluido automáticamente como dependencia transitiva)**
- Spring Boot Autoconfiguration

**`mysql-connector-j`** - Driver JDBC moderno para MySQL (versión actual en tu proyecto)

**HikariCP** - Pool de conexiones de alto rendimiento incluido automáticamente

## Notas de Configuración

**Entorno de Desarrollo**: `useSSL=false` es aceptable para desarrollo local

**Entorno de Producción**: Siempre usar `useSSL=true` y certificados válidos para mayor seguridad

**Pool de Conexiones**: Los valores mostrados son apropiados para aplicaciones de tráfico moderado a alto

**Zona Horaria**: UTC evita problemas de conversión de fechas entre servidor y base de datos

**Compatibilidad**: Configuración optimizada para MySQL 8.0+ y Spring Boot 3.x
