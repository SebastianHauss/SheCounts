$(document).ready(function () {
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
