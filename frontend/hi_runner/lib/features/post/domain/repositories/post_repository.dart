import 'package:hi_runner/core/error/failures.dart';
import 'package:hi_runner/core/utils/either.dart';
import 'package:hi_runner/features/post/domain/entities/post_entity.dart';

abstract interface class PostRepository {
  Future<Either<Failure, List<PostEntity>>> getPosts({int page = 0, int size = 10});
  Future<Either<Failure, List<PostEntity>>> getMyPosts({int page = 0, int size = 10});
  Future<Either<Failure, PostEntity>> getPost(int id);
  Future<Either<Failure, PostEntity>> createPost(String title, String content);
  Future<Either<Failure, PostEntity>> updatePost(int id, String title, String content);
  Future<Either<Failure, void>> deletePost(int id);
}
