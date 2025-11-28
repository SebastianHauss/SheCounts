const API_URL = 'http://localhost:8080/api/comments';

function getAllComments() {
  $.ajax({
    url: `${API_URL}`,
    type: 'GET',
    contentType: 'application/json',
    crossDomain: true,
    success: function (data) {
      console.log('Success:', data);
    },
    error: function (xhr, status, error) {
      console.log('Error:', error);
    }
  });
}

function getComment(id) {
  $.ajax({
    url: `${API_URL}/${id}`,
    method: 'GET',
    dataType: 'json',
    success: function (data) {
      console.log('Success:', data);
    },
    error: function (xhr, status, error) {
      console.log('Error:', error);
    }
  });
}


function createComment() {
  const title = $('#title-input').val().trim();
  const text =  $('#text-area').val().trim();

  // TODO VALIDATE

  $.ajax({
    url: `${API_URL}/`,
    type: 'POST',
    contentType: 'application/json',
    crossDomain: true,
    data:JSON.stringify({title, text}),
    success: function (data) {
      console.log('Success:', data);
    },
    error: function (xhr, status, error) {
      console.log('Error:', error);
    }
  });

  //UPDATE
  function updateComment(id) {
    $.ajax({
      url: `${API_URL}/${id}`,
      type: 'PUT',
      contentType: 'application/json',
      crossDomain: true,
      data: JSON.stringify({title, text}),
      success: function (data) {
        console.log('Success:', data);
      },
      error: function (xhr, status, error) {
        console.log('Error:', error);
      }
    });
  }
  //DELETE
  function deleteComment(id) {
    $.ajax({
      url: `${API_URL}/${id}`,
      type: 'DELETE',
      contentType: 'application/json',
      crossDomain: true,
      success: function (data) {
        console.log('Success:', data);
      },
      error: function (xhr, status, error) {
        console.log('Error:', error);
      }
    });
  }
}