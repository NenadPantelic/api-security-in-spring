const apiUrl = 'http://localhost:8080';

function processLoginSubmit(e) {
  e.preventDefault();

  let username = document.getElementById('username').value;
  let password = document.getElementById('password').value;

  let credential = 'Basic ' + btoa(username + ':' + password);

  fetch(apiUrl + '/api/auth/redis/v1/login', {
      method: 'POST',
      headers: {
        'Authorization': credential
      }
    })
    .then(res => {
      if (res.ok) {
        res.text().then(text => {
          localStorage.setItem('redis-token', text);
          window.location.replace('/redisTime.html');
        });
      } else {
        alert('Invalid login');
      }
    })
    .catch(error => console.error('Error logging in: ', error));
}