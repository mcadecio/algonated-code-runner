import {Link, useHistory} from 'react-router-dom';
import React from 'react';
import {useAuth} from './AuthContext';
import Nav from 'react-bootstrap/Nav';

export default function AuthButton() {
    let history = useHistory();
    let auth = useAuth();


    return (
        <Nav>
            {auth.user ? (
                <Nav.Link href='#' onSelect={() => {
                    auth.signout(() => history.push('/login'));
                }}>Sign out</Nav.Link>) : (
                <Nav.Link as={Link} to={'/login'}>Sign in</Nav.Link>
            )}
        </Nav>
    );
}