import React from 'react';
import {Container} from 'react-bootstrap';

export default function HomePage() {
    return (
        <Container className='text-center' style={{'border': 'solid 3px', "padding": "3%"}}>
            <h1>You are at the homepage</h1>

            <div className='row' style={{'border': 'solid 3px', "padding": "3%"}}>
                <div className='col s6 center-align'>
                    <h3>Welcome to My App</h3>
                </div>
            </div>
        </Container>
    );
}