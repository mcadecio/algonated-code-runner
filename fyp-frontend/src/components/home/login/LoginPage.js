import {useHistory, useLocation} from 'react-router-dom';
import React from 'react';
import {useAuth} from '../../auth/AuthContext';
import LoginForm from './LoginForm';

export default function LoginPage() {
    let history = useHistory();
    let location = useLocation();
    let auth = useAuth();

    let {from} = location.state || {from: {pathname: '/'}};
    let login = () => {
        auth.signin(() => {
            history.replace(from);
        });
    };

    return (
        <div>
            <p>You must log in to view the page at {from.pathname}</p>
            <LoginForm login={login}/>
        </div>
    );
}



