import 'package:hi_runner/core/error/failures.dart';
import 'package:hi_runner/core/utils/either.dart';
import 'package:hi_runner/features/auth/domain/entities/user_entity.dart';
import 'package:hi_runner/features/auth/domain/repositories/auth_repository.dart';

class RegisterUsecase {
  final AuthRepository _repository;
  const RegisterUsecase(this._repository);

  Future<Either<Failure, UserEntity>> call(
          String fullName, String email, String password) =>
      _repository.register(fullName, email, password);
}
