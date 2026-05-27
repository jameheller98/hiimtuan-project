import 'package:dio/dio.dart';
import 'package:hi_runner/core/error/failures.dart';
import 'package:hi_runner/core/utils/either.dart';
import 'package:hi_runner/features/auth/data/datasources/auth_remote_datasource.dart';
import 'package:hi_runner/features/auth/domain/entities/user_entity.dart';
import 'package:hi_runner/features/auth/domain/repositories/auth_repository.dart';
import 'package:hi_runner/shared/services/storage_service.dart';

class AuthRepositoryImpl implements AuthRepository {
  final AuthRemoteDatasource _remote;
  final StorageService _storage;

  const AuthRepositoryImpl(this._remote, this._storage);

  @override
  Future<Either<Failure, UserEntity>> login(
      String email, String password) async {
    try {
      final result = await _remote.login(email, password);
      await _storage.saveTokens(result.token, result.refreshToken);
      return Right(result.user);
    } on DioException catch (e) {
      return Left(_mapDioError(e));
    } catch (_) {
      return Left(const UnexpectedFailure());
    }
  }

  @override
  Future<Either<Failure, UserEntity>> register(
      String fullName, String email, String password) async {
    try {
      final result = await _remote.register(fullName, email, password);
      await _storage.saveTokens(result.token, result.refreshToken);
      return Right(result.user);
    } on DioException catch (e) {
      return Left(_mapDioError(e));
    } catch (_) {
      return Left(const UnexpectedFailure());
    }
  }

  @override
  Future<Either<Failure, void>> logout() async {
    try {
      await _remote.logout();
      await _storage.clearTokens();
      return const Right(null);
    } on DioException catch (e) {
      await _storage.clearTokens();
      return Left(_mapDioError(e));
    } catch (_) {
      await _storage.clearTokens();
      return Left(const UnexpectedFailure());
    }
  }

  @override
  Future<Either<Failure, UserEntity>> getProfile() async {
    try {
      final user = await _remote.getProfile();
      return Right(user);
    } on DioException catch (e) {
      return Left(_mapDioError(e));
    } catch (_) {
      return Left(const UnexpectedFailure());
    }
  }

  Failure _mapDioError(DioException e) {
    if (e.type == DioExceptionType.connectionError ||
        e.type == DioExceptionType.connectionTimeout) {
      return const NetworkFailure();
    }
    final status = e.response?.statusCode;
    if (status == 401 || status == 403) {
      return AuthFailure(
          e.response?.data?['message'] as String? ?? 'Unauthorized');
    }
    if (status == 404) return const NotFoundFailure();
    return ServerFailure(
        e.response?.data?['message'] as String? ?? 'Server error');
  }
}
