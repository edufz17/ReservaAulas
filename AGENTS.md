# Instrucciones para agentes

## Contexto del proyecto

ReservaAulas es una API REST para gestionar aulas, profesores, franjas horarias y
reservas. Está implementada con Java 21, Spring Boot 4.1.2, Spring Web MVC,
Spring Data JPA, Hibernate, Lombok y MySQL. El paquete raíz es
`es.iesjuanbosco.reservaaulas`.

La aplicación sigue una estructura sencilla:

- `src/main/java/.../backend/entity`: entidades JPA y relaciones del dominio.
- `src/main/java/.../backend/repository`: interfaces `JpaRepository`.
- `src/main/java/.../backend/controller`: endpoints REST y operaciones CRUD.
- `src/main/resources/application.properties`: configuración de MySQL y JPA.
- `src/test/java`: pruebas de contexto y futuras pruebas de API/dominio.

## Reglas de trabajo

1. Lee primero las entidades, repositorios y controladores relacionados antes de
   modificar un endpoint. Mantén las rutas y los nombres JSON existentes salvo
   que la tarea pida explícitamente un cambio incompatible.
2. Usa las abstracciones de Spring Data existentes antes de añadir SQL o lógica
   de acceso a datos manual. La lógica de negocio que crezca debe extraerse a
   servicios, en lugar de acumularla en los controladores.
3. Conserva las relaciones JPA y evita ciclos de serialización. Las colecciones
   inversas de `Aula`, `Profesor` y `FranjaHoraria` están ignoradas con
   `@JsonIgnore`; cualquier cambio de serialización debe probarse expresamente.
4. No guardes contraseñas, tokens ni credenciales en el repositorio. La
   configuración local de la base de datos debe sobrescribirse mediante un perfil
   o variables/configuración externa antes de usar la aplicación fuera del
   entorno local.
5. Valida entradas y errores con respuestas HTTP explícitas. No conviertas
   excepciones en respuestas exitosas ni ocultes fallos de persistencia.
6. Al añadir una regla de negocio (por ejemplo, impedir dos reservas para el
   mismo aula y franja), implementa también una prueba que cubra el caso
   válido y el conflicto.
7. Mantén los cambios centrados en la tarea y respeta el estilo existente:
   nombres en español para el dominio, tipos Java explícitos y formato Maven.
   No edites `target/`, `.idea/` ni archivos generados.

## Comandos habituales

Desde la raíz del repositorio:

```bash
./mvnw test
./mvnw package
./mvnw spring-boot:run
```

El arranque y la prueba de contexto necesitan una instancia accesible de MySQL
según `src/main/resources/application.properties`. Si la conexión falla, no
soluciones el problema introduciendo credenciales en el código: documenta la
configuración requerida o usa un perfil de pruebas aislado.

## Verificación antes de entregar

- Ejecuta las pruebas Maven afectadas y, cuando sea razonable, `./mvnw test`.
- Comprueba códigos HTTP, payloads JSON y casos de recurso inexistente.
- Revisa el diff para evitar cambios accidentales en configuración o secretos.
- Actualiza `README.md` cuando cambien endpoints, configuración, decisiones
  técnicas o limitaciones conocidas.
