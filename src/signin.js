/*Sign In*/
var request = require("request");

var options = { method: 'GET',
  url: 'https://www.linkedin.com/oauth/v2/authorization',
  qs: 
   { response_type: 'code',
     client_id: '86m77lzeen0tnc',
     redirect_uri: 'https://timothyle.github.io/iata-hackathon-2017/',
     state: '987654321',
     scope: 'r_basicprofile' },
  headers: 
   { 'postman-token': '15ec5734-6314-872e-5f36-cef878d3c6e6',
     'cache-control': 'no-cache' } };

request(options, function (error, response, body) {
  if (error) throw new Error(error);

  console.log(body);
});