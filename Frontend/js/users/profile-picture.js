console.log("Loaded profile-picture.js");

// API Base URL
const API_BASE_URL = 'http://localhost:8080/api';

// Konstanten
const MAX_FILE_SIZE = 20 * 1024 * 1024; // 20MB


const ALLOWED_TYPES = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];

// DOM Elements
let profilePictureInput;
let profilePicturePreview;
let selectPictureBtn;
let uploadPictureBtn;
let removePictureBtn;

let currentProfileUserId = null;
let isOwnProfile = false;

$(document).ready(function () {
  initProfilePicture();
  // when login/logout happens, refresh user + picture
  window.addEventListener('authChanged', () => {
    loadProfilePicture();
  });
});

async function initProfilePicture() {
  profilePictureInput = document.getElementById('profile-picture-input');
  profilePicturePreview = document.getElementById('profile-picture-preview');
  selectPictureBtn = document.getElementById('select-picture-btn');
  uploadPictureBtn = document.getElementById('upload-picture-btn');
  removePictureBtn = document.getElementById('remove-picture-btn');

  await determineProfileUserId();

  if (selectPictureBtn) {
    selectPictureBtn.addEventListener('click', () =>
      profilePictureInput.click()
    );
  }

  if (profilePictureInput) {
    profilePictureInput.addEventListener('change', handleFileSelect);
  }

  if (uploadPictureBtn) {
    uploadPictureBtn.addEventListener('click', handleUpload);
  }

  if (removePictureBtn) {
    removePictureBtn.addEventListener('click', handleRemove);
  }

  // Zeige/Verstecke Upload-Buttons je nachdem, ob es das eigene Profil ist
  if (!isOwnProfile) {
    if (selectPictureBtn) selectPictureBtn.style.display = 'none';
    if (uploadPictureBtn) uploadPictureBtn.style.display = 'none';
    if (removePictureBtn) removePictureBtn.style.display = 'none';
  }

  loadProfilePicture();
}

async function determineProfileUserId() {
  // Prüfe URL-Parameter
  const urlParams = new URLSearchParams(window.location.search);
  const userIdFromUrl = urlParams.get('id');

  if (userIdFromUrl) {
    // Admin schaut sich ein fremdes Profil an
    currentProfileUserId = userIdFromUrl;
    isOwnProfile = false;
    console.log('Viewing profile of user:', currentProfileUserId);
  } else {
    // Eigenes Profil
    try {
      const response = await fetch(`${API_BASE_URL}/auth/me`, {
        credentials: 'include',
      });

      if (response.ok) {
        const meData = await response.json();
        currentProfileUserId = meData.userId;
        isOwnProfile = true;
        console.log('Viewing own profile:', currentProfileUserId);
      }
    } catch (error) {
      console.error('Error getting current user:', error);
    }
  }
}

// File Select Handler - Show Preview
function handleFileSelect(event) {
  const file = event.target.files[0];

  if (!file) {
    uploadPictureBtn.style.display = 'none';
    return;
  }

  if (!validateFile(file)) {
    profilePictureInput.value = '';
    uploadPictureBtn.style.display = 'none';
    return;
  }

  const reader = new FileReader();
  reader.onload = (e) => {
    profilePicturePreview.src = e.target.result;
    uploadPictureBtn.style.display = 'block';
  };
  reader.readAsDataURL(file);
}

function validateFile(file) {
  if (!ALLOWED_TYPES.includes(file.type)) {
    alert('Ungültiger Dateityp! Nur JPEG, PNG, GIF und WebP sind erlaubt.');
    return false;
  }

  if (file.size > MAX_FILE_SIZE) {
    alert('Datei ist zu groß! Maximum: 20MB');
    return false;
  }

  return true;
}

// Upload Handler (jQuery AJAX)
function handleUpload() {
  if (!isOwnProfile) {
    alert('Du kannst nur dein eigenes Profilbild ändern!');
    return;
  }

  const file = profilePictureInput.files[0];

  if (!file) {
    alert('Bitte wähle ein Bild aus!');
    return;
  }

  if (!validateFile(file)) return;

  uploadPictureBtn.disabled = true;
  uploadPictureBtn.textContent = 'Lädt...';

  const formData = new FormData();
  formData.append('file', file);

  $.ajax({
    url: `${API_BASE_URL}/files/profile-picture`,
    type: 'POST',
    data: formData,

    processData: false,
    contentType: false,

    xhrFields: { withCredentials: true },
    crossDomain: true,

    success: function (data) {
      console.log('Upload erfolgreich:', data);
      alert('Profilbild erfolgreich hochgeladen!');

      uploadPictureBtn.style.display = 'none';
      uploadPictureBtn.disabled = false;
      uploadPictureBtn.textContent = 'Hochladen';
      profilePictureInput.value = '';

      loadProfilePicture();
    },

    error: function (xhr) {
      console.error('Upload failed:', xhr);

      if (xhr.status === 401) {
        alert('Bitte melde dich an!');
        window.location.href = '../../index.html';
        return;
      }

      let msg = 'Upload fehlgeschlagen';
      try {
        const json = xhr.responseJSON;
        if (json?.error) msg = json.error;
      } catch (e) {}

      alert('Upload fehlgeschlagen: ' + msg);

      uploadPictureBtn.disabled = false;
      uploadPictureBtn.textContent = 'Hochladen';
    },
  });
}

// Load Profile Picture from Backend (jQuery AJAX)
function loadProfilePicture() {
  if (!currentProfileUserId) {
    console.warn('No profile user ID set');
    profilePicturePreview.src = '../../img/users/profile-pic-avatar.png';
    return;
  }

  $.ajax({
    url: `${API_BASE_URL}/users/${currentProfileUserId}`,
    type: 'GET',

    xhrFields: { withCredentials: true },
    crossDomain: true,

    success: function (user) {
      console.log('User loaded:', user);

      if (user.profilePictureId) {
        profilePicturePreview.src = `${API_BASE_URL}/files/${user.profilePictureId}`;
        console.log('Loading profile picture:', user.profilePictureId);
      } else {
        profilePicturePreview.src = '../../img/users/profile-pic-avatar.png';
        console.log('No profile picture - using default');
      }
    },

    error: function (xhr) {
      console.error('User laden fehlgeschlagen:', xhr.status, xhr.responseText);

      if (xhr.status === 401 || xhr.status === 403) {
        alert('Bitte melde dich an!');
        window.location.href = '../../index.html';
        return;
      }

      profilePicturePreview.src = '../../img/users/profile-pic-avatar.png';
    },
  });
}

// Remove Profile Picture (jQuery AJAX)
function handleRemove() {
  if (!isOwnProfile) {
    alert('Du kannst nur dein eigenes Profilbild löschen!');
    return;
  }

  if (!confirm('Möchtest du dein Profilbild wirklich entfernen?')) return;

  $.ajax({
    url: `${API_BASE_URL}/files/profile-picture`,
    type: 'DELETE',

    xhrFields: { withCredentials: true },
    crossDomain: true,

    success: function () {
      alert('Profilbild entfernt!');

      profilePicturePreview.src = '../../img/users/profile-pic-avatar.png';
      profilePictureInput.value = '';
      uploadPictureBtn.style.display = 'none';
    },

    error: function (xhr) {
      console.error('Remove failed:', xhr.status, xhr.responseText);
      alert('Entfernen fehlgeschlagen');
    },
  });
}
