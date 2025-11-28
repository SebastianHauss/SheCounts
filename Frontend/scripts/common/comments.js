const API_URL = 'http://localhost:8080/api/comments';

$(document).ready(function () {

  function getAllComments() {
    $.ajax({
      url: `${API_URL}`,
      type: 'GET',
      contentType: 'application/json',
      crossDomain: true,
      success: function (data) {
        console.log('Successfully loaded all comments');
      },
    });
  }

    function getComment(id) {
    $.ajax({
      url: `${API_URL}/${id}`,
      type: 'GET',
      contentType: 'application/json',
      crossDomain: true,
      success: function (data) {
        console.log('Successfully loaded all comments');
      },
    });
  }
  

});
