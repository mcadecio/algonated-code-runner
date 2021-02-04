import React, {useState} from 'react';
import MonacoEditor from 'react-monaco-editor';

const MonacoDataEditor = ({data, setData, language, height = "500"}) => {
    return (
        <div>
            <MonacoExerciseEditor
                code={data} setCode={setData} language={language} height={height}/>
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

const MonacoExerciseEditor = ({code, setCode, language, height = "500"}) => {
    return (
        <div>
            <MonacoEditor
                width="auto"
                height={height}
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