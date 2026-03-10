# Django Notes Backend - Cviko 6

**Student:** Dominik Sabota  
**Course:** XPC-MMA (Cross-Platform Mobile Applications)  
**VUT FIT:** 2025/2026  
**Points:** 10/10

---

## 🌐 Project Overview

A Django REST API backend providing full CRUD (Create, Read, Update, Delete) operations for a notes application. Built with Django REST Framework, featuring a SQLite database and admin panel.

### Features Implemented

✅ **Virtual Environment & Setup (3 points)**
- Python virtual environment created
- Django 6.0.3 installed
- Project structure configured

✅ **REST API Functionality (7 points)**
- Note model with database migrations
- Django REST Framework integration
- Serializers for JSON conversion
- Complete CRUD operations:
  - **GET** - Retrieve all notes
  - **GET** - Retrieve single note
  - **POST** - Create new note
  - **PUT** - Update existing note
  - **DELETE** - Delete note
- URL routing configured
- Admin panel setup

**Total:** 10/10 points ✅

---

## 🛠️ Technology Stack

- **Framework:** Django 6.0.3
- **Language:** Python 3.12.2
- **REST Framework:** djangorestframework 3.16.1
- **Database:** SQLite3 (default)
- **Server:** Django development server

---

## 📂 Project Structure

```
django_notes_backend/
├── env/                          # Virtual environment
└── notes/                        # Django project
    ├── api/                      # API application
    │   ├── migrations/
    │   │   └── 0001_initial.py  # Note model migration
    │   ├── __init__.py
    │   ├── admin.py             # Admin panel config
    │   ├── apps.py
    │   ├── models.py            # Note model
    │   ├── serializers.py       # NoteSerializer
    │   ├── urls.py              # API routes
    │   ├── views.py             # CRUD view functions
    │   └── tests.py
    ├── notes/                    # Project settings
    │   ├── __init__.py
    │   ├── settings.py          # Configuration
    │   ├── urls.py              # Root URL config
    │   ├── wsgi.py              # WSGI server
    │   └── asgi.py              # ASGI server
    ├── db.sqlite3               # SQLite database
    └── manage.py                # Django CLI tool
```

---

## 🚀 Setup Instructions

### Prerequisites

1. **Python 3.8+** installed
   - Download from: https://www.python.org/downloads/
   - **IMPORTANT:** Check "Add Python to PATH" during installation
   - Verify: `python --version`

2. **PowerShell** or **Command Prompt** (Windows)

3. **VS Code** (recommended) or any text editor

---

### Installation Steps

#### 1. Navigate to Project Directory

```powershell
cd C:\Users\domin\Desktop\MMA\XPC-MMA-2025\django_notes_backend
```

#### 2. Create Virtual Environment

```powershell
python -m venv env
```

**What this does:** Creates isolated Python environment in `env` folder.

#### 3. Enable PowerShell Scripts (Windows)

```powershell
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```

**When prompted:** Type `Y` and press Enter.

#### 4. Activate Virtual Environment

```powershell
.\env\Scripts\Activate
```

**You should see:** `(env)` appears before your prompt.

```powershell
(env) PS C:\...\django_notes_backend>
```

#### 5. Install Django & REST Framework

```powershell
pip install django
pip install djangorestframework
```

**Expected output:**
```
Successfully installed django-6.0.3 ...
Successfully installed djangorestframework-3.16.1
```

#### 6. Navigate to Django Project

```powershell
cd notes
```

#### 7. Apply Database Migrations

```powershell
python manage.py migrate
```

**Creates:** Database tables for Django admin, auth, sessions, and your Note model.

#### 8. Create Superuser (Admin Access)

```powershell
python manage.py createsuperuser
```

