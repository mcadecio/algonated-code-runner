import React from 'react';
import './App.css';
import {BrowserRouter as Router, Link, Route, Switch} from 'react-router-dom';
import HomePage from './components/home/HomePage';
import Container from 'react-bootstrap/Container';
import ExercisePage from './components/exercise/ExercisePage';
import AuthProvider from './components/auth/AuthContext';
import AuthButton from './components/auth/AuthButton';
import LoginPage from './components/home/login/LoginPage';
import PrivateRoute from './components/PrivateRoute';
import AceEditor from 'react-ace';

import "ace-builds/src-noconflict/mode-java";
import "ace-builds/src-noconflict/theme-github";


function App() {

    return (
        <AuthProvider>
            <Router>
                <div style={{'border': 'solid 3px', 'padding': '3%'}}>
                        <AuthButton/>

                        <ul>
                            <li><Link to="/">Home Page</Link></li>
                            <li><Link to="/exercise">Exercise Page</Link></li>
                        </ul>

                        <Switch>
                            <Route exact path={'/'} component={HomePage}/>
                            <Route exact path="/login" component={LoginPage}/>
                            <PrivateRoute exact path="/exercise" component={ExercisePage}/>
                        </Switch>
                </div>
            </Router>
        </AuthProvider>
    );
}

export default App;


