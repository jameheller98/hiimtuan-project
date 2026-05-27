# Project: hiimtuan-project

Full-stack portfolio project. Spring Boot microservices backend + Next.js web + Flutter mobile app.

## Quick Start

### Prerequisites
- Java 17, Maven (or use `backend/mvnw` if Maven is not installed globally)
- Node.js 18+, Yarn
- Flutter SDK
- MySQL 8 (or Docker)

### Local Development

**Build common-service first** (shared library):
```bash
cd backend/springboot-common-service && mvn install -DskipTests
# or without system Maven: cd backend && ./mvnw -pl springboot-common-service install -DskipTests
```

**Start infrastructure**:
```bash
# Start MySQL locally or: docker run -p 3306:3306 -e MYSQL_ROOT_PASSWORD=... -e MYSQL_DATABASE=hiimtuan mysql:8
cd backend/springboot-naming-server && mvn spring-boot:run
cd backend/springboot-config && mvn spring-boot:run
```

**Start services** (in order):
```bash
cd backend/springboot-mail-service  && mvn spring-boot:run   # gRPC :9090
cd backend/springboot-user-service  && mvn spring-boot:run   # HTTP :8000 gRPC :9091
cd backend/springboot-post-service  && mvn spring-boot:run   # HTTP :8200
cd backend/springboot-api-gateway   && mvn spring-boot:run   # HTTP :8765
```

**Start frontend**:
```bash
cd frontend/hiimtuan-portfolio-web && yarn install && yarn dev  # :3000
```

### Docker
```bash
docker-compose up --build
```

## Project Structure

```
hiimtuan-project/
├── backend/
│   ├── springboot-api-gateway/         # Entry point, JWT auth, routing
│   ├── springboot-user-service/        # User CRUD + gRPC server
│   ├── springboot-post-service/        # Post CRUD
│   ├── springboot-mail-service/        # Email via SMTP (gRPC server)
│   ├── springboot-common-service/      # Shared JWT library (NOT a service)
│   ├── springboot-naming-server/       # Eureka service discovery
│   ├── springboot-config/             # Centralized config server
│   └── springboot-cloud-server-central/
├── frontend/
│   ├── hiimtuan-portfolio-web/        # Next.js web app
│   └── hi_runner/                     # Flutter mobile app
├── architecture.md                    # Detailed architecture doc
├── docker-compose.yml
└── CLAUDE.md
```

## Backend Architecture

See [ARCHITECTURE.md](./ARCHITECTURE.md) for full details.

### Key Principles
- **API Gateway** is a thin router: JWT validation only, no user DB
- **common-service** is a Maven library jar, NOT a deployable service — do not add it to docker-compose
- Inter-service communication uses **gRPC** (synchronous) for typed, efficient calls
- Each service owns its own DB tables; never share entity classes across services

### Adding a new service
1. Create Spring Boot project in `backend/`
2. Add `spring-cloud-starter-netflix-eureka-client` and `spring-cloud-starter-config` dependencies
3. Add common-service dependency if JWT parsing needed
4. Add to docker-compose with health check
5. Add route in api-gateway `route/` package

## Backend Conventions

### Package structure per service
```
com.hiimtuan.<service_name>/
├── config/         # Spring config beans, gRPC interceptors
├── controller/     # REST controllers
├── dto/
│   ├── request/    # *RequestDto
│   └── response/   # *ResponseDto, ApiResponse
├── entity/         # JPA entities
├── exception/
│   ├── custom/     # Custom RuntimeException subclasses
│   └── GlobalExceptionHandler.java
├── mapper/         # Entity ↔ DTO converters
├── repository/     # Spring Data repositories
└── service/        # *Service interface + *ServiceImpl
```

### gRPC
- Proto files: `src/main/proto/`
- Server implementations extend `*ImplBase` with `@Service`
- JWT interceptor (`GrpcJwtInterceptor`) auto-registers as `@Component` — provides `JWT_CTX_KEY` in gRPC context
- gRPC channel names in properties: `spring.grpc.client.channels.<name>.address`

### API Response format
All endpoints return:
```json
{ "status": 200, "message": "...", "data": { ... } }
```
Use `ApiResponse.success(message, data)` from each service's `dto/response/ApiResponse.java`.

## Frontend Conventions

### Next.js (`hiimtuan-portfolio-web`)
- State management: **Zustand** (`src/stores/`)
- Data fetching: **TanStack Query** mutations in `src/api/mutations/`, queries in `src/api/queries/`
- Auth token stored in Zustand store (`createTokenSlice.ts`)

### Flutter (`hi_runner`)
Clean Architecture pattern with BLoC + get_it:
- `domain/` — entities, repository interfaces, use cases (pure Dart, no Flutter)
- `data/` — implementations, API models, repository impls
- `presentation/bloc/` — BLoC (events + states as `part` files), screens, widgets
- `core/di/injection.dart` — get_it service locator (`sl`); register all deps here
- `shared/services/storage_service.dart` — token persistence via flutter_secure_storage

**State management**: `flutter_bloc` + `equatable`. Each feature has its own BLoC registered as `registerFactory` in get_it.
**DI**: `get_it`. Access via `sl<T>()`. Services/repos are `registerLazySingleton`, BLoCs are `registerFactory`.
**Navigation**: `go_router`. Auth redirect driven by `AuthBloc` state via `_BlocListenable`.

## Environment Variables

For local dev, configure in `application.properties`. For Docker, use `.env` file:
```env
MYSQL_ROOT_PASSWORD=your_password
MYSQL_DATABASE=hiimtuan
MAIL_USERNAME=your_gmail
MAIL_PASSWORD=your_app_password
```

## Service Ports

| Service | HTTP | gRPC |
|---|---|---|
| API Gateway | 8765 | — |
| User Service | 8000 | 9091 |
| Post Service | 8200 | — |
| Mail Service | 8100 | 9090 |
| Naming Server | 8761 | — |
| Config Server | 8888 | — |

## Common Tasks

### Add a new gRPC method to user-service
1. Update `src/main/proto/user_service.proto`
2. Implement in `UserGrpcServiceImpl.java`
3. If api-gateway needs to call it: update `src/main/proto/user_service.proto` in api-gateway too
4. Add stub call in `AuthenticationServiceImpl.java`

### Add a new REST endpoint
1. Add method to `*Service` interface
2. Implement in `*ServiceImpl`
3. Add handler in `*Controller`
4. If protected, ensure api-gateway's `SecurityConfiguration` allows/blocks the path

### Run tests
```bash
cd backend/<service> && mvn test
```
