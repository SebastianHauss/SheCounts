// profile.js

const BASE_URL = 'http://localhost:8080/api';

let currentUserId = null;
let currentUserData = null;
let isViewingOwnProfile = false;

/* =========================
   Utils
========================= */

function setFormDisabled(disabled) {
  document
    .querySelectorAll('#profileForm input, #profileForm select')
    .forEach((el) => (el.disabled = disabled));
}

function getUserIdFromUrl() {
  return new URLSearchParams(window.location.search).get('id');
}

function setCountrySelect(countryCode) {
  const select = document.getElementById('profile-country');
  if (!select || !countryCode) return;

  select.value = countryCode;
}

/* =========================
   API
========================= */

async function getCurrentUser() {
  try {
    const res = await fetch(`${BASE_URL}/auth/me`, {
      credentials: 'include',
    });
    return res.ok ? await res.json() : null;
  } catch (e) {
    console.error('getCurrentUser failed', e);
    return null;
  }
}

async function loadUserProfile(userId) {
  try {
    const res = await fetch(`${BASE_URL}/users/${userId}`, {
      credentials: 'include',
    });

    if (!res.ok) throw new Error(res.status);
    return await res.json();
  } catch (e) {
    console.error('loadUserProfile failed', e);
    return null;
  }
}

async function saveProfile(profileData) {
  try {
    // Update User
    await fetch(`${BASE_URL}/users/${currentUserId}`, {
      method: 'PUT',
      credentials: 'include',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        username: profileData.username,
        email: profileData.email,
        admin: currentUserData.admin,
      }),
    });

    // Update Profile
    await fetch(`${BASE_URL}/profiles/${currentUserData.profileId}`, {
      method: 'PUT',
      credentials: 'include',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        id: currentUserData.profileId,
        gender: profileData.gender.toUpperCase(),
        country: profileData.country,
        birthday: profileData.birthday,
        userId: currentUserId,
        profilePicUrl: currentUserData.profile?.profilePicUrl ?? null,
      }),
    });

    alert('Profil erfolgreich gespeichert!');

    // Navbar aktualisieren nach Profiländerung (zB Username)
    window.location.reload();

    // Aktualisierte Daten laden
    currentUserData = await loadUserProfile(currentUserId);

    // Wenn die loadAndUpdateUserProfile Funktion verfügbar ist
    if (typeof loadAndUpdateUserProfile === 'function') {
      await loadAndUpdateUserProfile(currentUserId);
    }

    return true;
  } catch (e) {
    console.error('saveProfile failed', e);
    alert('Fehler beim Speichern');
    return false;
  }
}

/* =========================
   UI
========================= */

function populateForm(userData) {
  document.getElementById('username').value = userData.username ?? '';
  document.getElementById('email').value = userData.email ?? '';

  if (!userData.profile) return;

  // Gender
  const gender = userData.profile.gender?.toLowerCase();
  if (gender) {
    const radio = document.querySelector(
      `input[name="gender"][value="${gender}"]`
    );
    if (radio) radio.checked = true;
  }

  // Country
  const country = userData.profile.country?.toLowerCase();
  setCountrySelect(country);

  // Birthday
  if (userData.profile.birthday) {
    document.getElementById('birthday').value = userData.profile.birthday;
  }
}

/* =========================
   Init
========================= */

document.addEventListener('DOMContentLoaded', async () => {
  const form = document.getElementById('profileForm');
  const editBtn = document.querySelector('.edit-profile-btn');
  const cancelBtn = document.querySelector('.myCancelBtn');
  const deleteBtn = document.getElementById('delete-account-btn');
  const countrySelect = document.getElementById('profile-country');

  countrySelect.addEventListener('change', (e) => {
    console.log('Country changed:', e.target.value);
  });

  // Determine user
  const idFromUrl = getUserIdFromUrl();

  if (idFromUrl) {
    currentUserId = idFromUrl;
    isViewingOwnProfile = false;
  } else {
    const me = await getCurrentUser();
    if (!me) {
      alert('Bitte loggen Sie sich ein, um Ihr Profil zu sehen.');
      return;
    }
    currentUserId = me.userId;
    isViewingOwnProfile = true;
  }

  currentUserData = await loadUserProfile(currentUserId);
  if (!currentUserData) return;

  populateForm(currentUserData);
  setFormDisabled(true);

  /* =========================
     Buttons
  ========================= */

  editBtn.addEventListener('click', () => {
    setFormDisabled(false);
    console.log('Edit mode');
  });

  cancelBtn.addEventListener('click', () => {
    populateForm(currentUserData);
    setFormDisabled(true);
    console.log('Edit cancelled');
  });

  deleteBtn.addEventListener('click', async () => {
    const confirmed = confirm(
      'Sind Sie sicher, dass Sie Ihr Konto löschen möchten? Diese Aktion kann nicht rückgängig gemacht werden.'
    );
    if (!confirmed) return;
    try {
      const res = await fetch(`${BASE_URL}/users/${currentUserId}`, {
        method: 'DELETE',
        credentials: 'include',
      });
      if (!res.ok) throw new Error(res.status);
      alert('Konto erfolgreich gelöscht.');
      window.location.href = '/';
    } catch (e) {
      console.error('Konto löschen fehlgeschlagen', e);
      alert('Fehler beim Löschen des Kontos.');
    }
  });

  /* =========================
     Submit
  ========================= */

  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    if (!form.checkValidity()) {
      form.classList.add('was-validated');
      return;
    }

    const profileData = {
      username: document.getElementById('username').value,
      email: document.getElementById('email').value,
      gender:
        document.querySelector('input[name="gender"]:checked')?.value ??
        'diverse',
      country: countrySelect.value,
      birthday: document.getElementById('birthday').value || null,
    };

    console.log('SEND PROFILE:', profileData);

    const success = await saveProfile(profileData);
    if (success) {
      currentUserData = await loadUserProfile(currentUserId);
      populateForm(currentUserData);
      setFormDisabled(true);
    }
  });
});
