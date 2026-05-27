import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';
import 'package:hi_runner/features/auth/presentation/bloc/auth_bloc.dart';
import 'package:hi_runner/features/post/domain/entities/post_entity.dart';
import 'package:hi_runner/features/post/presentation/bloc/post_bloc.dart';
import 'package:hi_runner/features/post/presentation/widgets/post_card.dart';
import 'package:hi_runner/shared/widgets/loading_widget.dart';

class PostListScreen extends StatefulWidget {
  const PostListScreen({super.key});

  @override
  State<PostListScreen> createState() => _PostListScreenState();
}

class _PostListScreenState extends State<PostListScreen> {
  @override
  void initState() {
    super.initState();
    context.read<PostBloc>().add(const PostLoadRequested());
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Posts'),
        actions: [
          IconButton(
            icon: const Icon(Icons.logout),
            onPressed: () =>
                context.read<AuthBloc>().add(const AuthLogoutRequested()),
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () => context.push('/posts/create'),
        child: const Icon(Icons.add),
      ),
      body: BlocConsumer<PostBloc, PostState>(
        listener: (context, state) {
          if (state is PostError) {
            ScaffoldMessenger.of(context)
              ..hideCurrentSnackBar()
              ..showSnackBar(SnackBar(content: Text(state.message)));
          }
          if (state is PostOperationSuccess) {
            ScaffoldMessenger.of(context)
              ..hideCurrentSnackBar()
              ..showSnackBar(SnackBar(content: Text(state.message)));
          }
        },
        builder: (context, state) {
          if (state is PostLoading) return const LoadingWidget();
          if (state is PostError) {
            return Center(
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Text(state.message),
                  const SizedBox(height: 12),
                  ElevatedButton(
                    onPressed: () =>
                        context.read<PostBloc>().add(const PostLoadRequested()),
                    child: const Text('Retry'),
                  ),
                ],
              ),
            );
          }
          final posts = switch (state) {
            PostLoaded(:final posts) => posts,
            PostOperationSuccess(:final posts) => posts,
            _ => <PostEntity>[],
          };
          if (posts.isEmpty) {
            return const Center(child: Text('No posts yet'));
          }
          return RefreshIndicator(
            onRefresh: () async =>
                context.read<PostBloc>().add(const PostLoadRequested()),
            child: ListView.builder(
              itemCount: posts.length,
              itemBuilder: (_, i) => PostCard(
                post: posts[i],
                onTap: () => context.push('/posts/${posts[i].id}'),
              ),
            ),
          );
        },
      ),
    );
  }
}
