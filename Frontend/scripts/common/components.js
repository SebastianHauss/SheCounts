$(document).ready(function() {

    // Prevent scroll on page load
    if ('scrollRestoration' in history) {
        history.scrollRestoration = 'manual';
    }
    window.scrollTo(0, 0);

    // Load navbar first, then initialize everything
    $('#navbar-placeholder').load('/components/navbar-home.html', function() {
        console.log('Navbar loaded successfully!');

        // 1. Re-initialize Bootstrap components (dropdowns, collapse)
        initializeNavbarComponents();

        // 2. Re-initialize Auth functionality
        initializeAuth();
        setActiveNavLink();

        // 3. Check if user is logged in
        checkAuthStatus();
    });

    // Load footer
    $('#footer-placeholder').load('/components/footer-home.html', function() {
        console.log('Footer loaded successfully!');
    });

});

// Add this at the end of components.js
function initializeRealTimeValidation() {
    console.log('Initializing real-time validation...');

    // Get all inputs and selects that need validation
    const inputs = document.querySelectorAll('.needs-validation input, .needs-validation select');

    inputs.forEach(input => {
        // Show validation on blur (when user leaves the field)
        input.addEventListener('blur', function() {
            if (this.value.trim() !== '') {
                // Only validate if field has content
                if (this.checkValidity()) {
                    this.classList.remove('is-invalid');
                    this.classList.add('is-valid');
                } else {
                    this.classList.remove('is-valid');
                    this.classList.add('is-invalid');
                }
            }
        });

        // Remove validation styling when user starts typing again
        input.addEventListener('input', function() {
            this.classList.remove('is-invalid', 'is-valid');
        });
    });

    console.log('Real-time validation initialized!');
}

// Function to re-initialize Bootstrap components
function initializeNavbarComponents() {
    console.log('Re-initializing Bootstrap components...');

    // Initialize all dropdowns
    const dropdowns = document.querySelectorAll('[data-bs-toggle="dropdown"]');
    dropdowns.forEach(dropdown => {
        new bootstrap.Dropdown(dropdown);
    });
    console.log(`Initialized ${dropdowns.length} dropdown(s)`);

    // Re-initialize Bootstrap Collapse for hamburger menu
    const navbarCollapse = document.getElementById('navbarContent');
    if (navbarCollapse) {
        new bootstrap.Collapse(navbarCollapse, {
            toggle: false  // Don't toggle immediately
        });
        console.log('Hamburger collapse initialized');
    }

    console.log('All Bootstrap components initialized!');
}

// === Highlight active navbar link (robust) ===
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

    $('#navbar-placeholder .nav-link, #navbar-placeholder .dropdown-item').each(function () {
        const href = $(this).attr('href');
        if (!href || href.startsWith('#') || href.startsWith('javascript')) {
            return;
        }

        const linkKey = normalizePathToKey(href);

        if (linkKey === currentKey) {
            $(this).addClass('active');
            $(this).attr('aria-current', 'page');
            matchFound = true;


            const dropdownParent = $(this).closest('.dropdown');
            if (dropdownParent.length) {
                const parentToggle = dropdownParent.find('> .nav-link.dropdown-toggle');
                parentToggle.addClass('active').attr('aria-current', 'page');
            }
        } else {
            $(this).removeClass('active');
            $(this).removeAttr('aria-current');
        }
    });

    if (!matchFound && currentKey === 'index') {
        $('#navbar-placeholder .nav-link').first()
            .addClass('active')
            .attr('aria-current', 'page');
    }
}

// Function to initialize authentication
function initializeAuth() {
    console.log('Initializing authentication...');

    const API_URL = 'http://localhost:8080/api/auth';

    // Attach event listeners for login
    $('#loginButton').off('click').on('click', function (e) {
        e.preventDefault();
        handleLogin(API_URL);
    });

    // Attach event listeners for logout (desktop)
    $('#logoutBtn').off('click').on('click', function (e) {
        e.preventDefault();
        handleLogout();
    });

    // Attach event listeners for logout (mobile)
    $('#logoutBtnMobile').off('click').on('click', function (e) {
        e.preventDefault();
        handleLogout();
    });

    // Attach event listeners for register
    $('#registerButton').off('click').on('click', function (e) {
        e.preventDefault();
        handleRegister(API_URL);
    });

    // Attach event listeners for password reset
    $('#resetPasswordBtn').off('click').on('click', function (e) {
        e.preventDefault();
        handlePasswordReset(API_URL);
    });

    initializeFormValidation();
    initializeRealTimeValidation();

    console.log('Authentication initialized!');
}

