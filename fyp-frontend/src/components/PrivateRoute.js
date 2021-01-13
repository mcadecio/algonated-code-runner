import {Redirect, Route} from 'react-router-dom';
import React from 'react';
import {useAuth} from './auth/AuthContext';

export default function PrivateRoute({component: Component, componentParams, ...rest}) {
    let auth = useAuth();
    return (
        <Route {...rest} render={({location}) =>
            auth.user ? (<Component {...componentParams}/>) :
                (<Redirect
                    to={{
                        pathname: '/login',
                        state: {from: location}
                    }}/>)
        }/>
    );
}