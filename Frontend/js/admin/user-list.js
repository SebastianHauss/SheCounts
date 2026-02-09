// user-list.js

const BASE_URL = 'http://localhost:8080/api';

let allUsers = [];
let currentUser = null;

const statusBadge = (isActive) =>
  `<span class="badge ${
    isActive ? 'bg-success' : 'bg-secondary'
  }">${isActive ? 'active' : 'inactive'}</span>`;

const formatDate = (dateString) => {
  if (!dateString) return 'N/A';
  const date = new Date(dateString);
  return date.toLocaleDateString('de-DE', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  });
};

const getCurrentUser = async () => {
  try {
    const response = await fetch(`${BASE_URL}/auth/me`, {
      method: 'GET',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      return null;
    }

    return await response.json();
  } catch (error) {
    console.error('Fehler beim Abrufen der Benutzerinformationen:', error);
    return null;
  }
};

const checkAdminAccess = async () => {
  currentUser = await getCurrentUser();

  if (!currentUser) {
    alert('Sie sind nicht angemeldet. Bitte melden Sie sich an.');
    return false;
  }

  if (!currentUser.isAdmin) {
    alert('Sie haben keine Berechtigung, diese Seite zu sehen.');
    window.location.href = '../../index.html';
    return false;
  }

  return true;
};

const loadUsers = async () => {
  try {
    const response = await fetch(`${BASE_URL}/users`, {
      method: 'GET',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      if (response.status === 401) {
        alert('Ihre Sitzung ist abgelaufen. Bitte melden Sie sich erneut an.');
        window.location.href = '../../index.html';
        return;
      }
      if (response.status === 403) {
        alert('Sie haben keine Berechtigung, Benutzerdaten anzuzeigen.');
        window.location.href = '../../index.html';
        return;
      }
      throw new Error(`HTTP Error: ${response.status}`);
    }

    allUsers = await response.json();
    console.log('Loaded users from backend:', allUsers);
    displayUsers(allUsers);
  } catch (error) {
    console.error('Fehler beim Laden der Benutzer:', error);
    $('#user-table-body').html(`
      <tr>
        <td colspan="6" class="text-center text-danger py-4">
          <i class="bi bi-exclamation-triangle"></i>
          Fehler beim Laden der Benutzerdaten.
        </td>
      </tr>
    `);
  }
};

window.handleImageError = function (img) {
  if (img.dataset.errorHandled === 'true') {
    return;
  }

  img.dataset.errorHandled = 'true';
  img.onerror = null;

  const fallback = img.dataset.fallback;
  if (fallback) {
    img.src = fallback;
  }
};

const displayUsers = (users) => {
  const tbody = $('#user-table-body');
  tbody.empty();

  if (users.length === 0) {
    tbody.append(`
      <tr>
        <td colspan="6" class="text-center py-5">
          <i class="bi bi-inbox" style="font-size: 3rem; color: #ddd;"></i>
          <p class="mt-3 text-muted">Keine Benutzer gefunden.</p>
        </td>
      </tr>
    `);
    return;
  }

  $('#user-count').text(`${users.length} Users`);

  users.forEach((user) => {
    // Prüft UUID-Format
    const isValidFileId =
      user.profilePictureId &&
      /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i.test(
        user.profilePictureId
      );

    const fallbackAvatar = `https://ui-avatars.com/api/?name=${encodeURIComponent(user.username || 'User')}&background=6c757d&color=fff&size=128&bold=true`;

    const profilePicUrl = isValidFileId
      ? `${BASE_URL}/files/${user.profilePictureId}`
      : fallbackAvatar;

    const country = user.profile?.country || 'N/A';
    const createdAt = formatDate(user.createdAt);
    const isAdmin = user.admin === true || user.isAdmin === true;
    const isActive = !user.deleted;

    tbody.append(`
      <tr data-user-id="${user.id}">
        <td>
          <div class="d-flex align-items-center">
            <img src="${profilePicUrl}" 
                 alt="${user.username}" 
                 class="rounded-circle me-3" 
                 width="40" 
                 height="40"
                 data-fallback="${fallbackAvatar}"
                 onerror="handleImageError(this)">
            <div>
              <a href="../users/profile.html?id=${user.id}" class="fw-semibold text-decoration-none">
                ${user.username}
              </a>
              ${isAdmin ? '<span class="badge bg-primary ms-2">Admin</span>' : ''}
            </div>
          </div>
        </td>
        <td><small class="text-muted">${createdAt}</small></td>
        <td>${user.email}</td>
        <td>${country}</td>
        <td>${statusBadge(isActive)}</td>
        <td class="text-end">
          <a href="../users/profile.html?id=${user.id}"
             class="btn btn-sm btn-outline-primary" 
             title="View Details">
            <i class="bi bi-eye"></i>
          </a>
        </td>
      </tr>
    `);
  });

  $('#pagination-container').show();
};

const searchUsers = (searchTerm) => {
  if (!searchTerm) {
    displayUsers(allUsers);
    return;
  }

  const term = searchTerm.toLowerCase();
  const filtered = allUsers.filter((user) => {
    return (
      user.username?.toLowerCase().includes(term) ||
      user.email?.toLowerCase().includes(term) ||
      user.profile?.country?.toLowerCase().includes(term)
    );
  });

  displayUsers(filtered);
};

const sortUsers = (sortBy) => {
  if (!sortBy) {
    displayUsers(allUsers);
    return;
  }

  const sorted = [...allUsers].sort((a, b) => {
    switch (sortBy) {
      case 'username':
        return a.username.localeCompare(b.username);
      case 'email':
        return a.email.localeCompare(b.email);
      case 'created':
        return new Date(b.createdAt) - new Date(a.createdAt);
      case 'country':
        return (a.profile?.country || '').localeCompare(
          b.profile?.country || ''
        );
      case 'status':
        return (a.deleted ? 1 : 0) - (b.deleted ? 1 : 0);
      default:
        return 0;
    }
  });

  displayUsers(sorted);
};

$(document).ready(async function () {
  console.log('User list page loaded');

  $('#user-table-body').html(`
    <tr>
      <td colspan="6" class="text-center py-5">
        <div class="spinner-border" role="status">
          <span class="visually-hidden">Laden...</span>
        </div>
      </td>
    </tr>
  `);

  const hasAccess = await checkAdminAccess();
  if (!hasAccess) {
    return;
  }

  await loadUsers();

  $('#searchInput').on('input', function () {
    searchUsers($(this).val());
  });

  $('#sortSelect').on('change', function () {
    sortUsers($(this).val());
  });
});
