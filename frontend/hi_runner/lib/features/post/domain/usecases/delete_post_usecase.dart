import 'package:hi_runner/core/error/failures.dart';
import 'package:hi_runner/core/utils/either.dart';
import 'package:hi_runner/features/post/domain/repositories/post_repository.dart';

class DeletePostUsecase {
  final PostRepository _repository;
  const DeletePostUsecase(this._repository);

  Future<Either<Failure, void>> call(int id) => _repository.deletePost(id);
}
