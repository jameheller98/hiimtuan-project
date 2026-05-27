import 'package:get_it/get_it.dart';
import 'package:hi_runner/core/network/dio_client.dart';
import 'package:hi_runner/features/auth/data/datasources/auth_remote_datasource.dart';
import 'package:hi_runner/features/auth/data/repositories/auth_repository_impl.dart';
import 'package:hi_runner/features/auth/domain/repositories/auth_repository.dart';
import 'package:hi_runner/features/auth/domain/usecases/get_profile_usecase.dart';
import 'package:hi_runner/features/auth/domain/usecases/login_usecase.dart';
import 'package:hi_runner/features/auth/domain/usecases/logout_usecase.dart';
import 'package:hi_runner/features/auth/domain/usecases/register_usecase.dart';
import 'package:hi_runner/features/auth/presentation/bloc/auth_bloc.dart';
import 'package:hi_runner/features/post/data/datasources/post_remote_datasource.dart';
import 'package:hi_runner/features/post/data/repositories/post_repository_impl.dart';
import 'package:hi_runner/features/post/domain/repositories/post_repository.dart';
import 'package:hi_runner/features/post/domain/usecases/create_post_usecase.dart';
import 'package:hi_runner/features/post/domain/usecases/delete_post_usecase.dart';
import 'package:hi_runner/features/post/domain/usecases/get_my_posts_usecase.dart';
import 'package:hi_runner/features/post/domain/usecases/get_posts_usecase.dart';
import 'package:hi_runner/features/post/domain/usecases/update_post_usecase.dart';
import 'package:hi_runner/features/post/presentation/bloc/post_bloc.dart';
import 'package:hi_runner/shared/services/storage_service.dart';

final sl = GetIt.instance;

void configureDependencies() {
  // Services
  sl.registerLazySingleton<StorageService>(() => StorageService());
  sl.registerLazySingleton<DioClient>(() => DioClient(sl()));

  // Auth — data
  sl.registerLazySingleton<AuthRemoteDatasource>(
      () => AuthRemoteDatasourceImpl(sl<DioClient>().dio));
  sl.registerLazySingleton<AuthRepository>(
      () => AuthRepositoryImpl(sl(), sl()));

  // Auth — usecases
  sl.registerLazySingleton(() => LoginUsecase(sl()));
  sl.registerLazySingleton(() => RegisterUsecase(sl()));
  sl.registerLazySingleton(() => LogoutUsecase(sl()));
  sl.registerLazySingleton(() => GetProfileUsecase(sl()));

  // Auth — bloc (factory so each widget subtree gets a fresh instance)
  sl.registerFactory(() => AuthBloc(
        login: sl(),
        register: sl(),
        logout: sl(),
        getProfile: sl(),
        storage: sl(),
      ));

  // Post — data
  sl.registerLazySingleton<PostRemoteDatasource>(
      () => PostRemoteDatasourceImpl(sl<DioClient>().dio));
  sl.registerLazySingleton<PostRepository>(
      () => PostRepositoryImpl(sl()));

  // Post — usecases
  sl.registerLazySingleton(() => GetPostsUsecase(sl()));
  sl.registerLazySingleton(() => GetMyPostsUsecase(sl()));
  sl.registerLazySingleton(() => CreatePostUsecase(sl()));
  sl.registerLazySingleton(() => UpdatePostUsecase(sl()));
  sl.registerLazySingleton(() => DeletePostUsecase(sl()));

  // Post — bloc
  sl.registerFactory(() => PostBloc(
        getPosts: sl(),
        getMyPosts: sl(),
        createPost: sl(),
        updatePost: sl(),
        deletePost: sl(),
      ));
}
