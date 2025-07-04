
document.addEventListener("click", function (e) {
    if (e.target.classList.contains("reply-btn")) {
        console.log("Antworten clicked");

        const parentContainer = e.target.closest(".comment-container");

        if (!parentContainer) return;

        const targetInsertBlock = parentContainer.querySelector(".flex-grow-1");
        if (!targetInsertBlock) return;

        const replyHTML = `
        <div class="d-flex position-relative comment-container ms-4 mt-3">
            <div class="avatar">
                <img src="/img/profilbild.png" class="rounded-circle" width="40" height="40">
            </div>
            <div class="ms-3 flex-grow-1 p-3 border rounded position-relative">
                <div class="small text-muted">username<br>timestamp</div>
                <div class="mt-2">New reply text...</div>
                <div class="d-flex justify-content-between mt-3">
                    <span><i class="bi bi-chat-left"></i> 0</span>
                    <button class="btn btn-light fw-bold reply-btn">Antworten</button>
                </div>
            </div>
        </div>`;

        targetInsertBlock.insertAdjacentHTML("beforeend", replyHTML);
    }
});