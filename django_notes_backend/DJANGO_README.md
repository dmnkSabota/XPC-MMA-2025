# Django Notes Backend

**Course:** XPC-MMA 2025/2026 — Cviko 6 & 7  
**Author:** Dominik Sabota  
**GitHub:** https://github.com/dmnkSabota/XPC-MMA-2025

---

## Stack

| Layer | Technology |
|---|---|
| Backend | Django 6.0.3 + Django REST Framework |
| Database | SQLite3 |
| Frontend | Flutter (Dart) |
| HTTP | http ^1.3.0 |
| CORS | django-cors-headers 4.9.0 |

---

## Project Structure

```
django_notes_backend/
├── env/                    # Virtual environment (not tracked)
├── flutter/
│   └── noteapp/
│       └── lib/
│           ├── main.dart   # Notes list
│           ├── note.dart   # Note detail
│           ├── create.dart # Create note
│           ├── update.dart # Update note
│           └── urls.dart   # API endpoints
└── notes/
    ├── api/
    │   ├── models.py
    │   ├── serializers.py
    │   ├── views.py
    │   └── urls.py
    ├── notes/
    │   ├── settings.py
    │   └── urls.py
    └── manage.py
```

---

## Running the Project

### Backend

```powershell
cd django_notes_backend
env\Scripts\activate
cd notes
python manage.py runserver
```

### Frontend (Chrome)

```powershell
cd django_notes_backend\flutter\noteapp
flutter run -d chrome
```

### Frontend (Android Emulator)

Change `urls.dart`:
```dart
const String baseUrl = 'http://10.0.2.2:8000';
```
Then run `flutter run`.

---

## API Endpoints

Base URL: `http://127.0.0.1:8000/api/`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/notes/` | All notes |
| GET | `/notes/<id>/` | Single note |
| POST | `/notes/create/` | Create note |
| PUT | `/notes/<id>/update/` | Update note |
| DELETE | `/notes/<id>/delete/` | Delete note |

Request/response body:
```json
{ "body": "Note text here" }
```

---

## Cviko 6 — Django REST API

### Model

```python
class Note(models.Model):
    body = models.TextField()
    updated = models.DateTimeField(auto_now=True)
    created = models.DateTimeField(auto_now_add=True)

    class Meta:
        ordering = ['-updated']
```

### Serializer

```python
class NoteSerializer(ModelSerializer):
    class Meta:
        model = Note
        fields = '__all__'
```

### Views

Each view is a simple function decorated with `@api_view`. The decorator restricts which HTTP methods are accepted. `NoteSerializer` handles converting the model instance to JSON and back.

```python
@api_view(['GET'])
def getNotes(request):
    notes = Note.objects.all()
    serializer = NoteSerializer(notes, many=True)
    return Response(serializer.data)

@api_view(['POST'])
def createNote(request):
    note = Note.objects.create(body=request.data['body'])
    serializer = NoteSerializer(note, many=False)
    return Response(serializer.data)

@api_view(['PUT'])
def updateNote(request, pk):
    note = Note.objects.get(id=pk)
    serializer = NoteSerializer(instance=note, data=request.data)
    if serializer.is_valid():
        serializer.save()
    return Response(serializer.data)

@api_view(['DELETE'])
def deleteNote(request, pk):
    note = Note.objects.get(id=pk)
    note.delete()
    return Response('Note was deleted!')
```

### URL Routing

```python
# api/urls.py
urlpatterns = [
    path('notes/', views.getNotes),
    path('notes/create/', views.createNote),  # must be before <pk>
    path('notes/<str:pk>/', views.getNote),
    path('notes/<str:pk>/update/', views.updateNote),
    path('notes/<str:pk>/delete/', views.deleteNote),
]

# notes/urls.py
urlpatterns = [
    path('admin/', admin.site.urls),
    path('api/', include('api.urls')),
]
```

`notes/create/` must be declared before `notes/<str:pk>/` — Django matches URLs top to bottom, so a wildcard pattern would otherwise intercept the create route.

---

## Cviko 7 — CORS + Flutter Frontend

### CORS Configuration

CORS is required because the Flutter app runs on a different port than Django. Without it, the browser blocks cross-origin requests.

```python
# settings.py

INSTALLED_APPS = [
    ...
    'corsheaders',
]

MIDDLEWARE = [
    'corsheaders.middleware.CorsMiddleware',  # must be first
    ...
]

CORS_ALLOW_ALL_ORIGINS = True
```

### Flutter — urls.dart

```dart
const String baseUrl = 'http://localhost:8000';

const String notesUrl = '$baseUrl/api/notes/';
const String createNoteUrl = '$baseUrl/api/notes/create/';

String getNoteUrl(String pk) => '$baseUrl/api/notes/$pk/';
String updateNoteUrl(String pk) => '$baseUrl/api/notes/$pk/update/';
String deleteNoteUrl(String pk) => '$baseUrl/api/notes/$pk/delete/';
```

### Flutter — HTTP Pattern

All screens follow the same pattern — call the endpoint, check status code, pop back with `true` to signal the list to refresh.

```dart
// GET
final response = await http.get(Uri.parse(notesUrl));
notes = jsonDecode(response.body);

// POST
final response = await http.post(
    Uri.parse(createNoteUrl),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({'body': bodyController.text}),
);

// PUT
final response = await http.put(
    Uri.parse(updateNoteUrl(id)),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({'body': bodyController.text}),
);

// DELETE
await http.delete(Uri.parse(deleteNoteUrl(id)));
```

### Flutter — Navigation

Child screens return `true` on success. The list screen uses this to decide whether to re-fetch.

```dart
final result = await Navigator.push(context, MaterialPageRoute(...));
if (result == true) fetchNotes();
```

---

## Common Issues

**CORS error in browser**  
Check that `CorsMiddleware` is the first entry in `MIDDLEWARE` and `CORS_ALLOW_ALL_ORIGINS = True` is set.

**Flutter can't connect to backend**  
Make sure Django is running. Use `localhost:8000` for Chrome, `10.0.2.2:8000` for Android emulator.

**Emulator won't start (disk space)**  
Run on Chrome with `flutter run -d chrome` instead.

**`notes/create/` returns 404**  
Check URL order in `api/urls.py` — `create/` must come before `<str:pk>/`.