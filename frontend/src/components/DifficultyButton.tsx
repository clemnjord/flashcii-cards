import React from 'react';
import { Tooltip } from "flowbite-react";
import '../styles/DifficultyButton.css';

interface DifficultyButtonProps {
  onClick: () => void;
  color: string;
  tooltip: string;
  children: React.ReactNode;
}

const DifficultyButton: React.FC<DifficultyButtonProps> = ({ onClick, children, color = 'difficulty-button', tooltip }) => {

    return (
            <Tooltip content={tooltip}>
                <button onClick={onClick} type="button" className={color}>
                    {children}
                </button>
            </Tooltip>
    );
};

export default DifficultyButton;