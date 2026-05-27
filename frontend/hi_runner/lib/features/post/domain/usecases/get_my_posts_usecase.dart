import 'package:hi_runner/core/error/failures.dart';
import 'package:hi_runner/core/utils/either.dart';
import 'package:hi_runner/features/post/domain/entities/post_entity.dart';
import 'package:hi_runner/features/post/domain/repositories/post_repository.dart';

class GetMyPostsUsecase {
  final PostRepository _repository;
  const GetMyPostsUsecase(this._repository);

  Future<Either<Failure, List<PostEntity>>> call({int page = 0, int size = 10}) =>
      _repository.getMyPosts(page: page, size: size);
}
