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
            <DifficultyButton onClick={() => onButtonClick(EAnswerDifficulty.GOOD)} color="difficulty-button-yellow">Good</DifficultyButton>
            <DifficultyButton onClick={() => onButtonClick(EAnswerDifficulty.HARD)} color="difficulty-button-red">Hard</DifficultyButton>
            <DifficultyButton onClick={() => onButtonClick(EAnswerDifficulty.AGAIN)} color="difficulty-button">Again</DifficultyButton>
        </div>
    );
};

export default DifficultyFooter;