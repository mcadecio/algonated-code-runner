import React, {useEffect, useState} from 'react';
import AceEditor from 'react-ace';
import 'ace-builds/src-noconflict/mode-java';
import 'ace-builds/src-noconflict/mode-json';
import 'ace-builds/src-noconflict/theme-eclipse';
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

const ExerciseEditor = ({code, setCode}) => {
    return (
        <div>
                <AceEditor
                    {...editorConfig}
                    value={code}
                    onChange={setCode}>
                </AceEditor>
        </div>
    );
};

const DataInputEditor = ({data, setData}) => {
    return (
        <div>
            <AceEditor
                {...editorConfig}
                mode={'json'}
                value={data}
                onChange={setData}>
            </AceEditor>
        </div>
    )
}

const MonacoDataEditor = ({data, setData, language}) => {
    return (
        <div>
            <MonacoExerciseEditor
                code={data} setCode={setData} language={language}/>
        </div>
    )
}

const editorConfig = {
    mode: 'java',
    theme: 'eclipse',
    name: 'blah2',
    fontSize: 14,
    showPrintMargin: true,
    showGutter: true,
    style: {
        borderRadius: '5px'
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
const monacoEditorConfig = () => {
    return {
        selectOnLineNumbers: false,
        fontFamily: 'SourceCodePro, monospace',
        fontSize: 15,
        wordWrap: 'on',
        autoSaveNamespace: 'hr-cedit-contest:1-challenge:13581',
        compile_button_text: 'Run Code',
        defaultLanguage: null,
        dynamicMode: true,
        enableIntellisense: true,
        enableLiveAutocomplete: true,
        enableLiveAutocompleteLinting: false,
        enableTrackTyping: true,
        enableVersioning: true,
        enablePositionInfo: true,
        foldCode: true,
        inReact: true,
        languages: ['c', 'clojure', 'cpp', 'cpp14', 'csharp', 'erlang', 'java'],
        showCompileTest: true,
        showCustomInput: true,
        showFullScreen: true,
        showSubmit: true,
        showUnused: true,
        autoIndent: 'full',
        showUploadCode: true,
        automaticLayout: true,
        versionIds: [],
        versioningRestUrl: '/rest/contests/master/challenges/2d-array/versions'
    }
}
const MyMonacoEditor = () => {

    const [editorValue, setValue] = useState('{}');


    return (
        <div style={{border: 'solid 1px', width: '803px', height: '300px'}}>
            <MonacoEditor
                width="800"
                height="300"
                theme="vs-dark"
                value={editorValue}
                language={'json'}
                options={monacoEditorConfig()}
                onChange={value => setValue(value)}
            />
        </div>
    );
};

const MonacoExerciseEditor = ({code, setCode, language}) => {
    return (
        <div>
            <MonacoEditor
                width="auto"
                height="500"
                theme="vs-light"
                value={code}
                language={language}
                options={monacoEditorConfig()}
                onChange={value => setCode(value)}
            />
        </div>
    )
}

export default ExerciseEditor;

export {MyMonacoEditor, DataInputEditor, MonacoExerciseEditor, MonacoDataEditor};