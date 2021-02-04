import React, {useEffect, useState} from 'react';
import ExerciseProblem from './ExerciseProblem';
import Card from 'react-bootstrap/Card';
import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';
import Container from 'react-bootstrap/Container';
import {MonacoDataEditor, MonacoExerciseEditor} from './ExerciseEditor';
import DangerDismissibleAlert from '../DangerDismissibleAlert';
import ListGroup from 'react-bootstrap/ListGroup';
import Button from 'react-bootstrap/Button';
import Spinner from 'react-bootstrap/Spinner';
import Nav from 'react-bootstrap/Nav';
import '../anime.css';
import ExerciseDemo from './demo/ExerciseDemo';

function ExercisePage({problem}) {
    return (
        <div style={{marginLeft: '2%', marginRight: '2%'}}>
            <ExerciseProblem name={problem.name} description={problem.customDescription}/>
            <br/>
            <TheWholePage exercise={problem.exercise} animation={problem.animation}/>
        </div>
    );
}

const TheWholePage = ({exercise, animation}) => {
    const alert = DangerDismissibleAlert({innerText: 'It looks like something went wrong, check the output !'});
    const [code, setCode] = useState(exercise.defaultStarterCode);
    const [isLoading, setLoading] = useState(false);
    const [iterations, setIterations] = useState(exercise.iterations.toString());
    const [exerciseData, setExerciseData] = useState(JSON.stringify({data: exercise.data}, null, 2));
    const [{consoleOutput, result, data, summary, solutions}, setConsoleOutput] = useState({
        isSuccess: false,
        consoleOutput: 'There\'s nothing here yet',
        result: [],
        solutions: [],
        data: exercise.data,
        summary: {
            fitness: 0,
            timeRun: 0,
            iterations: 0,
            efficacy: 0
        }
    });

    const runDemo = (algorithm, extraFields) => {
        setLoading(true);
        const request = {
            problem: exercise.problem,
            algorithm,
            data: JSON.parse(exerciseData).data,
            ...extraFields
        };
        console.debug({request});
        let endpoint = `${process.env.REACT_APP_FYP_SERVER_DOMAIN}/exercise/demo`;

        if (process.env.NODE_ENV === 'development') {
            console.debug(process.env.NODE_ENV);
            endpoint = `http://localhost:80/exercise/demo`;
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

    const sendCodeToServer = (value) => {
        setLoading(true);
        const request = {
            ...exercise,
            code: value,
            data: JSON.parse(exerciseData).data,
            iterations: Number.parseInt(iterations, 10)
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
                        setCode={setCode}
                        iterations={iterations}
                        setIterations={setIterations}
                        data={exerciseData}
                        setData={setExerciseData}
                        demoCallback={runDemo}
                    />
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
                    <AnimationTab solutions={solutions} solution={result} weights={data} animation={animation}/>
                </Col>
            </Row>
            <br/>
        </>
    );
};

const ExerciseCodingArea = ({code, setCode, iterations, setIterations, data, setData, demoCallback}) => {

    const [constantData] = useState(data)
    const map = new Map();
    map.set('#iterations', (
        <Card.Body>
            <IterationsOptions iterations={iterations} setIterations={setIterations}/>
        </Card.Body>
    ));

    map.set('#data', (
        <DataOptions data={data} setData={setData} height={'500'}/>
    ));

    map.set('#editor', (
        <MonacoExerciseEditor code={code} setCode={setCode} language={'java'}/>
    ));

    map.set('#demo', (
        <ExerciseDemo demoCallback={demoCallback} data={constantData}/>
    ));

    const [selected, setSelected] = useState(map.get('#editor'));

    return (
        <ShadowedCard>
            <CodingTabs changeTab={(selectedTab) => setSelected(map.get(selectedTab))}/>
            {selected}
        </ShadowedCard>
    );
};

const DataOptions = ({data, setData, height}) => {
    const [innerData, setInnerData] = useState(data);

    useEffect(() => {
        setData(innerData);
    }, [innerData, setData]);

    return (
        <MonacoDataEditor
            data={innerData}
            setData={setInnerData}
            language={'json'}
            height={height}/>
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
                <Nav.Item>
                    <Nav.Link href="#demo">Demo</Nav.Link>
                </Nav.Item>
            </Nav>
        </Card.Header>
    );
};

const IterationsOptions = ({iterations, setIterations}) => {

    const [innerValue, setInnerValue] = useState(iterations);

    useEffect(() => {
        setIterations(innerValue);
    }, [innerValue, setIterations]);

    return (
        <div style={{textAlign: 'center'}}>
            <h5>Number of Iterations: </h5>
            <h5>{(innerValue).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ' ')}</h5>
            <IterationsSlider value={(innerValue).toString()} setValue={setInnerValue}/>
        </div>
    );
};

const IterationsSlider = ({value, setValue}) => {
    return (
        <div className="line controls">
            <input className="progress" type="range" min="1" max="500000" value={value}
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
        <ListGroup.Item>Time Run: {summary.timeRun}ms | {summary.timeRun / 1000}s</ListGroup.Item>
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

const AnimationTab = ({solution, weights, animation, solutions}) => {

    return (
        <ShadowedCard>
            <Card.Header as={'h5'} className={'dark-blue-text'}>Animation</Card.Header>
            <Card.Body>
                <Container style={{background: 'white', border: '1px solid', textAlign: 'center'}}>
                    {animation({solution, weights, solutions})}
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

export {ExercisePage, ShadowedCard, IterationsOptions, DataOptions};
