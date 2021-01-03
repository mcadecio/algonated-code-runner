import React, {useEffect, useRef, useState} from 'react';
import ExerciseProblem from './ExerciseProblem';
import Card from 'react-bootstrap/Card';
import AceEditor from 'react-ace';
import 'ace-builds/src-noconflict/mode-java';
import 'ace-builds/src-noconflict/theme-monokai';
import 'ace-builds/src-noconflict/ext-beautify';
import 'ace-builds/src-noconflict/ext-code_lens';
import 'ace-builds/src-noconflict/ext-elastic_tabstops_lite';
import 'ace-builds/src-noconflict/ext-emmet';
import 'ace-builds/src-noconflict/ext-error_marker';
import 'ace-builds/src-noconflict/ext-keybinding_menu';
import 'ace-builds/src-noconflict/ext-language_tools';
import 'ace-builds/src-noconflict/ext-linking';
import 'ace-builds/src-noconflict/ext-modelist';
import 'ace-builds/src-noconflict/ext-options';
import 'ace-builds/src-noconflict/ext-prompt';
import 'ace-builds/src-noconflict/ext-rtl';
import 'ace-builds/src-noconflict/ext-searchbox';
import 'ace-builds/src-noconflict/ext-settings_menu';
import 'ace-builds/src-noconflict/ext-spellcheck';
import 'ace-builds/src-noconflict/ext-split';
import 'ace-builds/src-noconflict/ext-static_highlight';
import 'ace-builds/src-noconflict/ext-statusbar';
import 'ace-builds/src-noconflict/ext-textarea';
import 'ace-builds/src-noconflict/ext-themelist';
import 'ace-builds/src-noconflict/ext-whitespace';
import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';
import Button from 'react-bootstrap/Button';
import anime from 'animejs'

require('ace-builds/webpack-resolver');

export default function ExercisePage() {

    return (
        <div>
            <ExerciseProblem/>
            <br/>
            <Row>
                <Col><ExerciseCodingArea/></Col>
                <Col><VisualiserArea/></Col>
            </Row>
            <Animation/>
        </div>
    );
}

const ExerciseCodingArea = () => {
    const [consoleOutput, setConsoleOutput] = useState('There\'s nothing here yet');

    const sendCodeToServer = (value) => {
        fetch('http://localhost:3030/exercise/submit', {
            method: 'POSt',
            body: JSON.stringify({
                code: value
            }),
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(res => res.text())
            .then(text => {
                console.log(text);
                setConsoleOutput(text);
            });
    };

    return (
        <Card>
            <Card.Header as={'h5'}>Exercise Coding Area</Card.Header>
            <Card.Body>
                <Card.Text as={'p'}>Please write your code below</Card.Text>
                <Editor submitCallback={sendCodeToServer}/>
            </Card.Body>
            <Card.Footer>
                <Card.Text as={'h5'}>Console Output</Card.Text>
                <hr/>
                <Card.Text as={'pre'}>{consoleOutput}</Card.Text>
            </Card.Footer>
        </Card>
    );
};

const Editor = ({submitCallback}) => {

    const [code, setCode] = useState('public class HelloWorld {\n' +
        '    public void print() {\n' +
        '        System.out.println("Hello, world!");\n' +
        '    }\n' +
        '}');

    return (
        <div>

            <div style={{'border': '0px solid'}}>
                <AceEditor
                    mode="java"
                    theme="monokai"
                    name="blah2"
                    fontSize={14}
                    showPrintMargin={true}
                    showGutter={true}
                    style={{'borderRadius': '25px 25px 0 0'}}
                    highlightActiveLine={true}
                    value={code}
                    onChange={setCode}
                    height={'500px'}
                    width={'auto'}
                    setOptions={{
                        enableBasicAutocompletion: true,
                        enableLiveAutocompletion: true,
                        enableSnippets: true,
                        showLineNumbers: true,
                        tabSize: 4,
                        spellcheck: true,
                        minLines: 5,
                        displayIndentGuides: true
                    }}/>
            </div>
            <br/>
            <div className={'mb-2 float-right'}>
                <Button
                    type='button'
                    variant={'primary'}
                    onClick={() => {
                        console.log({code});
                        submitCallback(code);
                    }}
                >Submit Code</Button>
            </div>
        </div>
    );
};

const VisualiserArea = () => (
    <Card>
        <Card.Header as={'h5'}>Visualiser Area</Card.Header>
        <Card.Body>
            <Card.Text as={'p'}>This is where the visualiser should go</Card.Text>
        </Card.Body>
    </Card>
);

const Animation = () => {
    const animationRef = useRef(null);
    useEffect(() => {
        animationRef.current = anime({
            targets: ".el",
            translateX: 250,
            delay: function(el, i) {
                return i * 100;
            },
            loop: true,
            direction: "alternate",
            easing: "easeInOutSine"
        });
    }, []);
    return (
        <div className="App">
            <button onClick={()=>animationRef.current.restart()}>Restart</button>
            <div className="el" />
        </div>
    );

};