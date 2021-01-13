import React from 'react';
import './App.css';
import {BrowserRouter as Router, Link, Route, Switch} from 'react-router-dom';
import HomePage from './components/home/HomePage';
import ExercisePage from './components/exercise/ExercisePage';
import AuthProvider from './components/auth/AuthContext';
import AuthButton from './components/auth/AuthButton';
import LoginPage from './components/home/login/LoginPage';
import PrivateRoute from './components/PrivateRoute';
import ScalesExercise from './components/exercise/scales/ScalesExercise';
import AnyChartExample from './components/codejar/AnyChartExample';

function App() {

    return (
        <AuthProvider>
            <Router>
                <div style={{'border': 'solid 3px', 'padding': '3%'}}>
                        <AuthButton/>

                        <ul>
                            <li><Link to="/">Home Page</Link></li>
                            <li><Link to="/exercise/scales">Exercise Page</Link></li>
                            <li><Link to="/anychart">Any Chart Example</Link></li>
                        </ul>

                        <Switch>
                            <Route exact path={'/'} component={HomePage}/>
                            <Route exact path="/login" component={LoginPage}/>
                            <Route exact path="/anychart" component={AnyChartExample}/>
                            <PrivateRoute exact path="/exercise/scales" component={ExercisePage} componentParams={{problem: ScalesExercise()}}/>
                        </Switch>
                </div>
            </Router>
        </AuthProvider>
    );
}

export default App;


