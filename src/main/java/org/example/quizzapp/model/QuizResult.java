package org.example.quizzapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the collection of all results for a specific quiz.
 * Contains quiz metadata and all player results.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuizResult {
    
    @JsonProperty("quizId")
    private String quizId;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("results")
    private List<PlayerResult> results;
    
    @JsonProperty("numericId")
    private String numericId;
    
    /**
     * Default constructor for JSON deserialization.
     */
    public QuizResult() {
        this.results = new ArrayList<>();
    }
    
    /**
     * Constructor with parameters.
     * 
     * @param quizId Unique identifier for the quiz
     * @param name Name of the quiz
     */
    public QuizResult(String quizId, String name) {
        this();
        this.quizId = quizId;
        this.name = name;
        this.numericId = generateNumericId(quizId);
    }
    
    /**
     * Generates a numeric ID from the quiz ID using hash code.
     * 
     * @param quizId The quiz ID
     * @return A positive numeric ID
     */
    private static String generateNumericId(String quizId) {
        if (quizId == null || quizId.isEmpty()) {
            return "0";
        }
        // Use absolute value of hash code to ensure positive number
        int hashCode = Math.abs(quizId.hashCode());
        // Convert to 6-digit padded number for consistency
        return String.format("%06d", hashCode % 1000000);
    }
    
    /**
     * Adds a player result to the collection.
     * 
     * @param result The player result to add
     */
    public void addResult(PlayerResult result) {
        if (result != null) {
            if (results == null) {
                results = new ArrayList<>();
            }
            results.add(result);
        }
    }
    
    /**
     * Gets the number of results stored.
     * 
     * @return Number of player results
     */
    public int getResultCount() {
        return results != null ? results.size() : 0;
    }
    
    /**
     * Gets results sorted by score (highest first).
     * 
     * @return List of results sorted by score descending
     */
    public List<PlayerResult> getResultsSortedByScore() {
        if (results == null) {
            return new ArrayList<>();
        }
        
        List<PlayerResult> sorted = new ArrayList<>(results);
        sorted.sort((r1, r2) -> {
            // Sort by score descending, then by date descending
            int scoreCompare = Double.compare(r2.getScore(), r1.getScore());
            if (scoreCompare != 0) {
                return scoreCompare;
            }
            return r2.getDate().compareTo(r1.getDate());
        });
        
        return sorted;
    }
    
    // Getters and setters
    
    public String getQuizId() {
        return quizId;
    }
    
    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public List<PlayerResult> getResults() {
        return results != null ? new ArrayList<>(results) : new ArrayList<>();
    }
    
    public void setResults(List<PlayerResult> results) {
        this.results = results != null ? new ArrayList<>(results) : new ArrayList<>();
    }
    
    public String getNumericId() {
        if (numericId == null) {
            numericId = generateNumericId(quizId);
        }
        return numericId;
    }
    
    public void setNumericId(String numericId) {
        this.numericId = numericId;
    }
    
    @Override
    public String toString() {
        return "QuizResult{" +
                "quizId='" + quizId + '\'' +
                ", name='" + name + '\'' +
                ", results=" + (results != null ? results.size() : 0) + " results" +
                '}';
    }
}
