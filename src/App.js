import React, { Component } from 'react';
import logo from './airplane.png';
import './App.css';
import { Navbar, Jumbotron, Button } from 'react-bootstrap';

class App extends Component {
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
        <button className="btn btn-primary"><a href="./signin.js">Sign In</a></button>
        <footer>FlyMetrics &copy; 2017</footer>

      </div>
    );
  }
}

export default App;
