# Arquitectura Modular por Dominios - Sistema Clínica

## Descripción General

Este proyecto implementa una **arquitectura modular basada en dominios (Domain-Driven Design - DDD)** para un sistema de gestión de clínica médica. La estructura sigue principios de **Clean Architecture** y **Arquitectura Hexagonal**, organizando el código en capas bien definidas y módulos independientes por dominio de negocio.

## Estructura Principal del Proyecto

```
src/main/java/ReZherk/clinica/sistema/
├── SistemaApplication.java          # Clase principal de Spring Boot
├── core/                           # Núcleo compartido del sistema
├── infrastructure/                 # Capa de infraestructura
├── modules/                        # Módulos de dominio
└── web/                           # Configuración web global
```

---

## 📂 Descripción Detallada de Directorios

### **1. Core (Núcleo Compartido)**

El directorio `core/` contiene elementos compartidos por todos los módulos del sistema:

#### **core/application/**

- **Propósito**: Capa de aplicación compartida
- **dto/**: Objetos de transferencia de datos base
  - `UsuarioBaseDto.java` - DTO base para usuarios
- **service/**: Servicios de aplicación compartidos (vacío actualmente)

#### **core/domain/**

- **Propósito**: Lógica de dominio central y entidades compartidas
- **entity/**: Entidades de dominio principales

  - `Usuario.java` - Entidad principal de usuario
  - `Cita.java` - Gestión de citas médicas
  - `DetalleCita.java` - Detalles específicos de citas
  - `DocumentosClinicos.java` - Documentos médicos
  - `Especialidad.java` - Especialidades médicas
  - `HorarioMedico.java` - Horarios de médicos
  - `MedicoDetalle.java` - Información detallada de médicos
  - `PacienteDetalle.java` - Información detallada de pacientes
  - `RolPerfil.java` - Gestión de roles y perfiles
  - `Sede.java` - Sedes de la clínica
  - `Seguro.java` - Seguros médicos
  - `Tarifario.java` - Estructura de tarifas
  - `TokenSesion.java` - Gestión de sesiones
  - `Ubigeo.java` - Ubicaciones geográficas

- **enum/**: Enumeraciones del dominio (vacío actualmente)
- **repository/**: Interfaces de repositorio para cada entidad

#### **core/shared/**

- **Propósito**: Utilidades y excepciones compartidas
- **exception/**: Manejo centralizado de excepciones
  - `BusinessException.java` - Excepciones de lógica de negocio
  - `ErrorResponse.java` - Formato estándar de respuestas de error
  - `ResourceNotFoundException.java` - Recurso no encontrado
  - `ValidationException.java` - Errores de validación
- **utils/**: Utilidades compartidas (vacío actualmente)

#### **core/test/**

- **Propósito**: Controladores de prueba
- `TestController.java` - Endpoints para testing

---

### **2. Infrastructure (Infraestructura)**

El directorio `infrastructure/` maneja aspectos técnicos transversales:

#### **infrastructure/security/**

- **Propósito**: Configuración de seguridad de la aplicación
- `PasswordConfig.java` - Configuración de encriptación de contraseñas
- `SecurityConfig.java` - Configuración general de Spring Security

---

### **3. Modules (Módulos de Dominio)**

La carpeta `modules/` implementa la **separación por dominios de negocio**:

#### **modules/patient/ (Módulo de Pacientes)**

Módulo completamente implementado que sigue la arquitectura limpia:

**patient/application/**: Capa de aplicación

- **dto/request/**: DTOs para peticiones
  - `CreateRolPerfilDto.java` - Creación de roles de perfil
  - `PacienteDetalleDto.java` - Datos del paciente
  - `RegisterPacienteDto.java` - Registro de pacientes
- **dto/response/**: DTOs para respuestas
  - `RegisterResponseDto.java` - Respuesta de registro
  - `RolPerfilDto.java` - Información de rol/perfil
  - `UsuarioResponseDto.java` - Respuesta de usuario
- **mapper/**: Mapeo entre DTOs y entidades
  - `PacienteDetalleMapper.java`
- **service/**: Lógica de aplicación
  - `UsuarioService.java`

**patient/domain/**: Lógica de dominio específica

- **entity/**: Entidades específicas del dominio (vacío, usa las de core)
- **repository/**: Repositorios específicos (vacío, usa los de core)
- **service/**: Servicios de dominio específicos

**patient/web/**: Capa de presentación

- **controller/**: Controladores REST
  - `AuthController.java` - Autenticación

#### **modules/medical/ (Módulo Médico)**

Estructura preparada pero sin implementación:

- Seguirá el mismo patrón que el módulo patient
- **application/**: dto, mapper, service (vacíos)
- **domain/**: entity, repository, service (vacíos)
- **web/controller/**: (vacío)

#### **modules/future_modules/ (Módulos Futuros)**

Contiene funcionalidades en desarrollo:

- **controller/**: `AdminMedicosController.java`, `SuperAdminController.java`, `UsuarioController.java`
- **dto/**: DTOs específicos para estos controladores
- **mapper/**: Mapeadores específicos
- **service/**: `RolPerfilService.java`

---

### **4. Web (Configuración Web Global)**

#### **web/config/**

- `SwaggerConfig.java` - Configuración de documentación API

#### **web/exception/**

- `GlobalExceptionHandler.java` - Manejo global de excepciones

---

### **5. Resources**

#### **resources/**

- `application.properties` - Configuración de la aplicación
- `README_PROPERTIES.md` - Documentación de propiedades

#### **resources/db/migration/**

- `V1__create_database_schema.sql` - Script de migración de base de datos (Flyway)

#### **resources/static/** y **resources/templates/**

- Recursos estáticos y plantillas (vacíos)

---

## 🏗️ Principios Arquitectónicos Implementados

### **1. Domain-Driven Design (DDD)**

- **Separación por dominios**: patient, medical (futuro)
- **Entidades de dominio** bien definidas en core/domain/entity
- **Repositorios** como interfaces de acceso a datos

### **2. Clean Architecture**

- **Capa de Dominio** (core/domain): Lógica de negocio pura
- **Capa de Aplicación** (application): Casos de uso y orquestación
- **Capa de Infraestructura** (infrastructure): Detalles técnicos
- **Capa de Presentación** (web): Controllers y configuración web

### **3. Arquitectura Hexagonal**

- **Puerto-Adaptador**: Repositorios como puertos, implementaciones como adaptadores
- **Inversión de dependencias**: El dominio no depende de infraestructura

### **4. Modularidad**

- Cada módulo es **independiente** y **cohesivo**
- **Separación de responsabilidades** clara
- **Reutilización** de componentes core

---

## 🔄 Flujo de Datos

1. **Request** → Controller (web layer)
2. **Controller** → Service (application layer)
3. **Service** → Repository (domain layer)
4. **Repository** → Database (infrastructure layer)
5. **Response** ← Mapper + DTO (application layer)

---

## 🚀 Ventajas de esta Arquitectura

- **Mantenibilidad**: Código organizado y fácil de mantener
- **Escalabilidad**: Nuevos módulos se añaden fácilmente
- **Testabilidad**: Cada capa puede probarse independientemente
- **Flexibilidad**: Cambios en una capa no afectan otras
- **Reutilización**: Componentes core compartidos entre módulos
- **Separación de responsabilidades**: Cada capa tiene un propósito específico

Esta arquitectura es ideal para sistemas empresariales complejos que requieren alta mantenibilidad y escalabilidad.
