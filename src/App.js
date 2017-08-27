import React, { Component } from 'react';
import ReactDOM from "react-dom";
import logo from './airplane.png';
import linkedin from './linkedinButton.png'
import './App.css';
import { Navbar, Jumbotron, Button } from 'react-bootstrap';

class Layout extends React.Component {
render() {
return (
<div className="App">
  <div className="jumbotron">
    <img src={logo} className="App-logo" alt="logo" />
    <i><h2>FlyMetrics</h2></i>
  </div>
  <p className="App-intro">
    The sky is the limit.
  </p>
  <br></br>
  <a href="./signin.js"> <img className="linkedin" src={linkedin} alt="Linkedin Sign in" width="275px" height="75px"/></a>
  <br/>
  <footer>FlyMetrics &copy; 2017</footer>

  <script type="text/javascript">
  function linkedin {
    $('.img').on('click', function(){
      $(this).toggleClass('active');
    });

    var request = require("request");

    var options = { method: 'GET',
    url: 'https://www.linkedin.com/oauth/v2/authorization',
    qs: 
    { response_type: 'code',
    client_id: '86m77lzeen0tnc',
    redirect_uri: 'https://timothyle.github.io/fly-metrics/',
    state: '987654321',
    scope: 'r_basicprofile' },
    headers: 
    { 'postman-token': '15ec5734-6314-872e-5f36-cef878d3c6e6',
    'cache-control': 'no-cache' } };

    request(options, function (error, response, body) {
      if (error) throw new Error(error);

      console.log(body);
    });
  }
  </script>

</div>
);
}
}

const app = document.getElementById('app');

ReactDOM.render(<Layout/>, app);
