import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:go_router/go_router.dart';
import 'package:hi_runner/features/post/domain/entities/post_entity.dart';
import 'package:hi_runner/features/post/presentation/bloc/post_bloc.dart';
import 'package:hi_runner/shared/widgets/app_button.dart';
import 'package:hi_runner/shared/widgets/app_text_field.dart';

class CreateEditPostScreen extends StatefulWidget {
  final PostEntity? post;
  const CreateEditPostScreen({super.key, this.post});

  @override
  State<CreateEditPostScreen> createState() => _CreateEditPostScreenState();
}

class _CreateEditPostScreenState extends State<CreateEditPostScreen> {
  final _formKey = GlobalKey<FormState>();
  late final TextEditingController _titleCtrl;
  late final TextEditingController _contentCtrl;

  bool get _isEditing => widget.post != null;

  @override
  void initState() {
    super.initState();
    _titleCtrl = TextEditingController(text: widget.post?.title);
    _contentCtrl = TextEditingController(text: widget.post?.content);
  }

  @override
  void dispose() {
    _titleCtrl.dispose();
    _contentCtrl.dispose();
    super.dispose();
  }

  void _submit() {
    if (_formKey.currentState?.validate() ?? false) {
      if (_isEditing) {
        context.read<PostBloc>().add(PostUpdateRequested(
              id: widget.post!.id,
              title: _titleCtrl.text.trim(),
              content: _contentCtrl.text.trim(),
            ));
      } else {
        context.read<PostBloc>().add(PostCreateRequested(
              title: _titleCtrl.text.trim(),
              content: _contentCtrl.text.trim(),
            ));
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(_isEditing ? 'Edit post' : 'New post')),
      body: BlocConsumer<PostBloc, PostState>(
        listener: (context, state) {
          if (state is PostOperationSuccess) context.go('/posts');
          if (state is PostError) {
            ScaffoldMessenger.of(context)
              ..hideCurrentSnackBar()
              ..showSnackBar(SnackBar(content: Text(state.message)));
          }
        },
        builder: (context, state) {
          final isLoading = state is PostLoading;
          return SingleChildScrollView(
            padding: const EdgeInsets.all(16),
            child: Form(
              key: _formKey,
              child: Column(
                children: [
                  AppTextField(
                    label: 'Title',
                    controller: _titleCtrl,
                    validator: (v) => (v?.isEmpty ?? true) ? 'Required' : null,
                  ),
                  const SizedBox(height: 16),
                  TextFormField(
                    controller: _contentCtrl,
                    maxLines: 10,
                    decoration: const InputDecoration(labelText: 'Content'),
                    validator: (v) => (v?.isEmpty ?? true) ? 'Required' : null,
                  ),
                  const SizedBox(height: 24),
                  AppButton(
                    label: _isEditing ? 'Update' : 'Create',
                    onPressed: _submit,
                    isLoading: isLoading,
                  ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }
}
