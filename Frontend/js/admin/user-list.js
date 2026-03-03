console.log("Loaded user-list.js");

// ─── HELPERS ──────────────────────────────────────────────────────────────────

const statusBadge = (isActive) =>
    `<span class="badge ${isActive ? 'bg-success' : 'bg-secondary'}">
    ${isActive ? 'Active' : 'Inactive'}
  </span>`;

const formatDate = (dateString) => {
  if (!dateString) return 'N/A';
  return new Date(dateString).toLocaleDateString('de-DE', {
    day: '2-digit', month: '2-digit', year: 'numeric',
  });
};

// ─── AUTH ─────────────────────────────────────────────────────────────────────

const checkAdminAccess = async () => {
  currentUser = await getCurrentUser();
  if (!currentUser) {
    alert('Sie sind nicht angemeldet. Bitte melden Sie sich an.');
    window.location.href = '../../index.html';
    return false;
  }
  if (!currentUser.isAdmin) {
    alert('Sie haben keine Berechtigung, diese Seite zu sehen.');
    window.location.href = '../../index.html';
    return false;
  }
  return true;
};

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

async function checkAuthStatus() {
    try {
        const response = await fetch('http://localhost:8080/api/auth/me', {
            method: 'GET',
            credentials: 'include',
        });

        if (response.ok) {
            const authData = await response.json();

            // Zeige User-Block an
            $('#loginBlock').hide();
            $('#userBlock').show();
            $('#mobileLoginLink').hide();
            $('#mobileUserBlock').show();

            await loadAndUpdateUserProfile(authData.userId);
        } else {
            $('#loginBlock').show();
            $('#userBlock').hide();
            $('#mobileLoginLink').show();
            $('#mobileUserBlock').hide();
        }
    } catch (error) {
        console.error('Auth check failed:', error);
        $('#loginBlock').show();
        $('#userBlock').hide();
        $('#mobileLoginLink').show();
        $('#mobileUserBlock').hide();
    }
}

// ─── LOAD FROM BACKEND ────────────────────────────────────────────────────────

