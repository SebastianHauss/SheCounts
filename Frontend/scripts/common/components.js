$(document).ready(function() {

    // Load navbar first, then initialize everything
    $('#navbar-placeholder').load('/components/navbar-home.html', function() {
        console.log('Navbar loaded successfully!');

        // 1. Re-initialize Bootstrap components (dropdowns, collapse)
        initializeNavbarComponents();

        // 2. Re-initialize Auth functionality
        initializeAuth();

        // 3. Check if user is logged in
        refreshAuthUI();
    });

    // Load footer
    $('#footer-placeholder').load('/components/footer-home.html', function() {
        console.log('Footer loaded successfully!');
    });

});

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

// Function to initialize authentication
function initializeAuth() {
    console.log('Initializing authentication...');

    const API_URL = 'http://localhost:8080/api/auth';

    // Attach event listeners for login
    $('#loginButton').off('click').on('click', function (e) {
        e.preventDefault();
        handleLogin(API_URL);
    });

    // Attach event listeners for logout (desktop & mobile)
    $('#logoutBtn, #logoutBtnMobile').off('click').on('click', function (e) {
        e.preventDefault();
        handleLogout(API_URL);
    });

    // Attach event listeners for register
    $('#registerForm').off('submit').on('submit', function (e) {
        e.preventDefault();
        handleRegister(API_URL);
    });

    // Attach event listeners for password reset
    $('#resetPasswordBtn').off('click').on('click', function (e) {
        e.preventDefault();
        handlePasswordReset(API_URL);
    });

    console.log('Authentication initialized!');
}

// Check if user is logged in via cookie
async function refreshAuthUI() {
    console.log('Checking authentication status...');

    try {
        const response = await fetch('http://localhost:8080/api/auth/me', {
            method: 'GET',
            credentials: 'include'  // Send cookie with request
        });

        if (response.ok) {
            // User is logged in
            const data = await response.json();
            console.log('User is logged in');

            $('#loginBlock').hide();
            $('#userBlock').show();
            $('#mobileLoginLink').hide();
            $('#mobileUserBlock').show();

        } else {
            // User is logged out
            console.log('User is not logged in');

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

// Handle login with cookie
function handleLogin(API_URL) {
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
        xhrFields: {
            withCredentials: true  // Allow cookies
        },
        crossDomain: true,
        data: JSON.stringify({ email, password }),
        success: function (data) {
            window.location.reload();
            console.log('Login successful');
            $('#loginModal').modal('hide');
            $('.modal-backdrop').remove();
            $('body').removeClass('modal-open');

            // Update UI to show logged-in state
            refreshAuthUI();
        },
        error: function (xhr) {
            console.error(' Login failed:', xhr.responseText);

        },
    });
}

// Handle logout with cookie deletion
function handleLogout(API_URL) {
    $.ajax({
        url: `${API_URL}/logout`,
        type: 'POST',
        xhrFields: {
            withCredentials: true  // CRITICAL: Send cookie to delete it
        },
        crossDomain: true,
        success: function () {
            console.log('Logout successful');
            window.location.reload();

            // Update UI to show logged-out state
            refreshAuthUI();
        },
        error: function (xhr) {
            console.error('Logout failed:', xhr.responseText);

        },
    });
}

// Handle register with cookie
function handleRegister(API_URL) {
    const anrede = $('#anrede').val();
    const email = $('#emailField').val().trim();
    const username = $('#usernameField').val().trim();
    const password = $('#passwordField').val();
    const repeatPassword = $('#repeatPasswordField').val();
    const country = $('#country').val();

    if (password !== repeatPassword) {
        // Show error feedback for password mismatch
        $('#repeatPasswordField').addClass('is-invalid');
        return;
    }

    let gender = 'diverse';  // default
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
            withCredentials: true  // CRITICAL: Allow cookies
        },
        crossDomain: true,
        data: JSON.stringify({
            email: email,
            username: username,
            password: password,
            country: country,
            gender: gender
        }),
        success: function () {
            window.location.reload();
            console.log('Registration successful');

            // Clear form fields
            $('#anrede').val('');
            $('#emailField').val('');
            $('#usernameField').val('');
            $('#passwordField').val('');
            $('#repeatPasswordField').val('');
            $('#country').val('');

            // Remove validation classes
            $('#registerForm').removeClass('was-validated');
            $('#registerForm input, #registerForm select').removeClass('is-valid is-invalid');

            // Close modal
            $('#registerModal').modal('hide');
            $('.modal-backdrop').remove();
            $('body').removeClass('modal-open');

            // Update UI to show logged-in state
            refreshAuthUI();
        },
        error: function (xhr) {
            console.error('Registration failed:', xhr.responseText);
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

    $.ajax({
        url: `${API_URL}/reset-password`,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ email }),
        success: function () {

            $('#pswZurückModal').modal('hide');
            $('.modal-backdrop').remove();
            $('body').removeClass('modal-open');
        },
        error: function (xhr) {
            alert('Fehler beim Zurücksetzen: ' + xhr.responseText);
        },
    });
}