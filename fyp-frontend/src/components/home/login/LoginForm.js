import React, {useState} from 'react';
import Container from 'react-bootstrap/Container';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import Alert from 'react-bootstrap/Alert';
import ValidCredentials from './valid-cred.json';
import Card from 'react-bootstrap/Card';

const validCredentials = ValidCredentials;

const LoginForm = ({pathname, login}) => {
    const [validated, setValidated] = useState(false);
    const alertComponent = DangerDismissibleAlert();

    const [credentials, setCredentials] = useState({
        email: '',
        password: ''
    });

    const updateEmail = (email) => setCredentials(oldCredentials => {
        return {
            email: email,
            password: oldCredentials.password
        };
    });

    const updatePassword = (password) => setCredentials(oldCredentials => {
        return {
            email: oldCredentials.email,
            password: password
        };
    });

    const submitForm = (event) => {
        const isValid = event.currentTarget.checkValidity();

        if (isValid) {
            let matchedCred = validCredentials
                .filter(cred => cred.email === credentials.email)
                .filter(cred => cred.password === credentials.password)
                .length !== 0;
            if (matchedCred) {
                login();
            } else {
                alertComponent.setShow(true);
            }
        } else {
            event.stopPropagation();
        }
        event.preventDefault();

        setValidated(true);
    };


    return (
        <Container style={{'border': 'solid 3px'}}>
            <h3>Login Area</h3>
            <h4>Coming from {pathname}</h4>
            <Container style={{'padding': '3%'}}>
                <Card style={{'padding': '3%'}}>
                    <Form noValidate validated={validated} className={'text-left'}
                          onSubmit={event => submitForm(event)}>
                        {alertComponent.alert}
                        <Form.Group controlId={'emailAddress'}>
                            <Form.Label>Username</Form.Label>
                            <Form.Control
                                onChange={event => updateEmail(event.target.value)}
                                value={credentials.email}
                                required
                                type={'email'}
                                placeholder={'Enter email'}/>
                            <Form.Control.Feedback type='invalid'>
                                Please Provide a Valid Email Address
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group controlId={'password'}>
                            <Form.Label>Password</Form.Label>
                            <Form.Control
                                onChange={event => updatePassword(event.target.value)}
                                value={credentials.password}
                                required
                                type='password'
                                placeholder={'Enter password'}/>
                            <Form.Control.Feedback type='invalid'>
                                Please Provide a Valid Password
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Button variant='light' type={'submit'}>
                            Submit
                        </Button>
                    </Form>
                </Card>
            </Container>
        </Container>
    );
};


const DangerDismissibleAlert = () => {
    const [show, setShow] = useState(false);

    return {
        alert: (
            <>
                {show && <Alert variant='danger' onClose={() => setShow(false)} dismissible>
                    Wrong credentials entered!
                </Alert>}
            </>
        ),
        show,
        setShow
    };
};

export default LoginForm;