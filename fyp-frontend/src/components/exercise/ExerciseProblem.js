import React from 'react';
import Card from 'react-bootstrap/Card';

export default function ExerciseProblem() {

    return (
        <Card>
            <Card.Header as={'h5'}>Problem</Card.Header>
            <Card.Body>
                <Card.Text as={'p'}>This is where the problem definition should go</Card.Text>
            </Card.Body>
        </Card>
    );
}