function initializeRealTimeValidation() {
    const inputs = document.querySelectorAll(
        '.needs-validation input, .needs-validation select'
    );

    inputs.forEach((input) => {
        input.addEventListener('blur', function () {
            if (this.value.trim() !== '') {
                if (this.checkValidity()) {
                    this.classList.remove('is-invalid');
                    this.classList.add('is-valid');
                } else {
                    this.classList.remove('is-valid');
                    this.classList.add('is-invalid');
                }
            }
        });

        input.addEventListener('input', function () {
            this.classList.remove('is-invalid', 'is-valid');
        });
    });
}



function initializeFormValidation() {
    const forms = document.querySelectorAll('.needs-validation');
    Array.prototype.slice.call(forms).forEach(function (form) {
        form.addEventListener(
            'submit',
            function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            },
            false
        );
    });
}