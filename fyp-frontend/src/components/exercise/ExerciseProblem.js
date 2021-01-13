import React from 'react';
import Card from 'react-bootstrap/Card';

export default function ExerciseProblem({name, description}) {

    return (
        <Card>
            <Card.Header as={'h5'}>{name}</Card.Header>
            <Card.Body>
                <Card.Text as={'p'}>{description.join('')}</Card.Text>
            </Card.Body>
        </Card>
    );
}