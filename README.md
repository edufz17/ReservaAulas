# ReservaAulas

API REST para la gestión de aulas y reservas de un centro educativo. Permite
mantener aulas, profesores y franjas horarias, y asociarlos en reservas
persistidas en MySQL.

## Estado actual

El proyecto es un backend funcional de alcance inicial. Implementa operaciones
CRUD mediante Spring MVC y Spring Data JPA. No incluye todavía una interfaz
web, autenticación/autorización, documentación OpenAPI ni reglas completas de
planificación.

### Tecnologías

- Java 21
- Spring Boot 4.1.2
- Spring Web MVC
- Spring Data JPA e Hibernate
- MySQL Connector/J
- Lombok
- Maven Wrapper (`./mvnw`)

## Estructura

```text
src/main/java/es/iesjuanbosco/reservaaulas/
├── ReservaAulasApplication.java
├── controller/       # API HTTP
├── entity/           # Aula, Profesor, FranjaHoraria y Reserva
└── repository/       # Repositorios Spring Data
```

Una `Reserva` relaciona un `Profesor`, un `Aula` y una `FranjaHoraria`. La fecha
de creación se asigna automáticamente con `@PrePersist`. Las relaciones
inversas se excluyen del JSON para evitar ciclos.

## Requisitos y puesta en marcha

Se necesita Java 21 o compatible y Maven (o el Maven Wrapper). También se
necesita MySQL accesible en `localhost:3306`. La configuración por defecto usa
la base de datos `reserva_aulas` y permite crearla automáticamente mediante la
opción de conexión de MySQL. Ajusta las credenciales en una configuración local
no versionada antes de iniciar la aplicación.

```bash
./mvnw test
./mvnw spring-boot:run
```

La aplicación se inicia con el nombre `ReservaAulas`. JPA está configurado con
`ddl-auto=update`, por lo que Hibernate actualiza el esquema durante el
desarrollo. Esta opción debe revisarse antes de producción, donde conviene usar
migraciones versionadas.

## API disponible

| Recurso | Operaciones |
|---|---|
| `/aulas` | `GET`, `POST`, `GET /{id}`, `PUT /{id}`, `DELETE /{id}` |
| `/profesores` | `GET`, `POST`, `GET /{id}`, `PUT /{id}`, `DELETE /{id}` |
| `/franjasHorarias` | `GET`, `POST`, `GET /{id}`, `PUT /{id}`, `DELETE /{id}` |
| `/reservas` | `GET`, `POST`, `GET /{id}`, `PUT /{id}`, `DELETE /{id}` |

`GET /aulas` admite los filtros opcionales `ordenadores` y `capacidad`. Si se
envían ambos, devuelve aulas que cumplen ambas condiciones; `capacidad` se
interpreta como un mínimo estricto (`capacidad > valor`).

Ejemplo de creación de un aula:

```bash
curl -X POST http://localhost:8080/aulas \
  -H 'Content-Type: application/json' \
  -d '{"nombre":"Aula 101","capacidad":30,"tieneOrdenadores":true}'
```

Las operaciones de creación devuelven `201 Created` en aulas, profesores y
franjas horarias. Las reservas devuelven actualmente `200 OK`. La consulta de
un identificador inexistente devuelve `404 Not Found` y el borrado correcto
devuelve `204 No Content`.

## Problemas encontrados y decisiones tomadas

Durante el desarrollo se identificaron estos puntos relevantes:

1. **Configuración dependiente de MySQL.** La prueba de contexto y el arranque
   necesitan una base de datos disponible. Se mantuvo MySQL porque es la
   tecnología configurada por el proyecto y se evitó añadir una base embebida
   que pudiera ocultar problemas de integración.
2. **Lógica de negocio mínima.** Los controladores delegan directamente en los
   repositorios. Se decidió conservar este alcance para la primera versión,
   pero la siguiente ampliación debe validar fechas, relaciones y conflictos
   antes de guardar una reserva.
3. **Validación insuficiente.** Las entidades no usan `@Valid` ni restricciones
   Bean Validation. Por ello, datos incompletos pueden llegar hasta la capa de
   persistencia. Es una deuda conocida, no una garantía de la API.
4. **Esquema gestionado por Hibernate.** `ddl-auto=update` facilita el
   desarrollo local, pero no es una estrategia de despliegue reproducible. Se
   dejó documentada la necesidad de migraciones antes de producción.
5. **Cobertura de pruebas reducida.** Actualmente existe una prueba de carga del
   contexto (`contextLoads`). El resultado se verificó con `./mvnw test`;
   conviene añadir pruebas de controladores, repositorios y reglas de
   solapamiento antes de considerar estable la aplicación.
6. **Advertencias del entorno de pruebas.** Maven completó correctamente, aunque
   el runtime mostró advertencias de Mockito sobre el auto-attach de su agente y
   de Spring sobre `open-in-view`. No se ocultaron esas advertencias; deben
   revisarse al actualizar el JDK o endurecer la configuración de producción.

Las propuestas de la IA se aplicaron de forma conservadora: reutilizar
`JpaRepository`, devolver códigos HTTP coherentes, evitar ciclos JSON mediante
`@JsonIgnore`, separar futuras reglas de negocio en servicios y documentar las
deudas técnicas en vez de simular que ya estaban resueltas.

## Prompts utilizados como referencia

Algunos prompts representativos del desarrollo fueron:

- “Analiza la estructura de esta API Spring Boot y describe entidades,
  relaciones, endpoints y dependencias sin inventar funcionalidades.”
- “Propón una estrategia para impedir reservas duplicadas por aula y franja
  horaria; incluye cambios de dominio, repositorio, servicio y pruebas.”
- “Revisa este controlador REST buscando códigos HTTP inconsistentes, entradas
  sin validar y errores que se estén ocultando.”
- “Genera documentación para agentes que indique cómo ejecutar, probar y
  modificar este proyecto respetando su arquitectura actual.”

Estos prompts se usaron como apoyo para revisar decisiones, no como sustituto
de ejecutar las pruebas ni de comprobar el código existente.

## Errores y límites de la asistencia de IA

La IA puede proponer endpoints o clases que no existen, asumir que hay una
interfaz web, confundir `capacidad > valor` con `capacidad >= valor` o dar por
hecha la validación de reservas al ver las relaciones JPA. También puede
recomendar una base embebida para las pruebas sin considerar que el proyecto
está configurado explícitamente con MySQL. Por eso se verificó cada afirmación
contra el código y se ejecutó la prueba Maven con la base de datos disponible.

No se deben copiar credenciales sugeridas por la IA ni aceptar cambios que
modifiquen el esquema, los códigos HTTP o las relaciones sin una prueba y una
decisión explícita.

## Próximos pasos recomendados

- Añadir Bean Validation y manejo centralizado de errores.
- Crear una capa de servicio para reservas y rechazar solapamientos.
- Añadir pruebas HTTP y de persistencia independientes del entorno local.
- Incorporar migraciones de base de datos y perfiles de configuración.
- Añadir autenticación, autorización y documentación OpenAPI si el sistema se
  expone fuera del entorno académico.
