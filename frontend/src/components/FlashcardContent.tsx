import React, {useEffect, useState} from 'react';
import { renderAsciidoc } from '../api/render/asciidoctorRender';
import {parse} from "node-html-parser";

interface FlashcardContentProps {
    card: {id: string, data: string};
}

// FlashcardContent component to display the question and answer
const FlashcardContent: React.FC<FlashcardContentProps> = ({ card }) => {
    const [question, setQuestion] = useState('');
    const [answer, setAnswer] = useState('');

    useEffect(() => {
        const renderCard = async () => {
            try {
                // If no more cards are available, set default messages
                if (card.id === "-1")
                {
                    setQuestion("No more cards available");
                    setAnswer("");
                    return;
                }

                // TODO: Remove this once the backend serves pre-rendered pages
                // Render the AsciiDoc content to HTML
                const questionPath = `http://${process.env.REACT_APP_BACKEND_ADDRESS}:${process.env.REACT_APP_BACKEND_PORT}/files/${card.id}/`;
                const htmlContent = renderAsciidoc(card.data, questionPath);
                //

                // Extract and set the question and answer HTML content
                setQuestion(extractHtml(htmlContent, "#question"));
                setAnswer(extractHtml(htmlContent, "#answer"));
            } catch (error) {
                console.error(error);
            }
        };

        renderCard().catch(console.error);
    }, [card]);


    return (
        <div className="w-1/2 p-6 bg-gray-700 rounded-lg shadow-lg">
            <div dangerouslySetInnerHTML={{__html: question}}/>
            <hr className="h-px my-4 bg-gray-500 border-0"></hr>
            <div dangerouslySetInnerHTML={{__html: answer}}/>
        </div>
    );
};

export default FlashcardContent;

// Function to extract HTML content based on a given class or ID
const extractHtml = (content: string, classId: string) => {
    const root = parse(content);
    const div = root.querySelector(classId);
    if (div) {
        return div.outerHTML;
    }
    return "";
};