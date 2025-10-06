package org.example.quizzapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a page in the quiz containing questions and timing information.
 * Each page has a time limit and contains one or more questions (elements).
 */
public class Page {
    
    @JsonProperty("timeLimit")
    private int timeLimit;
    
    @JsonProperty("elements")
    private List<Question> elements;
    
    /**
     * Default constructor for JSON deserialization.
     */
    public Page() {
        this.elements = new ArrayList<>();
    }
    
    /**
     * Constructor with parameters.
     * 
     * @param timeLimit Time limit for this page in seconds
     * @param elements List of questions on this page
     */
    public Page(int timeLimit, List<Question> elements) {
        this.timeLimit = timeLimit;
        this.elements = elements != null ? new ArrayList<>(elements) : new ArrayList<>();
    }
    
    /**
     * Gets the first question on this page.
     * According to assignment, there's always only one question per page.
     * 
     * @return The first question, or null if no questions exist
     */
    public Question getQuestion() {
        return elements.isEmpty() ? null : elements.get(0);
    }
    
    /**
     * Adds a question to this page.
     * 
     * @param question The question to add
     */
    public void addQuestion(Question question) {
        if (question != null) {
            elements.add(question);
        }
    }
    
    // Getters and setters
    
    public int getTimeLimit() {
        return timeLimit;
    }
    
    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }
    
    public List<Question> getElements() {
        return elements != null ? new ArrayList<>(elements) : new ArrayList<>();
    }
    
    public void setElements(List<Question> elements) {
        this.elements = elements != null ? new ArrayList<>(elements) : new ArrayList<>();
    }
    
    @Override
    public String toString() {
        return "Page{" +
                "timeLimit=" + timeLimit +
                ", elements=" + elements +
                '}';
    }
}
