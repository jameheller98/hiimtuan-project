import 'package:bloc/bloc.dart';
import 'package:equatable/equatable.dart';
import 'package:hi_runner/features/post/domain/entities/post_entity.dart';
import 'package:hi_runner/features/post/domain/usecases/create_post_usecase.dart';
import 'package:hi_runner/features/post/domain/usecases/delete_post_usecase.dart';
import 'package:hi_runner/features/post/domain/usecases/get_my_posts_usecase.dart';
import 'package:hi_runner/features/post/domain/usecases/get_posts_usecase.dart';
import 'package:hi_runner/features/post/domain/usecases/update_post_usecase.dart';

part 'post_event.dart';
part 'post_state.dart';

class PostBloc extends Bloc<PostEvent, PostState> {
  final GetPostsUsecase _getPosts;
  final GetMyPostsUsecase _getMyPosts;
  final CreatePostUsecase _createPost;
  final UpdatePostUsecase _updatePost;
  final DeletePostUsecase _deletePost;

  List<PostEntity> _currentPosts = [];

  PostBloc({
    required GetPostsUsecase getPosts,
    required GetMyPostsUsecase getMyPosts,
    required CreatePostUsecase createPost,
    required UpdatePostUsecase updatePost,
    required DeletePostUsecase deletePost,
  })  : _getPosts = getPosts,
        _getMyPosts = getMyPosts,
        _createPost = createPost,
        _updatePost = updatePost,
        _deletePost = deletePost,
        super(const PostInitial()) {
    on<PostLoadRequested>(_onLoadRequested);
    on<PostMyLoadRequested>(_onMyLoadRequested);
    on<PostCreateRequested>(_onCreateRequested);
    on<PostUpdateRequested>(_onUpdateRequested);
    on<PostDeleteRequested>(_onDeleteRequested);
  }

  Future<void> _onLoadRequested(
    PostLoadRequested event,
    Emitter<PostState> emit,
  ) async {
    emit(const PostLoading());
    final result = await _getPosts(page: event.page);
    result.fold(
      (failure) => emit(PostError(failure.message)),
      (posts) {
        _currentPosts = posts;
        emit(PostLoaded(posts));
      },
    );
  }

  Future<void> _onMyLoadRequested(
    PostMyLoadRequested event,
    Emitter<PostState> emit,
  ) async {
    emit(const PostLoading());
    final result = await _getMyPosts(page: event.page);
    result.fold(
      (failure) => emit(PostError(failure.message)),
      (posts) {
        _currentPosts = posts;
        emit(PostLoaded(posts));
      },
    );
  }

  Future<void> _onCreateRequested(
    PostCreateRequested event,
    Emitter<PostState> emit,
  ) async {
    emit(const PostLoading());
    final result = await _createPost(event.title, event.content);
    result.fold(
      (failure) => emit(PostError(failure.message)),
      (post) {
        _currentPosts = [post, ..._currentPosts];
        emit(PostOperationSuccess('Post created', _currentPosts));
      },
    );
  }

  Future<void> _onUpdateRequested(
    PostUpdateRequested event,
    Emitter<PostState> emit,
  ) async {
    emit(const PostLoading());
    final result = await _updatePost(event.id, event.title, event.content);
    result.fold(
      (failure) => emit(PostError(failure.message)),
      (updated) {
        _currentPosts = _currentPosts
            .map((p) => p.id == updated.id ? updated : p)
            .toList();
        emit(PostOperationSuccess('Post updated', _currentPosts));
      },
    );
  }

  Future<void> _onDeleteRequested(
    PostDeleteRequested event,
    Emitter<PostState> emit,
  ) async {
    emit(const PostLoading());
    final result = await _deletePost(event.id);
    result.fold(
      (failure) => emit(PostError(failure.message)),
      (_) {
        _currentPosts =
            _currentPosts.where((p) => p.id != event.id).toList();
        emit(PostOperationSuccess('Post deleted', _currentPosts));
      },
    );
  }
}
