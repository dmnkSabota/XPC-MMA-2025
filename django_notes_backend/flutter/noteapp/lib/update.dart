import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

import 'urls.dart';

class UpdatePage extends StatefulWidget {
  final Map<String, dynamic> note;

  const UpdatePage({super.key, required this.note});

  @override
  State<UpdatePage> createState() => _UpdatePageState();
}

class _UpdatePageState extends State<UpdatePage> {
  late TextEditingController bodyController;

  @override
  void initState() {
    super.initState();
    bodyController = TextEditingController(text: widget.note['body']);
  }

  Future<void> updateNote() async {
    final id = widget.note['id'].toString();
    final response = await http.put(
      Uri.parse(updateNoteUrl(id)),
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
      appBar: AppBar(title: const Text('Update Note')),
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
              onPressed: updateNote,
              child: const Text('Update Note'),
            ),
          ],
        ),
      ),
    );
  }
}
