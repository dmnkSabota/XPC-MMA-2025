import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

import 'urls.dart';

class CreatePage extends StatefulWidget {
  const CreatePage({super.key});

  @override
  State<CreatePage> createState() => _CreatePageState();
}

class _CreatePageState extends State<CreatePage> {
  final bodyController = TextEditingController();

  Future<void> createNote() async {
    final response = await http.post(
      Uri.parse(createNoteUrl),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({'body': bodyController.text}),
    );
    if (response.statusCode == 200 && mounted) {
      Navigator.pop(context, true);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Create Note')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            TextField(
              controller: bodyController,
              decoration: const InputDecoration(labelText: 'Note'),
              maxLines: 8,
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: createNote,
              child: const Text('Save Note'),
            ),
          ],
        ),
      ),
    );
  }
}
