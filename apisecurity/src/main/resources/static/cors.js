const apiUrl = 'http://localhost:8080';

function fetchPing() {
  fetch(apiUrl + '/api/cors/v1/ping', {
      method: 'GET'
    })
    .then(res => {
      res.text().then(text => {
        display.innerHTML = text;
      });
    })
    .catch(error => console.error('Error : ', error));
}

function fetchPong() {
  fetch(apiUrl + '/api/cors/v1/pong', {
      method: 'POST'
    })
    .then(res => {
      res.text().then(text => {
        display.innerHTML = text;
      });
    })
    .catch(error => console.error('Error : ', error));
}

function fetchMad() {
  fetch(apiUrl + '/api/cors/v1/mad', {
      method: 'GET'
    })
    .then(res => {
      res.text().then(text => {
        display.innerHTML = text;
      });
    })
    .catch(error => console.error('Error : ', error));
}

function fetchMax() {
  fetch(apiUrl + '/api/cors/v1/max', {
      method: 'POST'
    })
    .then(res => {
      res.text().then(text => {
        display.innerHTML = text;
      });
    })
    .catch(error => console.error('Error : ', error));
}