import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

import 'update.dart';
import 'urls.dart';

class NotePage extends StatefulWidget {
  final String noteId;

  const NotePage({super.key, required this.noteId});

  @override
  State<NotePage> createState() => _NotePageState();
}

class _NotePageState extends State<NotePage> {
  Map<String, dynamic>? note;
  bool isLoading = true;

  @override
  void initState() {
    super.initState();
    fetchNote();
  }

  Future<void> fetchNote() async {
    final response = await http.get(Uri.parse(getNoteUrl(widget.noteId)));
    if (response.statusCode == 200) {
      setState(() {
        note = jsonDecode(response.body);
        isLoading = false;
      });
    }
  }

  Future<void> deleteNote() async {
    await http.delete(Uri.parse(deleteNoteUrl(widget.noteId)));
    if (mounted) {
      Navigator.pop(context, true);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Note Detail'),
        actions: [
          IconButton(
            icon: const Icon(Icons.edit),
            onPressed: note == null
                ? null
                : () async {
                    final updated = await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => UpdatePage(note: note!),
                      ),
                    );
                    if (updated == true) {
                      fetchNote();
                    }
                  },
          ),
          IconButton(icon: const Icon(Icons.delete), onPressed: deleteNote),
        ],
      ),
      body: isLoading
          ? const Center(child: CircularProgressIndicator())
          : Padding(
              padding: const EdgeInsets.all(16),
              child: Text(
                note!['body'] ?? '',
                style: const TextStyle(fontSize: 18),
              ),
            ),
    );
  }
}
