import 'package:hi_runner/features/auth/data/models/user_model.dart';

class LoginResponseModel {
  final String token;
  final String refreshToken;
  final UserModel user;

  const LoginResponseModel({
    required this.token,
    required this.refreshToken,
    required this.user,
  });

  factory LoginResponseModel.fromJson(Map<String, dynamic> json) =>
      LoginResponseModel(
        token: json['token'] as String,
        refreshToken: json['refreshToken'] as String,
        user: UserModel.fromJson(json['user'] as Map<String, dynamic>),
      );
}
