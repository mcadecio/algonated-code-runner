import React, {useEffect, useState} from 'react';
import {ResponsiveNetwork} from '@nivo/network';
import simple_data from './simple_data.json';
import Button from 'react-bootstrap/Button';


const AnyChartExample = () => {
    return (
        <>
          <App/>
        </>

    );
};


function App() {
    const [simpleData, updateData] = useState({
        ...simple_data,
        counter: 6
    });

    useEffect(() => {
        setInterval(() => {
            updateData(({nodes, links, counter}) => {

                return {
                    nodes: [...nodes, {
                        'id': `${counter + 1}`,
                        'radius': 8,
                        'depth': 1,
                        'color': 'rgb(97, 205, 187)'
                    }],
                    links: links,
                    count: counter + 1
                };
            });
        }, 1000);
    }, []);

    useEffect(() => {
        setInterval(() => {
            updateData(({nodes, links, counter}) => {

                return {
                    nodes: nodes,
                    links: [...links, {
                        "source": `${counter - 2}`,
                        "target": `${counter - 1}`
                    }],
                    count: counter
                };
            });
        }, 3000);
    }, []);

    return (
        <div className="App">
            <h1>Hello</h1>
            <div style={{height: '500px'}}>
                <MyResponsiveNetwork data={simpleData}/>
            </div>
            <Button>
                Hey
            </Button>
        </div>
    );
}

const MyResponsiveNetwork = ({data /* see data tab */}) => {
    return (
        <ResponsiveNetwork
            // data={adata}
            nodes={data.nodes}
            links={data.links}
            margin={{top: 0, right: 0, bottom: 0, left: 0}}
            repulsivity={6}
            iterations={60}
            nodeColor={function (e) {
                return e.color;
            }}
            nodeBorderWidth={1}
            nodeBorderColor={{from: 'color', modifiers: [['darker', 0.8]]}}
            linkThickness={function (e) {
                return 2 * (2 - e.source.depth);
            }}
            linkDistance={100}
            motionStiffness={160}
            motionDamping={12}
        />
    );
};


export default AnyChartExample;
