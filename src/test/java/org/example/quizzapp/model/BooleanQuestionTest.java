package org.example.quizzapp.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the BooleanQuestion model class.
 */
class BooleanQuestionTest {
    
    private BooleanQuestion question;
    
    @BeforeEach
    void setUp() {
        question = new BooleanQuestion("q1", "Java is object-oriented", 
                                     "True", "False", true, true);
    }
    
    @Test
    void testQuestionCreation() {
        assertNotNull(question);
        assertEquals("boolean", question.getType());
        assertEquals("q1", question.getName());
        assertEquals("Java is object-oriented", question.getTitle());
        assertTrue(question.isRequired());
        assertTrue(question.getCorrectAnswerBoolean());
        assertEquals("True", question.getLabelTrue());
        assertEquals("False", question.getLabelFalse());
    }
    
    @Test
    void testDefaultConstructor() {
        BooleanQuestion defaultQuestion = new BooleanQuestion();
        assertNotNull(defaultQuestion);
    }
    
    @Test
    void testIsCorrectAnswerWithBoolean() {
        assertTrue(question.isCorrectAnswer(true));
        assertFalse(question.isCorrectAnswer(false));
        
        // Test with false correct answer
        question.setCorrectAnswer(false);
        assertFalse(question.isCorrectAnswer(true));
        assertTrue(question.isCorrectAnswer(false));
    }
    
    @Test
    void testIsCorrectAnswerWithString() {
        assertTrue(question.isCorrectAnswer("true"));
        assertTrue(question.isCorrectAnswer("True"));
        assertTrue(question.isCorrectAnswer("TRUE"));
        assertTrue(question.isCorrectAnswer("True")); // Label match
        
        assertFalse(question.isCorrectAnswer("false"));
        assertFalse(question.isCorrectAnswer("False"));
        assertFalse(question.isCorrectAnswer("FALSE"));
        assertFalse(question.isCorrectAnswer("False")); // Label match for false
    }
    
    @Test
    void testIsCorrectAnswerWithCustomLabels() {
        BooleanQuestion customQuestion = new BooleanQuestion("q2", "Test", 
                                                           "Yes", "No", true, true);
        
        assertTrue(customQuestion.isCorrectAnswer("Yes"));
        assertTrue(customQuestion.isCorrectAnswer("yes"));
        assertTrue(customQuestion.isCorrectAnswer("true"));
        
        assertFalse(customQuestion.isCorrectAnswer("No"));
        assertFalse(customQuestion.isCorrectAnswer("no"));
        assertFalse(customQuestion.isCorrectAnswer("false"));
    }
    
    @Test
    void testIsCorrectAnswerWithNull() {
        assertFalse(question.isCorrectAnswer(null));
    }
    
    @Test
    void testIsCorrectAnswerWithInvalidString() {
        assertFalse(question.isCorrectAnswer("invalid"));
        assertFalse(question.isCorrectAnswer(""));
        assertFalse(question.isCorrectAnswer("maybe"));
    }
    
    @Test
    void testGetCorrectAnswerObject() {
        assertEquals(true, question.getCorrectAnswer());
        
        question.setCorrectAnswer(false);
        assertEquals(false, question.getCorrectAnswer());
    }
    
    @Test
    void testLabels() {
        assertEquals("True", question.getLabelTrue());
        assertEquals("False", question.getLabelFalse());
        
        question.setLabelTrue("Yes");
        question.setLabelFalse("No");
        
        assertEquals("Yes", question.getLabelTrue());
        assertEquals("No", question.getLabelFalse());
    }
    
    @Test
    void testCorrectAnswerProperty() {
        assertTrue(question.getCorrectAnswerBoolean());

        question.setCorrectAnswer(false);
        assertFalse(question.getCorrectAnswerBoolean());
    }
    
    @Test
    void testToString() {
        String result = question.toString();
        assertTrue(result.contains("BooleanQuestion"));
        assertTrue(result.contains("q1"));
        assertTrue(result.contains("Java is object-oriented"));
        assertTrue(result.contains("True"));
        assertTrue(result.contains("False"));
        assertTrue(result.contains("true")); // correctAnswer value
    }
    
    @Test
    void testAnswerMatchingWithFalseCorrectAnswer() {
        BooleanQuestion falseQuestion = new BooleanQuestion("q3", "Test false", 
                                                           "Yes", "No", false, true);
        
        assertFalse(falseQuestion.isCorrectAnswer(true));
        assertFalse(falseQuestion.isCorrectAnswer("true"));
        assertFalse(falseQuestion.isCorrectAnswer("Yes"));
        
        assertTrue(falseQuestion.isCorrectAnswer(false));
        assertTrue(falseQuestion.isCorrectAnswer("false"));
        assertTrue(falseQuestion.isCorrectAnswer("No"));
    }
}
