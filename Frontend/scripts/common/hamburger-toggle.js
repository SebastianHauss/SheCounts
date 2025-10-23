document.addEventListener('DOMContentLoaded', function () {
    const toggler = document.querySelector('.navbar-toggler');
    const nav = document.getElementById('navbarContent');

    toggler?.addEventListener('click', function () {
        if (nav.classList.contains('show')) {
            nav.classList.remove('show');
        } else {
            nav.classList.add('show');
        }
    });
});

