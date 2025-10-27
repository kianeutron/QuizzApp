package org.example.quizzapp.service;

import org.example.quizzapp.model.PlayerResult;
import org.example.quizzapp.model.QuizResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for exporting quiz results to CSV format.
 * Handles formatting and writing results to CSV files.
 */
public class CsvExporter {
    
    private static final String CSV_HEADER = "quizId;quizName;playerName;totalQuestions;correctQuestions;date";
    
    /**
     * Exports quiz results to a CSV file.
     *
     * @param quizResult The quiz results to export
     * @param filePath   The path where the CSV file should be saved
     * @throws IOException If the file cannot be written
     */
    public void exportToCsv(QuizResult quizResult, Path filePath) throws IOException {
        if (quizResult == null) {
            throw new IllegalArgumentException("Quiz result cannot be null");
        }
        
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        
        validateCsvFile(filePath);
        
        List<String> csvLines = generateCsvLines(quizResult);
        Files.write(filePath, csvLines);
    }
    
    /**
     * Generates CSV lines from quiz results.
     *
     * @param quizResult The quiz results to convert
     * @return List of CSV lines including header
     */
    private List<String> generateCsvLines(QuizResult quizResult) {
        List<String> lines = new ArrayList<>();
        
        // Add header
        lines.add(CSV_HEADER);
        
        // Add data rows
        for (PlayerResult result : quizResult.getResults()) {
            lines.add(formatResultToCsvLine(quizResult, result));
        }
        
        return lines;
    }
    
    /**
     * Formats a single player result as a CSV line.
     *
     * @param quizResult   The quiz result container
     * @param playerResult The player's individual result
     * @return Formatted CSV line
     */
    private String formatResultToCsvLine(QuizResult quizResult, PlayerResult playerResult) {
        return String.format(
            "%s;%s;%s;%d;%d;%s",
            quizResult.getNumericId(),
            escapeAndFormatCsvValue(quizResult.getName()),
            escapeAndFormatCsvValue(playerResult.getPlayerName()),
            playerResult.getTotalQuestions(),
            playerResult.getCorrectQuestions(),
            playerResult.getDateString()
        );
    }
    
    /**
     * Escapes and formats CSV values to prevent issues with special characters.
     * Wraps values in quotes if they contain semicolons or quotes.
     *
     * @param value The value to format
     * @return Properly formatted CSV value
     */
    private String escapeAndFormatCsvValue(String value) {
        if (value == null) {
            return "";
        }
        
        // If value contains semicolon or quote, wrap in quotes and escape quotes
        if (value.contains(";") || value.contains("\"")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        
        return value;
    }
    
    /**
     * Validates that the file path is valid for CSV export.
     * Ensures the parent directory exists and the file has .csv extension.
     *
     * @param filePath The file path to validate
     * @throws IOException If parent directory cannot be created
     * @throws IllegalArgumentException If file path is invalid
     */
    private void validateCsvFile(Path filePath) throws IOException {
        // Ensure parent directory exists
        Path parentDir = filePath.getParent();
        if (parentDir != null) {
            Files.createDirectories(parentDir);
        }
        
        // Ensure CSV extension
        String fileName = filePath.getFileName().toString();
        if (!fileName.toLowerCase().endsWith(".csv")) {
            throw new IllegalArgumentException("File must have .csv extension");
        }
    }
}