import React from 'react';
import '../styles/DifficultyButton.css';

interface ButtonProps {
  onClick: () => void;
  children: React.ReactNode;
  color: string;
}

const DifficultyButton: React.FC<ButtonProps> = ({ onClick, children, color = 'difficulty-button' }) => {

    return (
        <button onClick={onClick} type="button" className={color}>
        {/*<button onClick={onClick} type="button" className={color}>*/}
            {children}
        </button>
    );
};

export default DifficultyButton;