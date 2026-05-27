# Backend Architecture

Spring Boot 3.5.4 microservices with Spring Cloud (Eureka, Config Server) and gRPC inter-service communication.

## Services

### API Gateway (`:8765`)
Single entry point. Routes requests, validates JWT, owns all auth endpoints.

**Does NOT** own user data — delegates credential validation, password management to user-service via gRPC.

**Auth flow**:
| Endpoint | Action |
|---|---|
| `POST /api/v1/auth/login` | gRPC `validateCredentials` → user-service |
| `POST /api/v1/auth/refresh` | validates RefreshToken from own DB |
| `POST /api/v1/auth/reset-password` | gRPC `getUserByEmail` → user-service + gRPC `sendMail` → mail-service |
| `POST /api/v1/auth/change-password` | gRPC `updatePassword` → user-service |

**Own tables**: `refresh_tokens`, `password_reset_token`

**gRPC clients**: mail-service (`:9090`), user-service (`:9091`)

---

### User Service (`:8000`, gRPC `:9091`)
Source of truth for all user identity data.

**HTTP endpoints**: `/api/v1/user/**`, `/api/v1/admin/**`

**gRPC methods**:
| Method | Description |
|---|---|
| `getUser` | Decode JWT from gRPC metadata, return user info |
| `validateCredentials` | Verify email + password, return user |
| `getUserByEmail` | Find user by email |
| `updatePassword` | Hash and persist new password |

**Own tables**: `users`, `roles`

---

### Post Service (`:8200`)
Blog post CRUD. Identifies the current user by forwarding the JWT to user-service via gRPC.

**HTTP endpoints**: `/api/v1/post/**`
| Method | Path | Auth |
|---|---|---|
| POST | `/` | required |
| GET | `/list` | public |
| GET | `/{id}` | public |
| GET | `/my-posts` | required |
| PUT | `/{id}` | author only |
| DELETE | `/{id}` | author only |

**Own tables**: `posts`

**gRPC client**: user-service (`:9091`)

---

### Mail Service (`:8100`, gRPC `:9090`)
Pure infrastructure service. Sends email via SMTP (Gmail).

**gRPC methods**:
| Method | Description |
|---|---|
| `sendSimpleMail` | Send plain-text email |

---

### Common Service (library jar — not deployed)
Shared Maven library providing JWT utilities consumed by api-gateway and user-service.

- `JwtService` / `JwtServiceImpl` — token generation and validation
- `AuthenticationConfiguration` — binds `security.jwt.*` properties
- `RoleEnum` — USER, ADMIN, SUPER_ADMIN

## Database

Single MySQL 8 instance. Each service manages its own tables via JPA `ddl-auto=update`. No cross-service foreign keys.

| Table | Owner |
|---|---|
| `users` | user-service |
| `roles` | user-service |
| `posts` | post-service |
| `refresh_tokens` | api-gateway |
| `password_reset_token` | api-gateway |

## Configuration

Spring Cloud Config Server (`cloud-server-central`) serves per-environment properties from `springboot-config/`:

```
springboot-config/
├── application-{env}.properties       # shared (JWT secret, expiry times)
└── api-gateway-{env}.properties       # gateway-specific
```

JWT properties (shared config):
```properties
security.jwt.secret-key=...
security.jwt.expiration-time=3600000          # 1 hour
security.jwt.refresh-expiration-time=...      # 7 days
security.jwt.password-reset-expiration-time=900000  # 15 minutes
```

The config server URI is resolved from the `CONFIG_GIT_URI` env var, falling back to `file://${user.dir}/../springboot-config` for local dev.

## Package Structure (per service)

```
com.hiimtuan.<service_name>/
├── config/         # Spring beans, gRPC interceptors
├── controller/     # REST controllers
├── dto/
│   ├── request/    # *RequestDto
│   └── response/   # *ResponseDto, ApiResponse
├── entity/         # JPA entities
├── exception/
│   ├── custom/     # RuntimeException subclasses
│   └── GlobalExceptionHandler.java
├── mapper/         # Entity ↔ DTO
├── repository/     # Spring Data repositories
└── service/        # *Service interface + *ServiceImpl
```

## gRPC Patterns

- Proto files: `src/main/proto/`
- Server: extend `*ImplBase`, annotate with `@Service`
- JWT forwarding: `GrpcJwtInterceptor` (`@Component`) sets `JWT_CTX_KEY` in gRPC context for server-side; client passes metadata header `authorization: Bearer <token>`
- Channel config: `spring.grpc.client.channels.<name>.address=host:port`
