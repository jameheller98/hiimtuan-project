import 'package:dio/dio.dart';
import 'package:hi_runner/core/constants/api_constants.dart';
import 'package:hi_runner/core/network/api_interceptor.dart';
import 'package:hi_runner/shared/services/storage_service.dart';

class DioClient {
  final Dio dio;

  DioClient(StorageService storageService)
      : dio = Dio(
          BaseOptions(
            baseUrl: ApiConstants.baseUrl,
            connectTimeout: ApiConstants.connectTimeout,
            receiveTimeout: ApiConstants.receiveTimeout,
            headers: {'Content-Type': 'application/json'},
          ),
        ) {
    dio.interceptors.addAll([
      AuthInterceptor(storageService),
      LogInterceptor(requestBody: true, responseBody: true),
    ]);
  }
}
