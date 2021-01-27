import React from 'react';

const ControlledSVGScale = ({left, right, topPartRotation, translateY}) => (
    <svg x="0px" y="0px" viewBox="0 -15 300 250" style={{enableBackground: 'new 0 -15 300 250'}}
         xmlSpace="preserve">
        <ControlledScaleTopPart topPartRotation={topPartRotation}/>
        <ScaleBase/>
        <ControlledScaleLeftBasket innerText={left} translateY={translateY}/>
        <ControlledScaleRightBasket innerText={right} translateY={translateY}/>
    </svg>
);

const ControlledScaleBasket = ({rightHandleXCoord, leftHandleXCoord, basketXCoord, innerText, translateY}) => (
    <g style={{transform: `translateY(${translateY}px)`}}>
        <Basket leftHandleXCoord={leftHandleXCoord}
                rightHandleXCoord={rightHandleXCoord}
                basketXCoord={basketXCoord}
                innerText={innerText}/>
    </g>
);

const ControlledScaleTopPart = ({topPartRotation}) => (
    <g style={{...scalesStyles.boxTransform, transform: `rotate(${topPartRotation}deg)`}}>
        <ScaleLeftArm/>
        <ScaleCenterMechanism/>
        <ScaleRightArm/>
    </g>
);

const ControlledScaleRightBasket = ({innerText, translateY}) => (
    <ControlledScaleBasket
        basketXCoord={'200px'}
        leftHandleXCoord={'229'}
        rightHandleXCoord={'251'}
        innerText={innerText}
        translateY={translateY}/>
);

const ControlledScaleLeftBasket = ({innerText, translateY}) => (
    <ControlledScaleBasket
        basketXCoord={'0px'}
        leftHandleXCoord={'29'}
        rightHandleXCoord={'51'}
        innerText={innerText}
        translateY={-translateY}/>
);

const UncontrolledSVGScale = ({topPartId, leftBasketId, rightBasketId, basketInnerText}) => (
    <svg x="0px" y="0px" viewBox="0 -15 300 250" style={{enableBackground: 'new 0 -15 300 250'}}
         xmlSpace="preserve">
        <ScaleTopPart id={topPartId}/>
        <ScaleBase/>
        <ScaleLeftBasket id={leftBasketId} innerText={basketInnerText.left}/>
        <ScaleRightBasket id={rightBasketId} innerText={basketInnerText.right}/>
    </svg>
);

const ScaleArm = ({xCoordinates}) => (
    <rect x={xCoordinates} y="45" width="90" height="10" rx="5"/>
);

const ScaleLeftArm = () => (
    <ScaleArm xCoordinates={'35'}/>
);

const ScaleRightArm = () => (
    <ScaleArm xCoordinates={'165'}/>
);

const ScaleCenterMechanism = () => (
    <circle cx="145" cy="50" r="20" stroke="black" fill="transparent" strokeWidth="7"/>
);

const ScaleTopPart = () => (
    <g id='top-area' style={scalesStyles.boxTransform} className={'el'}>
        <ScaleLeftArm/>
        <ScaleCenterMechanism/>
        <ScaleRightArm/>
    </g>
);

const ScaleBase = () => (
    <g id="base">
        <rect x="140" y="70" width="10" height="130" rx="5"/>
        <rect x="95" y="190" width="100" height="10" rx="5"/>
    </g>
);

const ScaleBasket = ({id, rightHandleXCoord, leftHandleXCoord, basketXCoord, innerText}) => (
    <g id={id}>
        <Basket leftHandleXCoord={leftHandleXCoord}
                rightHandleXCoord={rightHandleXCoord}
                basketXCoord={basketXCoord}
                innerText={innerText}/>
    </g>
);

const Basket = ({rightHandleXCoord, leftHandleXCoord, basketXCoord, innerText}) => (
    <>
        <ScaleBasketHandle xCoord={leftHandleXCoord} style={scalesStyles.leftHandle}/>
        <ScaleBasketHandle xCoord={rightHandleXCoord} style={scalesStyles.rightHandle}/>
        <ScaleBasketBase xCoord={basketXCoord} innerText={innerText}/>
    </>
);

const ScaleBasketHandle = ({xCoord, style}) => (
    <rect x={xCoord} y="60" width="10" height="60" style={style} rx="5"/>
);

const ScaleLeftBasket = ({id, innerText}) => (
    <ScaleBasket
        id={id}
        basketXCoord={'0px'}
        leftHandleXCoord={'29'}
        rightHandleXCoord={'51'}
        innerText={innerText}/>
);

const ScaleRightBasket = ({id, innerText}) => (
    <ScaleBasket
        id={id}
        basketXCoord={'200px'}
        leftHandleXCoord={'229'}
        rightHandleXCoord={'251'}
        innerText={innerText}/>
);

const ScaleBasketBase = ({xCoord, innerText}) => (
    <svg x={xCoord} y="110px">
        <circle cx="45" cy="0" r="40" stroke="black" strokeWidth="7"/>
        <text x="15%" y="9%" textAnchor="middle" stroke="white" fontSize={10}>{innerText}</text>
    </svg>
);

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

export {ControlledSVGScale, UncontrolledSVGScale};