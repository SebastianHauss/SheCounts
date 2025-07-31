document.addEventListener('DOMContentLoaded', function() {

    const registerButton = document.querySelector('#registerModal .myPopUpBtn');

    if (registerButton) {
        registerButton.addEventListener('click', async function(e) {
            e.preventDefault();

            const emailField = document.getElementById('emailField');
            const usernameField = document.getElementById('usernameField');
            const passwordField = document.getElementById('passwordField');

            if (!emailField || !passwordField || !usernameField) {
                console.error('Register-Felder nicht gefunden!');
                return;
            }

            const email = emailField.value.trim();
            const username = usernameField.value.trim();
            const password = passwordField.value;

            if (!email || !password || !username) {
                alert('Bitte Email, Username und Passwort eingeben!');
                return;
            }

            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(email)) {
                alert('Bitte gib eine gültige Email-Adresse ein!');
                return;
            }

            if (password.length < 6) {
                alert('Passwort muss mindestens 6 Zeichen haben!');
                return;
            }

            try {
                const res = await fetch("http://localhost:8080/api/auth/register", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ email, password, username })
                });

                if (!res.ok) {
                    const errText = await res.text();
                    throw new Error(errText || "Registrierung fehlgeschlagen");
                }

                const data = await res.json();
                alert("Registrierung erfolgreich!");

                // Optional: Modal schließen
                const modal = bootstrap.Modal.getInstance(document.getElementById('registerModal'));
                if (modal) modal.hide();

                emailField.value = '';
                passwordField.value = '';

            } catch (err) {
                console.error(err);
                alert("Fehler bei der Registrierung: " + err.message);
            }
        });
    } else {
        console.warn('Register-Button nicht gefunden! Prüfe die Modal-Struktur.');
    }
});
