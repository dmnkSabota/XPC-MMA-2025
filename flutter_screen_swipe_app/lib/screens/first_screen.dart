import 'package:flutter/material.dart';

class FirstScreen extends StatefulWidget {
  final Function(String) onSendText;
  final VoidCallback onNavigateToSecond;

  const FirstScreen({
    super.key,
    required this.onSendText,
    required this.onNavigateToSecond,
  });

  @override
  State<FirstScreen> createState() => _FirstScreenState();
}

class _FirstScreenState extends State<FirstScreen> {
  final TextEditingController _textController = TextEditingController();

  @override
  void dispose() {
    _textController.dispose();
    super.dispose();
  }

  void _handleSendButton() {
    String text = _textController.text;
    
    if (text.isNotEmpty) {
      widget.onSendText(text);
      _textController.clear();
      widget.onNavigateToSecond();
      
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Text sent to Screen 2!'),
          duration: Duration(seconds: 2),
          backgroundColor: Colors.green,
        ),
      );
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Please enter some text first'),
          duration: Duration(seconds: 2),
          backgroundColor: Colors.orange,
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(24.0),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          const Icon(
            Icons.edit_note,
            size: 80,
            color: Colors.deepPurple,
          ),
          const SizedBox(height: 20),
          
          const Text(
            'Screen 1',
            textAlign: TextAlign.center,
            style: TextStyle(
              fontSize: 32,
              fontWeight: FontWeight.bold,
              color: Colors.deepPurple,
            ),
          ),
          const SizedBox(height: 10),
          
          const Text(
            'Enter text and send to Screen 2',
            textAlign: TextAlign.center,
            style: TextStyle(
              fontSize: 16,
              color: Colors.grey,
            ),
          ),
          const SizedBox(height: 40),
          
          TextField(
            controller: _textController,
            decoration: InputDecoration(
              labelText: 'Enter your message',
              hintText: 'Type something...',
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(12),
              ),
              prefixIcon: const Icon(Icons.message),
              filled: true,
              fillColor: Colors.grey.shade50,
            ),
            maxLines: 3,
            textCapitalization: TextCapitalization.sentences,
          ),
          const SizedBox(height: 20),
          
          ElevatedButton.icon(
            onPressed: _handleSendButton,
            icon: const Icon(Icons.send),
            label: const Text(
              'Send to Screen 2',
              style: TextStyle(fontSize: 16),
            ),
            style: ElevatedButton.styleFrom(
              backgroundColor: Colors.deepPurple,
              foregroundColor: Colors.white,
              padding: const EdgeInsets.symmetric(vertical: 16),
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(12),
              ),
            ),
          ),
          const SizedBox(height: 30),
          
          Container(
            padding: const EdgeInsets.all(16),
            decoration: BoxDecoration(
              color: Colors.blue.shade50,
              borderRadius: BorderRadius.circular(12),
              border: Border.all(color: Colors.blue.shade200),
            ),
            child: const Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Icon(Icons.swipe, color: Colors.blue),
                SizedBox(width: 10),
                Text(
                  'Swipe left to go to Screen 2',
                  style: TextStyle(
                    color: Colors.blue,
                    fontWeight: FontWeight.w500,
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}