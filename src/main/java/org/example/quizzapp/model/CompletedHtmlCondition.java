package org.example.quizzapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a conditional HTML message based on quiz completion results.
 * Used to display different messages based on the player's performance.
 */
public class CompletedHtmlCondition {
    
    @JsonProperty("expression")
    private String expression;
    
    @JsonProperty("html")
    private String html;
    
    /**
     * Default constructor for JSON deserialization.
     */
    public CompletedHtmlCondition() {
    }
    
    /**
     * Constructor with parameters.
     * 
     * @param expression The condition expression to evaluate
     * @param html The HTML content to display when condition is met
     */
    public CompletedHtmlCondition(String expression, String html) {
        this.expression = expression;
        this.html = html;
    }
    
    /**
     * Gets the condition expression.
     * 
     * @return The expression string
     */
    public String getExpression() {
        return expression;
    }
    
    /**
     * Sets the condition expression.
     * 
     * @param expression The expression to set
     */
    public void setExpression(String expression) {
        this.expression = expression;
    }
    
    /**
     * Gets the HTML content.
     * 
     * @return The HTML string
     */
    public String getHtml() {
        return html;
    }
    
    /**
     * Sets the HTML content.
     * 
     * @param html The HTML content to set
     */
    public void setHtml(String html) {
        this.html = html;
    }
    
    @Override
    public String toString() {
        return "CompletedHtmlCondition{" +
                "expression='" + expression + '\'' +
                ", html='" + html + '\'' +
                '}';
    }
}
