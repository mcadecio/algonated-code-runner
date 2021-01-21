import React from 'react';
import './App.css';
import {BrowserRouter as Router, Link, Redirect, Route, Switch} from 'react-router-dom';
import AuthProvider from './components/auth/AuthContext';
import AuthNav from './components/auth/AuthButton';
import LoginPage from './components/home/login/LoginPage';
import PrivateRoute from './components/PrivateRoute';
import ScalesExercise from './components/exercise/scales/ScalesExercise';
import {TSPAnimation, TSPExercise} from './components/exercise/tsp/TSPExercise';
import Animations from './components/Animations';
import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';
import ExerciseView from './components/exercise/ExerciseView';

function App() {

    return (
        <AuthProvider>
            <Router>
                <div style={{marginBottom: '5%'}}>
                    <Navbar variant={'dark'} expand="lg" style={{backgroundColor: '#00325b'}} fixed={'top'}>
                        <Navbar.Brand>Algorithms</Navbar.Brand>
                        <Nav className="mr-auto">
                            <Nav.Link as={Link} to={'/exercises'}>Exercises</Nav.Link>
                        </Nav>
                        <AuthNav/>
                    </Navbar>
                </div>
                <div style={{margin: '3%'}}>

                    <Switch>
                        <Route exact path={'/'}>
                            <Redirect to={'/exercises'}/>
                        </Route>
                        <Route exact path="/login" component={LoginPage}/>
                        <Route exact path="/exercises/animation" component={Animations}/>
                        <Route exact path="/exercises/tspanimation">
                            <TSPAnimation solution={[]} weights={[[]]} test={true}/>
                        </Route>
                        <PrivateRoute exact path='/exercises' component={ExerciseView}/>
                        <PrivateRoute exact path="/exercises/scales" component={ScalesExercise}/>
                        <PrivateRoute exact path="/exercises/tsp" component={TSPExercise}/>
                    </Switch>
                </div>
            </Router>
        </AuthProvider>
    );
}

export default App;


