import 'package:dio/dio.dart';
import 'package:hi_runner/core/constants/api_constants.dart';
import 'package:hi_runner/features/post/data/models/post_model.dart';

abstract interface class PostRemoteDatasource {
  Future<List<PostModel>> getPosts({int page, int size});
  Future<List<PostModel>> getMyPosts({int page, int size});
  Future<PostModel> getPost(int id);
  Future<PostModel> createPost(String title, String content);
  Future<PostModel> updatePost(int id, String title, String content);
  Future<void> deletePost(int id);
}

class PostRemoteDatasourceImpl implements PostRemoteDatasource {
  final Dio _dio;
  const PostRemoteDatasourceImpl(this._dio);

  List<PostModel> _parseList(dynamic data) =>
      (data as List).map((e) => PostModel.fromJson(e as Map<String, dynamic>)).toList();

  @override
  Future<List<PostModel>> getPosts({int page = 0, int size = 10}) async {
    final response = await _dio.get(
      ApiConstants.postPrefix,
      queryParameters: {'page': page, 'size': size},
    );
    return _parseList(response.data['data']);
  }

  @override
  Future<List<PostModel>> getMyPosts({int page = 0, int size = 10}) async {
    final response = await _dio.get(
      '${ApiConstants.postPrefix}/my',
      queryParameters: {'page': page, 'size': size},
    );
    return _parseList(response.data['data']);
  }

  @override
  Future<PostModel> getPost(int id) async {
    final response = await _dio.get('${ApiConstants.postPrefix}/$id');
    return PostModel.fromJson(response.data['data'] as Map<String, dynamic>);
  }

  @override
  Future<PostModel> createPost(String title, String content) async {
    final response = await _dio.post(
      ApiConstants.postPrefix,
      data: {'title': title, 'content': content},
    );
    return PostModel.fromJson(response.data['data'] as Map<String, dynamic>);
  }

  @override
  Future<PostModel> updatePost(int id, String title, String content) async {
    final response = await _dio.put(
      '${ApiConstants.postPrefix}/$id',
      data: {'title': title, 'content': content},
    );
    return PostModel.fromJson(response.data['data'] as Map<String, dynamic>);
  }

  @override
  Future<void> deletePost(int id) =>
      _dio.delete('${ApiConstants.postPrefix}/$id');
}
