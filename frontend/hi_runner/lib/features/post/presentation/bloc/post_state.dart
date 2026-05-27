part of 'post_bloc.dart';

sealed class PostState extends Equatable {
  const PostState();
  @override
  List<Object?> get props => [];
}

final class PostInitial extends PostState {
  const PostInitial();
}

final class PostLoading extends PostState {
  const PostLoading();
}

final class PostLoaded extends PostState {
  final List<PostEntity> posts;
  const PostLoaded(this.posts);
  @override
  List<Object?> get props => [posts];
}

final class PostOperationSuccess extends PostState {
  final String message;
  final List<PostEntity> posts;
  const PostOperationSuccess(this.message, this.posts);
  @override
  List<Object?> get props => [message, posts];
}

final class PostError extends PostState {
  final String message;
  const PostError(this.message);
  @override
  List<Object?> get props => [message];
}
