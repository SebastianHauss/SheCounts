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
