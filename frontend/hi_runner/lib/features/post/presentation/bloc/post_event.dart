part of 'post_bloc.dart';

sealed class PostEvent extends Equatable {
  const PostEvent();
  @override
  List<Object?> get props => [];
}

final class PostLoadRequested extends PostEvent {
  final int page;
  const PostLoadRequested({this.page = 0});
  @override
  List<Object?> get props => [page];
}

final class PostMyLoadRequested extends PostEvent {
  final int page;
  const PostMyLoadRequested({this.page = 0});
  @override
  List<Object?> get props => [page];
}

final class PostCreateRequested extends PostEvent {
  final String title;
  final String content;
  const PostCreateRequested({required this.title, required this.content});
  @override
  List<Object?> get props => [title, content];
}

final class PostUpdateRequested extends PostEvent {
  final int id;
  final String title;
  final String content;
  const PostUpdateRequested({
    required this.id,
    required this.title,
    required this.content,
  });
  @override
  List<Object?> get props => [id, title, content];
}

final class PostDeleteRequested extends PostEvent {
  final int id;
  const PostDeleteRequested(this.id);
  @override
  List<Object?> get props => [id];
}
