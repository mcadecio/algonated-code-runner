import React, {useState} from 'react';
import ExerciseProblem from './ExerciseProblem';
import Card from 'react-bootstrap/Card';
import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';
import Container from 'react-bootstrap/Container';
import ExerciseEditor from './ExerciseEditor';
import DangerDismissibleAlert from '../DangerDismissibleAlert';

export default function ExercisePage({problem}) {
    return (
        <div>
            <ExerciseProblem name={problem.name} description={problem.description}/>
            <br/>
            <ExerciseCodingArea exercise={problem.exercise} animation={problem.animation}/>
        </div>
    );
}

const ExerciseCodingArea = ({exercise, animation}) => {
    const alert = DangerDismissibleAlert({innerText: 'It looks like something went wrong, check the output !'});

    const [{consoleOutput, result, data}, setConsoleOutput] = useState({
        isSuccess: false,
        consoleOutput: 'There\'s nothing here yet',
        result: [1, 1, 0],
        data: exercise.data
    });

    const sendCodeToServer = (value) => {
        const request = {
            ...exercise,
            code: value
        };
        console.log({request});
        fetch('http://localhost:3030/exercise/submit/scales', {
            method: 'POSt',
            body: JSON.stringify(request),
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(res => res.json())
            .then(requestResult => {
                console.log(requestResult);
                setConsoleOutput(requestResult);
                alert.setShow(!requestResult.isSuccess);
            });
    };

    return (
        <>
            <Row>
                <Col>
                    <Card>
                        <Card.Header as={'h5'}>Exercise Coding Area</Card.Header>
                        <Card.Body>
                            <ExerciseEditor submitCallback={sendCodeToServer}
                                            defaultStarterCode={exercise.defaultStarterCode}/>
                        </Card.Body>
                    </Card>
                    <br/>
                    <ExerciseConsole alert={alert.alert} consoleOutput={consoleOutput}/>
                </Col>
                <Col><VisualiserArea solution={result} weights={data} animation={animation}/></Col>
            </Row>
            <br/>
        </>
    );
};

const ExerciseConsole = ({consoleOutput, alert}) => (
    <Card>
        <Card.Header as={'h5'}>Console Output</Card.Header>
        <Card.Body>
            {alert}
            <Card.Text as={'pre'}>{consoleOutput}</Card.Text>
        </Card.Body>
    </Card>
);


const VisualiserArea = ({solution, weights, animation}) => {

    return (
        <Card>
            <Card.Header as={'h5'}>Visualiser Area</Card.Header>
            <Card.Body>
                <Container style={{background: 'white', border: '1px solid', textAlign: 'center', padding: '10%'}}>
                    {animation({solution, weights})}
                </Container>
            </Card.Body>
        </Card>
    );
};