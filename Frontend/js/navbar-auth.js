const isLoggedIn = true; //Nur zu Testzwecken – wird später durch Backend ersetzt
const isAdmin = true; /*Auch nur zum Testen – Admin-Rolle kommt später vom Backend*/

document.addEventListener("DOMContentLoaded", () => {
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

    if (isAdmin && adminLink) {
        adminLink.style.setProperty("display", "block", "important");
    }
});