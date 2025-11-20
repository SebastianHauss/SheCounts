// components.js

$(document).ready(function() {

    // Load navbar first, then initialize everything
    $('#navbar-placeholder').load('/components/navbar-home.html', function() {
        console.log('Navbar loaded successfully!');

        // 1. Re-initialize Bootstrap components (dropdowns, collapse)
        initializeNavbarComponents();

        // 2. Re-initialize Auth functionality
        initializeAuth();
        setActiveNavLink();
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

    console.log('✅ All Bootstrap components initialized!');
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

    // Update navbar based on login state
    updateNavbar();

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

    console.log('Authentication initialized!');
}

// Helper: Update navbar visibility based on login state
function updateNavbar() {
    console.log('Updating navbar...');
    const userId = localStorage.getItem('userId');
    console.log('userId =', userId);

    if (userId && userId.trim() !== '') {
        // User is logged in - show profile, hide login
        $('#loginBlock').hide();
        $('#userBlock').show();
        $('#mobileLoginLink').hide();
        $('#mobileUserBlock').show();
        console.log('User is logged in');
    } else {
        // User is logged out - show login, hide profile
        $('#loginBlock').show();
        $('#userBlock').hide();
        $('#mobileLoginLink').show();
        $('#mobileUserBlock').hide();
        console.log('User is logged out');
    }
}

// Handle login
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
        data: JSON.stringify({ email, password }),
        success: function (data) {
            localStorage.setItem('userId', data);
            alert('Login erfolgreich!');
            $('#loginModal').modal('hide');
            updateNavbar();
            $('.modal-backdrop').remove();
            $('body').removeClass('modal-open');
        },
        error: function (xhr) {
            alert('Login fehlgeschlagen: ' + xhr.responseText);
        },
    });
}

// Handle logout
function handleLogout() {
    localStorage.removeItem('userId');
    updateNavbar();
    alert('Logout erfolgreich!');
}

// Handle register
function handleRegister(API_URL) {
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
            $('.modal-backdrop').remove();
            $('body').removeClass('modal-open');
        },
        error: function (xhr) {
            alert('Registrierung fehlgeschlagen: ' + xhr.responseText);
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
            alert('Zurücksetzen erfolgreich!');
            $('#pswZurückModal').modal('hide');
            $('.modal-backdrop').remove();
            $('body').removeClass('modal-open');
        },
        error: function (xhr) {
            alert('Fehler beim Zurücksetzen: ' + xhr.responseText);
        },
    });
}