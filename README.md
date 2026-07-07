# Sistema BT - Battery Trade

Aplicación de gestión comercial compuesta por un backend Java Spring Boot y un frontend Angular.

## Estructura del repositorio

- `Backend/app/`: aplicación backend Spring Boot.
- `Frontend/batterytrade-front/`: aplicación frontend Angular.

## Backend

### Qué hace

El backend gestiona la lógica de negocio, la persistencia y los endpoints REST para la aplicación de ventas. Usa PostgreSQL como base de datos y devuelve DTOs planos para el frontend.

### Tecnologías

- Java 21
- Spring Boot 4.1
- Spring Data JPA
- Spring MVC
- Spring Security
- PostgreSQL
- JWT (jjwt)
- Springdoc OpenAPI
- Validación con `spring-boot-starter-validation`

### Archivos clave

- `Backend/app/pom.xml`: configuración Maven y dependencias.
- `Backend/app/src/main/resources/application.properties`: configuración de base de datos y propiedades de Spring.
- `Backend/app/src/main/java/com/batterytrade/app/controller/`: controladores REST.
- `Backend/app/src/main/java/com/batterytrade/app/service/`: lógica de negocio y reglas de ventas.
- `Backend/app/src/main/java/com/batterytrade/app/model/`: entidades JPA y relaciones.
- `Backend/app/src/main/java/com/batterytrade/app/dto/`: objetos de transferencia de datos.
- `Backend/app/src/main/java/com/batterytrade/app/exception/`: manejo global de excepciones.

### Requisitos

- Java JDK 21
- Maven
- PostgreSQL

### Configuración

Edita `Backend/app/src/main/resources/application.properties` según tu entorno de base de datos:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/batterytrade
spring.datasource.username=postgres
spring.datasource.password=jeanccf7
```

Es recomendable usar la base de datos `batterytrade` y crear las tablas automáticamente con `spring.jpa.hibernate.ddl-auto=update`.

### Ejecutar backend

Desde `Backend/app/`:

```bash
./mvnw.cmd spring-boot:run
```

O crear el artefacto y ejecutar el JAR:

```bash
./mvnw.cmd package -DskipTests
java -jar target/app-0.0.1-SNAPSHOT.jar
```

El backend queda disponible en `http://localhost:8080`.

### Endpoints importantes

- `GET /api/ventas`
- `GET /api/ventas/{id}`
- `POST /api/ventas`
- `DELETE /api/ventas/{id}`

## Frontend

### Qué hace

El frontend es una aplicación Angular que consume la API del backend para mostrar y gestionar ventas, clientes, productos y usuarios.

### Tecnologías

- Angular 21.2
- TypeScript 5.9
- RxJS
- npm 11

### Archivos clave

- `Frontend/batterytrade-front/package.json`: dependencias y scripts de Angular.
- `Frontend/batterytrade-front/src/app/pages/ventas/ventas.ts`: página de ventas y formulario de creación.
- `Frontend/batterytrade-front/src/app/services/venta.service.ts`: llamadas HTTP al backend.
- `Frontend/batterytrade-front/src/app/models/`: modelos de datos usados por el frontend.
- `Frontend/batterytrade-front/src/app/shared/`: componentes reutilizables.

### Requisitos

- Node.js compatible con npm 11
- npm 11.12.1 (según `package.json`)

### Ejecutar frontend

Desde `Frontend/batterytrade-front/`:

```bash
npm install
npm start
```

La aplicación Angular corre en `http://localhost:4200`.

### Conexión con backend

El frontend consume el backend en `http://localhost:8080/api`. Asegúrate de que el backend esté levantado antes de abrir la aplicación.

Si cambias el host o puerto del backend, actualiza las URLs en `Frontend/batterytrade-front/src/app/services/venta.service.ts`.

## Flujo principal

1. El frontend consume el backend en `http://localhost:8080/api/ventas`.
2. El backend persiste datos en PostgreSQL.
3. El frontend muestra y crea ventas, clientes, productos y demás entidades.

## Notas

- El backend está configurado para permitir CORS desde `http://localhost:4200`.
- Si la API responde con errores 500, revisa que el backend actual esté arrancado y que no exista otra instancia antigua en el mismo puerto.
- El frontend espera que el backend sirva datos planos tipo DTO en lugar de entidades JPA completas.

## Patrones implementados

En el proyecto se documentaron e introdujeron tres patrones (uno por cada categoría):

- **Creacional — Factory**: `VentaFactory` centraliza la creación de instancias de `Venta`.
	- Archivo: `Backend/app/src/main/java/com/batterytrade/app/factory/VentaFactory.java`
	- Uso: `VentaService` utiliza `VentaFactory.create(cliente, vendedor)` en vez de instanciar `new Venta()` directamente.

- **Estructural — Adapter**: `SunatAPI` actúa como adaptador para integrar con un servicio externo (SUNAT).
	- Archivo: `Backend/app/src/main/java/com/batterytrade/app/adapter/SunatAPI.java`
	- Uso: Después de guardar una venta, `VentaService` llama a `sunat.sendInvoice(venta)` (implementación simulada).

- **Comportamental — Strategy**: `PaymentStrategy` define comportamientos diferentes según el método de pago (Efectivo, Yape,...).
	- Archivos:
		- `Backend/app/src/main/java/com/batterytrade/app/payment/PaymentStrategy.java`
		- `Backend/app/src/main/java/com/batterytrade/app/payment/EfectivoStrategy.java`
		- `Backend/app/src/main/java/com/batterytrade/app/payment/YapeStrategy.java`
		- `Backend/app/src/main/java/com/batterytrade/app/payment/PaymentStrategyFactory.java`
	- Uso: `VentaService` obtiene la estrategia con `PaymentStrategyFactory.getStrategy(metodo)` y la ejecuta tras persistir la venta.

### Ejemplos de prueba (curl)

- Obtener ventas:

```bash
curl -s http://localhost:8080/api/ventas | jq
```

- Crear una venta (ejemplo mínimo):

```bash
curl -X POST http://localhost:8080/api/ventas \
	-H "Content-Type: application/json" \
	-d '{
		"clienteId": 1,
		"vendedorId": 2,
		"metodoPago": "YAPE",
		"estadoPago": "PAGADO",
		"estadoEntrega": "ENTREGADO",
		"detalles": [{ "productoId": 1, "cantidad": 1 }]
	}'
```

Nota: ajusta los IDs según los registros existentes en tu base de datos.

### Uso manual de los patrones desde código (ejemplo breve)

Si quieres probar los patrones sin que se ejecuten automáticamente desde `VentaService`, puedes invocarlos manualmente desde un test o un `@Component` de prueba. Ejemplo rápido en Java:

```java
// Crear venta con Factory
Venta v = VentaFactory.create(cliente, vendedor);

// Enviar comprobante (Adapter)
SunatAPI sunat = new SunatAPI();
sunat.sendInvoice(v);

// Aplicar estrategia de pago (Strategy)
PaymentStrategy ps = PaymentStrategyFactory.getStrategy("YAPE");
ps.handle(v);
```

Los logs de estas operaciones aparecen en la salida estándar del servidor (Spring Boot usa Logback por defecto).

## README adicional

- Existe un `Frontend/batterytrade-front/README.md` con detalles específicos del frontend.

---

### Comandos útiles

- Compilar backend: `./mvnw.cmd compile`
- Empaquetar backend: `./mvnw.cmd package -DskipTests`
- Iniciar backend: `java -jar target/app-0.0.1-SNAPSHOT.jar`
- Iniciar frontend: `npm start`
