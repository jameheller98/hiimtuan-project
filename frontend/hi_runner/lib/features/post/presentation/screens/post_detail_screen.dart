import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';
import 'package:hi_runner/features/post/domain/entities/post_entity.dart';
import 'package:hi_runner/features/post/presentation/bloc/post_bloc.dart';
import 'package:hi_runner/shared/widgets/loading_widget.dart';

class PostDetailScreen extends StatelessWidget {
  final int postId;
  const PostDetailScreen({super.key, required this.postId});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Post'),
        actions: [
          BlocBuilder<PostBloc, PostState>(
            builder: (context, state) {
              final post = _findPost(state, postId);
              if (post == null) return const SizedBox.shrink();
              return Row(
                children: [
                  IconButton(
                    icon: const Icon(Icons.edit),
                    onPressed: () =>
                        context.push('/posts/$postId/edit', extra: post),
                  ),
                  IconButton(
                    icon: const Icon(Icons.delete),
                    onPressed: () => _confirmDelete(context),
                  ),
                ],
              );
            },
          ),
        ],
      ),
      body: BlocConsumer<PostBloc, PostState>(
        listener: (context, state) {
          if (state is PostOperationSuccess &&
              state.message == 'Post deleted') {
            context.go('/posts');
          }
          if (state is PostError) {
            ScaffoldMessenger.of(context)
              ..hideCurrentSnackBar()
              ..showSnackBar(SnackBar(content: Text(state.message)));
          }
        },
        builder: (context, state) {
          if (state is PostLoading) return const LoadingWidget();
          final post = _findPost(state, postId);
          if (post == null) {
            return const Center(child: Text('Post not found'));
          }
          return SingleChildScrollView(
            padding: const EdgeInsets.all(16),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(post.title,
                    style: Theme.of(context).textTheme.headlineSmall),
                const SizedBox(height: 8),
                Text('By ${post.authorName ?? 'Unknown'}',
                    style: Theme.of(context).textTheme.labelMedium),
                const Divider(height: 24),
                Text(post.content,
                    style: Theme.of(context).textTheme.bodyLarge),
              ],
            ),
          );
        },
      ),
    );
  }

  PostEntity? _findPost(PostState state, int id) {
    final posts = switch (state) {
      PostLoaded(:final posts) => posts,
      PostOperationSuccess(:final posts) => posts,
      _ => <PostEntity>[],
    };
    try {
      return posts.firstWhere((p) => p.id == id);
    } catch (_) {
      return null;
    }
  }

  void _confirmDelete(BuildContext context) {
    showDialog<void>(
      context: context,
      builder: (_) => AlertDialog(
        title: const Text('Delete post'),
        content: const Text('Are you sure you want to delete this post?'),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('Cancel'),
          ),
          TextButton(
            onPressed: () {
              Navigator.pop(context);
              context
                  .read<PostBloc>()
                  .add(PostDeleteRequested(postId));
            },
            child:
                Text('Delete', style: TextStyle(color: Colors.red.shade700)),
          ),
        ],
      ),
    );
  }
}
