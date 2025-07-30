package com.clemnjord.flashcii.domain.model.flashcard;

public class FlashcardFactory {

    public static Flashcard createNew(String questionText, String answerText) {
        return Flashcard.builder()
                .question(new Question(questionText))
                .answer(new Answer(answerText))
                .build();
    }

//    public static Flashcard createWithDifficulty(String questionText, String answerText, DifficultyLevel difficulty) {
//        Flashcard flashcard = createNew(questionText, answerText);
//        flashcard.setDifficulty(difficulty);
//        return flashcard;
//    }
}