const loadUsers = async () => {
  try {
    const response = await fetch(`${BASE_URL}/users`, {
      method: 'GET',
      credentials: 'include',
      headers: { 'Content-Type': 'application/json' },
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
    applyFilters();
  } catch (error) {
    console.error('Fehler beim Laden der Benutzer:', error);
    $('#user-cards-grid').html(`
      <div class="user-cards-empty">
        <i class="bi bi-exclamation-triangle"></i>
        <p>Fehler beim Laden der Benutzerdaten.</p>
      </div>
    `);
  }
};

async function loadAndUpdateUserProfile(userId) {
    try {
        const response = await fetch(`http://localhost:8080/api/users/${userId}`, {
            method: 'GET',
            credentials: 'include',
        });

        if (response.ok) {
            const userData = await response.json();
            console.log('Full user data loaded:', userData);
            updateProfileImage(userData);
        } else {
            console.warn('Could not load full user profile, using fallback');
            // Fallback: zeige nur Standard-Avatar
            const fallbackData = { username: 'User' };
            updateProfileImage(fallbackData);
        }
    } catch (error) {
        console.error('Error loading user profile:', error);
        // Fallback
        updateProfileImage({ username: 'User' });
    }
}

function updateProfileImage(userData) {
    const BASE_URL = 'http://localhost:8080/api';

    // Prüfe, ob eine gültige UUID vorhanden ist
    const isValidFileId =
        userData.profilePictureId &&
        /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i.test(
            userData.profilePictureId
        );

    // Fallback Avatar mit ui-avatars.com
    const fallbackAvatar = `https://ui-avatars.com/api/?name=${encodeURIComponent(
        userData.username || 'User'
    )}&background=6c757d&color=fff&size=128&bold=true`;

    // Profilbild-URL bestimmen
    const profilePicUrl = isValidFileId
        ? `${BASE_URL}/files/${userData.profilePictureId}`
        : fallbackAvatar;

    console.log('Updating navbar profile image:', profilePicUrl);

    // Aktualisiere das Profilbild in der Desktop-Navigation
    const $profileImg = $('#userBlock img');
    $profileImg.attr('src', profilePicUrl);
    $profileImg.attr('data-fallback', fallbackAvatar);

    // Füge Error-Handler hinzu
    $profileImg.off('error').on('error', function() {
        if (this.dataset.errorHandled !== 'true') {
            this.dataset.errorHandled = 'true';
            this.src = fallbackAvatar;
        }
    });

    // Aktualisiere den Benutzernamen
    if (userData.username) {
        $('#userBlock strong').text(userData.username);
    }
}

// ─── IMAGE FALLBACK ───────────────────────────────────────────────────────────

window.handleImageError = function (img) {
  if (img.dataset.errorHandled === 'true') return;
  img.dataset.errorHandled = 'true';
  img.onerror = null;
  if (img.dataset.fallback) img.src = img.dataset.fallback;
};

// ─── RENDER CARDS ─────────────────────────────────────────────────────────────

const displayUsers = (users) => {
  const grid = $('#user-cards-grid');
  grid.empty();

  // Update result count
  $('#user-count').text(`${users.length} Users`);
  $('#results-count').text(
      users.length === allUsers.length
          ? `${users.length} users`
          : `${users.length} of ${allUsers.length} users`
  );

  if (users.length === 0) {
    grid.append(`
      <div class="user-cards-empty">
        <i class="bi bi-search"></i>
        <p>No users match your filters.</p>
        <button class="btn btn-sm btn-outline-secondary mt-2" onclick="clearAllFilters()">
          Clear filters
        </button>
      </div>
    `);
    return;
  }

  users.forEach((user) => {
    const isValidFileId =
        user.profilePictureId &&
        /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i.test(user.profilePictureId);

    const fallbackAvatar = `https://ui-avatars.com/api/?name=${encodeURIComponent(user.username || 'User')}&background=a0616a&color=fff&size=128&bold=true`;
    const profilePicUrl = isValidFileId ? `${BASE_URL}/files/${user.profilePictureId}` : fallbackAvatar;

    const country  = countryName(user.profile?.country);
    const isAdmin  = user.admin === true || user.isAdmin === true;
    const isActive = !user.deleted;

    grid.append(`
      <div class="user-card" data-user-id="${user.id}">

        <div class="user-card__banner">
          <div class="user-card__avatar-wrap">
            <img src="${profilePicUrl}"
                 alt="${user.username}"
                 class="user-card__avatar"
                 data-fallback="${fallbackAvatar}"
                 onerror="handleImageError(this)" />
          </div>
        </div>

        <div class="user-card__body">
          <a href="../users/profile.html?id=${user.id}" class="user-card__username">
            ${user.username}
          </a>
          <div class="user-card__badges">
            ${isAdmin ? '<span class="badge bg-primary">Admin</span>' : ''}
            ${statusBadge(isActive)}
          </div>
          <div class="user-card__info">
            <div class="user-card__info-row">
              <i class="bi bi-envelope"></i>
              <span>${user.email}</span>
            </div>
            <div class="user-card__info-row">
              <i class="bi bi-geo-alt"></i>
              <span>${country}</span>
            </div>
            <div class="user-card__info-row">
              <i class="bi bi-calendar3"></i>
              <span>${formatDate(user.createdAt)}</span>
            </div>
          </div>
        </div>

        <div class="user-card__footer">
          <a href="../users/profile.html?id=${user.id}" class="user-card__btn">
            <i class="bi bi-person"></i> View Profile
          </a>
        </div>

      </div>
    `);
  });
};

// ─── COUNTRY CODE → DISPLAY NAME ─────────────────────────────────────────────

const COUNTRY_NAMES = { at: 'Österreich', de: 'Deutschland', ch: 'Schweiz' };
const countryName = (code) => COUNTRY_NAMES[code?.toLowerCase()] || code || 'N/A';

// ─── COMBINED FILTER LOGIC ────────────────────────────────────────────────────
// All filters (search, role, sort) run together so they always combine correctly.

const applyFilters = () => {
  if (!$('#searchInput').length) { displayUsers(allUsers); return; }
  const searchTerm = $('#searchInput').val().toLowerCase().trim();
  const roleFilter = $('#filterRole').val();   // '' | 'admin' | 'user'
  const sortBy     = $('#sortSelect').val();   // '' | 'username' | 'email' | 'created' | 'country' | 'status'

  let result = [...allUsers];

  // 1. Filter by search term (username, email, country)
  if (searchTerm) {
    result = result.filter((u) =>
        u.username?.toLowerCase().includes(searchTerm) ||
        u.email?.toLowerCase().includes(searchTerm) ||
        u.profile?.country?.toLowerCase().includes(searchTerm)
    );
  }

  // 2. Filter by role
  if (roleFilter === 'admin') {
    result = result.filter((u) => u.admin === true || u.isAdmin === true);
  } else if (roleFilter === 'user') {
    result = result.filter((u) => !u.admin && !u.isAdmin);
  }

  // 3. Sort — all fields null-safe with || '' fallback
  result.sort((a, b) => {
    switch (sortBy) {
      case 'username': return (a.username || '').localeCompare(b.username || '');
      case 'email':    return (a.email || '').localeCompare(b.email || '');
      case 'created':  return new Date(b.createdAt || 0) - new Date(a.createdAt || 0);
      case 'country':  return (a.profile?.country || '').localeCompare(b.profile?.country || '');
      case 'status':   return (a.deleted ? 1 : 0) - (b.deleted ? 1 : 0);
      default:         return 0;
    }
  });

  updateFilterTags(searchTerm, roleFilter, sortBy);
  displayUsers(result);
};

// ─── FILTER TAGS (visual indicators of active filters) ────────────────────────

const updateFilterTags = (searchTerm, roleFilter, sortBy) => {
  const container = $('#active-filters');
  container.empty();

  if (searchTerm) {
    container.append(filterTag(`Search: "${searchTerm}"`, () => {
      $('#searchInput').val('');
      applyFilters();
    }));
  }

  if (roleFilter) {
    const label = roleFilter === 'admin' ? 'Admins only' : 'Users only';
    container.append(filterTag(label, () => {
      $('#filterRole').val('');
      applyFilters();
    }));
  }

  if (sortBy) {
    const labels = { username: 'Sort: Username', email: 'Sort: Email', created: 'Sort: Newest', country: 'Sort: Country', status: 'Sort: Status' };
    container.append(filterTag(labels[sortBy], () => {
      $('#sortSelect').val('');
      applyFilters();
    }));
  }
};

const filterTag = (label, onRemove) => {
  const tag = $(`
    <span class="filter-tag">
      ${label}
      <button aria-label="Remove filter"><i class="bi bi-x"></i></button>
    </span>
  `);
  tag.find('button').on('click', onRemove);
  return tag;
};

window.clearAllFilters = () => {
  $('#searchInput').val('');
  $('#filterRole').val('');
  $('#sortSelect').val('');
  applyFilters();
};

// ─── INIT ─────────────────────────────────────────────────────────────────────

$(document).ready(async function () {
  console.log('User list page loaded');

  // Show skeleton cards while loading
  const skeleton = `
    <div class="user-card user-card--skeleton">
      <div class="user-card__banner"></div>
      <div class="user-card__body" style="align-items:center; gap:0.6rem;">
        <div class="skeleton-line avatar" style="margin-top:8px;"></div>
        <div class="skeleton-line short"></div>
        <div class="skeleton-line"></div>
        <div class="skeleton-line short"></div>
        <div class="skeleton-line"></div>
      </div>
      <div class="user-card__footer">
        <div class="skeleton-line" style="height:30px; border-radius:4px;"></div>
      </div>
    </div>`;
  $('#user-cards-grid').html(skeleton.repeat(6));

  const hasAccess = await checkAdminAccess();
  if (!hasAccess) return;

  await loadUsers();

  // All three controls call the same applyFilters function
  $('#searchInput').on('input', applyFilters);
  $('#sortSelect').on('change', applyFilters);
  $('#filterRole').on('change', applyFilters);
});
