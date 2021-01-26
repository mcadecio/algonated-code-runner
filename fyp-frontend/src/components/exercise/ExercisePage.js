import React, {useEffect, useState} from 'react';
import ExerciseProblem from './ExerciseProblem';
import Card from 'react-bootstrap/Card';
import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';
import Container from 'react-bootstrap/Container';
import ExerciseEditor from './ExerciseEditor';
import DangerDismissibleAlert from '../DangerDismissibleAlert';
import ListGroup from 'react-bootstrap/ListGroup';
import Button from 'react-bootstrap/Button';
import Spinner from 'react-bootstrap/Spinner';
import Nav from 'react-bootstrap/Nav';

function ExercisePage({problem}) {
    return (
        <div style={{marginLeft: '2%', marginRight: '2%'}}>
            <ExerciseProblem name={problem.name} description={problem.description}/>
            <br/>
            <TheWholePage exercise={problem.exercise} animation={problem.animation}/>
        </div>
    );
}

const TheWholePage = ({exercise, animation}) => {
    const alert = DangerDismissibleAlert({innerText: 'It looks like something went wrong, check the output !'});

    const [code, setCode] = useState(exercise.defaultStarterCode.join(' '));

    const [isLoading, setLoading] = useState(false);

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
        setLoading(true);
        const request = {
            ...exercise,
            code: value
        };
        console.debug({request});
        let endpoint = `${process.env.REACT_APP_FYP_SERVER_DOMAIN}${exercise.endpoint}`;

        if (process.env.NODE_ENV === 'development') {
            console.debug(process.env.NODE_ENV);
            endpoint = `http://localhost:80${exercise.endpoint}`;
        }
        fetch(endpoint, {
            method: 'POST',
            body: JSON.stringify(request),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(res => {
            return res.json();
        }).then(requestResult => {
            console.debug(requestResult);
            setConsoleOutput(requestResult);
            alert.setShow(!requestResult.isSuccess);
        }).finally(() => {
            setLoading(false);
        });
    };

    return (
        <>
            <Row xs={1} sm={1} md={1} lg={1} xl={2}>
                <Col>
                    <ExerciseCodingArea
                        code={code}
                        setCode={setCode}/>
                    <SubmitCodeButton
                        isLoading={isLoading}
                        callback={() => sendCodeToServer(code)}
                    />
                </Col>
                <Col>
                    <InformationArea
                        alert={alert.alert}
                        consoleOutput={consoleOutput}
                        summary={summary}
                    />
                    <br/>
                    <AnimationTab solution={result} weights={data} animation={animation}/>
                </Col>
            </Row>
            <br/>
        </>
    );
};

const ExerciseCodingArea = ({code, setCode}) => {

    const map = new Map();
    map.set('#iterations', (
        <Card.Body>
            <IterationsOptions/>
        </Card.Body>
    ));

    map.set('#data', (
        <Card.Body>
            <p>Data</p>
        </Card.Body>
    ));

    map.set('#editor', (
        <ExerciseEditor code={code} setCode={setCode}/>
    ));

    const [selected, setSelected] = useState(map.get('#editor'));

    return (
        <ShadowedCard>
            <CodingTabs changeTab={(selectedTab) => setSelected(map.get(selectedTab))}/>
            {selected}
        </ShadowedCard>
    );
};

const CodingTabs = ({changeTab}) => {
    return (
        <Card.Header as={'h5'}>
            <Nav
                onSelect={(selectedKey) => changeTab(selectedKey)}
                fill={true}
                variant="tabs"
                defaultActiveKey={'#editor'}>
                <Nav.Item>
                    <Nav.Link href="#editor">Editor</Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link href="#iterations">Iterations</Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link href="#data">Data</Nav.Link>
                </Nav.Item>
            </Nav>
        </Card.Header>
    );
};

const IterationsOptions = () => {
    const [value, setValue] = useState('1000');

    return (
        <div style={{textAlign: 'center'}}>
            <h5>Number of Iterations: </h5>
            <h5>{(value).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ' ')}</h5>
            <IterationsSlider value={value} setValue={setValue}/>
        </div>
    );
};

const IterationsSlider = ({value, setValue}) => {

    return (
        <div className="line controls">
            <input className="progress" type="range" min="1" max="1000000" value={value}
                   style={{width: '50%'}}
                   onChange={(event) => {
                       setValue(event.target.value);
                   }}/>
        </div>
    );
};

const InformationArea = ({consoleOutput, alert, summary}) => {

    const selectedComponent = () => {
        switch (selected) {
            case '#summary':
                return <SummaryTab summary={summary}/>;
            case '#console':
            default:
                return <ConsoleTab consoleOutput={consoleOutput}/>;
        }
    };

    const [selected, setSelected] = useState('#console');

    return (
        <ShadowedCard>
            <ConsoleTabs changeTab={(selectedTab) => setSelected(selectedTab)}/>
            <Card.Body>
                {alert}
                {selectedComponent()}
            </Card.Body>
        </ShadowedCard>
    );
};

const ConsoleTab = ({consoleOutput}) => {
    return (
        <Card.Text as={'pre'}>{consoleOutput}</Card.Text>
    );
};

const ConsoleTabs = ({changeTab}) => {
    return (
        <Card.Header as={'h5'}>
            <Nav
                onSelect={(selectedKey) => changeTab(selectedKey)}
                fill={true}
                variant="tabs"
                defaultActiveKey={'#console'}>
                <Nav.Item>
                    <Nav.Link href="#console">Console</Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link href="#summary">Summary</Nav.Link>
                </Nav.Item>
            </Nav>
        </Card.Header>
    );
};


const SummaryTab = ({summary}) => (
    <ListGroup variant={'flush'}>
        <ListGroup.Item>Fitness: {summary.fitness}</ListGroup.Item>
        <ListGroup.Item>Time Run: {summary.timeRun}</ListGroup.Item>
        <ListGroup.Item>Iterations: {summary.iterations}</ListGroup.Item>
        <ListGroup.Item>Efficacy: {summary.efficacy}</ListGroup.Item>
    </ListGroup>
);

const ShadowedCard = ({children}) => {
    return (
        <Card className={'shadow-sm'} style={{marginBottom: '1%'}}>
            {children}
        </Card>
    );
};

const AnimationTab = ({solution, weights, animation}) => {

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

const SubmitCodeButton = ({callback, isLoading}) => {
    return (

        <div className={'float-right'}>
            {isLoading && <Spinner
                animation="grow"
                size='sm'
                role='status'
                className={'dark-blue'}
            />}{' '}{' '}
            <Button
                type='button'
                className={'btn-dark-blue'}
                variant={'primary'}
                onClick={callback}
            >Submit Code</Button>
        </div>
    );
};

export {ExercisePage, ShadowedCard};
