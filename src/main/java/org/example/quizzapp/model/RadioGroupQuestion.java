package org.example.quizzapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a multiple-choice question with radio button selection.
 * Supports randomization of choice order.
 */
public class RadioGroupQuestion extends Question {
    
    @JsonProperty("choicesOrder")
    private String choicesOrder;
    
    @JsonProperty("choices")
    private List<String> choices;
    
    @JsonProperty("correctAnswer")
    private String correctAnswer;
    
    /**
     * Default constructor for JSON deserialization.
     */
    public RadioGroupQuestion() {
        super();
    }
    
    /**
     * Constructor with parameters.
     * 
     * @param name The unique name/identifier for the question
     * @param title The question text
     * @param choicesOrder Order of choices ("random" or null for original order)
     * @param choices List of possible answers
     * @param correctAnswer The correct answer string
     * @param isRequired Whether the question must be answered
     */
    public RadioGroupQuestion(String name, String title, String choicesOrder, 
                             List<String> choices, String correctAnswer, boolean isRequired) {
        super("radiogroup", name, title, isRequired);
        this.choicesOrder = choicesOrder;
        this.choices = choices != null ? new ArrayList<>(choices) : new ArrayList<>();
        this.correctAnswer = correctAnswer;
    }
    
    @Override
    public boolean isCorrectAnswer(Object answer) {
        if (answer == null || correctAnswer == null) {
            return false;
        }
        return correctAnswer.equals(answer.toString());
    }
    
    @Override
    public Object getCorrectAnswer() {
        return correctAnswer;
    }
    
    /**
     * Gets the choices in their original or shuffled order based on choicesOrder setting.
     * 
     * @return List of choices, potentially shuffled
     */
    public List<String> getShuffledChoices() {
        if (choices == null) {
            return new ArrayList<>();
        }
        
        List<String> result = new ArrayList<>(choices);
        
        if ("random".equals(choicesOrder)) {
            Collections.shuffle(result);
        }
        
        return result;
    }
    
    // Getters and setters
    
    public String getChoicesOrder() {
        return choicesOrder;
    }
    
    public void setChoicesOrder(String choicesOrder) {
        this.choicesOrder = choicesOrder;
    }
    
    public List<String> getChoices() {
        return choices != null ? new ArrayList<>(choices) : new ArrayList<>();
    }
    
    public void setChoices(List<String> choices) {
        this.choices = choices != null ? new ArrayList<>(choices) : new ArrayList<>();
    }
    
    public String getCorrectAnswerString() {
        return correctAnswer;
    }
    
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
    
    @Override
    public String toString() {
        return "RadioGroupQuestion{" +
                "choicesOrder='" + choicesOrder + '\'' +
                ", choices=" + choices +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", isRequired=" + isRequired +
                '}';
    }
}
