import React from 'react';
import DifficultyButton from './DifficultyButton'
import { EAnswerDifficulty } from "../api/types/EAnswerDifficulty";

interface DifficultyFooterProps {
    onButtonClick: (difficulty: EAnswerDifficulty) => void; // function that accepts a string argument
}

const DifficultyFooter: React.FC<DifficultyFooterProps> = ({ onButtonClick }) => {
    return (
        <div>
            <DifficultyButton onClick={() => onButtonClick(EAnswerDifficulty.EASY)} color="difficulty-button-green">Easy</DifficultyButton>
            <DifficultyButton onClick={() => onButtonClick(EAnswerDifficulty.MEDIUM)} color="difficulty-button-yellow">Medium</DifficultyButton>
            <DifficultyButton onClick={() => onButtonClick(EAnswerDifficulty.HARD)} color="difficulty-button-red">Hard</DifficultyButton>
        </div>
    );
};

export default DifficultyFooter;