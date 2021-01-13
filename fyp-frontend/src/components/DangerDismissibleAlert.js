import React, {useState} from 'react';
import Alert from 'react-bootstrap/Alert';

export default function DangerDismissibleAlert({innerText}) {
    const [show, setShow] = useState(false);

    return {
        alert: (
            <>
                {show && <Alert variant='danger' onClose={() => setShow(false)} dismissible>
                    {innerText}
                </Alert>}
            </>
        ),
        show,
        setShow
    };
}