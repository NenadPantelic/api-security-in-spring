import React,{Component} from 'react';
import {Link} from 'react-router-dom'; 
class App extends Component { 
   
    render() { 
      return ( 
		<div>
		<strong>DANGER</strong>
	    <ul>
		  <li><Link to="/greetingDanger">greetingDanger</Link></li>
		  <li><Link to="/articleDangerCreate">articleDangerCreate</Link></li>
		  <li><Link to="/articleDangerCreateWYSIWYG">articleDangerCreateWYSIWYG</Link></li>
		  <li><Link to="/articleDangerSearch">articleDangerSearch</Link></li>
		</ul>
		<div/>
		<strong>SAFE</strong>
	    <ul>
		  <li><Link to="/greetingSafe">greetingSafe</Link></li>
		  <li><Link to="/articleSafeCreate">articleSafeCreate</Link></li>
		  <li><Link to="/articleSafeSearch">articleSafeSearch</Link></li>
		</ul>
		</div>
      ); 
    } 
  } 
  
  export default App; 