import React, {useEffect, useState} from 'react';
import {primes} from './scales.primes.json';
import defaultConfig from './scales.exercise.json';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import {ExercisePage} from '../ExercisePage';
const maxWidth = 778;

const ScalesExercise = () => {

    const exerciseConfig = {
        ...defaultConfig
    };

    exerciseConfig.exercise.data = primes;

    return (
        <ExercisePage problem={{
            animation: Scale,
            ...exerciseConfig
        }}/>
    )
};

const Scale = ({solution, weights}) => {
    const {leftRectangleWidth, rightRectangleWidth} = WidthCalculator({solution, weights});

    return (
        <>
            <Row style={{border: '1px solid'}}>
                <ScaleBar
                    leftRectangleWidth={leftRectangleWidth}
                    rightRectangleWidth={rightRectangleWidth}/>
            </Row>
            <Row style={{border: '1px solid'}}>
                <ScaleLabel
                    leftRectangleWidth={leftRectangleWidth}
                    rightRectangleWidth={rightRectangleWidth}/>
            </Row>
        </>
    );
};

const WidthCalculator = ({solution, weights}) => {

    const [{
        leftRectangleWidth, rightRectangleWidth
    }, setWidth] = useState({
        leftRectangleWidth: maxWidth / 2,
        rightRectangleWidth: maxWidth / 2
    });

    const [inputWidth, updateWidth] = useState(50.0);

    const incrementWidth = (percentage) => {
        updateWidth(oldWidth => oldWidth + percentage);
    };

    const decrementWidth = (percentage) => {
        updateWidth(oldWidth => oldWidth - percentage);
    };

    useEffect(() => {
        setWidth(() => {
            let percentage = inputWidth * 0.01;
            let firstWidth = percentage * maxWidth;

            if (firstWidth > maxWidth) {
                firstWidth = maxWidth;
            } else if (firstWidth < 0) {
                firstWidth = 0;
            }

            return {
                leftRectangleWidth: firstWidth,
                rightRectangleWidth: maxWidth - firstWidth
            };
        });
    }, [inputWidth]);

    useEffect(() => {
        setTimeout(async () => {
            updateWidth(50);
            const totalWeight = weights.reduce((weight, anotherWeight) => weight + anotherWeight);
            const delay = ms => new Promise(res => setTimeout(res, ms));


            for (let i = 0; i < solution.length; i++) {
                let percentage = (weights[i] / totalWeight) * 100;

                if (solution[i] === 0) {
                    incrementWidth(percentage);
                } else {
                    decrementWidth(percentage);
                }
                await delay(50);
            }

        }, 500);
    }, [solution, weights]);

    return {
        leftRectangleWidth,
        rightRectangleWidth
    };
};

const ScaleBar = ({leftRectangleWidth, rightRectangleWidth}) => {
    const styles = {
        blueRectangle: {
            fill: 'rgb(0,0,255)',
            strokeWidth: 3,
            stroke: 'rgb(0,0,0)'
        },
        redRectangle: {
            fill: 'rgb(255,0,0)',
            strokeWidth: 3,
            stroke: 'rgb(0,0,0)'
        }
    };

    return (
        <Col>
            <Row>
                <svg width={leftRectangleWidth} height="50">
                    <rect width={leftRectangleWidth}
                          height="100%"
                          style={styles.blueRectangle}/>
                </svg>
                <svg width={rightRectangleWidth} height="50">
                    <rect width={rightRectangleWidth}
                          height="100%"
                          style={styles.redRectangle}/>
                </svg>
            </Row>
        </Col>
    );
};

const ScaleLabel = ({leftRectangleWidth, rightRectangleWidth}) => {
    const styles = {
        solidBorder: {
            border: '1px solid'
        }
    };

    const leftRectangleWidthWeight = ((leftRectangleWidth / maxWidth) * 100).toFixed(2);
    const rightRectangleWidthWeight = ((rightRectangleWidth / maxWidth) * 100).toFixed(2);

    return (
        <>
            <Col style={styles.solidBorder} as={'h6'}>
                {leftRectangleWidthWeight}
            </Col>
            <Col style={styles.solidBorder} as={'h6'}>
                {rightRectangleWidthWeight}
            </Col>
        </>
    );
};

export default ScalesExercise;