import React, {useEffect, useRef, useState} from 'react';
import {anime} from 'react-anime';
import '../App.css';
import './anime.css';
import {createMixedSpiralNodes, createSequentialEdges, Graph} from './exercise/tsp/GraphComponents';

const styles = {
    circle: {
        width: 64,
        height: 64,
        borderRadius: '100%',
        background: 'steelblue',
        margin: '0 auto',
        marginTop: '50%'
    },
    bar: {
        height: 500,
        width: 100,
        background: 'steelblue',
        margin: '0 auto'
    }
};

// eslint-disable-next-line
const ChargedAnimation = () => {

    const [battery, updateBattery] = useState({
        percentage: 0,
        charged: function () {
            return `${this.percentage}%`;
        },
        cycles: 120
    });

    const incrementBatteryAndCycles = (interval) => {
        updateBattery(oldBattery => {
            let newPercentage = oldBattery.percentage + 1;
            if (oldBattery.percentage > 99) {
                clearInterval(interval);
                newPercentage = 100;
            }

            return {
                charged: oldBattery.charged,
                percentage: newPercentage,
                cycles: oldBattery.cycles + 1
            };
        });
    };

    useEffect(() => {
        let interval;
        interval = setInterval(() => {
                incrementBatteryAndCycles(interval);
            },
            100);
    }, []);

    return (
        <div>
                <pre>

                {JSON.stringify({
                    charged: battery.charged(),
                    cycles: battery.cycles
                })}
                </pre>
        </div>
    );
};

// eslint-disable-next-line
const Animation = () => {
    const animationRef = useRef(null);
    useEffect(() => {
        animationRef.current = anime({
            targets: '.el',
            keyframes: [
                {translateY: -40},
                {translateX: 250},
                {translateY: 40},
                {translateX: 0},
                {translateY: 0}
            ],
            delay: function (el, i) {
                return i * 100;
            },
            loop: false,
            easing: 'easeInOutSine'
        });
    });
    return (
        <div className="App">
            <button onClick={() => animationRef.current.restart()}>Restart</button>
            <div className="el" style={{...styles.bar, height: 500, background: 'red'}}/>
        </div>
    );

};

// eslint-disable-next-line
const ChargedAnimationAnimeJS = () => {
    const animationRef = useRef(null);

    let battery = {
        charged: '0%',
        cycles: 500
    };

    useEffect(() => {
        animationRef.current = anime({
            targets: battery,
            charged: '100%',
            cycles: 15,
            round: 1,
            easing: 'linear',
            update: function () {
                try {
                    document.querySelector('.el').innerHTML = JSON.stringify(battery);
                } catch {
                    console.debug('interrupted');
                }
            }
        });
        // eslint-disable-next-line
    }, []);
    return (
        <div className="App">
            <button onClick={() => animationRef.current.restart()}>Restart</button>
            <div className="el"/>
        </div>
    );

};

const MyOwnNetworkGraph = () => {
    const max = 100;
    const nodes = createMixedSpiralNodes(max);
    const links = createSequentialEdges(0);

    return (
            <Graph links={links} nodes={nodes}/>
    );
};

export default function Animations() {
    return (
        <div>
            <MyOwnNetworkGraph/>
        </div>
    );
}