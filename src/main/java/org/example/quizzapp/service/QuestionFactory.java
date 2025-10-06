package org.example.quizzapp.service;

import org.example.quizzapp.model.Question;
import org.example.quizzapp.model.RadioGroupQuestion;
import org.example.quizzapp.model.BooleanQuestion;

/**
 * Factory class for creating Question objects based on type.
 * Implements the Factory pattern as a singleton.
 */
public class QuestionFactory {
    
    private static QuestionFactory instance;
    
    /**
     * Private constructor for singleton pattern.
     */
    private QuestionFactory() {
    }
    
    /**
     * Gets the singleton instance of QuestionFactory.
     * 
     * @return The QuestionFactory instance
     */
    public static QuestionFactory getInstance() {
        if (instance == null) {
            instance = new QuestionFactory();
        }
        return instance;
    }
    
    /**
     * Creates a Question object based on the specified type.
     * 
     * @param type The type of question to create ("radiogroup" or "boolean")
     * @return A new Question instance of the appropriate type
     * @throws IllegalArgumentException If the question type is not supported
     */
    public Question createQuestion(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Question type cannot be null");
        }
        
        switch (type.toLowerCase()) {
            case "radiogroup":
                return new RadioGroupQuestion();
            case "boolean":
                return new BooleanQuestion();
            default:
                throw new IllegalArgumentException("Unsupported question type: " + type);
        }
    }
    
    /**
     * Creates a Question object with basic properties set.
     * 
     * @param type The type of question to create
     * @param name The question name/identifier
     * @param title The question title/text
     * @param isRequired Whether the question is required
     * @return A new Question instance with properties set
     * @throws IllegalArgumentException If the question type is not supported
     */
    public Question createQuestion(String type, String name, String title, boolean isRequired) {
        Question question = createQuestion(type);
        question.setName(name);
        question.setTitle(title);
        question.setRequired(isRequired);
        return question;
    }
    
    /**
     * Checks if a question type is supported.
     * 
     * @param type The question type to check
     * @return true if the type is supported, false otherwise
     */
    public boolean isTypeSupported(String type) {
        if (type == null) {
            return false;
        }
        
        String lowerType = type.toLowerCase();
        return "radiogroup".equals(lowerType) || "boolean".equals(lowerType);
    }
    
    /**
     * Gets all supported question types.
     * 
     * @return Array of supported question type strings
     */
    public String[] getSupportedTypes() {
        return new String[]{"radiogroup", "boolean"};
    }
}
