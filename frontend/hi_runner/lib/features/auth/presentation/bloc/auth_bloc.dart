import 'package:bloc/bloc.dart';
import 'package:equatable/equatable.dart';
import 'package:hi_runner/features/auth/domain/entities/user_entity.dart';
import 'package:hi_runner/features/auth/domain/usecases/get_profile_usecase.dart';
import 'package:hi_runner/features/auth/domain/usecases/login_usecase.dart';
import 'package:hi_runner/features/auth/domain/usecases/logout_usecase.dart';
import 'package:hi_runner/features/auth/domain/usecases/register_usecase.dart';
import 'package:hi_runner/shared/services/storage_service.dart';

part 'auth_event.dart';
part 'auth_state.dart';

class AuthBloc extends Bloc<AuthEvent, AuthState> {
  final LoginUsecase _login;
  final RegisterUsecase _register;
  final LogoutUsecase _logout;
  final GetProfileUsecase _getProfile;
  final StorageService _storage;

  AuthBloc({
    required LoginUsecase login,
    required RegisterUsecase register,
    required LogoutUsecase logout,
    required GetProfileUsecase getProfile,
    required StorageService storage,
  })  : _login = login,
        _register = register,
        _logout = logout,
        _getProfile = getProfile,
        _storage = storage,
        super(const AuthInitial()) {
    on<AuthCheckRequested>(_onCheckRequested);
    on<AuthLoginRequested>(_onLoginRequested);
    on<AuthRegisterRequested>(_onRegisterRequested);
    on<AuthLogoutRequested>(_onLogoutRequested);
  }

  Future<void> _onCheckRequested(
    AuthCheckRequested event,
    Emitter<AuthState> emit,
  ) async {
    final token = await _storage.getToken();
    if (token == null) {
      emit(const AuthUnauthenticated());
      return;
    }
    emit(const AuthLoading());
    final result = await _getProfile();
    result.fold(
      (_) => emit(const AuthUnauthenticated()),
      (user) => emit(AuthAuthenticated(user)),
    );
  }

  Future<void> _onLoginRequested(
    AuthLoginRequested event,
    Emitter<AuthState> emit,
  ) async {
    emit(const AuthLoading());
    final result = await _login(event.email, event.password);
    result.fold(
      (failure) => emit(AuthError(failure.message)),
      (user) => emit(AuthAuthenticated(user)),
    );
  }

  Future<void> _onRegisterRequested(
    AuthRegisterRequested event,
    Emitter<AuthState> emit,
  ) async {
    emit(const AuthLoading());
    final result =
        await _register(event.fullName, event.email, event.password);
    result.fold(
      (failure) => emit(AuthError(failure.message)),
      (user) => emit(AuthAuthenticated(user)),
    );
  }

  Future<void> _onLogoutRequested(
    AuthLogoutRequested event,
    Emitter<AuthState> emit,
  ) async {
    await _logout();
    emit(const AuthUnauthenticated());
  }
}
