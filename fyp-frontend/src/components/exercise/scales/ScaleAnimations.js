import Row from 'react-bootstrap/Row';
import React, {useEffect, useState} from 'react';
import Col from 'react-bootstrap/Col';
import {ControlledSVGScale} from './SVGScales';

const maxWidth = 747;

const BarAnimation = ({solution, weights}) => {
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
        rightRectangleWidth,
        inputWidth
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

const BalanceAnimation = ({solution, weights, solutions}) => {
    const [{left, right}, setLeftRight] = useState(SimpleWidthCalculator({solution, weights}));

    const updateLeftRight = (newLeft, newRight) => {
        setLeftRight({
            left: newLeft,
            right: newRight
        });
    };

    useEffect(() => {
        if (solutions.length !== 0) {

            const replay = async () => {
                const delay = ms => new Promise(res => setTimeout(res, ms));

                for (let i = 0; i < solutions.length; i++) {
                    let newWidth = SimpleWidthCalculator({solution: solutions[i], weights});
                    if (i > 0 && !equals(solutions[i - 1], solutions[i])) {
                        updateLeftRight(newWidth.left, newWidth.right);
                    }
                    await delay(1);
                }

            };

            replay();
        }
    }, [solutions, weights]);

    return (
        <BalanceScale
            left={left}
            right={right}
            weights={weights.length}
        />
    );
};

const equals = (solution, anotherSolution) => {
    let i = solution.length;
    while (i--) {
        if (solution[i] !== anotherSolution[i]) return false;
    }
    return true;
};

const SimpleWidthCalculator = ({solution, weights}) => {
    const randomStart = Math.floor(Math.random() * Math.floor(1000));
    let left = randomStart;
    let right = Math.abs(randomStart - 1000);

    let sumOfWeightsOnTheLeft = 0;
    let sumOfWeightsOnTheRight = 0;

    for (let i = 0; i < solution.length; i++) {
        if (solution[i] === 0) {
            sumOfWeightsOnTheLeft = sumOfWeightsOnTheLeft + weights[i];
        } else {
            sumOfWeightsOnTheRight = sumOfWeightsOnTheRight + weights[i];
        }
    }

    if (sumOfWeightsOnTheRight !== 0 || sumOfWeightsOnTheLeft !== 0) {
        left = sumOfWeightsOnTheLeft;
        right = sumOfWeightsOnTheRight;
    }

    return {left, right};
};

const BalanceScale = ({left, right, weights}) => {

    const [topPartRotation, setTopPartRotation] = useState(0);
    const [translateY, setTranslateY] = useState(0);

    useEffect(() => {
        const maxRotation = 30;
        const maxYTranslation = 50;

        let offset = right - left;

        let rotation = (offset * maxRotation) / weights;
        let translation = (offset * maxYTranslation) / weights;

        if (rotation > maxRotation) {
            rotation = maxRotation;
        } else if (rotation < -maxRotation) {
            rotation = -maxRotation;
        }

        if (translation > maxYTranslation) {
            translation = maxYTranslation;
        } else if (translation < -maxYTranslation) {
            translation = -maxYTranslation;
        }

        setTopPartRotation(rotation);
        setTranslateY(translation);

    }, [left, right, weights]);

    return (
        <>
            <ControlledSVGScale
                left={left}
                right={right}
                topPartRotation={topPartRotation}
                translateY={translateY}/>
        </>
    );
};

export {BarAnimation, BalanceAnimation, equals};