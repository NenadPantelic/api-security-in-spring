import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import { BrowserRouter, Switch, Route } from 'react-router-dom'
import App from './App';
import ArticleDangerSearch from './ArticleDangerSearch';
import ArticleDangerCreate from './ArticleDangerCreate';
import ArticleDangerCreateWYSIWYG from './ArticleDangerCreateWYSIWYG';
import GreetingDanger from './GreetingDanger';
import ArticleSafeSearch from './ArticleSafeSearch';
import ArticleSafeCreate from './ArticleSafeCreate';
import GreetingSafe from './GreetingSafe';
 
const routing = (
  <BrowserRouter>
    <div>

      <Switch>
        <Route exact path="/" component={App} />
        <Route path="/greetingDanger" component={GreetingDanger} />
        <Route path="/articleDangerCreate" component={ArticleDangerCreate} />
        <Route path="/articleDangerCreateWYSIWYG" component={ArticleDangerCreateWYSIWYG} />
        <Route path="/articleDangerSearch" component={ArticleDangerSearch} />
        <Route path="/greetingSafe" component={GreetingSafe} />
        <Route path="/articleSafeCreate" component={ArticleSafeCreate} />
        <Route path="/articleSafeSearch" component={ArticleSafeSearch} />
      </Switch>
    </div>
  </BrowserRouter>
)
 
ReactDOM.render(routing, document.getElementById('root'));