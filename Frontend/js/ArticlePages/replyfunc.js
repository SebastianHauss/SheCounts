document.addEventListener("click", function (e) {
    if (e.target.classList.contains("reply-btn")) {
        console.log("Antworten clicked");

        const replyForm = document.querySelector(".reply-form");
        if (!replyForm) return;

        // показуємо форму, якщо вона схована
        replyForm.classList.remove("d-none");

        // скролимо до форми
        replyForm.scrollIntoView({ behavior: "smooth", block: "start" });
    }
});