class UserEntity {
  final int id;
  final String fullName;
  final String email;
  final List<String> roles;

  const UserEntity({
    required this.id,
    required this.fullName,
    required this.email,
    required this.roles,
  });
}
