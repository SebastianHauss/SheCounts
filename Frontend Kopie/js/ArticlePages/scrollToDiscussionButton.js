document.addEventListener('DOMContentLoaded', function () {
    const button = document.getElementById('floating-button');
    const target = document.getElementById('diskussion');

    if (!button || !target) return;

    window.addEventListener('scroll', function () {
        const rect = target.getBoundingClientRect();

        if (rect.top < window.innerHeight) {
            button.style.opacity = '0';
            button.style.pointerEvents = 'none';
        } else {
            button.style.opacity = '1';
            button.style.pointerEvents = 'auto';
        }
    });
});