const String baseUrl = 'http://localhost:8000';

const String notesUrl = '$baseUrl/api/notes/';
const String createNoteUrl = '$baseUrl/api/notes/create/';

String getNoteUrl(String pk) => '$baseUrl/api/notes/$pk/';
String updateNoteUrl(String pk) => '$baseUrl/api/notes/$pk/update/';
String deleteNoteUrl(String pk) => '$baseUrl/api/notes/$pk/delete/';