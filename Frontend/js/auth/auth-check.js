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
