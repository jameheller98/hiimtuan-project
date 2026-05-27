import 'package:dio/dio.dart';
import 'package:hi_runner/core/constants/api_constants.dart';
import 'package:hi_runner/shared/services/storage_service.dart';

/// Handles Authorization header injection and transparent token refresh.
/// Uses QueuedInterceptorsWrapper so concurrent 401s only trigger one refresh.
class AuthInterceptor extends QueuedInterceptorsWrapper {
  final StorageService _storageService;

  // Separate Dio instance used only for refresh calls — avoids infinite loops.
  final Dio _refreshDio = Dio(
    BaseOptions(
      baseUrl: ApiConstants.baseUrl,
      connectTimeout: ApiConstants.connectTimeout,
      receiveTimeout: ApiConstants.receiveTimeout,
    ),
  );

  AuthInterceptor(this._storageService);

  @override
  void onRequest(
    RequestOptions options,
    RequestInterceptorHandler handler,
  ) async {
    final token = await _storageService.getToken();
    if (token != null) {
      options.headers['Authorization'] = 'Bearer $token';
    }
    handler.next(options);
  }

  @override
  void onError(DioException err, ErrorInterceptorHandler handler) async {
    if (err.response?.statusCode != 401) {
      return handler.next(err);
    }

    try {
      final refreshToken = await _storageService.getRefreshToken();
      if (refreshToken == null) {
        await _storageService.clearTokens();
        return handler.next(err);
      }

      final refreshResponse = await _refreshDio.post(
        '${ApiConstants.authPrefix}/refresh',
        data: {'refreshToken': refreshToken},
      );

      final data = refreshResponse.data['data'] as Map<String, dynamic>;
      final newToken = data['token'] as String;
      final newRefresh = data['refreshToken'] as String;
      await _storageService.saveTokens(newToken, newRefresh);

      // Retry the original request with the new token.
      final retryOpts = err.requestOptions
        ..headers['Authorization'] = 'Bearer $newToken';
      final retryResponse = await _refreshDio.fetch(retryOpts);
      return handler.resolve(retryResponse);
    } catch (_) {
      await _storageService.clearTokens();
      handler.next(err);
    }
  }
}
