import React from 'react';
import Card from 'react-bootstrap/Card';
import {ShadowedCard} from './ExercisePage';

export default function ExerciseProblem({name, description: Description}) {

    return (
        <ShadowedCard>
            <Card.Header as={'h5'}>{name}</Card.Header>
            <Card.Body>
                <Description/>
            </Card.Body>
        </ShadowedCard>
    );
}