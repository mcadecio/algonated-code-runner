import React, {useEffect, useState} from 'react';

const SVGScale = ({topPartId, leftBasketId, rightBasketId, basketInnerText}) => {

    return (
        <svg x="0px" y="0px" viewBox="0 -15 300 250" style={{enableBackground: 'new 0 -15 300 250'}}
             xmlSpace="preserve">
            <ScaleTopPart id={topPartId}/>
            <ScaleBase/>
            <ScaleLeftBasket id={leftBasketId} innerText={basketInnerText.left}/>
            <ScaleRightBasket id={rightBasketId} innerText={basketInnerText.right}/>
        </svg>
    );
};

const ControlledSVGScale = (
    {
        topPartId,
        leftBasketId,
        rightBasketId,
        basketInnerText,
        topPartRotation,
        translateY
    }) => {

    return (
        <svg x="0px" y="0px" viewBox="0 -15 300 250" style={{enableBackground: 'new 0 -15 300 250'}}
             xmlSpace="preserve">
            <ControlledScaleTopPart id={topPartId} topPartRotation={topPartRotation}/>
            <ScaleBase/>
            <ControlledScaleLeftBasket id={leftBasketId} innerText={basketInnerText.left} translateY={translateY}/>
            <ControlledScaleRightBasket id={rightBasketId} innerText={basketInnerText.right} translateY={translateY}/>
        </svg>
    );
};

const ControlledScaleBasket = ({id, rightHandleXCoord, leftHandleXCoord, basketXCoord, innerText, translateY}) => {

    // const [yValue, setYValue] = useState(translateY);

    return (
        <g id={id} style={{transform: `translateY(${translateY}px)`}}>
            <ScaleBasketHandle handleXCoord={leftHandleXCoord} style={scalesStyles.leftHandle}/>
            <ScaleBasketHandle handleXCoord={rightHandleXCoord} style={scalesStyles.rightHandle}/>
            <ScaleBasketBase xCoordinates={basketXCoord} innerText={innerText}/>
        </g>
    );
};


const ScaleArm = ({xCoordinates}) => {
    return (
        <rect x={xCoordinates} y="45" width="90" height="10" rx="5"/>
    );
};

const ScaleLeftArm = () => {
    return (
        <ScaleArm xCoordinates={'35'}/>
    );
};

const ScaleRightArm = () => {
    return (
        <ScaleArm xCoordinates={'165'}/>
    );
};

const ScaleCenterMechanism = () => {
    return (
        <circle cx="145" cy="50" r="20" stroke="black" fill="transparent" strokeWidth="7"/>
    );
};

const ScaleTopPart = () => {
    return (
        <g id='top-area' style={scalesStyles.boxTransform} className={'el'}>
            <ScaleLeftArm/>
            <ScaleCenterMechanism/>
            <ScaleRightArm/>
        </g>
    );
};

const ControlledScaleTopPart = ({topPartRotation}) => {

    const [style, setStyle] = useState(scalesStyles.boxTransform);

    useEffect(() => {
        setStyle(old => {
            return {
                ...old,
                transform: `rotate(${topPartRotation})`
            }
        })
    }, [topPartRotation])


    return (
        <g id='top-area' style={style} className={'el'}>
            <ScaleLeftArm/>
            <ScaleCenterMechanism/>
            <ScaleRightArm/>
        </g>
    );
};

const ScaleBase = () => {
    return (
        <g id="base">
            <rect x="140" y="70" width="10" height="130" rx="5"/>
            <rect x="95" y="190" width="100" height="10" rx="5"/>
        </g>
    );
};

const ScaleBasket = ({id, rightHandleXCoord, leftHandleXCoord, basketXCoord, innerText}) => {

    return (
        <g id={id}>
            <ScaleBasketHandle handleXCoord={leftHandleXCoord} style={scalesStyles.leftHandle}/>
            <ScaleBasketHandle handleXCoord={rightHandleXCoord} style={scalesStyles.rightHandle}/>
            <ScaleBasketBase xCoordinates={basketXCoord} innerText={innerText}/>
        </g>
    );
};
const ScaleBasketHandle = ({handleXCoord, style}) => {
    return (
        <rect x={handleXCoord} y="60" width="10" height="60" style={style} rx="5"/>
    );
};

const ScaleLeftBasket = ({id, innerText}) => {
    return (
        <ScaleBasket
            id={id}
            basketXCoord={'0px'}
            leftHandleXCoord={'29'}
            rightHandleXCoord={'51'}
            innerText={innerText}/>
    );
};

const ControlledScaleLeftBasket = ({id, innerText, translateY}) => {
    return (
        <ControlledScaleBasket
            id={id}
            basketXCoord={'0px'}
            leftHandleXCoord={'29'}
            rightHandleXCoord={'51'}
            innerText={innerText}
            translateY={-translateY}/>
    );
};

const ScaleRightBasket = ({id, innerText}) => {
    return (
        <ScaleBasket
            id={id}
            basketXCoord={'200px'}
            leftHandleXCoord={'229'}
            rightHandleXCoord={'251'}
            innerText={innerText}/>
    );
};

const ControlledScaleRightBasket = ({id, innerText, translateY}) => {
    return (
        <ControlledScaleBasket
            id={id}
            basketXCoord={'200px'}
            leftHandleXCoord={'229'}
            rightHandleXCoord={'251'}
            innerText={innerText}
            translateY={translateY}/>
    );
};

const ScaleBasketBase = ({xCoordinates, innerText}) => {
    return (
        <svg x={xCoordinates} y="110px">
            <circle cx="45" cy="0" r="40" stroke="black" strokeWidth="7"/>
            <text x="15%" y="9%" textAnchor="middle" stroke="white" fontSize={10}>{innerText}</text>
        </svg>
    );
};

const scalesStyles = {
    leftHandle: {
        transformBox: 'fill-box',
        transformOrigin: 'center',
        transform: 'rotate(25deg)'
    },
    rightHandle: {
        transformBox: 'fill-box',
        transformOrigin: 'center',
        transform: 'rotate(-25deg)'
    },
    boxTransform: {
        transformBox: 'fill-box',
        transformOrigin: 'center'
    }
};

export default SVGScale;
export {ControlledSVGScale}