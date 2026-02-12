console.log("Loaded create-article.js");

const API_URL = 'http://localhost:8080/api/articles';
const AUTH_URL = 'http://localhost:8080/api/auth/me';

let currentUser = null;

function loadUser() {
  $.ajax({
    url: AUTH_URL,
    method: 'GET',
    xhrFields: { withCredentials: true },
    success: function (response) {
      currentUser = response.username;
      console.log('Logged in as:', currentUser);
    },
    error: function () {
      currentUser = null;
      console.warn('User not logged in');
    },
  });
}

function initQuill() {
  return new Quill('#editor-container', {
    theme: 'snow',
    placeholder: 'Write your article here...',
    modules: {
      toolbar: [
        [{ font: [] }],
        [{ size: [] }],
        ['bold', 'italic', 'underline', 'strike'],
        [{ color: [] }, { background: [] }],
        [{ header: [1, 2, 3, 4, 5, 6, false] }],
        [{ list: 'ordered' }, { list: 'bullet' }],
        ['blockquote', 'code-block'],
        ['link', 'image', 'video'],
        ['clean'],
      ],
    },
  });
}

function showMessage(text, type) {
  $('#message').html(`
    <div class="alert alert-${type}">
      ${text}
    </div>
  `);
}

$(document).ready(function () {
  const quill = initQuill();
  loadUser();

  $('#submit-article').click(function () {
    const title = $('#article-title').val().trim();
    const content = quill.root.innerHTML;

    if (!title || !content || content === '<p><br></p>') {
      return showMessage('Please enter a title and content.', 'warning');
    }

    if (!currentUser) {
      return showMessage('You must be logged in to publish.', 'danger');
    }

    $.ajax({
      url: API_URL,
      method: 'POST',
      contentType: 'application/json',
      xhrFields: { withCredentials: true },
      data: JSON.stringify({
        author: currentUser,
        title: title,
        content: content,
      }),
      success: function (response) {
        showMessage(`Article created! ID: ${response.id}`, 'success');
        $('#article-title').val('');
        quill.setContents([]);
      },
      error: function () {
        showMessage('Error sending article.', 'danger');
      },
    });
  });
});
