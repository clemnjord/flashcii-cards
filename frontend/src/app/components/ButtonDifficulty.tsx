import React from 'react';
import '../styles/ButtonDifficulty.css';

interface ButtonProps {
  onClick: () => void;
  children: React.ReactNode;
  color: string;
}

const ButtonDifficulty: React.FC<ButtonProps> = ({ onClick, children, color = 'button-difficulty' }) => {

    return (
        <button onClick={onClick} type="button" className={color}>
            {children}
        </button>
    );
};

export default ButtonDifficulty;