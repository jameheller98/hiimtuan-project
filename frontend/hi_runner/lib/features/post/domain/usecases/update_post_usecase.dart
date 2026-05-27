import 'package:hi_runner/core/error/failures.dart';
import 'package:hi_runner/core/utils/either.dart';
import 'package:hi_runner/features/post/domain/entities/post_entity.dart';
import 'package:hi_runner/features/post/domain/repositories/post_repository.dart';

class UpdatePostUsecase {
  final PostRepository _repository;
  const UpdatePostUsecase(this._repository);

  Future<Either<Failure, PostEntity>> call(
          int id, String title, String content) =>
      _repository.updatePost(id, title, content);
}
