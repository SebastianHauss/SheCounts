console.log("Loaded comment-service.js");

const API_URL = 'http://localhost:8080/api';

let currentUser = null;

$(document).ready(function () {
  const articleId = getArticleIdFromPage();

  if (articleId) {
    loadComments(articleId);
  }

  loadCurrentUser().then(() => {
    if (articleId) {
      loadComments(articleId);
    }
  });

  // Erkennt ob ein Login oder Logout passiert und aktualisiert dann die Kommentar-Berechtigungen
  window.addEventListener('authChanged', function () {
    loadCurrentUser().then(() => {
      const articleId = getArticleIdFromPage();
      if (articleId) {
        loadComments(articleId);
      }
    });
  });

  $('#post-button').on('click', function () {
    createComment(articleId);
  });

  $(document).on('click', '.edit-btn', function () {
    const commentDiv = $(this).closest('.comment');
    toggleEditMode(commentDiv);
  });

  $(document).on('click', '.save-btn', function () {
    const commentDiv = $(this).closest('.comment');
    saveComment(commentDiv);
  });

  $(document).on('click', '.delete-btn', function () {
    const commentDiv = $(this).closest('.comment');
    deleteComment(commentDiv);
  });

  $(document).on('click', '.cancel-btn', function () {
    const commentDiv = $(this).closest('.comment');
    const articleId = getArticleIdFromPage();
    loadComments(articleId); // Reload um Änderungen zu verwerfen
  });
});

function loadCurrentUser() {
  return $.ajax({
    url: `${API_URL}/auth/me`,
    type: 'GET',
    xhrFields: { withCredentials: true },
    success: function (user) {
      currentUser = user;
      console.log('Current user loaded:', currentUser);
    },
    error: function (xhr) {
      console.debug('User not logged in');
      currentUser = null;
    },
  });
}

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

  if (!currentUser) {
    const loginModalEl = document.getElementById('loginModal');
    if (loginModalEl) {
      const loginModal = new bootstrap.Modal(loginModalEl);
      loginModal.show();

      showMessage('Bitte melde dich an, um zu kommentieren', 'info');
    } else {
      // Fallback falls Modal nicht gefunden wird
      showMessage('Bitte melde dich an, um zu kommentieren', 'warning');
    }
    return;
  }

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

function toggleEditMode(commentDiv) {
  const title = commentDiv.find('.comment-title').text();
  const text = commentDiv.find('.comment-text').text();

  // Ersetze Titel mit Input
  commentDiv.find('.comment-title').html(`
      <input type="text" class="form-control form-control-sm edit-title" 
            value="${title}" />
    `);

  // Ersetze Text mit Textarea
  commentDiv.find('.comment-text').html(`
      <textarea class="form-control edit-text" rows="3">${text}</textarea>
    `);

  // Ersetze Edit-Button mit Save/Cancel
  commentDiv.find('.d-flex.justify-content-end').html(`
      <button class="btn btn-sm btn-light cancel-btn me-2">
        <i class="bi bi-x-lg"></i> Abbrechen
      </button>
      <button class="btn btn-sm btn-primary save-btn">
        <i class="bi bi-check-lg"></i> Speichern
      </button>
    `);
}

function saveComment(commentDiv) {
  const commentId = commentDiv.data('comment-id');
  const title = commentDiv.find('.edit-title').val().trim();
  const text = commentDiv.find('.edit-text').val().trim();

  if (!title || !text) {
    showMessage('Titel und Text dürfen nicht leer sein', 'warning');
    return;
  }

  const articleId = getArticleIdFromPage();

  $.ajax({
    url: `${API_URL}/comments/${commentId}`,
    type: 'PUT',
    contentType: 'application/json',
    xhrFields: { withCredentials: true },
    data: JSON.stringify({
      title,
      text,
      articleId,
    }),
    success: function () {
      showMessage('Kommentar erfolgreich aktualisiert!');
      const articleId = getArticleIdFromPage();
      loadComments(articleId);
    },
    error: function (xhr) {
      console.error('Error updating comment:', xhr);
      if (xhr.status === 403) {
        showMessage(
          'Du kannst nur deine eigenen Kommentare bearbeiten',
          'danger'
        );
      } else {
        showMessage('Fehler beim Aktualisieren', 'danger');
      }
    },
  });
}

