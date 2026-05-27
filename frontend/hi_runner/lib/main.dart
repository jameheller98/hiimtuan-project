import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:hi_runner/core/di/injection.dart';
import 'package:hi_runner/core/router/app_router.dart';
import 'package:hi_runner/core/theme/app_theme.dart';
import 'package:hi_runner/features/auth/presentation/bloc/auth_bloc.dart';

void main() {
  configureDependencies();
  runApp(const AppRoot());
}

class AppRoot extends StatefulWidget {
  const AppRoot({super.key});

  @override
  State<AppRoot> createState() => _AppRootState();
}

class _AppRootState extends State<AppRoot> {
  late final AuthBloc _authBloc;

  @override
  void initState() {
    super.initState();
    _authBloc = sl<AuthBloc>()..add(const AuthCheckRequested());
  }

  @override
  void dispose() {
    _authBloc.close();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return BlocProvider.value(
      value: _authBloc,
      child: Builder(
        builder: (context) {
          final router = buildRouter(_authBloc);
          return MaterialApp.router(
            title: 'HiRunner',
            theme: AppTheme.light,
            routerConfig: router,
          );
        },
      ),
    );
  }
}