**Enter:**
- Username: `admin` (or your choice)
- Email: (press Enter to skip)
- Password: `admin123` (or your choice - won't display as you type)

**Note:** Password must be at least 8 characters.

---

### Running the Server

```powershell
python manage.py runserver
```

**Expected output:**
```
Django version 6.0.3, using settings 'notes.settings'
Starting development server at http://127.0.0.1:8000/
Quit the server with CTRL-BREAK.
```

**Server is now running at:** http://127.0.0.1:8000/

**To stop:** Press `Ctrl + C`

---

## 🧪 Testing the API

### Available Endpoints

| Endpoint | Method | Description | URL |
|----------|--------|-------------|-----|
| Routes | GET | List all endpoints | http://127.0.0.1:8000/api/ |
| Get All Notes | GET | Retrieve all notes | http://127.0.0.1:8000/api/notes/ |
| Get Single Note | GET | Retrieve note by ID | http://127.0.0.1:8000/api/notes/1/ |
| Create Note | POST | Create new note | http://127.0.0.1:8000/api/notes/create/ |
| Update Note | PUT | Update existing note | http://127.0.0.1:8000/api/notes/1/update/ |
| Delete Note | DELETE | Delete note | http://127.0.0.1:8000/api/notes/1/delete/ |
| Admin Panel | - | Manage data visually | http://127.0.0.1:8000/admin/ |

---

### 1. View API Routes

**URL:** http://127.0.0.1:8000/api/

**Response:** JSON array listing all available endpoints.

---

### 2. Admin Panel - Add Test Data

**URL:** http://127.0.0.1:8000/admin/

**Login:**
- Username: `admin`
- Password: `admin123`

**Steps:**
1. Click **"Notes"**
2. Click **"+ Add Note"**
3. Enter text in **Body** field
4. Click **"Save"**
5. Add 2-3 more test notes

---

### 3. GET All Notes

**URL:** http://127.0.0.1:8000/api/notes/

**Response:**
```json
[
    {
        "id": 1,
        "body": "First Django backend note",
        "updated": "2026-03-09T23:30:00Z",
        "created": "2026-03-09T23:30:00Z"
    },
    {
        "id": 2,
        "body": "Learning REST API development",
        "updated": "2026-03-09T23:30:05Z",
        "created": "2026-03-09T23:30:05Z"
    }
]
```

---

### 4. GET Single Note

**URL:** http://127.0.0.1:8000/api/notes/1/

**Response:**
```json
{
    "id": 1,
    "body": "First Django backend note",
    "updated": "2026-03-09T23:30:00Z",
    "created": "2026-03-09T23:30:00Z"
}
```

---

### 5. POST Create Note

**URL:** http://127.0.0.1:8000/api/notes/create/

**In REST Framework Interface:**
1. Scroll to **Content** field
2. Enter:
```json
{
    "body": "New note created via API"
}
```
3. Click **POST** button

**Response:** Newly created note with ID, timestamps.

---

### 6. PUT Update Note

**URL:** http://127.0.0.1:8000/api/notes/1/update/

**In REST Framework Interface:**
1. Enter:
```json
{
    "body": "This note has been updated!"
}
```
2. Click **PUT** button

**Response:** Updated note with new `updated` timestamp.

---

### 7. DELETE Note

**URL:** http://127.0.0.1:8000/api/notes/1/delete/

**Click:** DELETE button

**Response:** `"Note was deleted!"`

---

## 💡 Code Explanation

### models.py - Database Schema

```python
from django.db import models

class Note(models.Model):
    body = models.TextField()
    updated = models.DateTimeField(auto_now=True)
    created = models.DateTimeField(auto_now_add=True)
    
    def __str__(self):
        return self.body[0:50]
    
    class Meta:
        ordering = ['-updated']
```

**Fields:**
- `body`: TextField - unlimited text storage
- `updated`: DateTimeField - auto-updates on save
- `created`: DateTimeField - auto-sets on creation
- `__str__`: Returns first 50 characters for display
- `ordering`: Newest notes first (descending by updated)

**Database Table:** `api_note`  
**Columns:** id, body, updated, created

---

### serializers.py - JSON Conversion

```python
from rest_framework.serializers import ModelSerializer
from .models import Note

class NoteSerializer(ModelSerializer):
    class Meta:
        model = Note
        fields = '__all__'
```

**Purpose:** Converts Python Note objects ↔ JSON

**How it works:**
```
Python Object → Serializer → JSON → API Response
```

---

### views.py - API Logic

#### GET All Notes
```python
@api_view(['GET'])
def getNotes(request):
    notes = Note.objects.all()
    serializer = NoteSerializer(notes, many=True)
    return Response(serializer.data)
```

#### GET Single Note
```python
@api_view(['GET'])
def getNote(request, pk):
    note = Note.objects.get(id=pk)
    serializer = NoteSerializer(note, many=False)
    return Response(serializer.data)
```

#### POST Create Note
```python
@api_view(['POST'])
def createNote(request):
    data = request.data
    note = Note.objects.create(body=data['body'])
    serializer = NoteSerializer(note, many=False)
    return Response(serializer.data)
```

#### PUT Update Note
```python
@api_view(['PUT'])
def updateNote(request, pk):
    data = request.data
    note = Note.objects.get(id=pk)
    serializer = NoteSerializer(instance=note, data=data)
    
    if serializer.is_valid():
        serializer.save()
    
    return Response(serializer.data)
```

#### DELETE Note
```python
@api_view(['DELETE'])
def deleteNote(request, pk):
    note = Note.objects.get(id=pk)
    note.delete()
    return Response('Note was deleted!')
```

---

### urls.py - URL Routing

**api/urls.py** (Application URLs):
```python
from django.urls import path
from . import views

urlpatterns = [
    path('', views.getRoutes, name="routes"),
    path('notes/', views.getNotes, name="notes"),
    path('notes/create/', views.createNote, name="create-note"),  # Specific first
    path('notes/<str:pk>/', views.getNote, name="note"),
    path('notes/<str:pk>/update/', views.updateNote, name="update-note"),
    path('notes/<str:pk>/delete/', views.deleteNote, name="delete-note"),
]
```

**notes/urls.py** (Project URLs):
```python
from django.contrib import admin
from django.urls import path, include

urlpatterns = [
    path('admin/', admin.site.urls),
    path('api/', include('api.urls')),
]
```

**Important:** Specific paths (`notes/create/`) must come **before** wildcard paths (`notes/<str:pk>/`)

---

## 🔧 Key Concepts

### 1. MVT Pattern (Model-View-Template)
- **Model** (models.py): Database structure
- **View** (views.py): Business logic
- **Template**: HTML (not used - REST API only)

### 2. ORM (Object-Relational Mapping)
```python
# Python code
notes = Note.objects.all()

# SQL equivalent
SELECT * FROM api_note;
```

### 3. REST API Principles
| HTTP Method | CRUD Operation | Example |
|-------------|----------------|---------|
| GET | Read | Get notes |
| POST | Create | Add note |
| PUT | Update | Modify note |
| DELETE | Delete | Remove note |

### 4. Serialization
```
Database → Python Object → Serializer → JSON → API
```

---

## 🐛 Troubleshooting

### Issue: "python is not recognized"
**Solution:**
- Reinstall Python with "Add to PATH" checked
- Or add manually to system PATH

### Issue: Virtual environment won't activate
**Solution:**
```powershell
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```

### Issue: "No module named 'rest_framework'"
**Solution:**
```powershell
# Ensure env is activated
.\env\Scripts\Activate
pip install djangorestframework
```

### Issue: Migration errors
**Solution:**
```powershell
python manage.py makemigrations
python manage.py migrate
```

### Issue: Port 8000 already in use
**Solution:**
```powershell
# Use different port
python manage.py runserver 8080
```

### Issue: CREATE endpoint shows error
**Solution:** Check `api/urls.py` - `notes/create/` must come **before** `notes/<str:pk>/`

---

## 📊 Database Management

### View Database Shell
```powershell
python manage.py dbshell
```

### Python Shell with Django
```powershell
python manage.py shell
```

**Example usage:**
```python
from api.models import Note

# Create note
note = Note.objects.create(body="Test note")

# Get all notes
notes = Note.objects.all()

# Update note
note.body = "Updated text"
note.save()

# Delete note
note.delete()
```

---

## 🔄 Daily Development Workflow

### Start Working
```powershell
cd C:\Users\domin\Desktop\MMA\XPC-MMA-2025\django_notes_backend\notes
.\env\Scripts\Activate
python manage.py runserver
```

### Make Changes to Models
```powershell
# After editing models.py
python manage.py makemigrations
python manage.py migrate
```

### Stop Working
```powershell
# Stop server: Ctrl + C
deactivate
```

---

## 📚 File Changes Summary

### Files Created

1. **`api/models.py`** - Note model definition
2. **`api/serializers.py`** - NoteSerializer class (NEW FILE)
3. **`api/views.py`** - CRUD view functions
4. **`api/urls.py`** - API URL routing (NEW FILE)
5. **`api/admin.py`** - Admin panel registration
6. **`notes/settings.py`** - Added apps to INSTALLED_APPS
7. **`notes/urls.py`** - Included API URLs
8. **`db.sqlite3`** - Database file (auto-generated)
9. **`api/migrations/0001_initial.py`** - Note model migration (auto-generated)

### Files Modified

- `notes/settings.py` - Added `'api.apps.ApiConfig'` and `'rest_framework'`
- `notes/urls.py` - Added `path('api/', include('api.urls'))`
- `api/admin.py` - Registered Note model
- `api/models.py` - Created Note model
- `api/views.py` - Created all view functions

---

## ✅ Assignment Checklist

**Part 1: Setup (3 points)**
- [x] Virtual environment created
- [x] Django installed
- [x] Project created and running

**Part 2: Functionality (7 points)**
- [x] Note model (body, updated, created)
- [x] REST Framework installed
- [x] NoteSerializer created
- [x] GET all notes endpoint
- [x] GET single note endpoint
- [x] POST create endpoint
- [x] PUT update endpoint
- [x] DELETE endpoint
- [x] URL routing configured
- [x] Admin panel setup

**Total: 10/10 points** ✅

---

## 🎓 Learning Outcomes

### Skills Demonstrated
1. ✅ Django project structure
2. ✅ Virtual environment management
3. ✅ Database modeling (ORM)
4. ✅ REST API development
5. ✅ Serialization
6. ✅ URL routing
7. ✅ CRUD operations
8. ✅ Admin panel configuration

### Django Concepts Mastered
- MVT pattern
- Models and migrations
- Django ORM
- REST Framework
- Serializers
- ViewSets and API views
- URL configuration
- Admin interface

---

## 🔄 Git Workflow

```bash
cd C:\Users\domin\Desktop\MMA\XPC-MMA-2025

# Stage changes
git add django_notes_backend

# Commit
git commit -m "Complete Django REST API backend - Cviko6 (10pts)"

# Push
git push origin main
```

---

## 📚 Resources

- **Django Documentation:** https://docs.djangoproject.com/
- **REST Framework:** https://www.django-rest-framework.org/
- **Django Tutorial:** https://docs.djangoproject.com/en/stable/intro/tutorial01/
- **REST API Guide:** https://restfulapi.net/

---

## 👨‍💻 Author

**Dominik Sabota**  
VUT FIT - XPC-MMA 2025/2026  
GitHub: https://github.com/dmnkSabota/XPC-MMA-2025

---

## 📄 License

This project is part of VUT FIT coursework.

---

**Last Updated:** March 10, 2026  
**Django Version:** 6.0.3  
**Python Version:** 3.12.2  
**REST Framework:** 3.16.1
