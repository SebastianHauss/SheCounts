// Тестовий логін
const isLoggedIn = true;
const loggedInUsername = "anna.gruber"; // <- заміни на динамічне, коли буде бекенд

document.addEventListener("DOMContentLoaded", () => {
    const loginBlock = document.getElementById("loginBlock");
    const userBlock = document.getElementById("userBlock");
    const userAvatar = document.getElementById("userAvatar");
    const userName = document.getElementById("navbarUserName");

    console.log("loginBlock:", loginBlock);
    console.log("userBlock:", userBlock);

    if (isLoggedIn) {
        loginBlock?.classList.add("d-none");
        userBlock?.classList.remove("d-none");

        const user = users.find(u => u.username === loggedInUsername);
        if (user) {
            userAvatar.src = user.avatar;
            userName.textContent = user.username;
        }
    } else {
        loginBlock?.classList.remove("d-none");
        userBlock?.classList.add("d-none");
    }
});