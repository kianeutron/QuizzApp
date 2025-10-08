package org.example.quizzapp.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

/**
 * Unit tests for the Quiz model class.
 */
class QuizTest {
    
    private Quiz quiz;
    private Page page1;
    private Page page2;
    private RadioGroupQuestion question1;
    private BooleanQuestion question2;
    
    @BeforeEach
    void setUp() {
        quiz = new Quiz();
        
        // Create test questions
        question1 = new RadioGroupQuestion("q1", "What is Java?", "random",
            Arrays.asList("Language", "Coffee", "Island", "Framework"), "Language", true);
        
        question2 = new BooleanQuestion("q2", "Java is object-oriented", "True", "False", true, true);
        
        // Create test pages
        page1 = new Page(30, Arrays.asList(question1));
        page2 = new Page(20, Arrays.asList(question2));
    }
    
    @Test
    void testQuizCreation() {
        assertNotNull(quiz);
        assertEquals(0, quiz.getTotalQuestions());
        assertNotNull(quiz.getPages());
        assertTrue(quiz.getPages().isEmpty());
    }
    
    @Test
    void testQuizWithTitleAndDescription() {
        Quiz namedQuiz = new Quiz("Test Quiz", "A test quiz");
        assertEquals("Test Quiz", namedQuiz.getTitle());
        assertEquals("A test quiz", namedQuiz.getDescription());
    }
    
    @Test
    void testAddPage() {
        quiz.addPage(page1);
        assertEquals(1, quiz.getTotalQuestions());
        assertEquals(page1, quiz.getPage(0));
    }
    
    @Test
    void testAddMultiplePages() {
        quiz.addPage(page1);
        quiz.addPage(page2);
        
        assertEquals(2, quiz.getTotalQuestions());
        assertEquals(page1, quiz.getPage(0));
        assertEquals(page2, quiz.getPage(1));
    }
    
    @Test
    void testGetQuestion() {
        quiz.addPage(page1);
        quiz.addPage(page2);
        
        assertEquals(question1, quiz.getQuestion(0));
        assertEquals(question2, quiz.getQuestion(1));
    }
    
    @Test
    void testGetPageOutOfBounds() {
        quiz.addPage(page1);
        
        assertNull(quiz.getPage(-1));
        assertNull(quiz.getPage(1));
        assertNull(quiz.getQuestion(-1));
        assertNull(quiz.getQuestion(1));
    }
    
    @Test
    void testAddNullPage() {
        quiz.addPage(null);
        assertEquals(0, quiz.getTotalQuestions());
    }
    
    @Test
    void testSetPages() {
        List<Page> pages = Arrays.asList(page1, page2);
        quiz.setPages(pages);
        
        assertEquals(2, quiz.getTotalQuestions());
        assertEquals(pages.size(), quiz.getPages().size());
    }
    
    @Test
    void testSetNullPages() {
        quiz.setPages(null);
        assertNotNull(quiz.getPages());
        assertTrue(quiz.getPages().isEmpty());
    }
    
    @Test
    void testCompletedHtmlConditions() {
        CompletedHtmlCondition condition1 = new CompletedHtmlCondition("{score} == 100", "Perfect!");
        CompletedHtmlCondition condition2 = new CompletedHtmlCondition("{score} == 0", "Try again!");
        
        quiz.setCompletedHtmlOnCondition(Arrays.asList(condition1, condition2));
        
        assertEquals(2, quiz.getCompletedHtmlOnCondition().size());
        assertEquals(condition1.getExpression(), quiz.getCompletedHtmlOnCondition().get(0).getExpression());
        assertEquals(condition2.getHtml(), quiz.getCompletedHtmlOnCondition().get(1).getHtml());
    }
    
    @Test
    void testToString() {
        quiz.setTitle("Test Quiz");
        quiz.addPage(page1);
        
        String result = quiz.toString();
        assertTrue(result.contains("Test Quiz"));
        assertTrue(result.contains("1 pages"));
    }
}
