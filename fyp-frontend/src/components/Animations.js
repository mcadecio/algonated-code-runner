import React, {useEffect, useState} from 'react';
import '../App.css';
import './anime.css'
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