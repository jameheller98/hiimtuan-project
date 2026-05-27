# Architecture Overview

## System Diagram

```
                        ┌─────────────┐
                        │   Flutter   │  Mobile (Android/iOS)
                        │  (hi_runner)│
                        └──────┬──────┘
                               │ HTTP
                        ┌──────▼──────┐
                        │   Next.js   │  Web (localhost:3000)
                        │  (portfolio)│
                        └──────┬──────┘
                               │ HTTP
                   ┌───────────▼───────────┐
                   │      API Gateway       │  :8765
                   │  - JWT validation      │
                   │  - Routing             │
                   │  - Auth endpoints      │
                   └──┬──────────┬──────────┘
                      │          │ gRPC
              HTTP    │          ├──── mail-service :9090
         ┌────────────┤          └──── user-service :9091
         │            │
 ┌───────▼──┐   ┌─────▼──────┐   ┌────────────┐
 │   User   │   │    Post    │   │    Mail    │
 │ Service  │   │  Service   │   │  Service   │
 │  :8000   │   │   :8200    │   │  gRPC :9090│
 │  gRPC    │   │            │   │            │
 │  :9091   │   │            │   │            │
 └────┬─────┘   └─────┬──────┘   └────────────┘
      │               │ gRPC
      │               └──── user-service :9091
 ┌────▼─────────────────────────────────────────┐
 │                MySQL Database                 │
 │  users, roles, posts,                        │
 │  refresh_tokens, password_reset_token        │
 └──────────────────────────────────────────────┘

┌──────────────────────────────────────────────┐
│          Spring Cloud Infrastructure          │
│  Naming Server (Eureka)  :8761               │
│  Config Server            :8888               │
└──────────────────────────────────────────────┘
```

## Communication Patterns

| From | To | Protocol | Purpose |
|---|---|---|---|
| Frontend | API Gateway | HTTP/REST | All client requests |
| API Gateway | User Service | gRPC | Credential validation, user lookup, password update |
| API Gateway | Mail Service | gRPC | Send password reset email |
| Post Service | User Service | gRPC | Get current user from JWT |

## Security

- **JWT**: HS256, signed with `security.jwt.secret-key`, expires per `security.jwt.expiration-time`
- **JWT subject**: user ID (not email) — stateless, no DB lookup needed in gateway
- **Refresh token**: UUID stored in `refresh_tokens` table (api-gateway)
- **Password reset token**: UUID stored in `password_reset_token`, expires in 15 min
- **CORS**: api-gateway allows `http://localhost:3000`

## Port Reference

| Service | HTTP | gRPC |
|---|---|---|
| Naming Server (Eureka) | 8761 | — |
| Config Server | 8888 | — |
| API Gateway | 8765 | — |
| User Service | 8000 | 9091 |
| Post Service | 8200 | — |
| Mail Service | 8100 | 9090 |
| Next.js Web | 3000 | — |

## Detailed Docs

- [Backend — microservices, gRPC, DB](./backend/ARCHITECTURE.md)
- [Flutter — Clean Architecture, BLoC, DI](./frontend/hi_runner/ARCHITECTURE.md)
- [Next.js — App Router, Zustand, TanStack Query](./frontend/hiimtuan-portfolio-web/ARCHITECTURE.md)
