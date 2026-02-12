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