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