package org.example.quizzapp.service;

import org.example.quizzapp.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

/**
 * Unit tests for the GameManager service class.
 */
class GameManagerTest {
    
    private GameManager gameManager;
    private Quiz testQuiz;
    
    @BeforeEach
    void setUp() {
        gameManager = GameManager.getInstance();
        gameManager.resetGame(); // Ensure clean state
        
        // Create test quiz
        testQuiz = new Quiz("Test Quiz", "A test quiz");
        
        RadioGroupQuestion q1 = new RadioGroupQuestion("q1", "Question 1", "normal",
            Arrays.asList("A", "B", "C"), "B", true);
        BooleanQuestion q2 = new BooleanQuestion("q2", "Question 2", "True", "False", true, true);
        
        Page page1 = new Page(30, Arrays.asList(q1));
        Page page2 = new Page(20, Arrays.asList(q2));
        
        testQuiz.addPage(page1);
        testQuiz.addPage(page2);
    }
    
    @Test
    void testSingletonInstance() {
        GameManager instance1 = GameManager.getInstance();
        GameManager instance2 = GameManager.getInstance();
        assertSame(instance1, instance2);
    }
    
    @Test
    void testLoadQuiz() {
        gameManager.loadQuiz(testQuiz);
        assertEquals(testQuiz, gameManager.getCurrentQuiz());
        assertEquals(2, gameManager.getTotalQuestions());
        assertEquals(0, gameManager.getCurrentQuestionIndex());
        assertEquals(0, gameManager.getScore());
    }
    
    @Test
    void testPlayerName() {
        String playerName = "Test Player";
        gameManager.setPlayerName(playerName);
        assertEquals(playerName, gameManager.getPlayerName());
        assertEquals(playerName, gameManager.playerNameProperty().get());
    }
    
    @Test
    void testGetCurrentQuestion() {
        gameManager.loadQuiz(testQuiz);
        
        Question firstQuestion = gameManager.getCurrentQuestion();
        assertNotNull(firstQuestion);
        assertEquals("q1", firstQuestion.getName());
        
        // Move to next question
        gameManager.nextQuestion();
        Question secondQuestion = gameManager.getCurrentQuestion();
        assertNotNull(secondQuestion);
        assertEquals("q2", secondQuestion.getName());
        
        // Move past last question
        gameManager.nextQuestion();
        assertNull(gameManager.getCurrentQuestion());
    }
    
    @Test
    void testGetCurrentTimeLimit() {
        gameManager.loadQuiz(testQuiz);
        
        assertEquals(30, gameManager.getCurrentTimeLimit());
        
        gameManager.nextQuestion();
        assertEquals(20, gameManager.getCurrentTimeLimit());
        
        gameManager.nextQuestion();
        assertEquals(0, gameManager.getCurrentTimeLimit()); // No more questions
    }
    
    @Test
    void testSubmitAnswer() {
        gameManager.loadQuiz(testQuiz);
        
        // Submit correct answer
        boolean result1 = gameManager.submitAnswer("B");
        assertTrue(result1);
        assertEquals(1, gameManager.getScore());
        
        // Move to next question and submit incorrect answer
        gameManager.nextQuestion();
        boolean result2 = gameManager.submitAnswer(false);
        assertFalse(result2);
        assertEquals(1, gameManager.getScore()); // Score shouldn't increase
    }
    
    @Test
    void testSubmitAnswerWithNoCurrentQuestion() {
        // No quiz loaded
        boolean result = gameManager.submitAnswer("A");
        assertFalse(result);
        assertEquals(0, gameManager.getScore());
    }
    
    @Test
    void testNextQuestion() {
        gameManager.loadQuiz(testQuiz);
        
        assertEquals(0, gameManager.getCurrentQuestionIndex());
        
        boolean hasNext1 = gameManager.nextQuestion();
        assertTrue(hasNext1);
        assertEquals(1, gameManager.getCurrentQuestionIndex());
        
        boolean hasNext2 = gameManager.nextQuestion();
        assertFalse(hasNext2);
        assertEquals(2, gameManager.getCurrentQuestionIndex());
        assertTrue(gameManager.isQuizComplete());
    }
    
    @Test
    void testIsQuizComplete() {
        gameManager.loadQuiz(testQuiz);
        
        assertFalse(gameManager.isQuizComplete());
        
        gameManager.nextQuestion();
        assertFalse(gameManager.isQuizComplete());
        
        gameManager.nextQuestion();
        assertTrue(gameManager.isQuizComplete());
    }
    
    @Test
    void testScoreProperty() {
        gameManager.loadQuiz(testQuiz);
        
        assertEquals(0, gameManager.scoreProperty().get());
        
        gameManager.submitAnswer("B"); // Correct answer
        assertEquals(1, gameManager.scoreProperty().get());
        
        gameManager.nextQuestion();
        gameManager.submitAnswer(false); // Incorrect answer
        assertEquals(1, gameManager.scoreProperty().get());
    }
    
    @Test
    void testCalculateFinalScore() {
        gameManager.loadQuiz(testQuiz);
        gameManager.setPlayerName("Test Player");
        
        // Answer first question correctly
        gameManager.submitAnswer("B");
        gameManager.nextQuestion();
        
        // Answer second question incorrectly
        gameManager.submitAnswer(false);
        
        PlayerResult result = gameManager.calculateFinalScore();
        
        assertEquals("Test Player", result.getPlayerName());
        assertEquals(2, result.getTotalQuestions());
        assertEquals(1, result.getCorrectQuestions());
        assertNotNull(result.getDate());
    }
    
    @Test
    void testResetGame() {
        gameManager.loadQuiz(testQuiz);
        gameManager.setPlayerName("Test Player");
        gameManager.submitAnswer("B");
        gameManager.nextQuestion();
        
        // Verify state before reset
        assertEquals(1, gameManager.getCurrentQuestionIndex());
        assertEquals(1, gameManager.getScore());
        assertEquals("Test Player", gameManager.getPlayerName());
        
        gameManager.resetGame();
        
        // Verify state after reset
        assertEquals(0, gameManager.getCurrentQuestionIndex());
        assertEquals(0, gameManager.getScore());
        assertEquals("", gameManager.getPlayerName());
        assertTrue(gameManager.getPlayerAnswers().isEmpty());
        assertTrue(gameManager.getAnswerCorrectness().isEmpty());
    }
    
    @Test
    void testPlayerAnswersTracking() {
        gameManager.loadQuiz(testQuiz);
        
        gameManager.submitAnswer("B");
        gameManager.nextQuestion();
        gameManager.submitAnswer(true);
        
        assertEquals(2, gameManager.getPlayerAnswers().size());
        assertEquals("B", gameManager.getPlayerAnswers().get(0));
        assertEquals(true, gameManager.getPlayerAnswers().get(1));
        
        assertEquals(2, gameManager.getAnswerCorrectness().size());
        assertTrue(gameManager.getAnswerCorrectness().get(0)); // Correct
        assertTrue(gameManager.getAnswerCorrectness().get(1)); // Correct
    }
}
