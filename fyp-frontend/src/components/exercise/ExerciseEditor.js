import React, {useState} from 'react';
import MonacoEditor from 'react-monaco-editor';

const MonacoDataEditor = ({data, setData, language}) => {
    return (
        <div>
            <MonacoExerciseEditor
                code={data} setCode={setData} language={language}/>
        </div>
    )
}

const monacoEditorConfig = () => {
    return {
        selectOnLineNumbers: false,
        fontFamily: 'SourceCodePro, monospace',
        fontSize: 15,
        wordWrap: 'on',
        showUnused: true,
        scrollBeyondLastLine: false,
        renderWhitespace: 'boundary',
        autoIndent: 'full',
        automaticLayout: true
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

export {MyMonacoEditor, MonacoExerciseEditor, MonacoDataEditor};