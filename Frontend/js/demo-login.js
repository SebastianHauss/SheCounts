// Simple Authentication System for School Project
// Add this to your existing navbar-auth.js file or create a new JS file

// Hardcoded credentials for demo purposes
const DEMO_CREDENTIALS = {
  email: 'admin@gmail.com',
  password: 'Password123',
};

// Check if user is already logged in when page loads
document.addEventListener('DOMContentLoaded', function () {
  checkLoginStatus();
});

// Handle login form submission
document.getElementById('loginForm').addEventListener('submit', function (e) {
  e.preventDefault(); // Prevent form from submitting normally

  // Get form values
  const email = document.getElementById('loginEmailField').value.trim();
  const password = document.getElementById('loginPasswordField').value;

  // Validate credentials
  if (
    email === DEMO_CREDENTIALS.email &&
    password === DEMO_CREDENTIALS.password
  ) {
    // Login successful
    loginUser(email);

    // Close the login modal
    const loginModal = bootstrap.Modal.getInstance(
      document.getElementById('loginModal')
    );
    loginModal.hide();

    // Clear form fields
    document.getElementById('loginForm').reset();

    // Optional: Show success message
    showMessage('Login erfolgreich!', 'success');
  } else {
    // Login failed
    showMessage(
      'Ungültige Anmeldedaten. Bitte versuchen Sie es erneut.',
      'error'
    );
  }
});

// Handle logout
document.getElementById('logoutBtn').addEventListener('click', function (e) {
  e.preventDefault();
  logoutUser();
});

// Function to handle successful login
function loginUser(email) {
  // Store login status (in real app, use secure tokens)
  localStorage.setItem('isLoggedIn', 'true');
  localStorage.setItem('userEmail', email);

  // Update UI
  updateUIForLoggedInUser();
}

// Function to handle logout
function logoutUser() {
  // Clear login status
  localStorage.removeItem('isLoggedIn');
  localStorage.removeItem('userEmail');

  // Update UI
  updateUIForLoggedOutUser();

  // Optional: Show logout message
  showMessage('Sie wurden erfolgreich abgemeldet.', 'info');
}

// Function to check login status on page load
function checkLoginStatus() {
  const isLoggedIn = localStorage.getItem('isLoggedIn');

  if (isLoggedIn === 'true') {
    updateUIForLoggedInUser();
  } else {
    updateUIForLoggedOutUser();
  }
}

// Update UI when user is logged in
function updateUIForLoggedInUser() {
  // Hide login buttons
  document.getElementById('loginBlock').style.display = 'none';
  document.getElementById('login-btn-navbar').style.display = 'none';

  // Show user profile dropdown
  document.getElementById('userBlock').style.display = 'block';

  // Optional: Update profile name with user email or username
  const userEmail = localStorage.getItem('userEmail');
  if (userEmail) {
    const profileName = document.querySelector('#userBlock strong');
    // Extract username from email (part before @)
    const username = userEmail.split('@')[0];
    profileName.textContent =
      username.charAt(0).toUpperCase() + username.slice(1);
  }
}

// Update UI when user is logged out
function updateUIForLoggedOutUser() {
  // Show login buttons
  document.getElementById('loginBlock').style.display = 'block';
  document.getElementById('login-btn-navbar').style.display = 'block';

  // Hide user profile dropdown
  document.getElementById('userBlock').style.display = 'none';
}

// Function to show messages to user
function showMessage(message, type) {
  // Create alert element
  const alertDiv = document.createElement('div');
  alertDiv.className = `alert alert-${
    type === 'error' ? 'danger' : type === 'success' ? 'success' : 'info'
  } alert-dismissible fade show position-fixed`;
  alertDiv.style.top = '20px';
  alertDiv.style.right = '20px';
  alertDiv.style.zIndex = '9999';
  alertDiv.style.minWidth = '300px';

  alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;

  // Add to page
  document.body.appendChild(alertDiv);

  // Auto-remove after 4 seconds
  setTimeout(() => {
    if (alertDiv.parentNode) {
      alertDiv.remove();
    }
  }, 4000);
}

// Optional: Add some validation styling
function addValidationStyling() {
  const emailField = document.getElementById('loginEmailField');
  const passwordField = document.getElementById('loginPasswordField');

  // Add real-time validation feedback
  emailField.addEventListener('input', function () {
    if (this.value.includes('@')) {
      this.classList.remove('is-invalid');
      this.classList.add('is-valid');
    } else {
      this.classList.remove('is-valid');
      this.classList.add('is-invalid');
    }
  });

  passwordField.addEventListener('input', function () {
    if (this.value.length >= 6) {
      this.classList.remove('is-invalid');
      this.classList.add('is-valid');
    } else {
      this.classList.remove('is-valid');
      this.classList.add('is-invalid');
    }
  });
}

// Initialize validation styling when page loads
document.addEventListener('DOMContentLoaded', function () {
  addValidationStyling();
});
