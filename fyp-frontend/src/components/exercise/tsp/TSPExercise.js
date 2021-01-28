import React from 'react';
import defaultConfig from './tsp.exercise.json';
import {distances} from './tsp.data.48.json';
import {ExercisePage} from '../ExercisePage';
import {FixedNetworkAnimation} from './TSPAnimations';

const TSPExercise = () => {

    const exerciseConfig = {
        ...defaultConfig
    };

    exerciseConfig.exercise.data = distances;

    return (
        <ExercisePage problem={{
            animation: FixedNetworkAnimation,
            ...exerciseConfig
        }}/>
    );
};


export {TSPExercise};