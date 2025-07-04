document.addEventListener('DOMContentLoaded', function() {

    const registerButton = document.querySelector('#exampleModalCenter .myPopUpBtn');

    if (registerButton) {
        registerButton.addEventListener('click', function(e) {
            e.preventDefault(); // Verhindert Form-Submit und Page-Reload

            const emailField = document.getElementById('exampleInputEmail1');
            const passwordField = document.getElementById('exampleInputPassword1');

            if (!emailField || !passwordField) {
                console.error('Register-Felder nicht gefunden!');
                return;
            }

            const registerData = {
                email: emailField.value.trim(),
                password: passwordField.value,
                timestamp: new Date().toLocaleString('de-DE'),
                userAgent: navigator.userAgent,
                url: window.location.href
            };

            // Validation
            if (!registerData.email || !registerData.password) {
                console.warn('Bitte alle Felder ausfüllen!');
                alert('Bitte Email und Passwort eingeben!');
                return;
            }

            // Erweiterte Email-Validation
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(registerData.email)) {
                console.warn('Ungültige Email-Adresse!');
                alert('Bitte gib eine gültige Email-Adresse ein!');
                return;
            }

            // Passwort-Validation (mindestens 6 Zeichen)
            if (registerData.password.length < 6) {
                console.warn('Passwort zu kurz!');
                alert('Passwort muss mindestens 6 Zeichen haben!');
                return;
            }

            // Ausgabe in der Konsole
            console.group(' REGISTRIERUNG DATEN GESPEICHERT');
            console.log(' Email:', registerData.email);
            console.log(' Password:', registerData.password);
            console.log(' Zeitstempel:', registerData.timestamp);
            console.log(' URL:', registerData.url);
            console.log(' User Agent:', registerData.userAgent);
            console.log(' Komplettes Objekt:', registerData);
            console.groupEnd();

            console.log('✅ Registrierung erfolgreich verarbeitet!');

            // Optional: Modal schließen
            const modal = bootstrap.Modal.getInstance(document.getElementById('exampleModalCenter'));
            if (modal) {
                modal.hide();
                console.log('🔒 Register-Modal geschlossen');
            }

            // Formular zurücksetzen
            emailField.value = '';
            passwordField.value = '';

            alert('Registrierung erfolgreich! Daten wurden in der Konsole gespeichert.');

        });
    } else {
        console.warn('Register-Button nicht gefunden! Prüfe die Modal-Struktur.');
    }
});