// adds to bootstrap frontend validation
function initializeFormValidation() {
    console.log('Initializing form validation...');

    var forms = document.querySelectorAll('.needs-validation');
    Array.prototype.slice.call(forms).forEach(function (form) {
        form.addEventListener('submit', function (event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });

    console.log('Form validation initialized!');
}

// Handle login - REAL VERSION with cookie authentication
function handleLogin(API_URL) {
    const email = $('#loginEmailField').val().trim();
    const password = $('#loginPasswordField').val();

    // Validate form first
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
            withCredentials: true  // ✅ Allow cookies
        },
        crossDomain: true,
        data: JSON.stringify({ email, password }),
        success: function (data) {
            console.log('Login successful');

            // Remove focus and close modal properly
            document.activeElement.blur();
            const modalEl = document.getElementById('loginModal');
            const loginModal = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);
            loginModal.hide();

            // Update UI to show user profile
            $('#loginBlock').hide();
            $('#userBlock').show();
            $('#mobileLoginLink').hide();
            $('#mobileUserBlock').show();

            alert('Login erfolgreich!');
        },
        error: function (xhr) {
            console.error('Login error:', xhr);
            const errorMsg = xhr.responseJSON?.message || xhr.responseText || 'Unbekannter Fehler';
            alert('Login fehlgeschlagen: ' + errorMsg);
        },
    });
}

// Handle logout - REAL VERSION
function handleLogout() {
    $.ajax({
        url: 'http://localhost:8080/api/auth/logout',
        type: 'POST',
        xhrFields: {
            withCredentials: true  // Send cookie to delete it
        },
        crossDomain: true,
        success: function () {
            console.log('Logout successful');

            // Update UI to show login button
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

// Handle register - REAL VERSION
function handleRegister(API_URL) {
    const anrede = $('#anrede').val();
    const email = $('#emailField').val().trim();
    const username = $('#usernameField').val().trim();
    const password = $('#passwordField').val();
    const repeatPassword = $('#repeatPasswordField').val();
    const country = $('#country').val();

    // Validate form first
    const registerForm = document.getElementById('registerForm');
    if (!registerForm.checkValidity()) {
        registerForm.classList.add('was-validated');
        return;
    }

    // Check password match
    if (password !== repeatPassword) {
        $('#repeatPasswordField').addClass('is-invalid');
        alert('Passwörter stimmen nicht überein!');
        return;
    }

    // Map anrede to gender
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
            withCredentials: true
        },
        data: JSON.stringify({
            email: email,
            username: username,
            password: password,
            country: country,
            gender: gender
        }),
        success: function () {
            console.log('Registration successful');

            // Remove focus and close modal properly
            document.activeElement.blur();
            const regEl = document.getElementById('registerModal');
            const regModal = bootstrap.Modal.getInstance(regEl) || new bootstrap.Modal(regEl);
            regModal.hide();

            // Update UI to show user profile
            $('#loginBlock').hide();
            $('#userBlock').show();
            $('#mobileLoginLink').hide();
            $('#mobileUserBlock').show();

            alert('Registrierung erfolgreich!');
        },
        error: function (xhr) {
            console.error('Registration error:', xhr);
            const errorMsg = xhr.responseJSON?.message || xhr.responseText || 'Unbekannter Fehler';
            alert('Registrierung fehlgeschlagen: ' + errorMsg);
        },
    });
}

// Handle password reset
function handlePasswordReset(API_URL) {
    const email = $('#resetEmailField').val().trim();

    if (!email) {
        alert('Bitte Email eingeben!');
        return;
    }

    // Close modal
    document.activeElement.blur();
    const resetEl = document.getElementById('pswZurückModal');
    const resetModal = bootstrap.Modal.getInstance(resetEl) || new bootstrap.Modal(resetEl);
    resetModal.hide();

    // Show success message
    alert('Ein Link zum Zurücksetzen des Passworts wurde an ' + email + ' gesendet.');
}

// Handle password reset - DEMO VERSION (no backend call)
function handlePasswordReset(API_URL) {
    const email = $('#resetEmailField').val().trim();

    // Validate form first
    const resetForm = document.getElementById('resetPasswordForm');
    if (!resetForm.checkValidity()) {
        resetForm.classList.add('was-validated');
        return;
    }

    if (!email) {
        alert('Bitte Email eingeben!');
        return;
    }

    // Close modal
    document.activeElement.blur();
    const resetEl = document.getElementById('pswZurückModal');
    const resetModal = bootstrap.Modal.getInstance(resetEl) || new bootstrap.Modal(resetEl);
    resetModal.hide();

    // Show success message (no actual API call)
    alert('Ein Link zum Zurücksetzen des Passworts wurde an ' + email + ' gesendet.');

    // Clear the email field for next time
    $('#resetEmailField').val('');
    resetForm.classList.remove('was-validated');
}

// Check if user is logged in via cookie
 async function checkAuthStatus() {
     try {
         const response = await fetch('http://localhost:8080/api/auth/me', {
             method: 'GET',
             credentials: 'include'  // Send cookie with request
         });

         if (response.ok) {
             // User is logged in
             $('#loginBlock').hide();
             $('#userBlock').show();
             $('#mobileLoginLink').hide();
             $('#mobileUserBlock').show();
             console.log('User is logged in');
         } else {
             // User is logged out
             $('#loginBlock').show();
             $('#userBlock').hide();
             $('#mobileLoginLink').show();
             $('#mobileUserBlock').hide();
             console.log('User is logged out');
         }
     }
     catch (error) {
         console.error('Auth check failed:', error);
         // Default to logged out state
         $('#loginBlock').show();
         $('#userBlock').hide();
         $('#mobileLoginLink').show();
         $('#mobileUserBlock').hide();
     }
 }
