/*
document.addEventListener("DOMContentLoaded", () => {
document.getElementById("loginForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    try {
        const res = await fetch("http://localhost:8080/api/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        });

        if (!res.ok) throw new Error("Login fehlgeschlagen");

        const data = await res.json();

        // зберігаєш userId (або ще й токен якщо буде)
        localStorage.setItem("userId", data.userId); // або data.id якщо так називається
        // localStorage.setItem("token", data.token); якщо треба

        // редірект
        window.location.href = "/"; // або куди тобі треба
    } catch (err) {
        alert("Fehler beim Login: " + err.message);
    }
});
});*/
