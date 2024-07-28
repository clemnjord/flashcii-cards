import React, { useState, useEffect } from "react";
import DifficultyFooter from "./components/DifficultyFooter";
import FlashcardContent from "./components/FlashcardContent";
import { EAnswerDifficulty } from "./api/types/EAnswerDifficulty";
import "./styles/globals.css";

function App() {
    const { card, getData } = useData();

    // Function to handle difficulty button clicks
    const difficultyButtonClick = async (difficulty: EAnswerDifficulty) => {
        console.log("Answer was: " + difficulty);

        // Send the selected difficulty to the backend
        await fetch(`http://${process.env.REACT_APP_BACKEND_ADDRESS}:${process.env.REACT_APP_BACKEND_PORT}/api/answer`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ cardId: card.id, difficulty: difficulty }),
            cache: 'no-store'
        });

        // Fetch the next question
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

                    <FlashcardContent card={card} />

                    {card.id !== "-1" ? (
                        <div className="flex justify-left w-1/2">
                            <DifficultyFooter onButtonClick={difficultyButtonClick} />
                        </div>
                    ) : (
                        <div className="flex justify-left w-1/2">
                            <button onClick={getData} className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
                                Get next question
                            </button>
                        </div>
                    )}
                </div>
            </main>
        </div>
    );
}

export default App;


// Custom hook for data fetching
const useData = () => {
    const [card, setCard] = useState({id: "-1", data: "None"});

    const getData = async () => {
        try {
            const response = await fetch(`http://${process.env.REACT_APP_BACKEND_ADDRESS}:${process.env.REACT_APP_BACKEND_PORT}/api/nextQuestion`, { cache: 'no-store'});
            let asciipage = await response.json();

            // Check if the response status is 404 or if the asciipage id is undefined
            if (response.status === 404 || asciipage.id === undefined)
            {
                asciipage.id = "-1"
                asciipage.data = ""
            }

            setCard({id: asciipage.id, data: asciipage.data});
        } catch (error) {
            console.error(error);
        }
    };

    useEffect(() => {
        getData();
    }, []);

    return { card, getData };
};