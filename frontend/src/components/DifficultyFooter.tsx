import React from 'react';
import DifficultyButton from './DifficultyButton'
import { EAnswerDifficulty } from "../api/types/EAnswerDifficulty";
import "../styles/DifficultyFooter.css";

interface DifficultyFooterProps {
    onButtonClick: (difficulty: EAnswerDifficulty) => void; // function that accepts a string argument
    nextOccurrence: {againTime: string, goodTime: string, easyTime: string, hardTime: string};
}

const DifficultyFooter: React.FC<DifficultyFooterProps> = ({ onButtonClick, nextOccurrence}) => {
    return (
        <div className="difficulty-footer">
            <DifficultyButton onClick={() => onButtonClick(EAnswerDifficulty.EASY)} color="difficulty-button-green" tooltip={nextOccurrence.easyTime}>Easy</DifficultyButton>
            <DifficultyButton onClick={() => onButtonClick(EAnswerDifficulty.GOOD)} color="difficulty-button-yellow" tooltip={nextOccurrence.goodTime}>Good</DifficultyButton>
            <DifficultyButton onClick={() => onButtonClick(EAnswerDifficulty.HARD)} color="difficulty-button-red" tooltip={nextOccurrence.hardTime}>Hard</DifficultyButton>
            <DifficultyButton onClick={() => onButtonClick(EAnswerDifficulty.AGAIN)} color="difficulty-button-white" tooltip={nextOccurrence.againTime}>Again</DifficultyButton>
        </div>
    );
};

export default DifficultyFooter;