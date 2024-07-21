import React, { useState, useEffect } from "react";
import DifficultyFooter from "./components/DifficultyFooter";
import FlashcardContent from "./components/FlashcardContent";
import { EAnswerDifficulty } from "./api/types/EAnswerDifficulty";
import "./styles/globals.css";

// Custom hook for data fetching
const useData = () => {
    const [cardId, setCardId] = useState("");

    const getData = async () => {
        try {
            const response = await fetch(`http://${process.env.REACT_APP_BACKEND_ADDRESS}:${process.env.REACT_APP_BACKEND_PORT}/api/nextQuestion`, { cache: 'no-store'});
            const asciipage = await response.json();

            if (response.status === 404 || asciipage.id === undefined)
            {
                asciipage.id = "-1"
            }

            setCardId(asciipage.id);
        } catch (error) {
            console.error(error);
        }
    };

    useEffect(() => {
        getData();
    }, []);

    return { cardId, getData };
};

function App() {
    const { cardId, getData } = useData();

    const difficultyButtonClick = async (difficulty: EAnswerDifficulty) => {
        console.log("Answer was: " + difficulty);

        await fetch(`http://${process.env.REACT_APP_BACKEND_ADDRESS}:${process.env.REACT_APP_BACKEND_PORT}/api/answer`, {
            method: 'POST', // or 'PUT'
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ difficulty }),
            cache: 'no-store'
        });

        getData().catch(console.error);
    };

    return (
        <div>
            <main>
                <div className="flex flex-col justify-center items-center h-screen space-y-4">
                    <div className="flex justify-left w-1/2">
                        <h1 className="text-3xl justify-left font-bold mb-1">
                            Hello, World!
                        </h1>
                    </div>

                    <FlashcardContent cardId={cardId} />

                    <div className="flex justify-left w-1/2">
                        <DifficultyFooter onButtonClick={difficultyButtonClick} />
                    </div>
                </div>
            </main>
        </div>
    );
}

export default App;