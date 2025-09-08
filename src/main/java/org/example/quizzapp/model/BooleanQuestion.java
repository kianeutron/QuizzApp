package org.example.quizzapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a true/false question with boolean answer.
 * Supports custom labels for true and false options.
 */
public class BooleanQuestion extends Question {
    
    @JsonProperty("labelTrue")
    private String labelTrue;
    
    @JsonProperty("labelFalse")
    private String labelFalse;
    
    @JsonProperty("correctAnswer")
    private boolean correctAnswer;
    
    /**
     * Default constructor for JSON deserialization.
     */
    public BooleanQuestion() {
        super();
    }
    
    /**
     * Constructor with parameters.
     * 
     * @param name The unique name/identifier for the question
     * @param title The question text
     * @param labelTrue Label for the true option
     * @param labelFalse Label for the false option
     * @param correctAnswer The correct boolean answer
     * @param isRequired Whether the question must be answered
     */
    public BooleanQuestion(String name, String title, String labelTrue, 
                          String labelFalse, boolean correctAnswer, boolean isRequired) {
        super("boolean", name, title, isRequired);
        this.labelTrue = labelTrue;
        this.labelFalse = labelFalse;
        this.correctAnswer = correctAnswer;
    }
    
    @Override
    public boolean isCorrectAnswer(Object answer) {
        if (answer == null) {
            return false;
        }
        
        // Handle different answer formats
        if (answer instanceof Boolean) {
            return correctAnswer == (Boolean) answer;
        }
        
        if (answer instanceof String) {
            String answerStr = answer.toString().toLowerCase();
            if ("true".equals(answerStr) || labelTrue.equalsIgnoreCase(answerStr)) {
                return correctAnswer;
            } else if ("false".equals(answerStr) || labelFalse.equalsIgnoreCase(answerStr)) {
                return !correctAnswer;
            }
        }
        
        return false;
    }
    
    @Override
    public Object getCorrectAnswer() {
        return correctAnswer;
    }
    
    // Getters and setters
    
    public String getLabelTrue() {
        return labelTrue;
    }
    
    public void setLabelTrue(String labelTrue) {
        this.labelTrue = labelTrue;
    }
    
    public String getLabelFalse() {
        return labelFalse;
    }
    
    public void setLabelFalse(String labelFalse) {
        this.labelFalse = labelFalse;
    }
    
    public boolean getCorrectAnswerBoolean() {
        return correctAnswer;
    }
    
    public void setCorrectAnswer(boolean correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
    
    @Override
    public String toString() {
        return "BooleanQuestion{" +
                "labelTrue='" + labelTrue + '\'' +
                ", labelFalse='" + labelFalse + '\'' +
                ", correctAnswer=" + correctAnswer +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", isRequired=" + isRequired +
                '}';
    }
}
