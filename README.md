# Parcial Final Programación N-Capas – (Seguridad con Spring Security + JWT)

Este repositorio contiene un proyecto para evaluar y practicar los conceptos de seguridad en aplicaciones Spring Boot usando JWT, roles y Docker.

### Estudiantes
- **Nombre del estudiante 1**: Daniel Alejndro Martínez Rivas - 00085920
- **Nombre del estudiante 2**: Kevin Edgardo Martínez Castellon - 00129421
- Sección: 02
---

## Sistema de Soporte Técnico

### Descripción
Simula un sistema donde los usuarios pueden crear solicitudes de soporte (tickets) y los técnicos pueden gestionarlas. Actualmente **no tiene seguridad implementada**.

Su tarea es **agregar autenticación y autorización** utilizando **Spring Security + JWT**, y contenerizar la aplicación con Docker.

### Requisitos generales

- Proyecto funcional al ser clonado y ejecutado con Docker.
- Uso de PostgreSQL (ya incluido en docker-compose).
- Seguridad implementada con JWT.
- Roles `USER` y `TECH`.
- Acceso restringido según el rol del usuario.
- Evidencia de funcionamiento (colección de Postman/Insomnia/Bruno o capturas de pantalla).

**Nota: El proyecto ya tiene una estructura básica de Spring Boot con endpoints funcionales para manejar tickets. No es necesario modificar la lógica de negocio, solo agregar seguridad. Ademas se inclye un postman collection para probar los endpoints. **

_Si van a crear mas endpoints como el login o registrarse recuerden actualizar postman/insomnia/bruno collection_

### Partes de desarrollo

#### Parte 1: Implementar login con JWT
- [ ] Crear endpoint `/auth/login`.
- [ ] Validar usuario y contraseña (puede estar en memoria o en BD).
- [ ] Retornar JWT firmado.

#### Parte 2: Configurar filtros y validación del token
- [ ] Crear filtro para validar el token en cada solicitud.
- [ ] Extraer usuario desde el JWT.
- [ ] Añadir a contexto de seguridad de Spring.

#### Parte 3: Proteger endpoints con Spring Security
- [ ] Permitir solo el acceso al login sin token.
- [ ] Proteger todos los demás endpoints.
- [ ] Manejar errores de autorización adecuadamente.

#### Parte 4: Aplicar roles a los endpoints

| Rol   | Acceso permitido                                 |
|--------|--------------------------------------------------|
| USER  | Crear tickets, ver solo sus tickets              |
| TECH  | Ver todos los tickets, actualizar estado         |

- [ ] Usar `@PreAuthorize` o reglas en el `SecurityFilterChain`.
- [ ] Validar que un USER solo vea sus tickets.
- [ ] Validar que solo un TECH pueda modificar tickets.

#### Parte 5: Agregar Docker
- [ ] `Dockerfile` funcional para la aplicación.
- [ ] `docker-compose.yml` que levante la app y la base de datos.
- [ ] Documentar cómo levantar el entorno (`docker compose up`).

#### Parte 6: Evidencia de pruebas
- [ ] Probar todos los flujos con Postman/Insomnia/Bruno.
- [ ] Mostrar que los roles se comportan correctamente.
- [ ] Incluir usuarios de prueba (`user`, `tech`) y contraseñas.


# Sistema de Soporte Técnico – Proyecto N-Capas

Simula un sistema donde los usuarios pueden crear solicitudes de soporte (tickets) y los técnicos pueden gestionarlas.

Este proyecto está **contenarizado con Docker**, utilizando **PostgreSQL y Spring Boot** de forma integrada para facilitar pruebas y despliegue.

---

## 🚀 Como se contenerizó el proyecto:

## ⚙️ Estructura principal del proyecto

* `Dockerfile` → Conteneriza la aplicación Spring Boot.
* `docker-compose.yml` → Orquesta PostgreSQL y la app.
* `src/main/resources/application.yml` → Configuración con variables de entorno para conexión a PostgreSQL.

---

## ⚙️ Levantar el proyecto con Docker

1️⃣ Compilar el proyecto (opcional si quieres un build limpio):

```bash
./mvnw clean package -DskipTests
```

2️⃣ Levantar la aplicación y la base de datos:

```bash
docker compose up --build
```

✅ Esto:

* Levantará **PostgreSQL en el puerto `5433` local**.
* Levantará **Spring Boot en el puerto `8080` local**.

---

## ⚙️ Verificar contenedores en ejecución

```bash
docker ps
```

Debes ver:

* `parcial_postgres` → PostgreSQL.
* `parcial_app` → Aplicación Spring Boot.
