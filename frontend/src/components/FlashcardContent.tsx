import React, {useEffect, useState} from 'react';
import { renderAsciidoc } from '../api/render/asciidoctorRender';import {parse} from "node-html-parser";
import axios from "axios";

const extractHtml = (content: string, classId: string) => {
    const root = parse(content);
    const questionDiv = root.querySelector(classId);
    if (questionDiv) {
        return questionDiv.outerHTML;
    }
    return "";
};

interface FlashcardContentProps {
    cardId: string;
}

const FlashcardContent: React.FC<FlashcardContentProps> = ({ cardId }) => {
    const [question, setQuestion] = useState('');
    const [answer, setAnswer] = useState('');

    useEffect(() => {
        const renderCard = async () => {
            try {
                if (!cardId) {
                    return;
                }

                if (cardId === "-1")
                {
                    setQuestion("No more cards available");
                    setAnswer("");
                    return;
                }

                const questionPath = `http://${process.env.REACT_APP_BACKEND_ADDRESS}:${process.env.REACT_APP_BACKEND_PORT}/files/${cardId}/`;

                const response = await fetch(questionPath + "/card.adoc", {cache: 'no-store'});
                const fileContent = await response.text();

                const htmlContent = renderAsciidoc(fileContent, questionPath);

                const question = extractHtml(htmlContent, "#question");
                const answer = extractHtml(htmlContent, "#answer");

                setQuestion(question);
                setAnswer(answer);
            } catch (error) {
                console.error(error);
            }
        };

        renderCard();
    }, [cardId]);


    return (
        <div className="w-1/2 p-6 bg-gray-700 rounded-lg shadow-lg">
            <div dangerouslySetInnerHTML={{__html: question}}/>
            <hr className="h-px my-4 bg-gray-500 border-0"></hr>
            <div dangerouslySetInnerHTML={{__html: answer}}/>
        </div>
    );
};

export default FlashcardContent;