class PostEntity {
  final int id;
  final String title;
  final String content;
  final int authorId;
  final String? authorName;
  final DateTime createdAt;
  final DateTime updatedAt;

  const PostEntity({
    required this.id,
    required this.title,
    required this.content,
    required this.authorId,
    this.authorName,
    required this.createdAt,
    required this.updatedAt,
  });
}
