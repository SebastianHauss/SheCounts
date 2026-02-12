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
