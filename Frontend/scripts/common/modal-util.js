document
  .querySelectorAll('[data-bs-toggle="modal"][data-bs-dismiss="modal"]')
  .forEach((link) => {
    link.addEventListener('click', function (e) {
      e.preventDefault();

      const currentModalId = this.closest('.modal').id;
      const targetModalId = this.getAttribute('data-bs-target').replace(
        '#',
        ''
      );

      const currentModal = bootstrap.Modal.getInstance(
        document.getElementById(currentModalId)
      );
      const targetModalEl = document.getElementById(targetModalId);

      currentModal.hide();

      // Wait for the modal to be completely hidden
      document
        .getElementById(currentModalId)
        .addEventListener('hidden.bs.modal', function showNextModal() {
          // Remove this event listener to prevent multiple triggers
          this.removeEventListener('hidden.bs.modal', showNextModal);

          // Clean up any leftover backdrops
          document
            .querySelectorAll('.modal-backdrop')
            .forEach((backdrop) => backdrop.remove());
          document.body.classList.remove('modal-open');
          document.body.style.overflow = '';
          document.body.style.paddingRight = '';

          // Show new modal
          const newModal = new bootstrap.Modal(targetModalEl);
          newModal.show();
        });
    });
  });
