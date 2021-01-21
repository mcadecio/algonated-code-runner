import React, {useState} from 'react';
import ExerciseProblem from './ExerciseProblem';
import Card from 'react-bootstrap/Card';
import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';
import Container from 'react-bootstrap/Container';
import ExerciseEditor from './ExerciseEditor';
import DangerDismissibleAlert from '../DangerDismissibleAlert';
import ListGroup from 'react-bootstrap/ListGroup';
import Button from 'react-bootstrap/Button';

function ExercisePage({problem}) {
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

    const [code, setCode] = useState(exercise.defaultStarterCode.join(' '));

    const [{consoleOutput, result, data, summary}, setConsoleOutput] = useState({
        isSuccess: false,
        consoleOutput: 'There\'s nothing here yet',
        result: [],
        data: exercise.data,
        summary: {
            fitness: 0,
            timeRun: '0ms',
            iterations: 0,
            efficacy: 'None'
        }
    });

    const sendCodeToServer = (value) => {
        const request = {
            ...exercise,
            code: value
        };
        console.log({request});
        console.log(process.env);
        let endpoint = `https://fyp-algorithms-server.herokuapp.com${exercise.endpoint}`;

        if (process.env.NODE_ENV === 'development') {
            console.log(process.env.NODE_ENV);
            endpoint = `http://localhost:80${exercise.endpoint}`;
        }
        fetch(endpoint, {
            method: 'POST',
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
                    <ShadowedCard>
                        <Card.Header as={'h5'}>Exercise Coding Area</Card.Header>
                        <ExerciseEditor code={code} setCode={setCode}/>
                    </ShadowedCard>
                    <div className={'float-right'}>
                        <Button
                            type='button'
                            className={'btn-dark-blue'}
                            variant={'primary'}
                            onClick={() => {
                                console.log({code});
                                sendCodeToServer(code);
                            }}
                        >Submit Code</Button>
                    </div>
                    <br/>
                    <br/>
                    <ExerciseConsole alert={alert.alert} consoleOutput={consoleOutput}/>
                </Col>
                <Col>
                    <ExerciseSummary summary={summary}/>
                    <br/>
                    <VisualiserArea solution={result} weights={data} animation={animation}/>
                </Col>
            </Row>
            <br/>
        </>
    );
};

const ExerciseConsole = ({consoleOutput, alert}) => (
    <ShadowedCard>
        <Card.Header as={'h5'}>Console Output</Card.Header>
        <Card.Body>
            {alert}
            <Card.Text as={'pre'}>{consoleOutput}</Card.Text>
        </Card.Body>
    </ShadowedCard>
);


const ExerciseSummary = ({summary}) => (
    <ShadowedCard>
        <Card.Header as={'h5'}>Summary</Card.Header>
        <ListGroup variant={'flush'}>
            <ListGroup.Item>Fitness: {summary.fitness}</ListGroup.Item>
            <ListGroup.Item>Time Run: {summary.timeRun}</ListGroup.Item>
            <ListGroup.Item>Iterations: {summary.iterations}</ListGroup.Item>
            <ListGroup.Item>Efficacy: {summary.efficacy}</ListGroup.Item>
        </ListGroup>
    </ShadowedCard>
);

const ShadowedCard = ({children}) => {
    return (
        <Card className={'shadow-sm'} style={{marginBottom: '1%'}}>
            {children}
        </Card>
    );
};

const VisualiserArea = ({solution, weights, animation}) => {

    return (
        <ShadowedCard>
            <Card.Header as={'h5'}>Visualiser Area</Card.Header>
            <Card.Body>
                <Container style={{background: 'white', border: '1px solid', textAlign: 'center'}}>
                    {animation({solution, weights})}
                </Container>
            </Card.Body>
        </ShadowedCard>
    );
};

export {ExercisePage, ShadowedCard};