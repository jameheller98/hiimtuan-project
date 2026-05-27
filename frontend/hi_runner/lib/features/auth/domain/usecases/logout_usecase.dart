import 'package:hi_runner/core/error/failures.dart';
import 'package:hi_runner/core/utils/either.dart';
import 'package:hi_runner/features/auth/domain/repositories/auth_repository.dart';

class LogoutUsecase {
  final AuthRepository _repository;
  const LogoutUsecase(this._repository);

  Future<Either<Failure, void>> call() => _repository.logout();
}
