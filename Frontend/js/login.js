$(document).ready(function () {
  const API_URL = 'http://localhost:8080/api/auth';

  // Helper: switch navbar blocks
  function updateNavbar() {
    console.log('updateNavbar called');
    const userId = localStorage.getItem('userId');
    console.log('userId =', userId);

    if (userId && userId.trim() !== '') {
      $('#loginBlock').hide();
      $('#userBlock').show();
    } else {
      $('#loginBlock').show();
      $('#userBlock').hide();
    }
  }

  // Initial state
  updateNavbar();

  // LOGIN
  $('#loginButton').on('click', function (e) {
    e.preventDefault();

    const email = $('#loginEmailField').val().trim();
    const password = $('#loginPasswordField').val();

    if (!email || !password) {
      alert('Bitte Email und Passwort eingeben!');
      return;
    }

    $.ajax({
      url: `${API_URL}/login`,
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify({ email, password }),
      success: function (data) {
        localStorage.setItem('userId', data);
        alert('Login erfolgreich!');
        $('#loginModal').modal('hide');
        updateNavbar();
        $('#loginModal').on('hidden.bs.modal', function () {
          $('.modal-backdrop').remove();
        });
      },
      error: function (xhr) {
        alert('Login fehlgeschlagen: ' + xhr.responseText);
      },
    });
  });

  // LOGOUT
  $('#logoutBtn').on('click', function (e) {
    e.preventDefault();
    localStorage.removeItem('userId');
    updateNavbar();
  });

  // REGISTER
  $('#registerButton').on('click', function (e) {
    e.preventDefault();

    const email = $('#emailField').val().trim();
    const username = $('#usernameField').val().trim();
    const password = $('#passwordField').val();

    if (!email || !username || !password) {
      alert('Bitte alle Felder ausfüllen!');
      return;
    }

    $.ajax({
      url: `${API_URL}/register`,
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify({ email, username, password }),
      success: function () {
        alert('Registrierung erfolgreich!');
        $('#registerModal').modal('hide');
        $('#emailField, #usernameField, #passwordField').val('');
        $('#registerModal').on('hidden.bs.modal', function () {
          $('.modal-backdrop').remove();
        });
      },
      error: function (xhr) {
        alert('Registrierung fehlgeschlagen: ' + xhr.responseText);
      },
    });
  });

  // RESET PASSWORD
  $('#resetPasswordBtn').on('click', function (e) {
    e.preventDefault();

    const email = $('#InputEmail1').val().trim();
    if (!email) {
      alert('Bitte Email eingeben!');
      return;
    }

    $.ajax({
      url: `${API_URL}/reset-password`,
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify({ email }),
      success: function () {
        alert('Zurücksetzen erfolgreich!');
        $('#pswZurückModal').modal('hide');
        $('#pswZurückModal').on('hidden.bs.modal', function () {
          $('.modal-backdrop').remove();
        });
      },
      error: function (xhr) {
        alert('Fehler beim Zurücksetzen: ' + xhr.responseText);
      },
    });
  });
});
