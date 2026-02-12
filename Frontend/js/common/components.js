console.log("Loaded components.js");

$(document).ready(function () {
    // Prevent scroll on page load
    if ('scrollRestoration' in history) {
        history.scrollRestoration = 'manual';
    }
    window.scrollTo(0, 0);

    $('#navbar-placeholder').load('/components/navbar-home.html', function () {
        initializeNavbarComponents();
        initializeAuth();
        setActiveNavLink();
        checkAuthStatus();
    });

    $('#footer-placeholder').load('/components/footer-home.html');
});

function initializeRealTimeValidation() {
    const inputs = document.querySelectorAll(
        '.needs-validation input, .needs-validation select'
    );

    inputs.forEach((input) => {
        input.addEventListener('blur', function () {
            if (this.value.trim() !== '') {
                if (this.checkValidity()) {
                    this.classList.remove('is-invalid');
                    this.classList.add('is-valid');
                } else {
                    this.classList.remove('is-valid');
                    this.classList.add('is-invalid');
                }
            }
        });

        input.addEventListener('input', function () {
            this.classList.remove('is-invalid', 'is-valid');
        });
    });
}

function initializeNavbarComponents() {
    const dropdowns = document.querySelectorAll('[data-bs-toggle="dropdown"]');
    dropdowns.forEach((dropdown) => {
        new bootstrap.Dropdown(dropdown);
    });

    const navbarCollapse = document.getElementById('navbarContent');
    if (navbarCollapse) {
        new bootstrap.Collapse(navbarCollapse, {
            toggle: false,
        });
    }
}

function normalizePathToKey(path) {
    if (!path) return 'index';
    let p = path.split('#')[0].split('?')[0];

    try {
        if (p.startsWith('http')) {
            p = new URL(p).pathname;
        }
    } catch (e) {}

    p = p.replace(/\/$/, '');

    if (p === '' || p === '/') return 'index';

    const segments = p.split('/');
    const last = segments[segments.length - 1] || '';

    if (last === '' || last === '/') return 'index';

    return last.replace(/\.html$/i, '');
}

function setActiveNavLink() {
    const currentKey = normalizePathToKey(window.location.pathname);
    let matchFound = false;

    $('#navbar-placeholder .nav-link, #navbar-placeholder .dropdown-item').each(
        function () {
            const href = $(this).attr('href');
            if (
                !href ||
                href.startsWith('#') ||
                href.startsWith('javascript')
            ) {
                return;
            }

            const linkKey = normalizePathToKey(href);

            if (linkKey === currentKey) {
                $(this).addClass('active');
                $(this).attr('aria-current', 'page');
                matchFound = true;

                const dropdownParent = $(this).closest('.dropdown');
                if (dropdownParent.length) {
                    const parentToggle = dropdownParent.find(
                        '> .nav-link.dropdown-toggle'
                    );
                    parentToggle
                        .addClass('active')
                        .attr('aria-current', 'page');
                }
            } else {
                $(this).removeClass('active');
                $(this).removeAttr('aria-current');
            }
        }
    );

    if (!matchFound && currentKey === 'index') {
        $('#navbar-placeholder .nav-link')
            .first()
            .addClass('active')
            .attr('aria-current', 'page');
    }
}

function initializeAuth() {
    const API_URL = 'http://localhost:8080/api/auth';

    $('#loginButton')
        .off('click')
        .on('click', function (e) {
            e.preventDefault();
            handleLogin(API_URL);
        });

    $('#logoutBtn')
        .off('click')
        .on('click', function (e) {
            e.preventDefault();
            handleLogout();
        });

    $('#logoutBtnMobile')
        .off('click')
        .on('click', function (e) {
            e.preventDefault();
            handleLogout();
        });

    $('#registerButton')
        .off('click')
        .on('click', function (e) {
            e.preventDefault();
            handleRegister(API_URL);
        });

    $('#resetPasswordBtn')
        .off('click')
        .on('click', function (e) {
            e.preventDefault();
            handlePasswordReset(API_URL);
        });

    initializeFormValidation();
    initializeRealTimeValidation();
}

function initializeFormValidation() {
    const forms = document.querySelectorAll('.needs-validation');
    Array.prototype.slice.call(forms).forEach(function (form) {
        form.addEventListener(
            'submit',
            function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            },
            false
        );
    });
}

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
            console.error('Login failed:', xhr);
            const errorMsg =
                xhr.responseJSON?.message ||
                xhr.responseText ||
                'Unbekannter Fehler';
            alert('Login fehlgeschlagen: ' + errorMsg);
        },
    });
}

function handleLogout() {
    $.ajax({
        url: 'http://localhost:8080/api/auth/logout',
        type: 'POST',
        xhrFields: {
            withCredentials: true,
        },
        crossDomain: true,
        success: function () {
            window.dispatchEvent(new Event('authChanged'));
            window.location.reload();

            $('#loginBlock').show();
            $('#userBlock').hide();
            $('#mobileLoginLink').show();
            $('#mobileUserBlock').hide();

            alert('Logout erfolgreich!');
        },
        error: function (xhr) {
            console.error('Logout failed:', xhr.responseText);
            alert('Logout fehlgeschlagen');
        },
    });
}

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

function handlePasswordReset(API_URL) {
    const email = $('#resetEmailField').val().trim();

    const resetForm = document.getElementById('resetPasswordForm');
    if (!resetForm.checkValidity()) {
        resetForm.classList.add('was-validated');
        return;
    }

    if (!email) {
        alert('Bitte Email eingeben!');
        return;
    }

    document.activeElement.blur();
    const resetEl = document.getElementById('pswZurückModal');
    const resetModal =
        bootstrap.Modal.getInstance(resetEl) || new bootstrap.Modal(resetEl);
    resetModal.hide();

    alert(
        'Ein Link zum Zurücksetzen des Passworts wurde an ' +
            email +
            ' gesendet.'
    );

    $('#resetEmailField').val('');
    resetForm.classList.remove('was-validated');
}
