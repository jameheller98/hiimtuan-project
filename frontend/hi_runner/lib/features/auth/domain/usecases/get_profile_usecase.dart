import 'package:hi_runner/core/error/failures.dart';
import 'package:hi_runner/core/utils/either.dart';
import 'package:hi_runner/features/auth/domain/entities/user_entity.dart';
import 'package:hi_runner/features/auth/domain/repositories/auth_repository.dart';

class GetProfileUsecase {
  final AuthRepository _repository;
  const GetProfileUsecase(this._repository);

  Future<Either<Failure, UserEntity>> call() => _repository.getProfile();
}
