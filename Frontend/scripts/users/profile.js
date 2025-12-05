document.addEventListener('DOMContentLoaded', function () {
  const form = document.getElementById('profileForm');
  const fieldset = document.getElementById('formFieldset');
  const editButton = document.querySelector('.edit-profile-btn');
  const saveButton = document.querySelector('.mySubBtn');
  const cancelButton = document.querySelector('.myCancelBtn');

  // Profil bearbeiten: Felder aktivieren
  editButton.addEventListener('click', function (e) {
    e.preventDefault();
    fieldset.removeAttribute('disabled');
    console.log('Profil-Bearbeitung aktiviert');
  });

  // Stornieren: wieder sperren + Fehler zurücksetzen
  cancelButton.addEventListener('click', function (e) {
    e.preventDefault();
    fieldset.setAttribute('disabled', 'disabled');
    form.classList.remove('was-validated');
    form.reset();
    console.log('Änderungen storniert');
  });

  // Bootstrap-Validierung beim Absenden
  form.addEventListener(
      'submit',
      function (event) {
        // Eigentliche Form-Submission verhindern (wir machen es per JS)
        event.preventDefault();
        event.stopPropagation();

        // Browser-API zur Validierung nutzen
        if (!form.checkValidity()) {
          // ungültig -> Bootstrap zeigt .invalid-feedback an
          form.classList.add('was-validated');
          // wichtig: Feldset NICHT deaktivieren, sonst sind Felder für Validierung "weg"
          console.log('Formular ist UNGÜLTIG');
          return;
        }

        // gültig
        form.classList.add('was-validated');
        console.log('Formular ist GÜLTIG');

        // Daten sammeln
        const profileData = {
          username: document.getElementById('username').value,
          email: document.getElementById('exampleInputEmail1').value,
          password: document.getElementById('exampleInputPassword1').value,
          repeatPassword: document.getElementById('exampleInputPassword2').value,
          birthday: document.getElementById('birthday').value,
          gender:
              document.querySelector('input[name="gender"]:checked')?.value ||
              null,
          country: document.getElementById('country').value,
        };

        console.log('JSON:', JSON.stringify(profileData, null, 2));

        // TODO: hier dein AJAX/fetch an den Spring-Boot-Endpoint
        // fetch('/api/profile', { method: 'POST', headers: {...}, body: JSON.stringify(profileData) })

        // Nach erfolgreichem Speichern: Feldset wieder deaktivieren
        fieldset.setAttribute('disabled', 'disabled');
      },
      false
  );
});

