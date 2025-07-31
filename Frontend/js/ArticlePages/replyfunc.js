
document.addEventListener("DOMContentLoaded", () => {
    const postBtn = document.querySelector('.discussion-box-form button');
    const userId = document.body.dataset.userId;
    const articleId = document.body.dataset.articleId;

    postBtn.addEventListener("click", () => {
        const title = document.querySelector('input[placeholder="Titel hinzufügen"]').value;
        const text = document.querySelector('textarea[placeholder="Was sagst du dazu?"]').value;

        if (!text.trim()) return alert("Bitte Text eingeben");

        const payload = { title, text, userId, articleId }; //für test
        console.log("sende:", JSON.stringify(payload));

        fetch("http://localhost:8080/api/comments", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ title, text, userId, articleId })
        })
            .then(res => {
                if (!res.ok) {
                    return res.text().then(text => {
                        console.error("Fehlerantwort:", text);
                        throw new Error(text);
                    });
                }
                return res.json();
            })
            .then(data => {
                console.log("Kommentar gespeichert:", data);
            })
            .catch(err => {
                console.error(err);
                alert("Fehler beim Posten");
            });
    });
});

document.addEventListener("click", function (e) {
    if (e.target.classList.contains("reply-btn")) {
        console.log("Antworten clicked");

        const replyForm = document.querySelector(".reply-form");
        if (!replyForm) return;

        replyForm.classList.remove("d-none");

        replyForm.scrollIntoView({ behavior: "smooth", block: "start" });
    }
});