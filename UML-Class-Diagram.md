# Quiz Application UML Class Diagram

This document contains the UML class diagram for the Quiz Application, showing the relationships between all model classes and design patterns used.

## Class Diagram

```mermaid
classDiagram
    class Quiz {
        -String title
        -String description
        -List~Page~ pages
        -String completedHtml
        -List~CompletedHtmlCondition~ completedHtmlOnCondition
        +Quiz()
        +getTitle() String
        +setTitle(String)
        +getDescription() String
        +setDescription(String)
        +getPages() List~Page~
        +setPages(List~Page~)
        +getCompletedHtml() String
        +setCompletedHtml(String)
        +getCompletedHtmlOnCondition() List~CompletedHtmlCondition~
        +setCompletedHtmlOnCondition(List~CompletedHtmlCondition~)
    }

    class Page {
        -int timeLimit
        -List~Question~ elements
        +Page()
        +getTimeLimit() int
        +setTimeLimit(int)
        +getElements() List~Question~
        +setElements(List~Question~)
    }

    class Question {
        <<abstract>>
        -String type
        -String name
        -String title
        -boolean isRequired
        +Question()
        +getType() String
        +setType(String)
        +getName() String
        +setName(String)
        +getTitle() String
        +setTitle(String)
        +isRequired() boolean
        +setRequired(boolean)
        +isCorrectAnswer(Object answer)* boolean
        +getCorrectAnswer()* Object
    }

    class RadioGroupQuestion {
        -String choicesOrder
        -List~String~ choices
        -String correctAnswer
        +RadioGroupQuestion()
        +getChoicesOrder() String
        +setChoicesOrder(String)
        +getChoices() List~String~
        +setChoices(List~String~)
        +getCorrectAnswer() String
        +setCorrectAnswer(String)
        +isCorrectAnswer(Object answer) boolean
        +getShuffledChoices() List~String~
    }

    class BooleanQuestion {
        -String labelTrue
        -String labelFalse
        -boolean correctAnswer
        +BooleanQuestion()
        +getLabelTrue() String
        +setLabelTrue(String)
        +getLabelFalse() String
        +setLabelFalse(String)
        +getCorrectAnswer() boolean
        +setCorrectAnswer(boolean)
        +isCorrectAnswer(Object answer) boolean
    }

    class CompletedHtmlCondition {
        -String expression
        -String html
        +CompletedHtmlCondition()
        +getExpression() String
        +setExpression(String)
        +getHtml() String
        +setHtml(String)
    }

    class QuizResult {
        -String quizId
        -String name
        -List~PlayerResult~ results
        +QuizResult()
        +getQuizId() String
        +setQuizId(String)
        +getName() String
        +setName(String)
        +getResults() List~PlayerResult~
        +setResults(List~PlayerResult~)
        +addResult(PlayerResult)
    }

    class PlayerResult {
        -String playerName
        -int totalQuestions
        -int correctQuestions
        -LocalDateTime date
        +PlayerResult()
        +getPlayerName() String
        +setPlayerName(String)
        +getTotalQuestions() int
        +setTotalQuestions(int)
        +getCorrectQuestions() int
        +setCorrectQuestions(int)
        +getDate() LocalDateTime
        +setDate(LocalDateTime)
        +getScore() double
    }

    class QuestionFactory {
        <<singleton>>
        -QuestionFactory instance
        +getInstance() QuestionFactory
        +createQuestion(String type) Question
    }

    class GameManager {
        <<singleton>>
        -GameManager instance
        -Quiz currentQuiz
        -String playerName
        -int currentQuestionIndex
        -IntegerProperty score
        -List~Object~ playerAnswers
        +getInstance() GameManager
        +loadQuiz(Quiz)
        +setPlayerName(String)
        +getCurrentQuestion() Question
        +submitAnswer(Object)
        +nextQuestion() boolean
        +isQuizComplete() boolean
        +getScore() int
        +scoreProperty() IntegerProperty
        +calculateFinalScore() PlayerResult
    }

    Quiz ||--o{ Page : contains
    Page ||--o{ Question : contains
    Question <|-- RadioGroupQuestion : extends
    Question <|-- BooleanQuestion : extends
    Quiz ||--o{ CompletedHtmlCondition : contains
    QuizResult ||--o{ PlayerResult : contains
    QuestionFactory ..> Question : creates
    GameManager --> Quiz : manages
    GameManager --> Question : tracks current
```

## Design Patterns Used

1. **Factory Pattern**: `QuestionFactory` creates different types of questions based on the type string
2. **Singleton Pattern**: `GameManager` ensures single instance for game state management
3. **Observer Pattern**: JavaFX property binding for automatic UI updates when score changes
4. **MVC Pattern**: Separation of Model (data classes), View (FXML), and Controller (JavaFX controllers)

## Key Relationships

- Quiz contains multiple Pages
- Each Page contains multiple Questions (elements)
- Question is abstract with RadioGroupQuestion and BooleanQuestion implementations
- QuizResult manages multiple PlayerResult entries
- GameManager acts as central coordinator for game state
- QuestionFactory creates appropriate Question instances based on type
