package org.example.quizzapp.service;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import org.example.quizzapp.model.Quiz;
import org.example.quizzapp.model.Question;
import org.example.quizzapp.model.PlayerResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton GameManager that manages the current game state.
 * Implements the Singleton pattern and Observer pattern through JavaFX properties.
 */
public class GameManager {
    
    private static GameManager instance;
    
    private Quiz currentQuiz;
    private final StringProperty playerName = new SimpleStringProperty("");
    private int currentQuestionIndex = 0;
    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final List<Object> playerAnswers = new ArrayList<>();
    private final List<Boolean> answerCorrectness = new ArrayList<>();
    private boolean isPracticeMode = false;
    
    /**
     * Private constructor for singleton pattern.
     */
    private GameManager() {
    }
    
    /**
     * Gets the singleton instance of GameManager.
     * 
     * @return The GameManager instance
     */
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }
    
    /**
     * Sets the practice mode flag.
     * 
     * @param practiceMode true for practice mode, false for normal mode
     */
    public void setPracticeMode(boolean practiceMode) {
        this.isPracticeMode = practiceMode;
    }
    
    /**
     * Checks if the current game is in practice mode.
     * 
     * @return true if in practice mode, false otherwise
     */
    public boolean isPracticeMode() {
        return isPracticeMode;
    }
    
    /**
     * Loads a quiz and resets game state.
     * 
     * @param quiz The quiz to load
     */
    public void loadQuiz(Quiz quiz) {
        this.currentQuiz = quiz;
        resetGame();
    }
    
    /**
     * Resets the game state for a new game.
     */
    public void resetGame() {
        currentQuestionIndex = 0;
        score.set(0);
        playerAnswers.clear();
        answerCorrectness.clear();
        playerName.set("");
        isPracticeMode = false;
    }
    
    /**
     * Sets the player name.
     * 
     * @param name The player's name
     */
    public void setPlayerName(String name) {
        this.playerName.set(name);
    }
    
    /**
     * Gets the player name property for binding.
     * 
     * @return StringProperty for player name
     */
    public StringProperty playerNameProperty() {
        return playerName;
    }
    
    /**
     * Gets the current player name.
     * 
     * @return The player's name
     */
    public String getPlayerName() {
        return playerName.get();
    }
    
    /**
     * Gets the current question.
     * 
     * @return The current Question object, or null if no more questions
     */
    public Question getCurrentQuestion() {
        if (currentQuiz == null || currentQuestionIndex >= currentQuiz.getTotalQuestions()) {
            return null;
        }
        return currentQuiz.getQuestion(currentQuestionIndex);
    }
    
    /**
     * Gets the current question index.
     * 
     * @return The current question index (0-based)
     */
    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }
    
    /**
     * Gets the total number of questions.
     * 
     * @return Total question count
     */
    public int getTotalQuestions() {
        return currentQuiz != null ? currentQuiz.getTotalQuestions() : 0;
    }
    
    /**
     * Gets the time limit for the current question.
     * 
     * @return Time limit in seconds, or 0 if no current question
     */
    public int getCurrentTimeLimit() {
        if (currentQuiz == null || currentQuestionIndex >= currentQuiz.getTotalQuestions()) {
            return 0;
        }
        return currentQuiz.getPage(currentQuestionIndex).getTimeLimit();
    }
    
    /**
     * Submits an answer for the current question.
     * 
     * @param answer The player's answer
     * @return true if the answer was correct, false otherwise
     */
    public boolean submitAnswer(Object answer) {
        Question currentQuestion = getCurrentQuestion();
        if (currentQuestion == null) {
            return false;
        }
        
        boolean isCorrect = currentQuestion.isCorrectAnswer(answer);
        
        // Store the answer and correctness
        playerAnswers.add(answer);
        answerCorrectness.add(isCorrect);
        
        // Update score if correct
        if (isCorrect) {
            score.set(score.get() + 1);
        }
        
        return isCorrect;
    }
    
    /**
     * Moves to the next question.
     * 
     * @return true if there is a next question, false if quiz is complete
     */
    public boolean nextQuestion() {
        currentQuestionIndex++;
        return currentQuestionIndex < getTotalQuestions();
    }
    
    /**
     * Checks if the quiz is complete.
     * 
     * @return true if all questions have been answered
     */
    public boolean isQuizComplete() {
        return currentQuestionIndex >= getTotalQuestions();
    }
    
    /**
     * Gets the current score.
     * 
     * @return The current score
     */
    public int getScore() {
        return score.get();
    }
    
    /**
     * Gets the score property for binding.
     * 
     * @return IntegerProperty for score
     */
    public IntegerProperty scoreProperty() {
        return score;
    }
    
    /**
     * Gets the current quiz.
     * 
     * @return The current Quiz object
     */
    public Quiz getCurrentQuiz() {
        return currentQuiz;
    }
    
    /**
     * Calculates and returns the final player result.
     * 
     * @return PlayerResult object with final scores
     */
    public PlayerResult calculateFinalScore() {
        return new PlayerResult(
            getPlayerName(),
            getTotalQuestions(),
            getScore(),
            LocalDateTime.now()
        );
    }
    
    /**
     * Gets the list of player answers.
     * 
     * @return List of answers given by the player
     */
    public List<Object> getPlayerAnswers() {
        return new ArrayList<>(playerAnswers);
    }
    
    /**
     * Gets the list of answer correctness.
     * 
     * @return List of boolean values indicating if each answer was correct
     */
    public List<Boolean> getAnswerCorrectness() {
        return new ArrayList<>(answerCorrectness);
    }
}
