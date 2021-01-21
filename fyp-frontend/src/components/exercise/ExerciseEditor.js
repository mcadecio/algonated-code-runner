import React, {useEffect, useState} from 'react';
import Button from 'react-bootstrap/Button';
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
import MonacoEditor from 'react-monaco-editor';

require('ace-builds/webpack-resolver');

const ExerciseEditor = ({submitCallback, defaultStarterCode}) => {

    const [code, setCode] = useState(defaultStarterCode.join(''));

    return (
        <div>
            <div>
                <AceEditor
                    {...editorConfig}
                    value={code}
                    onChange={setCode}/>
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

const editorConfig = {
    mode: 'java',
    theme: 'monokai',
    name: 'blah2',
    fontSize: 14,
    showPrintMargin: true,
    showGutter: true,
    style: {
        borderRadius: '25px 25px 0 0'
    },
    highlightActiveLine: true,
    height: '500px',
    width: 'auto',
    setOptions: {
        enableBasicAutocompletion: true,
        enableLiveAutocompletion: true,
        enableSnippets: true,
        showLineNumbers: true,
        tabSize: 4,
        spellcheck: true,
        minLines: 5,
        displayIndentGuides: true
    }
};

const MyMonacoEditor = () => {

    const [editorValue, setValue] = useState('')

    useEffect(() => {
        console.log(editorValue);
    }, [editorValue])

    return (
        <MonacoEditor
            width="800"
            height="600"
            language="java"
            theme="vs-light"
            value={editorValue}
            options={{
                selectOnLineNumbers: true,
                fontFamily: "SourceCodePro, monospace",
                fontSize: 15,
                wordWrap: "on",
                autoSaveNamespace: "hr-cedit-contest:1-challenge:13581",
                compile_button_text: "Run Code",
                defaultLanguage: null,
                dynamicMode: true,
                enableIntellisense: true,
                enableLiveAutocomplete: true,
                enableLiveAutocompleteLinting: false,
                enableTrackTyping: true,
                enableVersioning: true,
                foldCode: true,
                inReact: true,
                languages: ["c", "clojure", "cpp", "cpp14", "csharp", "erlang"],
                showCompileTest:true,
                showCustomInput:true,
                showFullScreen:true,
                showSubmit:true,
                showUploadCode: true,
                versionIds: [],
                versioningRestUrl: "/rest/contests/master/challenges/2d-array/versions"
            }}
            onChange={value => setValue(value)}
        />
    );
}

export default ExerciseEditor;

export {MyMonacoEditor};