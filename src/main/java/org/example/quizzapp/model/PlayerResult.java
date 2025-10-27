package org.example.quizzapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents the result of a single player's quiz attempt.
 * Contains player information, score, and completion timestamp.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerResult {
    
    @JsonProperty("playerName")
    private String playerName;
    
    @JsonProperty("totalQuestions")
    private int totalQuestions;
    
    @JsonProperty("correctQuestions")
    private int correctQuestions;
    
    @JsonIgnore
    private LocalDateTime date;

    @JsonProperty("date")
    private String dateString;

    // Additional fields that may be present in JSON but are computed
    @JsonProperty("score")
    private Double jsonScore; // This will be ignored, we compute it

    @JsonProperty("scoreString")
    private String jsonScoreString; // This will be ignored, we compute it
    
    /**
     * Default constructor for JSON deserialization.
     */
    public PlayerResult() {
        this.date = LocalDateTime.now();
        this.dateString = this.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }
    
    /**
     * Constructor with parameters.
     * 
     * @param playerName Name of the player
     * @param totalQuestions Total number of questions in the quiz
     * @param correctQuestions Number of questions answered correctly
     * @param date Date and time when the quiz was completed
     */
    public PlayerResult(String playerName, int totalQuestions, int correctQuestions, LocalDateTime date) {
        this.playerName = playerName;
        this.totalQuestions = totalQuestions;
        this.correctQuestions = correctQuestions;
        this.date = date;
        this.dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }
    
    /**
     * Calculates the score as a percentage.
     * 
     * @return Score percentage (0.0 to 100.0)
     */
    public double getScore() {
        if (totalQuestions == 0) {
            return 0.0;
        }
        return (double) correctQuestions / totalQuestions * 100.0;
    }
    
    /**
     * Gets the score as a percentage string without decimals for leaderboard display.
     */
    public String getScorePercentage() {
        return String.format("%d%%", (int) Math.round(getScore()));
    }
    
    /**
     * Gets the score as a formatted string.
     * 
     * @return Score string in format "X/Y (Z%)"
     */
    public String getScoreString() {
        return String.format("%d/%d (%.1f%%)", correctQuestions, totalQuestions, getScore());
    }
    
    // Getters and setters
    
    public String getPlayerName() {
        return playerName;
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    public int getTotalQuestions() {
        return totalQuestions;
    }
    
    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
    
    public int getCorrectQuestions() {
        return correctQuestions;
    }
    
    public void setCorrectQuestions(int correctQuestions) {
        this.correctQuestions = correctQuestions;
    }
    
    public LocalDateTime getDate() {
        return date;
    }
    
    public void setDate(LocalDateTime date) {
        this.date = date;
        this.dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }

    public String getDateString() {
        return dateString;
    }

    @JsonSetter("date")
    public void setDateString(String dateString) {
        this.dateString = dateString;
        if (dateString != null && !dateString.isEmpty()) {
            try {
                this.date = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            } catch (Exception e) {
                System.err.println("Error parsing date string: " + dateString + " - " + e.getMessage());
                this.date = LocalDateTime.now(); // fallback
            }
        } else {
            this.date = LocalDateTime.now(); // fallback for null/empty
        }
    }
    
    @Override
    public String toString() {
        return "PlayerResult{" +
                "playerName='" + playerName + '\'' +
                ", totalQuestions=" + totalQuestions +
                ", correctQuestions=" + correctQuestions +
                ", score=" + getScore() + "%" +
                ", date=" + date +
                '}';
    }
}
