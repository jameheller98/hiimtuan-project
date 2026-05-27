abstract final class ApiConstants {
  static const String baseUrl = String.fromEnvironment('API_BASE_URL', defaultValue: 'http://localhost:8765');
  static const String authPrefix = '/api/v1/auth';
  static const String userPrefix = '/api/v1/user';
  static const String postPrefix = '/api/v1/post';

  static const Duration connectTimeout = Duration(seconds: 15);
  static const Duration receiveTimeout = Duration(seconds: 15);
}
