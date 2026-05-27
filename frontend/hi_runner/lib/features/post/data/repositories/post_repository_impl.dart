import 'package:dio/dio.dart';
import 'package:hi_runner/core/error/failures.dart';
import 'package:hi_runner/core/utils/either.dart';
import 'package:hi_runner/features/post/data/datasources/post_remote_datasource.dart';
import 'package:hi_runner/features/post/domain/entities/post_entity.dart';
import 'package:hi_runner/features/post/domain/repositories/post_repository.dart';

class PostRepositoryImpl implements PostRepository {
  final PostRemoteDatasource _remote;
  const PostRepositoryImpl(this._remote);

  @override
  Future<Either<Failure, List<PostEntity>>> getPosts(
      {int page = 0, int size = 10}) =>
      _execute(() => _remote.getPosts(page: page, size: size));

  @override
  Future<Either<Failure, List<PostEntity>>> getMyPosts(
      {int page = 0, int size = 10}) =>
      _execute(() => _remote.getMyPosts(page: page, size: size));

  @override
  Future<Either<Failure, PostEntity>> getPost(int id) =>
      _execute(() => _remote.getPost(id));

  @override
  Future<Either<Failure, PostEntity>> createPost(
          String title, String content) =>
      _execute(() => _remote.createPost(title, content));

  @override
  Future<Either<Failure, PostEntity>> updatePost(
          int id, String title, String content) =>
      _execute(() => _remote.updatePost(id, title, content));

  @override
  Future<Either<Failure, void>> deletePost(int id) =>
      _execute(() => _remote.deletePost(id));

  Future<Either<Failure, T>> _execute<T>(Future<T> Function() call) async {
    try {
      return Right(await call());
    } on DioException catch (e) {
      if (e.type == DioExceptionType.connectionError ||
          e.type == DioExceptionType.connectionTimeout) {
        return Left(const NetworkFailure());
      }
      final status = e.response?.statusCode;
      if (status == 401 || status == 403) {
        return Left(AuthFailure(
            e.response?.data?['message'] as String? ?? 'Unauthorized'));
      }
      if (status == 404) return Left(const NotFoundFailure());
      return Left(
          ServerFailure(e.response?.data?['message'] as String? ?? 'Server error'));
    } catch (_) {
      return Left(const UnexpectedFailure());
    }
  }
}
