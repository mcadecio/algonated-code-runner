import React from 'react';
import config from './scales.exercise.json';
import {ExercisePage} from '../ExercisePage';
import {BalanceAnimation} from './ScaleAnimations';

export const ScalesExercise = () => {

    return (
        <ExercisePage problem={{
            animation: BalanceAnimation,
            ...config
        }}/>
    )
};