const API_URL = 'http://localhost:8080/api';

$(document).ready(function () {
  const articleId = getArticleIdFromPage();

  if (articleId) {
    loadComments(articleId);
  }

  $('#post-button').on('click', function () {
    createComment(articleId);
  });
});

function loadComments(articleId) {
  $.ajax({
    url: `${API_URL}/comments?articleId=${articleId}`,
    type: 'GET',
    xhrFields: { withCredentials: true },
    success: function (comments) {
      renderComments(comments);
      updateCommentCount(comments.length);
    },
    error: function (xhr) {
      console.error('Error loading comments:', xhr);
      showMessage('Fehler beim Laden der Kommentare', 'danger');
    },
  });
}

function createComment(articleId) {
  const title = $('#title-field').val().trim();
  const text = $('#content-field').val().trim();

  if (!title || !text) {
    showMessage('Bitte Titel und Text eingeben', 'warning');
    return;
  }

  $.ajax({
    url: `${API_URL}/comments`,
    type: 'POST',
    contentType: 'application/json',
    xhrFields: { withCredentials: true },
    data: JSON.stringify({ title, text, articleId }),
    success: function () {
      showMessage('Kommentar erfolgreich gepostet!');
      $('#title-field').val('');
      $('#content-field').val('');
      loadComments(articleId);
    },
    error: function (xhr) {
      console.error('Error creating comment:', xhr);
      showMessage('Fehler beim Posten', 'danger');
    },
  });
}

function getArticleIdFromPage() {
  const articleId = $('#discussion').data('article-id');
  if (articleId) {
    return articleId;
  }
  const params = new URLSearchParams(window.location.search);
  return params.get('articleId');
}

function renderComments(comments) {
  const container = $('#comments-container');
  container.empty();

  comments.forEach((comment) => {
    const commentDiv = $(`
      <div class="comment d-flex mt-4" data-comment-id="${comment.id}">
        <img src="../../img/users/profile-picture.png" 
             class="rounded-circle" width="40" height="40" />
        <div class="ms-3 flex-grow-1 p-3 border rounded">
          <div class="small text-muted">
            <span class="username"></span><br/>
            <span class="timestamp"></span>
          </div>
          <h6 class="mt-2 comment-title"></h6>
          <div class="mt-2 comment-text"></div>
          <div class="d-flex justify-content-end mt-3">
            <button class="btn btn-sm btn-light reply-btn">Antworten</button>
          </div>
        </div>
      </div>
    `);

    commentDiv.find('.username').text(comment.userName || 'Anonymous');
    commentDiv
      .find('.timestamp')
      .text(new Date(comment.createdAt).toLocaleString('de-DE'));
    commentDiv.find('.comment-title').text(comment.title);
    commentDiv.find('.comment-text').text(comment.text);

    container.append(commentDiv);
  });
}

function updateCommentCount(count) {
  // Update discussion counter
  $('#discussion p')
    .first()
    .html(`<i class="bi bi-chat-left-text"></i> ${count} Kommentare`);

  // Update the top icon counter
  $('#postings-count').text(count + ' Postings');
}

function showMessage(message, type = 'success') {
  const alert = `
        <div class="alert alert-${type} alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `;
  $('#discussion').prepend(alert);
  setTimeout(() => $('.alert').fadeOut(), 3000);
}
