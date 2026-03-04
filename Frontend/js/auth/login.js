function handleLogin(API_URL) {
  const email = $('#loginEmailField').val().trim();
  const password = $('#loginPasswordField').val();

  const loginForm = document.getElementById('loginForm');
  if (!loginForm.checkValidity()) {
    loginForm.classList.add('was-validated');
    return;
  }

  $.ajax({
    url: `${API_URL}/login`,
    type: 'POST',
    contentType: 'application/json',
    xhrFields: {
      withCredentials: true,
    },
    crossDomain: true,
    data: JSON.stringify({ email, password }),
    success: async function (data) {
      window.dispatchEvent(new Event('authChanged'));

      document.activeElement.blur();
      const modalEl = document.getElementById('loginModal');
      const loginModal =
        bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);
      loginModal.hide();

      $('#loginBlock').hide();
      $('#userBlock').show();
      $('#mobileLoginLink').hide();
      $('#mobileUserBlock').show();

      if (data.userId) {
        await loadAndUpdateUserProfile(data.userId);
      }

      alert('Login erfolgreich!');
      window.location.reload();
    },
    error: function (xhr) {
      console.error('Login failed:', xhr);
      const errorMsg =
        xhr.responseJSON?.message || xhr.responseText || 'Unbekannter Fehler';
      alert('Login fehlgeschlagen: ' + errorMsg);
    },
  });
}

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
