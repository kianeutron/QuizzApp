package org.example.quizzapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.quizzapp.model.Quiz;
import org.example.quizzapp.model.QuizResult;
import org.example.quizzapp.model.PlayerResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

/**
 * Service class for handling quiz file operations.
 * Manages loading quizzes from JSON and saving results.
 */
public class QuizService {
    
    private final ObjectMapper objectMapper;
    private static final String RESULTS_DIRECTORY = "quiz-results";
    
    public QuizService() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // Create results directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get(RESULTS_DIRECTORY));
        } catch (IOException e) {
            System.err.println("Warning: Could not create results directory: " + e.getMessage());
        }
    }
    
    /**
     * Loads a quiz from a JSON file.
     * 
     * @param file The JSON file to load
     * @return The loaded Quiz object
     * @throws IOException If file cannot be read or parsed
     * @throws IllegalArgumentException If file content is invalid
     */
    public Quiz loadQuiz(File file) throws IOException {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("File does not exist");
        }
        
        if (!file.getName().toLowerCase().endsWith(".json")) {
            throw new IllegalArgumentException("File must be a JSON file");
        }
        
        try {
            Quiz quiz = objectMapper.readValue(file, Quiz.class);
            
            // Validate quiz content
            if (quiz.getTitle() == null || quiz.getTitle().trim().isEmpty()) {
                throw new IllegalArgumentException("Quiz must have a title");
            }
            
            if (quiz.getPages() == null || quiz.getPages().isEmpty()) {
                throw new IllegalArgumentException("Quiz must have at least one question");
            }
            
            // Validate each page has a question
            for (int i = 0; i < quiz.getPages().size(); i++) {
                if (quiz.getPage(i).getQuestion() == null) {
                    throw new IllegalArgumentException("Page " + (i + 1) + " must have a question");
                }
            }
            
            return quiz;
            
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                throw e;
            }
            throw new IOException("Failed to parse quiz file: " + e.getMessage(), e);
        }
    }
    
    /**
     * Saves a player result to the appropriate results file.
     * 
     * @param quizTitle The title of the quiz
     * @param playerResult The player's result
     * @throws IOException If file cannot be written
     */
    public void saveResult(String quizTitle, PlayerResult playerResult) throws IOException {
        String quizId = generateQuizId(quizTitle);
        String fileName = quizId + "-results.json";
        Path filePath = Paths.get(RESULTS_DIRECTORY, fileName);
        
        QuizResult quizResult;
        
        // Load existing results or create new
        if (Files.exists(filePath)) {
            try {
                quizResult = objectMapper.readValue(filePath.toFile(), QuizResult.class);
            } catch (IOException e) {
                // If file is corrupted, create new results
                quizResult = new QuizResult(quizId, quizTitle);
            }
        } else {
            quizResult = new QuizResult(quizId, quizTitle);
        }
        
        // Add new result
        quizResult.addResult(playerResult);
        
        // Save back to file
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), quizResult);
    }
    
    /**
     * Loads results for a specific quiz.
     * 
     * @param quizTitle The title of the quiz
     * @return QuizResult object, or empty one if file doesn't exist
     */
    public QuizResult loadResults(String quizTitle) {
        String quizId = generateQuizId(quizTitle);
        String fileName = quizId + "-results.json";
        Path filePath = Paths.get(RESULTS_DIRECTORY, fileName);
        
        if (Files.exists(filePath)) {
            try {
                return objectMapper.readValue(filePath.toFile(), QuizResult.class);
            } catch (IOException e) {
                System.err.println("Error loading results: " + e.getMessage());
            }
        }
        
        return new QuizResult(quizId, quizTitle);
    }
    
    /**
     * Generates a quiz ID from the quiz title.
     * 
     * @param title The quiz title
     * @return A sanitized quiz ID
     */
    private String generateQuizId(String title) {
        if (title == null || title.trim().isEmpty()) {
            return "quiz001";
        }
        
        String processedTitle = title.toLowerCase()
                                     .replaceAll("[^a-z0-9]", "");
        return processedTitle.substring(0, Math.min(processedTitle.length(), 20));
    }
}
