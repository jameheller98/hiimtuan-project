import 'package:dio/dio.dart';
import 'package:hi_runner/core/constants/api_constants.dart';
import 'package:hi_runner/features/auth/data/models/login_response_model.dart';
import 'package:hi_runner/features/auth/data/models/user_model.dart';

abstract interface class AuthRemoteDatasource {
  Future<LoginResponseModel> login(String email, String password);
  Future<LoginResponseModel> register(String fullName, String email, String password);
  Future<void> logout();
  Future<UserModel> getProfile();
}

class AuthRemoteDatasourceImpl implements AuthRemoteDatasource {
  final Dio _dio;
  const AuthRemoteDatasourceImpl(this._dio);

  @override
  Future<LoginResponseModel> login(String email, String password) async {
    final response = await _dio.post(
      '${ApiConstants.authPrefix}/login',
      data: {'email': email, 'password': password},
    );
    return LoginResponseModel.fromJson(
        response.data['data'] as Map<String, dynamic>);
  }

  @override
  Future<LoginResponseModel> register(
      String fullName, String email, String password) async {
    final response = await _dio.post(
      '${ApiConstants.authPrefix}/register',
      data: {'fullName': fullName, 'email': email, 'password': password},
    );
    return LoginResponseModel.fromJson(
        response.data['data'] as Map<String, dynamic>);
  }

  @override
  Future<void> logout() =>
      _dio.post('${ApiConstants.authPrefix}/logout');

  @override
  Future<UserModel> getProfile() async {
    final response = await _dio.get('${ApiConstants.userPrefix}/me');
    return UserModel.fromJson(response.data['data'] as Map<String, dynamic>);
  }
}
