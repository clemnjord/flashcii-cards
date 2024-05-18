"use client"

import { useState, useEffect } from "react";
import DifficultyFooter from "./components/DifficultyFooter";
import FlashcardContent from "./components/FlashcardContent";
import { EAnswerDifficulty } from "./api/types/EAnswerDifficulty";

// Custom hook for data fetching
const useData = () => {
  const [question, setQuestion] = useState<string>("");
  const [answer, setAnswer] = useState<string>("");

  const getData = async () => {
    try {
      let response = await fetch(`/api/next`);
      let asciipage = await response.json();

      setQuestion(asciipage.question);
      setAnswer(asciipage.answer);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    getData();
  }, []);

  return { question, answer, getData };
};

export default function Index() {
  const { question, answer, getData } = useData();

  const difficultyButtonClick = async (difficulty: EAnswerDifficulty) => {
    console.log("Answer was: " + difficulty);

    await fetch(`/api/answer/?difficulty=${difficulty}`, {cache: 'no-store'});

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

            <FlashcardContent question={question} answer={answer} />

            <div className="flex justify-left w-1/2">
              <DifficultyFooter onButtonClick={difficultyButtonClick} />
            </div>
          </div>
        </main>
      </div>
  );
}