document.addEventListener("DOMContentLoaded", () => {
    const userId = localStorage.getItem("userId");
    const isLoggedIn = !!userId;

    // NAVBAR
    const loginBlock = document.getElementById("loginBlock");
    const userBlock = document.getElementById("userBlock");
    const adminLink = document.getElementById("adminLink");

    if (isLoggedIn) {
        loginBlock?.style.setProperty("display", "none", "important");
        userBlock?.style.setProperty("display", "block", "important");
    } else {
        loginBlock?.style.setProperty("display", "block", "important");
        userBlock?.style.setProperty("display", "none", "important");
    }

    //LOGOUT
    const logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", () => {
            localStorage.clear();
            location.reload();
        });
    }

    //REGISTRIERUNG
    const registerButton = document.querySelector('#registerModal .myPopUpBtn');
    if (registerButton) {
        registerButton.addEventListener("click", async (e) => {
            e.preventDefault();

            const emailField = document.getElementById("emailField");
            const usernameField = document.getElementById("usernameField");
            const passwordField = document.getElementById("passwordField");

            if (!emailField || !passwordField || !usernameField) {
                console.error("Register-Felder nicht gefunden!");
                return;
            }

            const email = emailField.value.trim();
            const username = usernameField.value.trim();
            const password = passwordField.value;

            if (!email || !password || !username) {
                alert("Bitte Email, Username und Passwort eingeben!");
                return;
            }

            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(email)) {
                alert("Bitte gib eine gültige Email-Adresse ein!");
                return;
            }

            if (password.length < 6) {
                alert("Passwort muss mindestens 6 Zeichen haben!");
                return;
            }

            try {
                const res = await fetch("http://localhost:8080/api/auth/register", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ email, password, username }),
                });

                if (!res.ok) {
                    const errText = await res.text();
                    throw new Error(errText || "Registrierung fehlgeschlagen");
                }

                alert("Registrierung erfolgreich!");
                const modal = bootstrap.Modal.getInstance(document.getElementById("registerModal"));
                modal?.hide();

                emailField.value = "";
                passwordField.value = "";
                usernameField.value = "";
            } catch (err) {
                console.error(err);
                alert("Fehler bei der Registrierung: " + err.message);
            }
        });
    }

    //LOGIN
    const loginButton = document.querySelector('#loginModal .myLoginBtn');
    if (loginButton) {
        loginButton.addEventListener("click", async (e) => {
            e.preventDefault();

            const emailField = document.getElementById("loginEmailField");
            const passwordField = document.getElementById("loginPasswordField");

            if (!emailField || !passwordField) {
                console.error("Login-Felder nicht gefunden!");
                return;
            }

            const email = emailField.value.trim();
            const password = passwordField.value;

            if (!email || !password) {
                alert("Bitte Email und Passwort eingeben!");
                return;
            }

            try {
                const res = await fetch("http://localhost:8080/api/auth/login", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ email, password }),
                });

                if (!res.ok) {
                    const errText = await res.text();
                    throw new Error(errText || "Login fehlgeschlagen");
                }

                const data = await res.json();
                localStorage.setItem("userId", data.userId);

                alert("Login erfolgreich!");
                const modal = bootstrap.Modal.getInstance(document.getElementById("loginModal"));
                modal?.hide();
                location.reload();
            } catch (err) {
                console.error(err);
                alert("Fehler beim Login: " + err.message);
            }
        });
    }
    // PASSWORT ZURÜCKSETZEN
    const resetBtn = document.querySelector('#pswZurückModal .myResetBtn');
    if (resetBtn) {
        resetBtn.addEventListener("click", async (e) => {
            e.preventDefault();

            const emailField = document.getElementById("resetEmailField");

            if (!emailField) {
                console.error("Reset-Feld nicht gefunden!");
                return;
            }

            const email = emailField.value.trim();
            if (!email) {
                alert("Bitte gib deine Email-Adresse ein!");
                return;
            }

            try {
                const res = await fetch("http://localhost:8080/api/auth/reset-password", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ email }),
                });

                if (!res.ok) {
                    const errText = await res.text();
                    throw new Error(errText || "Zurücksetzen fehlgeschlagen");
                }

                alert("Passwort-Zurücksetzungsanfrage wurde gesendet!");
                const modal = bootstrap.Modal.getInstance(document.getElementById("pswZurückModal"));
                modal?.hide();

                emailField.value = "";
            } catch (err) {
                console.error(err);
                alert("Fehler beim Zurücksetzen: " + err.message);
            }
        });
    }
});