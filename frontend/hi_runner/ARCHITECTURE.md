# hi_runner — Flutter Architecture

Clean Architecture with BLoC state management and get_it dependency injection.

## Layer Overview

```
lib/
├── main.dart                        # DI setup (configureDependencies) + root BlocProvider
│
├── core/                            # Framework-level, feature-agnostic
│   ├── constants/api_constants.dart # Base URL, route prefixes, timeouts
│   ├── di/injection.dart            # get_it service locator — all wiring here
│   ├── error/failures.dart          # Sealed failure types (ServerFailure, AuthFailure, …)
│   ├── network/
│   │   ├── dio_client.dart          # Dio instance factory (takes StorageService)
│   │   └── api_interceptor.dart     # JWT inject on request + transparent token refresh on 401
│   ├── router/app_router.dart       # GoRouter + auth-based redirect
│   ├── theme/app_theme.dart         # Material 3 theme
│   └── utils/either.dart            # Custom Either<L, R> sealed class
│
├── shared/                          # Reusable across features
│   ├── services/storage_service.dart  # Secure token persistence (flutter_secure_storage)
│   └── widgets/                       # AppButton, AppTextField, LoadingWidget
│
└── features/
    ├── auth/
    │   ├── domain/                  # Pure Dart — zero Flutter imports
    │   │   ├── entities/user_entity.dart
    │   │   ├── repositories/auth_repository.dart   # abstract interface
    │   │   └── usecases/            # LoginUsecase, RegisterUsecase, LogoutUsecase, GetProfileUsecase
    │   ├── data/
    │   │   ├── models/              # UserModel, LoginResponseModel (fromJson/toJson)
    │   │   ├── datasources/         # AuthRemoteDatasourceImpl (Dio calls)
    │   │   └── repositories/        # AuthRepositoryImpl — maps DioException → Failure
    │   └── presentation/
    │       ├── bloc/                # AuthBloc + auth_event.dart + auth_state.dart (part files)
    │       └── screens/             # LoginScreen, RegisterScreen
    │
    └── post/                        # Mirrors auth — same 3-layer pattern
        ├── domain/
        ├── data/
        └── presentation/
            ├── bloc/                # PostBloc + events + states
            ├── screens/             # PostListScreen, PostDetailScreen, CreateEditPostScreen
            └── widgets/post_card.dart
```

## Dependency Rule

`domain` → imports nothing outside itself

`data` → imports `domain` only

`presentation` → imports `domain` only (never imports `data` directly)

`core/di/injection.dart` is the only place that imports all layers together.

## State Management — BLoC

Each feature has one BLoC. Events and states are `part` files of the BLoC.

**AuthBloc states**: `AuthInitial` → `AuthLoading` → `AuthAuthenticated(user)` | `AuthUnauthenticated` | `AuthError(message)`

**PostBloc states**: `PostInitial` → `PostLoading` → `PostLoaded(posts)` | `PostOperationSuccess(message, posts)` | `PostError(message)`

BLoCs are registered as `registerFactory` in get_it so each widget subtree gets a fresh instance.

## Dependency Injection — get_it

All registrations live in `core/di/injection.dart`. Call `configureDependencies()` once in `main()`.

```
StorageService (singleton)
  └── DioClient (singleton)
        ├── AuthRemoteDatasource (singleton)
        │     └── AuthRepositoryImpl (singleton)
        │           └── usecases (singletons)
        │                 └── AuthBloc (factory)
        └── PostRemoteDatasource (singleton)
              └── PostRepositoryImpl (singleton)
                    └── usecases (singletons)
                          └── PostBloc (factory)
```

Access via `sl<T>()` anywhere; BLoCs are provided to the widget tree via `BlocProvider`.

## Navigation — GoRouter

Routes are defined in `core/router/app_router.dart`. Auth redirect fires on every `AuthBloc` state change via a `ChangeNotifier` adapter (`_BlocListenable`).

| Route | Screen | Auth required |
|---|---|---|
| `/login` | LoginScreen | no |
| `/register` | RegisterScreen | no |
| `/posts` | PostListScreen | yes |
| `/posts/create` | CreateEditPostScreen | yes |
| `/posts/:id` | PostDetailScreen | yes |
| `/posts/:id/edit` | CreateEditPostScreen | yes |

Unauthenticated users are redirected to `/login`. Authenticated users visiting `/login` or `/register` are redirected to `/posts`.

## Token Refresh

`AuthInterceptor` (extends `QueuedInterceptorsWrapper`) handles silent token refresh:
1. Injects `Authorization: Bearer <token>` on every request
2. On 401: calls `/api/v1/auth/refresh` via a separate `_refreshDio` instance (avoids interceptor loop)
3. Saves new tokens, retries the original request
4. Concurrent 401s are queued — only one refresh call is made

## Key Dependencies

| Package | Version | Purpose |
|---|---|---|
| `flutter_bloc` | ^8.1.6 | BLoC state management |
| `get_it` | ^8.0.3 | Service locator / DI |
| `dio` | ^5.7.0 | HTTP client |
| `go_router` | ^14.6.1 | Declarative navigation |
| `flutter_secure_storage` | ^9.2.2 | Encrypted token storage |
| `equatable` | ^2.0.5 | Value equality for BLoC states/events |
