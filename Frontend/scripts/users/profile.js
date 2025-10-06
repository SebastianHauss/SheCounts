document.addEventListener('DOMContentLoaded', function () {
  const editButton = document.querySelector('.edit-profile-btn');
  const saveButton = document.querySelector('.mySubBtn');
  const fieldset = document.querySelector('fieldset');

  // Profil bearbeiten Button
  editButton.addEventListener('click', function (e) {
    e.preventDefault();
    fieldset.disabled = false; // Felder aktivieren
    console.log('Profil-Bearbeitung aktiviert');
  });

  // Speichern Button
  saveButton.addEventListener('click', function (e) {
    e.preventDefault();

    // Debug: Prüfe ob Felder gefunden werden
    console.log('Debug - Felder gefunden:');
    console.log('Username Feld:', document.getElementById('username'));
    console.log('Email Feld:', document.getElementById('exampleInputEmail1'));

    // Alle Daten sammeln
    const username = document.getElementById('username').value;
    const email = document.getElementById('exampleInputEmail1').value;
    const password = document.getElementById('exampleInputPassword1').value;
    const repeatPassword = document.getElementById(
      'exampleInputPassword2'
    ).value;
    const birthday = document.getElementById('birthday').value;
    const gender =
      document.querySelector('input[name="gender"]:checked')?.value ||
      'nicht ausgewählt';
    const country = document.getElementById('country').value;

    // Einzeln in Console loggen
    console.log('=== PROFIL DATEN ===');
    console.log('Username:', username);
    console.log('Email:', email);
    console.log('Password:', password);
    console.log('Repeat Password:', repeatPassword);
    console.log('Birthday:', birthday);
    console.log('Gender:', gender);
    console.log('Country:', country);

    // Als JSON loggen
    const profileData = {
      username: username,
      email: email,
      password: password,
      repeatPassword: repeatPassword,
      birthday: birthday,
      gender: gender,
      country: country,
    };

    console.log('JSON:', JSON.stringify(profileData, null, 2));
    console.log('==================');

    fieldset.disabled = true;
  });
});
