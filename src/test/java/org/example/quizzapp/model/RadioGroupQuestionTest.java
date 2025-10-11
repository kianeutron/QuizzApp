package org.example.quizzapp.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

/**
 * Unit tests for the RadioGroupQuestion model class.
 */
class RadioGroupQuestionTest {
    
    private RadioGroupQuestion question;
    private List<String> choices;
    
    @BeforeEach
    void setUp() {
        choices = Arrays.asList("Option A", "Option B", "Option C", "Option D");
        question = new RadioGroupQuestion("q1", "Test Question", "random", 
                                        choices, "Option B", true);
    }
    
    @Test
    void testQuestionCreation() {
        assertNotNull(question);
        assertEquals("radiogroup", question.getType());
        assertEquals("q1", question.getName());
        assertEquals("Test Question", question.getTitle());
        assertTrue(question.isRequired());
        assertEquals("Option B", question.getCorrectAnswerString());
        assertEquals("random", question.getChoicesOrder());
    }
    
    @Test
    void testDefaultConstructor() {
        RadioGroupQuestion defaultQuestion = new RadioGroupQuestion();
        assertNotNull(defaultQuestion);
        assertNotNull(defaultQuestion.getChoices());
        assertTrue(defaultQuestion.getChoices().isEmpty());
    }
    
    @Test
    void testIsCorrectAnswer() {
        assertTrue(question.isCorrectAnswer("Option B"));
        assertFalse(question.isCorrectAnswer("Option A"));
        assertFalse(question.isCorrectAnswer("Option C"));
        assertFalse(question.isCorrectAnswer("Option D"));
        assertFalse(question.isCorrectAnswer(null));
        assertFalse(question.isCorrectAnswer(""));
    }
    
    @Test
    void testIsCorrectAnswerWithNullCorrectAnswer() {
        question.setCorrectAnswer(null);
        assertFalse(question.isCorrectAnswer("Option A"));
        assertFalse(question.isCorrectAnswer(null));
    }
    
    @Test
    void testGetCorrectAnswerObject() {
        assertEquals("Option B", question.getCorrectAnswer());
    }
    
    @Test
    void testGetChoices() {
        List<String> retrievedChoices = question.getChoices();
        assertEquals(4, retrievedChoices.size());
        assertTrue(retrievedChoices.contains("Option A"));
        assertTrue(retrievedChoices.contains("Option B"));
        assertTrue(retrievedChoices.contains("Option C"));
        assertTrue(retrievedChoices.contains("Option D"));
        
        // Test that returned list is a copy (defensive copying)
        retrievedChoices.clear();
        assertEquals(4, question.getChoices().size());
    }
    
    @Test
    void testGetShuffledChoices() {
        // Test with random order
        List<String> shuffled = question.getShuffledChoices();
        assertEquals(4, shuffled.size());
        assertTrue(shuffled.contains("Option A"));
        assertTrue(shuffled.contains("Option B"));
        assertTrue(shuffled.contains("Option C"));
        assertTrue(shuffled.contains("Option D"));
        
        // Test with non-random order
        question.setChoicesOrder("normal");
        List<String> normal = question.getShuffledChoices();
        assertEquals(choices, normal);
    }
    
    @Test
    void testGetShuffledChoicesWithNullChoices() {
        question.setChoices(null);
        List<String> result = question.getShuffledChoices();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testSetChoices() {
        List<String> newChoices = Arrays.asList("New A", "New B");
        question.setChoices(newChoices);
        
        assertEquals(2, question.getChoices().size());
        assertTrue(question.getChoices().contains("New A"));
        assertTrue(question.getChoices().contains("New B"));
    }
    
    @Test
    void testSetNullChoices() {
        question.setChoices(null);
        assertNotNull(question.getChoices());
        assertTrue(question.getChoices().isEmpty());
    }
    
    @Test
    void testChoicesOrder() {
        assertEquals("random", question.getChoicesOrder());
        
        question.setChoicesOrder("normal");
        assertEquals("normal", question.getChoicesOrder());
    }
    
    @Test
    void testToString() {
        String result = question.toString();
        assertTrue(result.contains("RadioGroupQuestion"));
        assertTrue(result.contains("q1"));
        assertTrue(result.contains("Test Question"));
        assertTrue(result.contains("Option B"));
    }
}
