const userId = localStorage.getItem("userId");
const isLoggedIn = !!userId;

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

    // TODO: додати логіку для перевірки isAdmin, коли буде бекенд-підтримка
});