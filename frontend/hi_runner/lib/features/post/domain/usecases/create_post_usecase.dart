import 'package:hi_runner/core/error/failures.dart';
import 'package:hi_runner/core/utils/either.dart';
import 'package:hi_runner/features/post/domain/entities/post_entity.dart';
import 'package:hi_runner/features/post/domain/repositories/post_repository.dart';

class CreatePostUsecase {
  final PostRepository _repository;
  const CreatePostUsecase(this._repository);

  Future<Either<Failure, PostEntity>> call(String title, String content) =>
      _repository.createPost(title, content);
}
