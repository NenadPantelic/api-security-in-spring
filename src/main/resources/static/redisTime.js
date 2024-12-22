const apiUrl = 'http://localhost:8080';

function loadTime() {
  let time = document.getElementById('time');
  let redisToken = localStorage.getItem('redis-token');

  fetch(apiUrl + '/api/auth/redis/v1/time', {
      method: 'GET',
      headers: {
        'Authorization': 'Bearer ' + redisToken
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