package org.example.quizzapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Abstract base class for all question types in the quiz.
 * Uses Jackson annotations for proper JSON deserialization of polymorphic types.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = RadioGroupQuestion.class, name = "radiogroup"),
    @JsonSubTypes.Type(value = BooleanQuestion.class, name = "boolean")
})
public abstract class Question {
    
    @JsonProperty("type")
    protected String type;
    
    @JsonProperty("name")
    protected String name;
    
    @JsonProperty("title")
    protected String title;
    
    @JsonProperty("isRequired")
    protected boolean isRequired;
    
    /**
     * Default constructor for JSON deserialization.
     */
    public Question() {
    }
    
    /**
     * Constructor with basic parameters.
     * 
     * @param type The type of question
     * @param name The unique name/identifier for the question
     * @param title The question text displayed to the user
     * @param isRequired Whether the question must be answered
     */
    public Question(String type, String name, String title, boolean isRequired) {
        this.type = type;
        this.name = name;
        this.title = title;
        this.isRequired = isRequired;
    }
    
    /**
     * Checks if the provided answer is correct for this question.
     * 
     * @param answer The answer to check
     * @return true if the answer is correct, false otherwise
     */
    public abstract boolean isCorrectAnswer(Object answer);
    
    /**
     * Gets the correct answer for this question.
     * 
     * @return The correct answer object
     */
    public abstract Object getCorrectAnswer();
    
    // Getters and setters
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public boolean isRequired() {
        return isRequired;
    }
    
    public void setRequired(boolean required) {
        isRequired = required;
    }
    
    @Override
    public String toString() {
        return "Question{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", isRequired=" + isRequired +
                '}';
    }
}
