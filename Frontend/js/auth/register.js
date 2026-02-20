function handleRegister(API_URL) {
    const anrede = $('#anrede').val();
    const email = $('#emailField').val().trim();
    const username = $('#usernameField').val().trim();
    const password = $('#passwordField').val();
    const repeatPassword = $('#repeatPasswordField').val();
    const country = $('#register-country').val();

    const registerForm = document.getElementById('registerForm');
    if (!registerForm.checkValidity()) {
        registerForm.classList.add('was-validated');
        return;
    }

    if (password !== repeatPassword) {
        $('#repeatPasswordField').addClass('is-invalid');
        alert('Passwörter stimmen nicht überein!');
        return;
    }

    let gender = 'diverse';
    if (anrede === 'frau') {
        gender = 'female';
    } else if (anrede === 'herr') {
        gender = 'male';
    }

    $.ajax({
        url: `${API_URL}/register`,
        type: 'POST',
        contentType: 'application/json',
        xhrFields: {
            withCredentials: true,
        },
        data: JSON.stringify({
            email: email,
            username: username,
            password: password,
            country: country,
            gender: gender,
        }),
        success: async function (data) {
            window.dispatchEvent(new Event('authChanged'));

            document.activeElement.blur();
            const modalEl = document.getElementById('loginModal');
            const loginModal =
                bootstrap.Modal.getInstance(modalEl) ||
                new bootstrap.Modal(modalEl);
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
            console.error('Registration failed:', xhr);
            const errorMsg =
                xhr.responseJSON?.message ||
                xhr.responseText ||
                'Unbekannter Fehler';
            alert('Registrierung fehlgeschlagen: ' + errorMsg);
        },
    });
}
