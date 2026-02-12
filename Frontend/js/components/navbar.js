function initializeNavbarComponents() {
    const dropdowns = document.querySelectorAll('[data-bs-toggle="dropdown"]');
    dropdowns.forEach((dropdown) => {
        new bootstrap.Dropdown(dropdown);
    });

    const navbarCollapse = document.getElementById('navbarContent');
    if (navbarCollapse) {
        new bootstrap.Collapse(navbarCollapse, {
            toggle: false,
        });
    }
}

/**
 * Sets the active class on the current page's nav link
 * Also highlights parent dropdown if inside a dropdown menu
 */
function setActiveNavLink() {
    const currentKey = normalizePathToKey(window.location.pathname);
    let matchFound = false;

    $('#navbar-placeholder .nav-link, #navbar-placeholder .dropdown-item').each(
        function () {
            const href = $(this).attr('href');
            if (
                !href ||
                href.startsWith('#') ||
                href.startsWith('javascript')
            ) {
                return;
            }

            const linkKey = normalizePathToKey(href);

            if (linkKey === currentKey) {
                $(this).addClass('active');
                $(this).attr('aria-current', 'page');
                matchFound = true;

                const dropdownParent = $(this).closest('.dropdown');
                if (dropdownParent.length) {
                    const parentToggle = dropdownParent.find(
                        '> .nav-link.dropdown-toggle'
                    );
                    parentToggle
                        .addClass('active')
                        .attr('aria-current', 'page');
                }
            } else {
                $(this).removeClass('active');
                $(this).removeAttr('aria-current');
            }
        }
    );

    if (!matchFound && currentKey === 'index') {
        $('#navbar-placeholder .nav-link')
            .first()
            .addClass('active')
            .attr('aria-current', 'page');
    }
}