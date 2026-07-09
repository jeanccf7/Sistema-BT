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

Interfaz de Swagger `http://localhost:8080/swagger-ui/index.html#`

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

En el proyecto se integraron tres patrones de diseño para separar responsabilidades y hacer más mantenible el flujo de ventas.

### 1) Factory (creacional)

El patrón Factory se usa para centralizar la creación de objetos de tipo `Venta`.

- Archivo principal: `Backend/app/src/main/java/com/batterytrade/app/factory/VentaFactory.java`
- Propósito: evitar que la lógica de negocio cree directamente nuevas instancias de `Venta` con `new Venta()` en distintos puntos.
- Beneficio: si en el futuro cambia la forma en que se construye una venta, solo se modifica la factory.

En este proyecto, `VentaService` utiliza `VentaFactory.create(cliente, vendedor)` para crear la entidad de venta con datos base como fecha, cliente y vendedor. Esto mantiene el código más limpio y desacoplado.

### 2) Adapter (estructural)

El patrón Adapter se usa para integrar una clase o sistema externo con la lógica interna del proyecto sin acoplar directamente los modelos del sistema a la API externa.

- Archivos principales:
  - `Backend/app/src/main/java/com/batterytrade/app/adapter/SunatAPI.java`
  - `Backend/app/src/main/java/com/batterytrade/app/adapter/SunatAdapter.java`
  - `Backend/app/src/main/java/com/batterytrade/app/adapter/SunatExterna.java`
- Propósito: simular la comunicación con SUNAT sin modificar la estructura interna de la venta.
- Funcionamiento: cuando se guarda una venta, el backend invoca `sunat.sendInvoice(ventaGuardada)`. El adaptador transforma o encapsula esa operación y devuelve un mensaje de texto como `SUNAT adapter: comprobante enviado para la venta X`.

Este mensaje se devuelve en la respuesta de la venta y luego se imprime en consola desde el frontend para mostrar que la integración simulada se ejecutó.

### 3) Strategy (comportamental)

El patrón Strategy se usa para seleccionar distintos comportamientos según el método de pago elegido por el usuario.

- Archivos principales:
  - `Backend/app/src/main/java/com/batterytrade/app/payment/PaymentStrategy.java`
  - `Backend/app/src/main/java/com/batterytrade/app/payment/EfectivoStrategy.java`
  - `Backend/app/src/main/java/com/batterytrade/app/payment/YapeStrategy.java`
  - `Backend/app/src/main/java/com/batterytrade/app/payment/PlinStrategy.java`
  - `Backend/app/src/main/java/com/batterytrade/app/payment/TransferenciaStrategy.java`
  - `Backend/app/src/main/java/com/batterytrade/app/payment/PaymentStrategyFactory.java`
- Propósito: evitar grandes condicionales para decidir qué hacer con cada tipo de pago.
- Funcionamiento: la factory devuelve la estrategia adecuada según el valor de `metodoPago` elegido en el formulario. Si la venta se guarda como `PAGADO`, el backend ejecuta esa estrategia y devuelve un mensaje como `Procesando pago YAPE para la venta X`.

### Flujo real de registro de ventas

1. En la vista de ventas del frontend, al hacer clic en Guardar Venta, se construye un objeto con cliente, vendedor, método de pago, estado de pago, estado de entrega y detalles.
2. El frontend envía esa información al endpoint `POST /api/ventas` mediante `VentaService`.
3. En el backend, `VentaService.registrar()` persiste la venta en base de datos.
4. Inmediatamente después, se ejecuta el adapter SUNAT para simular el envío del comprobante y se genera un mensaje de respuesta.
5. Si la venta quedó marcada como `PAGADO`, se activa la strategy correspondiente según el `metodoPago` y se genera otro mensaje.
6. Ambos mensajes se devuelven en la respuesta de la venta y el frontend los muestra en consola para evidenciar la ejecución de los patrones.

### Relación entre los tres patrones

Aunque cada patrón resuelve un problema distinto, en este proyecto trabajan juntos en el mismo flujo:

- `Factory` crea la venta base.
- `Adapter` simula la integración con SUNAT al guardar la venta.
- `Strategy` define cómo procesar el pago según el método seleccionado.

Esta combinación permite que la lógica de negocio quede más organizada, extensible y fácil de entender.

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
