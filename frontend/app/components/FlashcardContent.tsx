import React from 'react';

interface FlashcardContentProps {
    question: string;
    answer: string;
}

const FlashcardContent: React.FC<FlashcardContentProps> = ({ question, answer }) => {

    return (
        <div className="w-1/2 p-6 bg-gray-700 rounded-lg shadow-lg">
            <div dangerouslySetInnerHTML={{__html: question}}/>
            <hr className="h-px my-4 bg-gray-500 border-0"></hr>
            <div dangerouslySetInnerHTML={{__html: answer}}/>
        </div>
    );
};

export default FlashcardContent;