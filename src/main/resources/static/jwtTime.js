const apiUrl = 'http://localhost:8080';

function loadTime() {
  let time = document.getElementById('time');
  let jwtToken = localStorage.getItem('jwt-token');

  fetch(apiUrl + '/api/auth/jwt/v1/time', {
      method: 'GET',
      headers: {
        'Authorization': 'Bearer ' + jwtToken
      }
    })
    .then(res => {
      res.text().then(text => {
        time.innerHTML = text;
      });
    })
    .catch(error => console.error('Error getting time : ', error));
}

function getCookie(cookieName) {
  var cookieValue = document.cookie.split(';')
    .map(item => item.split('=')
      .map(x => decodeURIComponent(x.trim())))
    .filter(item => item[0] === cookieName)[0]

  if (cookieValue) {
    return cookieValue[1];
  }
}