function deleteComment(commentDiv) {
  const commentId = commentDiv.data('comment-id');

  $.ajax({
    url: `${API_URL}/comments/${commentId}`,
    type: 'DELETE',
    xhrFields: { withCredentials: true },
    success: function () {
      showMessage('Kommentar erfolgreich gelöscht');
      const articleId = getArticleIdFromPage();
      loadComments(articleId);
    },
    error: function (xhr) {
      console.error('Error deleting comment:', xhr);
      if (xhr.status === 403) {
        showMessage('Du kannst nur deine eigenen Kommentare löschen', 'danger');
      } else {
        showMessage('Fehler beim Löschen', 'danger');
      }
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

function getProfileImageUrl(comment) {
  // Prüfe, ob eine gültige UUID vorhanden ist
  const isValidFileId =
    comment.profilePictureId &&
    /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i.test(
      comment.profilePictureId
    );

  // Fallback Avatar mit ui-avatars.com
  const fallbackAvatar = `https://ui-avatars.com/api/?name=${encodeURIComponent(
    comment.userName || 'Anonymous'
  )}&background=6c757d&color=fff&size=128&bold=true`;

  // Profilbild-URL bestimmen
  return isValidFileId
    ? `${API_URL}/files/${comment.profilePictureId}`
    : fallbackAvatar;
}

function handleImageError(img, fallbackUrl) {
  if (img.dataset.errorHandled !== 'true') {
    img.dataset.errorHandled = 'true';
    img.src = fallbackUrl;
  }
}

function renderComments(comments) {
  const container = $('#comments-container');
  container.empty();

  if (comments.length === 0) {
    container.html(`
            <div class="no-comments-message">
                <i class="bi bi-chat-left-dots"></i>
                <p class="mb-0">Noch keine Kommentare vorhanden.</p>
                <small>Teile als erste Person deine Meinung!</small>
            </div>
        `);
    return;
  }

  comments.forEach((comment) => {
    // Erstelle Profilbild-URLs
    const profilePicUrl = getProfileImageUrl(comment);
    const fallbackAvatar = `https://ui-avatars.com/api/?name=${encodeURIComponent(
      comment.userName || 'Anonymous'
    )}&background=6c757d&color=fff&size=128&bold=true`;

    const commentDiv = $(`
            <div class="comment d-flex" data-comment-id="${comment.id}">
                <img src="${profilePicUrl}" 
                     class="rounded-circle flex-shrink-0 comment-avatar" 
                     width="48" 
                     height="48" 
                     alt="${comment.userName || 'User'} avatar"
                     data-fallback="${fallbackAvatar}" />
                <div class="ms-3 flex-grow-1">
                    <div class="d-flex justify-content-between align-items-start mb-2">
                        <div>
                            <span class="username d-block"></span>
                            <span class="timestamp d-block"></span>
                        </div>
                    </div>
                    <h6 class="comment-title"></h6>
                    <div class="comment-text"></div>
                    <div class="d-flex justify-content-end mt-3">
                        <button class="btn btn-sm edit-btn">
                            <i class="bi bi-pencil me-1"></i>Bearbeiten
                        </button>
                        <button class="btn btn-sm delete-btn">
                            <i class="bi bi-trash me-1"></i>Löschen
                        </button>
                    </div>
                </div>
            </div>
        `);

    // Füge Error-Handler für das Profilbild hinzu
    const imgElement = commentDiv.find('.comment-avatar')[0];
    imgElement.onerror = function () {
      handleImageError(this, fallbackAvatar);
    };

    commentDiv.find('.username').text(comment.userName || 'Anonymous');
    commentDiv
      .find('.timestamp')
      .text(new Date(comment.createdAt).toLocaleString('de-DE'));
    commentDiv.find('.comment-title').text(comment.title);
    commentDiv.find('.comment-text').text(comment.text);

    const editBtn = commentDiv.find('.edit-btn');
    const deleteBtn = commentDiv.find('.delete-btn');
    const canEdit =
      currentUser &&
      (comment.userId === currentUser.userId || currentUser.isAdmin);

    if (!canEdit) {
      editBtn.addClass('unauthorized');
      deleteBtn.addClass('unauthorized');
    }

    container.append(commentDiv);
  });
}

function updateCommentCount(count) {
  let text;

  if (count === 0) {
    text = 'Keine Postings';
  } else if (count === 1) {
    text = '1 Posting';
  } else {
    text = `${count} Postings`;
  }

  // Für den Discussion-Bereich
  $('#discussion p')
    .first()
    .html(`<i class="bi bi-chat-left-text"></i> ${text}`);

  // Für den Top-Counter
  $('#postings-count').text(text);
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
