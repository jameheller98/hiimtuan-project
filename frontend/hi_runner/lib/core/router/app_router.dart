import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';
import 'package:hi_runner/features/auth/presentation/bloc/auth_bloc.dart';
import 'package:hi_runner/features/auth/presentation/screens/login_screen.dart';
import 'package:hi_runner/features/auth/presentation/screens/register_screen.dart';
import 'package:hi_runner/features/post/domain/entities/post_entity.dart';
import 'package:hi_runner/features/post/presentation/bloc/post_bloc.dart';
import 'package:hi_runner/features/post/presentation/screens/create_edit_post_screen.dart';
import 'package:hi_runner/features/post/presentation/screens/post_detail_screen.dart';
import 'package:hi_runner/features/post/presentation/screens/post_list_screen.dart';
import 'package:hi_runner/core/di/injection.dart';
import 'package:mapbox_maps_flutter/mapbox_maps_flutter.dart';

GoRouter buildRouter(AuthBloc authBloc) => GoRouter(
  initialLocation: '/posts',
  redirect: (context, state) {
    final authState = authBloc.state;
    final isAuth = authState is AuthAuthenticated;
    final isInitial = authState is AuthInitial || authState is AuthLoading;
    final onAuthPage =
        state.matchedLocation == '/login' ||
        state.matchedLocation == '/register';

    if (isInitial) return null;
    if (!isAuth && !onAuthPage) return '/login';
    if (isAuth && onAuthPage) return '/posts';
    return null;
  },
  refreshListenable: _BlocListenable(authBloc),
  routes: [
    GoRoute(path: '/login', builder: (_, __) => const MapWidget()),
    GoRoute(path: '/register', builder: (_, __) => const RegisterScreen()),
    GoRoute(
      path: '/posts',
      builder: (_, __) => BlocProvider(
        create: (_) => sl<PostBloc>(),
        child: const PostListScreen(),
      ),
      routes: [
        GoRoute(
          path: 'create',
          builder: (context, __) => BlocProvider.value(
            value: context.read<PostBloc>(),
            child: const CreateEditPostScreen(),
          ),
        ),
        GoRoute(
          path: ':id',
          builder: (context, state) => BlocProvider.value(
            value: context.read<PostBloc>(),
            child: PostDetailScreen(
              postId: int.parse(state.pathParameters['id']!),
            ),
          ),
          routes: [
            GoRoute(
              path: 'edit',
              builder: (context, state) => BlocProvider.value(
                value: context.read<PostBloc>(),
                child: CreateEditPostScreen(post: state.extra as PostEntity?),
              ),
            ),
          ],
        ),
      ],
    ),
  ],
);

/// Makes GoRouter react to BLoC state changes (for redirect to fire).
class _BlocListenable extends ChangeNotifier {
  _BlocListenable(AuthBloc bloc) {
    bloc.stream.listen((_) => notifyListeners());
  }
}
