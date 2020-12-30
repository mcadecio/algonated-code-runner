import React, {useState} from 'react';
import './App.css';
import {Link, Route, BrowserRouter as Router, Switch} from 'react-router-dom';


/*
One landing page with a link to an exercise

The exercise can be static at the moment

The exercise should have text box.

The text box should take some input and send it to the server.
 */
function App() {
    return (
        <Router>
            <div className="App">
                <ul>
                    <li><Link to={'/'}>HomePage</Link></li>
                    <li><Link to={'/exercise'}>Exercise</Link></li>
                </ul>
                <Switch>
                    <Route path={'/'} exact={true} component={HomePage}/>
                    <Route path={'/exercise'} exact={true} component={Exercise}/>
                </Switch>
            </div>
        </Router>
    );
}

const HomePage = () => {
    return (
        <div>
            <h1>You are at the homepage</h1>
        </div>
    );
};

const Exercise = () => {

    const sendCodeToServer = (value) => {
        fetch('http://localhost:3030/exercise/submit', {
            method: 'POSt',
            body: JSON.stringify({
                code: value
            }),
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(res => res.text())
            .then(text => console.log(text));
    };

    return (
        <div>
            <h1>You are at the Exercise Page</h1>
            <form onSubmit={event => {
                let text = event.target[0].value;
                console.log(text);
                sendCodeToServer(text);
                event.preventDefault();
            }}>
                <label>Enter your code here:</label>
                <textarea defaultValue={'Go on, try it !'}/>
                <input type={'submit'} value={'Submit'}/>
            </form>
        </div>
    );
};

export default App;
