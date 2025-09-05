package org.example.quizzapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a complete quiz with metadata, pages, and completion conditions.
 * This is the root model class that contains all quiz data.
 */
public class Quiz {
    
    @JsonProperty("title")
    private String title;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("pages")
    private List<Page> pages;
    
    @JsonProperty("completedHtml")
    private String completedHtml;
    
    @JsonProperty("completedHtmlOnCondition")
    private List<CompletedHtmlCondition> completedHtmlOnCondition;
    
    /**
     * Default constructor for JSON deserialization.
     */
    public Quiz() {
        this.pages = new ArrayList<>();
        this.completedHtmlOnCondition = new ArrayList<>();
    }
    
    /**
     * Constructor with parameters.
     * 
     * @param title The quiz title
     * @param description The quiz description
     */
    public Quiz(String title, String description) {
        this();
        this.title = title;
        this.description = description;
    }
    
    /**
     * Gets the total number of questions in the quiz.
     * 
     * @return Total question count
     */
    public int getTotalQuestions() {
        return pages != null ? pages.size() : 0;
    }
    
    /**
     * Gets a specific page by index.
     * 
     * @param index The page index (0-based)
     * @return The page at the specified index, or null if index is invalid
     */
    public Page getPage(int index) {
        if (pages == null || index < 0 || index >= pages.size()) {
            return null;
        }
        return pages.get(index);
    }
    
    /**
     * Gets a question by page index.
     * 
     * @param pageIndex The page index (0-based)
     * @return The question on the specified page, or null if not found
     */
    public Question getQuestion(int pageIndex) {
        Page page = getPage(pageIndex);
        return page != null ? page.getQuestion() : null;
    }
    
    /**
     * Adds a page to the quiz.
     * 
     * @param page The page to add
     */
    public void addPage(Page page) {
        if (page != null) {
            if (pages == null) {
                pages = new ArrayList<>();
            }
            pages.add(page);
        }
    }
    
    // Getters and setters
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public List<Page> getPages() {
        return pages != null ? new ArrayList<>(pages) : new ArrayList<>();
    }
    
    public void setPages(List<Page> pages) {
        this.pages = pages != null ? new ArrayList<>(pages) : new ArrayList<>();
    }
    
    public String getCompletedHtml() {
        return completedHtml;
    }
    
    public void setCompletedHtml(String completedHtml) {
        this.completedHtml = completedHtml;
    }
    
    public List<CompletedHtmlCondition> getCompletedHtmlOnCondition() {
        return completedHtmlOnCondition != null ? 
               new ArrayList<>(completedHtmlOnCondition) : new ArrayList<>();
    }
    
    public void setCompletedHtmlOnCondition(List<CompletedHtmlCondition> completedHtmlOnCondition) {
        this.completedHtmlOnCondition = completedHtmlOnCondition != null ? 
                                       new ArrayList<>(completedHtmlOnCondition) : new ArrayList<>();
    }
    
    @Override
    public String toString() {
        return "Quiz{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", pages=" + (pages != null ? pages.size() : 0) + " pages" +
                '}';
    }
}
