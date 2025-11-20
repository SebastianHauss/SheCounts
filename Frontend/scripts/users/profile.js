// document.addEventListener('DOMContentLoaded', function () {
//   const editButton = document.querySelector('.edit-profile-btn');
//   const saveButton = document.querySelector('.mySubBtn');
//   const fieldset = document.querySelector('fieldset');
//
//   // Profil bearbeiten Button
//   editButton.addEventListener('click', function (e) {
//     e.preventDefault();
//     fieldset.disabled = false; // Felder aktivieren
//     // fieldset.removeAttribute('disabled');
//     console.log('Profil-Bearbeitung aktiviert');
//   });
//
//   // Speichern Button
//   saveButton.addEventListener('click', function (e) {
//     e.preventDefault();
//
//     // Debug: Prüfe ob Felder gefunden werden
//     console.log('Debug - Felder gefunden:');
//     console.log('Username Feld:', document.getElementById('username'));
//     console.log('Email Feld:', document.getElementById('exampleInputEmail1'));
//
//     // Alle Daten sammeln
//     const username = document.getElementById('username').value;
//     const email = document.getElementById('exampleInputEmail1').value;
//     const password = document.getElementById('exampleInputPassword1').value;
//     const repeatPassword = document.getElementById(
//       'exampleInputPassword2'
//     ).value;
//     const birthday = document.getElementById('birthday').value;
//     const gender =
//       document.querySelector('input[name="gender"]:checked')?.value ||
//       'nicht ausgewählt';
//     const country = document.getElementById('country').value;
//
//     // Einzeln in Console loggen
//     console.log('=== PROFIL DATEN ===');
//     console.log('Username:', username);
//     console.log('Email:', email);
//     console.log('Password:', password);
//     console.log('Repeat Password:', repeatPassword);
//     console.log('Birthday:', birthday);
//     console.log('Gender:', gender);
//     console.log('Country:', country);
//
//     // Als JSON loggen
//     const profileData = {
//       username: username,
//       email: email,
//       password: password,
//       repeatPassword: repeatPassword,
//       birthday: birthday,
//       gender: gender,
//       country: country,
//     };
//
//     console.log('JSON:', JSON.stringify(profileData, null, 2));
//     console.log('==================');
//
//     fieldset.disabled = true;
//   });
// });

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

  // Stornieren: wieder sperren + Fehler zurücksetzen (optional)
  cancelButton.addEventListener('click', function (e) {
    e.preventDefault();
    fieldset.setAttribute('disabled', 'disabled');
    form.classList.remove('was-validated');
    form.reset(); // wenn du ursprüngliche Werte laden willst, hier anpassen
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

        // Daten einsammeln
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

        // Danach optional wieder sperren:
        fieldset.setAttribute('disabled', 'disabled');
      },
      false
  );
});

