// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.*;
// import java.util.List;
// import java.util.Timer;
// import java.util.TimerTask;

// /**
//  * Main quiz screen that displays questions and handles user interaction
//  */
// public class Quiz extends JFrame {
//     private UserProfile userProfile;
//     private String domain;
//     private String difficulty;
//     private QuestionBank questionBank;
//     private int questionCount;
//     private int timePerQuestion;
//     private boolean reviewAnswers;
//     private boolean showExplanations;
//     private boolean soundEffects;
    
//     private List<Question> questions;
//     private int currentQuestionIndex;
//     private int correctAnswers;
//     private long startTime;
//     private long questionStartTime;
//     private Timer questionTimer;
//     private int timeRemaining;
//     private int totalScore;
    
//     // UI Components
//     private JLabel questionNumberLabel;
//     private JLabel questionTextLabel;
//     private JPanel optionsPanel;
//     private JButton[] optionButtons;
//     private JLabel timerLabel;
//     private JLabel scoreLabel;
//     private JProgressBar progressBar;
//     private JButton nextButton;
    
//     public Quiz(UserProfile userProfile, String domain, String difficulty,
//                QuestionBank questionBank, int questionCount, int timePerQuestion,
//                boolean reviewAnswers, boolean showExplanations, boolean soundEffects) {
        
//         this.userProfile = userProfile;
//         this.domain = domain;
//         this.difficulty = difficulty;
//         this.questionBank = questionBank;
//         this.questionCount = questionCount;
//         this.timePerQuestion = timePerQuestion;
//         this.reviewAnswers = reviewAnswers;
//         this.showExplanations = showExplanations;
//         this.soundEffects = soundEffects;
        
//         // Initialize quiz state
//         this.currentQuestionIndex = 0;
//         this.correctAnswers = 0;
//         this.totalScore = 0;
        
//         // Load questions
//         this.questions = questionBank.getQuestionsForDomain(domain, difficulty, questionCount);
        
//         // Set up the frame
//         setTitle("Quiz - " + domain + " (" + difficulty + ")");
//         setSize(800, 600);
//         setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//         setLocationRelativeTo(null);
        
//         // Handle window closing
//         addWindowListener(new WindowAdapter() {
//             @Override
//             public void windowClosing(WindowEvent e) {
//                 confirmExit();
//             }
//         });
        
//         initComponents();
//         applyTheme(userProfile.getPreferredTheme());
//     }
    
//     private void initComponents() {
//         // Main layout
//         JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
//         mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
//         // Header panel
//         JPanel headerPanel = new JPanel(new BorderLayout());
        
//         // Question number and progress
//         questionNumberLabel = new JLabel("Question 1 of " + questionCount);
//         questionNumberLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
//         // Score label
//         scoreLabel = new JLabel("Score: 0");
//         scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
//         // Timer label
//         timerLabel = new JLabel("Time: " + timePerQuestion + "s");
//         timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
//         timerLabel.setHorizontalAlignment(JLabel.RIGHT);
        
//         // Progress bar
//         progressBar = new JProgressBar(0, questionCount);
//         progressBar.setValue(1);
//         progressBar.setStringPainted(true);
        
//         // Add components to header
//         JPanel headerTopPanel = new JPanel(new BorderLayout());
//         headerTopPanel.add(questionNumberLabel, BorderLayout.WEST);
//         headerTopPanel.add(scoreLabel, BorderLayout.CENTER);
//         headerTopPanel.add(timerLabel, BorderLayout.EAST);
        
//         headerPanel.add(headerTopPanel, BorderLayout.NORTH);
//         headerPanel.add(progressBar, BorderLayout.SOUTH);
        
//         // Question panel
//         JPanel questionPanel = new JPanel(new BorderLayout(10, 20));
//         questionPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
//         questionTextLabel = new JLabel();
//         questionTextLabel.setFont(new Font("Arial", Font.BOLD, 18));
//         JScrollPane questionScrollPane = new JScrollPane(questionTextLabel);
//         questionScrollPane.setBorder(null);
//         questionPanel.add(questionScrollPane, BorderLayout.NORTH);
        
//         // Options panel
//         optionsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
//         optionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
//         // Create option buttons (maximum of 6 possible options)
//         optionButtons = new JButton[6];
//         for (int i = 0; i < optionButtons.length; i++) {
//             final int optionIndex = i;
//             optionButtons[i] = new JButton();
//             optionButtons[i].setFont(new Font("Arial", Font.PLAIN, 16));
//             optionButtons[i].addActionListener(e -> selectAnswer(optionIndex));
//             optionButtons[i].setFocusPainted(false);
//             optionsPanel.add(optionButtons[i]);
//         }
        
//         // Wrap options in scroll pane in case there are many options
//         JScrollPane optionsScrollPane = new JScrollPane(optionsPanel);
//         optionsScrollPane.setBorder(null);
//         questionPanel.add(optionsScrollPane, BorderLayout.CENTER);
        
//         // Next button panel
//         JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//         nextButton = new JButton("Next");
//         nextButton.setFont(new Font("Arial", Font.BOLD, 16));
//         nextButton.addActionListener(e -> nextQuestion());
//         nextButton.setEnabled(false);
//         buttonPanel.add(nextButton);
        
//         // Add components to main panel
//         mainPanel.add(headerPanel, BorderLayout.NORTH);
//         mainPanel.add(questionPanel, BorderLayout.CENTER);
//         mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
//         // Add to frame
//         add(mainPanel);
//     }
    
//     public void startQuiz() {
//         // Show the quiz window
//         setVisible(true);
        
//         // Record the start time
//         startTime = System.currentTimeMillis();
        
//         // Display the first question
//         displayCurrentQuestion();
//     }
    
//     private void displayCurrentQuestion() {
//         // Get current question
//         Question currentQuestion = questions.get(currentQuestionIndex);
        
//         // Update question number label
//         questionNumberLabel.setText("Question " + (currentQuestionIndex + 1) + " of " + questionCount);
        
//         // Update progress bar
//         progressBar.setValue(currentQuestionIndex + 1);
        
//         // Update question text
//         questionTextLabel.setText("<html><body style='width: 500px'>" + 
//                                  currentQuestion.getText() + "</body></html>");
        
//         // Update option buttons
//         List<String> options = currentQuestion.getOptions();
//         for (int i = 0; i < optionButtons.length; i++) {
//             if (i < options.size()) {
//                 optionButtons[i].setText(options.get(i));
//                 optionButtons[i].setEnabled(true);
//                 optionButtons[i].setBackground(UIManager.getColor("Button.background"));
//                 optionButtons[i].setVisible(true);
//             } else {
//                 optionButtons[i].setVisible(false);
//             }
//         }
        
//         // Disable next button until an answer is selected
//         nextButton.setEnabled(false);
        
//         // Reset and start timer
//         startQuestionTimer();
//     }
    
//     private void startQuestionTimer() {
//         // Cancel existing timer if running
//         if (questionTimer != null) {
//             questionTimer.cancel();
//         }
        
//         // Reset time remaining
//         timeRemaining = timePerQuestion;
//         timerLabel.setText("Time: " + timeRemaining + "s");
        
//         // Store question start time
//         questionStartTime = System.currentTimeMillis();
        
//         // Create new timer
//         questionTimer = new Timer();
//         questionTimer.scheduleAtFixedRate(new TimerTask() {
//             @Override
//             public void run() {
//                 timeRemaining--;
                
//                 // Update timer label on EDT
//                 SwingUtilities.invokeLater(() -> {
//                     timerLabel.setText("Time: " + timeRemaining + "s");
                    
//                     // Visual warning when time is running out
//                     if (timeRemaining <= 5) {
//                         timerLabel.setForeground(Color.RED);
//                     } else {
//                         timerLabel.setForeground(UIManager.getColor("Label.foreground"));
//                     }
//                 });
                
//                 // Time's up
//                 if (timeRemaining <= 0) {
//                     cancel();
                    
//                     // Handle time's up on EDT
//                     SwingUtilities.invokeLater(() -> {
//                         timeUp();
//                     });
//                 }
//             }
//         }, 1000, 1000);
//     }
    
//     private void timeUp() {
//         // Disable all option buttons
//         for (JButton button : optionButtons) {
//             if (button.isVisible()) {
//                 button.setEnabled(false);
//             }
//         }
        
//         // Highlight correct answer if review is enabled
//         if (reviewAnswers) {
//             Question currentQuestion = questions.get(currentQuestionIndex);
//             int correctIndex = currentQuestion.getCorrectOptionIndex();
            
//             optionButtons[correctIndex].setBackground(new Color(100, 200, 100));
//         }
        
//         // Enable next button
//         nextButton.setEnabled(true);
        
//         // Play sound effect if enabled
//         if (soundEffects) {
//             playSound("timeout");
//         }
//     }
    
//     private void selectAnswer(int selectedIndex) {
//         // Stop the timer
//         questionTimer.cancel();
        
//         // Calculate time taken for this question
//         long timeTaken = System.currentTimeMillis() - questionStartTime;
        
//         // Get current question
//         Question currentQuestion = questions.get(currentQuestionIndex);
        
//         // Check if answer is correct
//         boolean isCorrect = currentQuestion.isCorrect(selectedIndex);
        
//         // Disable all option buttons
//         for (JButton button : optionButtons) {
//             if (button.isVisible()) {
//                 button.setEnabled(false);
//             }
//         }
        
//         // Calculate score for this question
//         int questionScore = 0;
        
//         if (isCorrect) {
//             correctAnswers++;
            
//             // Base score based on difficulty
//             switch (difficulty) {
//                 case "Easy":
//                     questionScore = 10;
//                     break;
//                 case "Medium":
//                     questionScore = 20;
//                     break;
//                 case "Hard":
//                     questionScore = 30;
//                     break;
//                 default:
//                     questionScore = 10;
//             }
            
//             // Quick answer bonus (if answered in less than half the allowed time)
//             if (timeTaken < (timePerQuestion * 1000 / 2)) {
//                 questionScore = (int)(questionScore * 1.5);
//             }
            
//             // Update total score
//             totalScore += questionScore;
//             scoreLabel.setText("Score: " + totalScore);
//         }
        
//         // Highlight selected answer
//         optionButtons[selectedIndex].setBackground(
//             isCorrect ? new Color(100, 200, 100) : new Color(200, 100, 100));
        
//         // If review answers is enabled, highlight correct answer if user selected wrong
//         if (reviewAnswers && !isCorrect) {
//             int correctIndex = currentQuestion.getCorrectOptionIndex();
//             optionButtons[correctIndex].setBackground(new Color(100, 200, 100));
//         }
        
//         // Enable next button
//         nextButton.setEnabled(true);
        
//         // Show explanation if enabled
//         if (showExplanations && !currentQuestion.getExplanation().isEmpty()) {
//             JOptionPane.showMessageDialog(this,
//                 currentQuestion.getExplanation(),
//                 isCorrect ? "Correct!" : "Incorrect",
//                 JOptionPane.INFORMATION_MESSAGE);
//         }
        
//         // Play sound effect if enabled
//         if (soundEffects) {
//             playSound(isCorrect ? "correct" : "incorrect");
//         }
//     }
    
//     private void nextQuestion() {
//         currentQuestionIndex++;
        
//         if (currentQuestionIndex < questionCount && currentQuestionIndex < questions.size()) {
//             // Display next question
//             displayCurrentQuestion();
//         } else {
//             // Quiz is completed
//             finishQuiz();
//         }
//     }
    
//     private void finishQuiz() {
//         // Calculate total time taken
//         long totalTimeTaken = (System.currentTimeMillis() - startTime) / 1000;
        
//         // Create quiz result
//         QuizResult result = new QuizResult(
//             domain, difficulty, correctAnswers, 
//             questions.size(), totalTimeTaken
//         );
        
//         // Add result to user profile
//         userProfile.addQuizResult(domain, result);
//         userProfile.saveProfile();
        
//         // Show results dialog
//         showResultsDialog(result);
//     }
    
//     private void showResultsDialog(QuizResult result) {
//         // Create results dialog
//         JDialog resultsDialog = new JDialog(this, "Quiz Results", true);
//         resultsDialog.setSize(500, 400);
//         resultsDialog.setLocationRelativeTo(this);
        
//         JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
//         mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
//         // Results header
//         JLabel headerLabel = new JLabel("Quiz Completed!", JLabel.CENTER);
//         headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
//         // Results summary
//         JPanel summaryPanel = new JPanel(new GridLayout(0, 1, 5, 10));
//         summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
//         double percentScore = result.getPercentageScore();
//         String performanceMessage;
        
//         if (percentScore >= 90) {
//             performanceMessage = "Excellent! You're a master!";
//         } else if (percentScore >= 70) {
//             performanceMessage = "Great job! You know your stuff!";
//         } else if (percentScore >= 50) {
//             performanceMessage = "Good effort. Keep practicing!";
//         } else {
//             performanceMessage = "Keep learning and try again!";
//         }
        
//         JLabel performanceLabel = new JLabel(performanceMessage);
//         performanceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
//         JLabel scoreLabel = new JLabel(String.format("Score: %d points", totalScore));
//         JLabel correctLabel = new JLabel(String.format("Correct Answers: %d out of %d (%.1f%%)",
//                                                      result.getCorrectAnswers(),
//                                                      result.getTotalQuestions(),
//                                                      result.getPercentageScore()));
//         JLabel timeLabel = new JLabel(String.format("Time Taken: %d seconds", 
//                                                   result.getTimeTakenSeconds()));
        
//         summaryPanel.add(performanceLabel);
//         summaryPanel.add(scoreLabel);
//         summaryPanel.add(correctLabel);
//         summaryPanel.add(timeLabel);
        
//         // Show newly earned badges if any
//         List<String> recentBadges = userProfile.getEarnedBadges();
//         int previousCount = recentBadges.size() - userProfile.getEarnedBadges().size();
        
//         if (previousCount < recentBadges.size()) {
//             JLabel badgesLabel = new JLabel("New Achievements Earned:");
//             badgesLabel.setFont(new Font("Arial", Font.BOLD, 16));
//             summaryPanel.add(badgesLabel);
            
//             for (int i = previousCount; i < recentBadges.size(); i++) {
//                 summaryPanel.add(new JLabel("• " + recentBadges.get(i)));
//             }
//         }
        
//         // Buttons
//         JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
//         JButton newQuizButton = new JButton("Take Another Quiz");
//         newQuizButton.addActionListener(e -> {
//             resultsDialog.dispose();
//             dispose();
//             SwingUtilities.invokeLater(() -> {
//                 new Login().setVisible(true);
//             });
//         });
        
//         JButton reviewButton = new JButton("Review Answers");
//         reviewButton.addActionListener(e -> {
//             resultsDialog.dispose();
//             showAnswerReviewDialog();
//         });
//         reviewButton.setEnabled(reviewAnswers);
        
//         JButton exitButton = new JButton("Exit");
//         exitButton.addActionListener(e -> {
//             resultsDialog.dispose();
//             System.exit(0);
//         });
        
//         buttonPanel.add(newQuizButton);
//         buttonPanel.add(reviewButton);
//         buttonPanel.add(exitButton);
        
//         // Add components to main panel
//         mainPanel.add(headerLabel, BorderLayout.NORTH);
//         mainPanel.add(summaryPanel, BorderLayout.CENTER);
//         mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
//         // Apply theme
//         applyThemeToComponent(mainPanel, userProfile.getPreferredTheme());
        
//         resultsDialog.add(mainPanel);
//         resultsDialog.setVisible(true);
//     }
    
//     private void showAnswerReviewDialog() {
//         JDialog reviewDialog = new JDialog(this, "Review Answers", true);
//         reviewDialog.setSize(700, 500);
//         reviewDialog.setLocationRelativeTo(this);
        
//         JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
//         mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
//         // Header
//         JLabel headerLabel = new JLabel("Review Your Answers", JLabel.CENTER);
//         headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
//         // Create tabbed pane for questions
//         JTabbedPane tabbedPane = new JTabbedPane();
        
//         // Add a tab for each question
//         for (int i = 0; i < questions.size(); i++) {
//             Question question = questions.get(i);
            
//             JPanel questionPanel = new JPanel(new BorderLayout(10, 10));
//             questionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
//             // Question text
//             JLabel questionLabel = new JLabel("<html><body style='width: 500px'>" + 
//                                              question.getText() + "</body></html>");
//             questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
            
//             // Options panel
//             JPanel optionsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
//             List<String> options = question.getOptions();
            
//             for (int j = 0; j < options.size(); j++) {
//                 JLabel optionLabel = new JLabel(options.get(j));
//                 optionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
//                 optionLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                
//                 // Highlight correct answer in green
//                 if (j == question.getCorrectOptionIndex()) {
//                     optionLabel.setOpaque(true);
//                     optionLabel.setBackground(new Color(200, 255, 200));
//                     optionLabel.setBorder(BorderFactory.createCompoundBorder(
//                         BorderFactory.createLineBorder(new Color(0, 150, 0), 1),
//                         BorderFactory.createEmptyBorder(5, 10, 5, 10)
//                     ));
//                 }
                
//                 optionsPanel.add(optionLabel);
//             }
            
//             // Explanation if available
//             if (showExplanations && !question.getExplanation().isEmpty()) {
//                 JPanel explanationPanel = new JPanel(new BorderLayout());
//                 explanationPanel.setBorder(BorderFactory.createTitledBorder("Explanation"));
                
//                 JLabel explanationLabel = new JLabel("<html><body style='width: 500px'>" + 
//                                                    question.getExplanation() + "</body></html>");
//                 explanationLabel.setFont(new Font("Arial", Font.ITALIC, 14));
//                 explanationLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                
//                 explanationPanel.add(explanationLabel, BorderLayout.CENTER);
//                 questionPanel.add(explanationPanel, BorderLayout.SOUTH);
//             }
            
//             questionPanel.add(questionLabel, BorderLayout.NORTH);
//             questionPanel.add(new JScrollPane(optionsPanel), BorderLayout.CENTER);
            
//             tabbedPane.addTab("Question " + (i + 1), questionPanel);
//         }
        
//         // Close button
//         JButton closeButton = new JButton("Close");
//         closeButton.addActionListener(e -> reviewDialog.dispose());
        
//         JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//         buttonPanel.add(closeButton);
        
//         // Add components to main panel
//         mainPanel.add(headerLabel, BorderLayout.NORTH);
//         mainPanel.add(tabbedPane, BorderLayout.CENTER);
//         mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
//         // Apply theme
//         applyThemeToComponent(mainPanel, userProfile.getPreferredTheme());
        
//         reviewDialog.add(mainPanel);
//         reviewDialog.setVisible(true);
//     }
    
//     private void confirmExit() {
//         int result = JOptionPane.showConfirmDialog(this,
//             "Are you sure you want to quit? Your progress will be lost.",
//             "Confirm Exit",
//             JOptionPane.YES_NO_OPTION,
//             JOptionPane.WARNING_MESSAGE);
            
//         if (result == JOptionPane.YES_OPTION) {
//             dispose();
//             new Login().setVisible(true);
//         }
//     }
    
//     private void playSound(String soundType) {
//         // This would be implemented using Java sound APIs
//         // For now, just a placeholder for sound effects
        
//         // Different sound effects for different events:
//         // - correct: when an answer is correct
//         // - incorrect: when an answer is wrong
//         // - timeout: when time runs out
        
//         // No implementation needed for this code example
//     }
    
//     private void applyTheme(String theme) {
//         Color bgColor, fgColor, btnBgColor, btnFgColor, panelBgColor;
        
//         switch (theme) {
//             case "Dark":
//                 bgColor = new Color(50, 50, 50);
//                 fgColor = new Color(220, 220, 220);
//                 btnBgColor = new Color(80, 80, 80);
//                 btnFgColor = new Color(220, 220, 220);
//                 panelBgColor = new Color(60, 60, 60);
//                 break;
//             case "Blue":
//                 bgColor = new Color(220, 230, 255);
//                 fgColor = new Color(20, 20, 70);
//                 btnBgColor = new Color(100, 150, 220);
//                 btnFgColor = Color.WHITE;
//                 panelBgColor = new Color(200, 210, 240);
//                 break;
//             case "Green":
//                 bgColor = new Color(230, 250, 230);
//                 fgColor = new Color(20, 70, 20);
//                 btnBgColor = new Color(100, 200, 100);
//                 btnFgColor = Color.WHITE;
//                 panelBgColor = new Color(210, 240, 210);
//                 break;
//             default: // Light
//                 bgColor = new Color(240, 240, 240);
//                 fgColor = Color.BLACK;
//                 btnBgColor = new Color(230, 230, 230);
//                 btnFgColor = Color.BLACK;
//                 panelBgColor = new Color(250, 250, 250);
//                 break;
//         }
        
//         // Apply theme to all components recursively
//         applyThemeToComponent(this.getContentPane(), bgColor, fgColor, btnBgColor, btnFgColor, panelBgColor);
//     }
    
//     private void applyThemeToComponent(Container container, String theme) {
//         Color bgColor, fgColor, btnBgColor, btnFgColor, panelBgColor;
        
//         switch (theme) {
//             case "Dark":
//                 bgColor = new Color(50, 50, 50);
//                 fgColor = new Color(220, 220, 220);
//                 btnBgColor = new Color(80, 80, 80);
//                 btnFgColor = new Color(220, 220, 220);
//                 panelBgColor = new Color(60, 60, 60);
//                 break;
//             case "Blue":
//                 bgColor = new Color(220, 230, 255);
//                 fgColor = new Color(20, 20, 70);
//                 btnBgColor = new Color(100, 150, 220);
//                 btnFgColor = Color.WHITE;
//                 panelBgColor = new Color(200, 210, 240);
//                 break;
//             case "Green":
//                 bgColor = new Color(230, 250, 230);
//                 fgColor = new Color(20, 70, 20);
//                 btnBgColor = new Color(100, 200, 100);
//                 btnFgColor = Color.WHITE;
//                 panelBgColor = new Color(210, 240, 210);
//                 break;
//             default: // Light
//                 bgColor = new Color(240, 240, 240);
//                 fgColor = Color.BLACK;
//                 btnBgColor = new Color(230, 230, 230);
//                 btnFgColor = Color.BLACK;
//                 panelBgColor = new Color(250, 250, 250);
//                 break;
//         }
        
//         applyThemeToComponent(container, bgColor, fgColor, btnBgColor, btnFgColor, panelBgColor);
//     }
    
//     private void applyThemeToComponent(Container container, Color bgColor, Color fgColor, 
//                                       Color btnBgColor, Color btnFgColor, Color panelBgColor) {
//         container.setBackground(bgColor);
//         container.setForeground(fgColor);
        
//         for (Component comp : container.getComponents()) {
//             if (comp instanceof JPanel) {
//                 comp.setBackground(panelBgColor);
//                 comp.setForeground(fgColor);
//                 applyThemeToComponent((Container) comp, bgColor, fgColor, btnBgColor, btnFgColor, panelBgColor);
//             } else if (comp instanceof JButton) {
//                 // Don't override colors for option buttons that have been selected
//                 if (comp.getBackground() != new Color(100, 200, 100) && 
//                     comp.getBackground() != new Color(200, 100, 100)) {
//                     comp.setBackground(btnBgColor);
//                 }
//                 comp.setForeground(btnFgColor);
//             } else if (comp instanceof JLabel) {
//                 comp.setForeground(fgColor);
//             } else if (comp instanceof JTextField || comp instanceof JComboBox) {
//                 comp.setBackground(Color.WHITE);
//                 comp.setForeground(Color.BLACK);
//             } else if (comp instanceof JTabbedPane) {
//                 comp.setBackground(panelBgColor);
//                 comp.setForeground(fgColor);
//                 applyThemeToComponent((Container) comp, bgColor, fgColor, btnBgColor, btnFgColor, panelBgColor);
//             } else if (comp instanceof Container) {
//                 applyThemeToComponent((Container) comp, bgColor, fgColor, btnBgColor, btnFgColor, panelBgColor);
//             }
//         }
//     }
// }

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Main quiz screen that displays questions and handles user interaction
 * Enhanced with modern UI design, attractive fonts, and colors
 */
public class Quiz extends JFrame {
    private UserProfile userProfile;
    private String domain;
    private String difficulty;
    private QuestionBank questionBank;
    private int questionCount;
    private int timePerQuestion;
    private boolean reviewAnswers;
    private boolean showExplanations;
    private boolean soundEffects;
    
    private List<Question> questions;
    private int currentQuestionIndex;
    private int correctAnswers;
    private long startTime;
    private long questionStartTime;
    private Timer questionTimer;
    private int timeRemaining;
    private int totalScore;
    
    // UI Components
    private JLabel questionNumberLabel;
    private JLabel questionTextLabel;
    private JPanel optionsPanel;
    private JButton[] optionButtons;
    private JLabel timerLabel;
    private JLabel scoreLabel;
    private JProgressBar progressBar;
    private JButton nextButton;
    
    // Enhanced UI Colors and Fonts
    private static final Color PRIMARY_COLOR = new Color(74, 144, 226);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color ERROR_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(243, 156, 18);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(44, 62, 80);
    private static final Color TEXT_SECONDARY = new Color(127, 140, 141);
    
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    
    public Quiz(UserProfile userProfile, String domain, String difficulty,
               QuestionBank questionBank, int questionCount, int timePerQuestion,
               boolean reviewAnswers, boolean showExplanations, boolean soundEffects) {
        
        this.userProfile = userProfile;
        this.domain = domain;
        this.difficulty = difficulty;
        this.questionBank = questionBank;
        this.questionCount = questionCount;
        this.timePerQuestion = timePerQuestion;
        this.reviewAnswers = reviewAnswers;
        this.showExplanations = showExplanations;
        this.soundEffects = soundEffects;
        
        // Initialize quiz state
        this.currentQuestionIndex = 0;
        this.correctAnswers = 0;
        this.totalScore = 0;
        
        // Load questions
        this.questions = questionBank.getQuestionsForDomain(domain, difficulty, questionCount);
        
        // Set up the frame with enhanced styling
        setTitle("Quiz Master - " + domain + " (" + difficulty + ")");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(BACKGROUND_COLOR);
        
        // Handle window closing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });
        
        initComponents();
        applyModernTheme();
    }
    
    private void initComponents() {
        // Main layout with gradient background
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(74, 144, 226, 20),
                    0, getHeight(), new Color(155, 89, 182, 20)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        // Header panel with modern card design
        JPanel headerPanel = createCardPanel();
        headerPanel.setLayout(new BorderLayout(10, 15));
        headerPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        
        // Question number and progress section
        JPanel topHeaderPanel = new JPanel(new BorderLayout());
        topHeaderPanel.setOpaque(false);
        
        questionNumberLabel = new JLabel("Question 1 of " + questionCount);
        questionNumberLabel.setFont(SUBTITLE_FONT);
        questionNumberLabel.setForeground(TEXT_PRIMARY);
        
        // Score panel with icon
        JPanel scorePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        scorePanel.setOpaque(false);
        JLabel scoreIcon = new JLabel("🏆");
        scoreIcon.setFont(new Font("Arial", Font.PLAIN, 16));
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(SUBTITLE_FONT);
        scoreLabel.setForeground(SUCCESS_COLOR);
        scorePanel.add(scoreIcon);
        scorePanel.add(scoreLabel);
        
        // Timer panel with icon
        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        timerPanel.setOpaque(false);
        JLabel timerIcon = new JLabel("⏱️");
        timerIcon.setFont(new Font("Arial", Font.PLAIN, 16));
        timerLabel = new JLabel("Time: " + timePerQuestion + "s");
        timerLabel.setFont(SUBTITLE_FONT);
        timerLabel.setForeground(PRIMARY_COLOR);
        timerPanel.add(timerIcon);
        timerPanel.add(timerLabel);
        
        topHeaderPanel.add(questionNumberLabel, BorderLayout.WEST);
        topHeaderPanel.add(scorePanel, BorderLayout.CENTER);
        topHeaderPanel.add(timerPanel, BorderLayout.EAST);
        
        // Enhanced progress bar
        progressBar = new JProgressBar(0, questionCount) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background
                g2d.setColor(new Color(236, 240, 241));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                // Progress
                int progressWidth = (int) ((double) getValue() / getMaximum() * getWidth());
                GradientPaint gradient = new GradientPaint(
                    0, 0, PRIMARY_COLOR,
                    progressWidth, 0, SECONDARY_COLOR
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, progressWidth, getHeight(), 10, 10);
                
                // Text
                if (isStringPainted()) {
                    FontMetrics fm = g2d.getFontMetrics();
                    String text = getString();
                    int textWidth = fm.stringWidth(text);
                    int textHeight = fm.getAscent();
                    g2d.setColor(Color.WHITE);
                    g2d.drawString(text, (getWidth() - textWidth) / 2, (getHeight() + textHeight) / 2 - 2);
                }
                
                g2d.dispose();
            }
        };
        progressBar.setValue(1);
        progressBar.setStringPainted(true);
        progressBar.setString("1 / " + questionCount);
        progressBar.setPreferredSize(new Dimension(0, 25));
        progressBar.setFont(BUTTON_FONT);
        
        headerPanel.add(topHeaderPanel, BorderLayout.NORTH);
        headerPanel.add(progressBar, BorderLayout.SOUTH);
        
        // Question panel with card design
        JPanel questionPanel = createCardPanel();
        questionPanel.setLayout(new BorderLayout(15, 20));
        questionPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        
        // Question text with enhanced styling
        questionTextLabel = new JLabel();
        questionTextLabel.setFont(BODY_FONT);
        questionTextLabel.setForeground(TEXT_PRIMARY);
        questionTextLabel.setBorder(new EmptyBorder(10, 0, 20, 0));
        
        JScrollPane questionScrollPane = new JScrollPane(questionTextLabel);
        questionScrollPane.setBorder(null);
        questionScrollPane.setOpaque(false);
        questionScrollPane.getViewport().setOpaque(false);
        questionPanel.add(questionScrollPane, BorderLayout.NORTH);
        
        // Options panel with enhanced styling
        optionsPanel = new JPanel(new GridLayout(0, 1, 0, 12));
        optionsPanel.setOpaque(false);
        optionsPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        // Create enhanced option buttons
        optionButtons = new JButton[6];
        for (int i = 0; i < optionButtons.length; i++) {
            final int optionIndex = i;
            optionButtons[i] = createStyledButton("", false);
            optionButtons[i].setFont(BODY_FONT);
            optionButtons[i].addActionListener(e -> selectAnswer(optionIndex));
            optionButtons[i].setHorizontalAlignment(SwingConstants.LEFT);
            optionButtons[i].setBorder(new EmptyBorder(15, 20, 15, 20));
            
            // Add hover effects
            addHoverEffect(optionButtons[i]);
            
            optionsPanel.add(optionButtons[i]);
        }
        
        JScrollPane optionsScrollPane = new JScrollPane(optionsPanel);
        optionsScrollPane.setBorder(null);
        optionsScrollPane.setOpaque(false);
        optionsScrollPane.getViewport().setOpaque(false);
        questionPanel.add(optionsScrollPane, BorderLayout.CENTER);
        
        // Next button panel with enhanced styling
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        nextButton = createStyledButton("Next Question →", true);
        nextButton.setFont(BUTTON_FONT);
        nextButton.addActionListener(e -> nextQuestion());
        nextButton.setEnabled(false);
        nextButton.setPreferredSize(new Dimension(150, 45));
        
        buttonPanel.add(nextButton);
        
        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(questionPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add to frame
        add(mainPanel);
    }
    
    private JPanel createCardPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Drop shadow
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 15, 15);
                
                // Card background
                g2d.setColor(CARD_COLOR);
                g2d.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 15, 15);
                
                g2d.dispose();
            }
        };
        panel.setOpaque(false);
        return panel;
    }
    
    private JButton createStyledButton(String text, boolean isPrimary) {
        JButton button = new JButton(text) {
            private boolean isHovered = false;
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bgColor = isPrimary ? PRIMARY_COLOR : new Color(236, 240, 241);
                Color hoverColor = isPrimary ? SECONDARY_COLOR : new Color(220, 225, 230);
                
                if (!isEnabled()) {
                    bgColor = new Color(189, 195, 199);
                } else if (isHovered) {
                    bgColor = hoverColor;
                }
                
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                // Text
                g2d.setColor(isPrimary ? Color.WHITE : TEXT_PRIMARY);
                if (!isEnabled()) {
                    g2d.setColor(new Color(127, 140, 141));
                }
                
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getAscent();
                g2d.drawString(getText(), (getWidth() - textWidth) / 2, (getHeight() + textHeight) / 2 - 2);
                
                g2d.dispose();
            }
            
            @Override
            public void paintBorder(Graphics g) {
                // Custom border painting handled in paintComponent
            }
        };
        
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private void addHoverEffect(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.putClientProperty("isHovered", true);
                    button.repaint();
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.putClientProperty("isHovered", false);
                button.repaint();
            }
        });
    }
    
    public void startQuiz() {
        setVisible(true);
        startTime = System.currentTimeMillis();
        displayCurrentQuestion();
    }
    
    private void displayCurrentQuestion() {
        Question currentQuestion = questions.get(currentQuestionIndex);
        
        // Update question number label
        questionNumberLabel.setText("Question " + (currentQuestionIndex + 1) + " of " + questionCount);
        
        // Update progress bar
        progressBar.setValue(currentQuestionIndex + 1);
        progressBar.setString((currentQuestionIndex + 1) + " / " + questionCount);
        
        // Update question text with enhanced HTML formatting
        String questionHtml = String.format(
            "<html><body style='width: 600px; font-family: Segoe UI; color: rgb(%d, %d, %d); line-height: 1.4;'>" +
            "<div style='margin: 10px 0;'>%s</div></body></html>",
            TEXT_PRIMARY.getRed(), TEXT_PRIMARY.getGreen(), TEXT_PRIMARY.getBlue(),
            currentQuestion.getText()
        );
        questionTextLabel.setText(questionHtml);
        
        // Update option buttons with enhanced styling
        List<String> options = currentQuestion.getOptions();
        for (int i = 0; i < optionButtons.length; i++) {
            if (i < options.size()) {
                String optionText = String.format("%c) %s", (char)('A' + i), options.get(i));
                optionButtons[i].setText(optionText);
                optionButtons[i].setEnabled(true);
                optionButtons[i].setBackground(new Color(249, 250, 251));
                optionButtons[i].setVisible(true);
            } else {
                optionButtons[i].setVisible(false);
            }
        }
        
        nextButton.setEnabled(false);
        nextButton.setText("Next Question →");
        startQuestionTimer();
    }
    
    private void startQuestionTimer() {
        if (questionTimer != null) {
            questionTimer.cancel();
        }
        
        timeRemaining = timePerQuestion;
        updateTimerDisplay();
        questionStartTime = System.currentTimeMillis();
        
        questionTimer = new Timer();
        questionTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeRemaining--;
                
                SwingUtilities.invokeLater(() -> {
                    updateTimerDisplay();
                    
                    if (timeRemaining <= 0) {
                        cancel();
                        timeUp();
                    }
                });
            }
        }, 1000, 1000);
    }
    
    private void updateTimerDisplay() {
        timerLabel.setText("Time: " + timeRemaining + "s");
        
        if (timeRemaining <= 10) {
            timerLabel.setForeground(WARNING_COLOR);
        } else if (timeRemaining <= 5) {
            timerLabel.setForeground(ERROR_COLOR);
        } else {
            timerLabel.setForeground(PRIMARY_COLOR);
        }
    }
    
    private void timeUp() {
        for (JButton button : optionButtons) {
            if (button.isVisible()) {
                button.setEnabled(false);
            }
        }
        
        if (reviewAnswers) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            int correctIndex = currentQuestion.getCorrectOptionIndex();
            optionButtons[correctIndex].setBackground(SUCCESS_COLOR);
            optionButtons[correctIndex].setForeground(Color.WHITE);
        }
        
        nextButton.setEnabled(true);
        
        if (soundEffects) {
            playSound("timeout");
        }
    }
    
    private void selectAnswer(int selectedIndex) {
        questionTimer.cancel();
        long timeTaken = System.currentTimeMillis() - questionStartTime;
        Question currentQuestion = questions.get(currentQuestionIndex);
        boolean isCorrect = currentQuestion.isCorrect(selectedIndex);
        
        // Disable all option buttons
        for (JButton button : optionButtons) {
            if (button.isVisible()) {
                button.setEnabled(false);
            }
        }
        
        // Calculate score
        int questionScore = 0;
        if (isCorrect) {
            correctAnswers++;
            
            switch (difficulty) {
                case "Easy": questionScore = 10; break;
                case "Medium": questionScore = 20; break;
                case "Hard": questionScore = 30; break;
                default: questionScore = 10;
            }
            
            if (timeTaken < (timePerQuestion * 1000 / 2)) {
                questionScore = (int)(questionScore * 1.5);
            }
            
            totalScore += questionScore;
            scoreLabel.setText("Score: " + totalScore);
        }
        
        // Enhanced visual feedback
        optionButtons[selectedIndex].setBackground(isCorrect ? SUCCESS_COLOR : ERROR_COLOR);
        optionButtons[selectedIndex].setForeground(Color.WHITE);
        
        if (reviewAnswers && !isCorrect) {
            int correctIndex = currentQuestion.getCorrectOptionIndex();
            optionButtons[correctIndex].setBackground(SUCCESS_COLOR);
            optionButtons[correctIndex].setForeground(Color.WHITE);
        }
        
        nextButton.setEnabled(true);
        
        if (showExplanations && !currentQuestion.getExplanation().isEmpty()) {
            showStyledDialog(
                currentQuestion.getExplanation(),
                isCorrect ? "Correct! ✓" : "Incorrect ✗",
                isCorrect ? SUCCESS_COLOR : ERROR_COLOR
            );
        }
        
        if (soundEffects) {
            playSound(isCorrect ? "correct" : "incorrect");
        }
    }
    
    private void showStyledDialog(String message, String title, Color color) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(CARD_COLOR);
        
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(SUBTITLE_FONT);
        titleLabel.setForeground(color);
        
        JLabel messageLabel = new JLabel("<html><body style='width: 300px; text-align: center;'>" + 
                                        message + "</body></html>");
        messageLabel.setFont(BODY_FONT);
        messageLabel.setForeground(TEXT_PRIMARY);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        
        JButton okButton = createStyledButton("OK", true);
        okButton.addActionListener(e -> dialog.dispose());
        okButton.setPreferredSize(new Dimension(80, 35));
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.add(okButton);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(messageLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void nextQuestion() {
        currentQuestionIndex++;
        
        if (currentQuestionIndex < questionCount && currentQuestionIndex < questions.size()) {
            displayCurrentQuestion();
        } else {
            finishQuiz();
        }
    }
    
    private void finishQuiz() {
        long totalTimeTaken = (System.currentTimeMillis() - startTime) / 1000;
        
        QuizResult result = new QuizResult(
            domain, difficulty, correctAnswers, 
            questions.size(), totalTimeTaken
        );
        
        userProfile.addQuizResult(domain, result);
        userProfile.saveProfile();
        
        showResultsDialog(result);
    }
    
    private void showResultsDialog(QuizResult result) {
        JDialog resultsDialog = new JDialog(this, "Quiz Results", true);
        resultsDialog.setSize(550, 450);
        resultsDialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(74, 144, 226, 30),
                    0, getHeight(), new Color(155, 89, 182, 30)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        // Results header with icon
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setOpaque(false);
        
        JLabel headerIcon = new JLabel("🎉");
        headerIcon.setFont(new Font("Arial", Font.PLAIN, 32));
        
        JLabel headerLabel = new JLabel("Quiz Completed!");
        headerLabel.setFont(TITLE_FONT);
        headerLabel.setForeground(PRIMARY_COLOR);
        
        headerPanel.add(headerIcon);
        headerPanel.add(Box.createHorizontalStrut(10));
        headerPanel.add(headerLabel);
        
        // Results summary card
        JPanel summaryPanel = createCardPanel();
        summaryPanel.setLayout(new GridLayout(0, 1, 5, 10));
        summaryPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        
        double percentScore = result.getPercentageScore();
        String performanceMessage;
        String emoji;
        
        if (percentScore >= 90) {
            performanceMessage = "Excellent! You're a master!";
            emoji = "🌟";
        } else if (percentScore >= 70) {
            performanceMessage = "Great job! You know your stuff!";
            emoji = "👍";
        } else if (percentScore >= 50) {
            performanceMessage = "Good effort. Keep practicing!";
            emoji = "👌";
        } else {
            performanceMessage = "Keep learning and try again!";
            emoji = "💪";
        }
        
        JLabel performanceLabel = new JLabel(emoji + " " + performanceMessage);
        performanceLabel.setFont(SUBTITLE_FONT);
        performanceLabel.setForeground(SUCCESS_COLOR);
        performanceLabel.setHorizontalAlignment(JLabel.CENTER);
        
        JLabel scoreLabel = new JLabel(String.format("🏆 Final Score: %d points", totalScore));
        scoreLabel.setFont(BODY_FONT);
        scoreLabel.setForeground(TEXT_PRIMARY);
        
        JLabel correctLabel = new JLabel(String.format("✅ Correct Answers: %d out of %d (%.1f%%)",
                                                     result.getCorrectAnswers(),
                                                     result.getTotalQuestions(),
                                                     result.getPercentageScore()));
        correctLabel.setFont(BODY_FONT);
        correctLabel.setForeground(TEXT_PRIMARY);
        
        JLabel timeLabel = new JLabel(String.format("⏱️ Time Taken: %d seconds", 
                                                  result.getTimeTakenSeconds()));
        timeLabel.setFont(BODY_FONT);
        timeLabel.setForeground(TEXT_PRIMARY);
        
        summaryPanel.add(performanceLabel);
        summaryPanel.add(Box.createVerticalStrut(5));
        summaryPanel.add(scoreLabel);
        summaryPanel.add(correctLabel);
        summaryPanel.add(timeLabel);
        
        // Buttons with enhanced styling
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        
        JButton newQuizButton = createStyledButton("Take Another Quiz", true);
        newQuizButton.addActionListener(e -> {
            resultsDialog.dispose();
            dispose();
            SwingUtilities.invokeLater(() -> {
                new Login().setVisible(true);
            });
        });
        newQuizButton.setPreferredSize(new Dimension(150, 40));
        
        JButton reviewButton = createStyledButton("Review Answers", false);
        reviewButton.addActionListener(e -> {
            resultsDialog.dispose();
            showAnswerReviewDialog();
        });
        reviewButton.setEnabled(reviewAnswers);
        reviewButton.setPreferredSize(new Dimension(130, 40));
        
        JButton exitButton = createStyledButton("Exit", false);
        exitButton.addActionListener(e -> {
            resultsDialog.dispose();
            System.exit(0);
        });
        exitButton.setPreferredSize(new Dimension(80, 40));
        
        buttonPanel.add(newQuizButton);
        buttonPanel.add(reviewButton);
        buttonPanel.add(exitButton);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(summaryPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        resultsDialog.add(mainPanel);
        resultsDialog.setVisible(true);
    }
    
    private void showAnswerReviewDialog() {
        JDialog reviewDialog = new JDialog(this, "Review Answers", true);
        reviewDialog.setSize(750, 550);
        reviewDialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel headerLabel = new JLabel("📋 Review Your Answers", JLabel.CENTER);
        headerLabel.setFont(TITLE_FONT);
        headerLabel.setForeground(PRIMARY_COLOR);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(BUTTON_FONT);
        
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            
            JPanel questionPanel = createCardPanel();
            questionPanel.setLayout(new BorderLayout(10, 15));
            questionPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
            
            JLabel questionLabel = new JLabel("<html><body style='width: 550px; font-family: Segoe UI;'>" + 
                                             question.getText() + "</body></html>");
            questionLabel.setFont(BODY_FONT);
            questionLabel.setForeground(TEXT_PRIMARY);
            
            JPanel optionsPanel = new JPanel(new GridLayout(0, 1, 8, 8));
            optionsPanel.setOpaque(false);
            List<String> options = question.getOptions();
            
            for (int j = 0; j < options.size(); j++) {
                JPanel optionPanel = new JPanel(new BorderLayout());
                optionPanel.setOpaque(false);
                
                String optionText = String.format("%c) %s", (char)('A' + j), options.get(j));
                JLabel optionLabel = new JLabel(optionText);
                optionLabel.setFont(BODY_FONT);
                optionLabel.setBorder(new EmptyBorder(10, 15, 10, 15));
                
                // Highlight correct answer
                if (j == question.getCorrectOptionIndex()) {
                    optionPanel.setOpaque(true);
                    optionPanel.setBackground(new Color(212, 237, 218));
                    optionPanel.setBorder(new LineBorder(SUCCESS_COLOR, 2, true));
                    optionLabel.setForeground(new Color(21, 87, 36));
                    
                    // Add checkmark icon
                    JLabel correctIcon = new JLabel("✓");
                    correctIcon.setFont(new Font("Arial", Font.BOLD, 16));
                    correctIcon.setForeground(SUCCESS_COLOR);
                    optionPanel.add(correctIcon, BorderLayout.EAST);
                } else {
                    optionPanel.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
                    optionLabel.setForeground(TEXT_PRIMARY);
                }
                
                optionPanel.add(optionLabel, BorderLayout.CENTER);
                optionsPanel.add(optionPanel);
            }
            
            // Add explanation if available
            if (showExplanations && !question.getExplanation().isEmpty()) {
                JPanel explanationPanel = new JPanel(new BorderLayout());
                explanationPanel.setOpaque(false);
                explanationPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                    "💡 Explanation",
                    0, 0, BUTTON_FONT, PRIMARY_COLOR
                ));
                
                JLabel explanationLabel = new JLabel("<html><body style='width: 500px; font-family: Segoe UI;'>" + 
                                                   question.getExplanation() + "</body></html>");
                explanationLabel.setFont(BODY_FONT);
                explanationLabel.setForeground(TEXT_SECONDARY);
                explanationLabel.setBorder(new EmptyBorder(10, 15, 10, 15));
                
                explanationPanel.add(explanationLabel, BorderLayout.CENTER);
                questionPanel.add(explanationPanel, BorderLayout.SOUTH);
            }
            
            questionPanel.add(questionLabel, BorderLayout.NORTH);
            questionPanel.add(new JScrollPane(optionsPanel), BorderLayout.CENTER);
            
            tabbedPane.addTab("Q" + (i + 1), questionPanel);
        }
        
        JButton closeButton = createStyledButton("Close", true);
        closeButton.addActionListener(e -> reviewDialog.dispose());
        closeButton.setPreferredSize(new Dimension(100, 40));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(closeButton);
        
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        reviewDialog.add(mainPanel);
        reviewDialog.setVisible(true);
    }
    
    private void confirmExit() {
        // Create custom styled confirmation dialog
        JDialog confirmDialog = new JDialog(this, "Confirm Exit", true);
        confirmDialog.setSize(400, 200);
        confirmDialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(CARD_COLOR);
        
        JLabel iconLabel = new JLabel("⚠️", JLabel.CENTER);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 32));
        
        JLabel messageLabel = new JLabel("<html><body style='text-align: center; width: 300px;'>" +
                                        "Are you sure you want to quit?<br>" +
                                        "Your progress will be lost.</body></html>");
        messageLabel.setFont(BODY_FONT);
        messageLabel.setForeground(TEXT_PRIMARY);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setOpaque(false);
        messagePanel.add(iconLabel, BorderLayout.NORTH);
        messagePanel.add(messageLabel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        
        JButton yesButton = createStyledButton("Yes, Quit", false);
        yesButton.addActionListener(e -> {
            confirmDialog.dispose();
            dispose();
            new Login().setVisible(true);
        });
        yesButton.setPreferredSize(new Dimension(100, 35));
        
        JButton noButton = createStyledButton("Continue Quiz", true);
        noButton.addActionListener(e -> confirmDialog.dispose());
        noButton.setPreferredSize(new Dimension(120, 35));
        
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        
        panel.add(messagePanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        confirmDialog.add(panel);
        confirmDialog.setVisible(true);
    }
    
    private void playSound(String soundType) {
        // Placeholder for sound effects
        // Implementation would use Java sound APIs like AudioSystem
        // Different sound effects for different events:
        // - correct: success sound
        // - incorrect: error sound  
        // - timeout: warning sound
    }
    
    private void applyModernTheme() {
        // Set system look and feel for better integration
        try {
           UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (Exception e) {
            // Fall back to default if system L&F not available
        }
        
        // Update UI manager defaults for consistent theming
        UIManager.put("Button.font", BUTTON_FONT);
        UIManager.put("Label.font", BODY_FONT);
        UIManager.put("TabbedPane.font", BUTTON_FONT);
        UIManager.put("TabbedPane.selected", PRIMARY_COLOR);
        UIManager.put("ProgressBar.font", SMALL_FONT);
        
        // Apply theme based on user preference
        applyTheme(userProfile.getPreferredTheme());
    }
    
    private void applyTheme(String theme) {
        // Enhanced theme application with modern color schemes
        Color bgColor, fgColor, cardColor, primaryColor, secondaryColor;
        
        switch (theme) {
            case "Dark":
                bgColor = new Color(45, 52, 67);
                fgColor = new Color(236, 240, 241);
                cardColor = new Color(52, 73, 94);
                primaryColor = new Color(52, 152, 219);
                secondaryColor = new Color(155, 89, 182);
                break;
            case "Blue":
                bgColor = new Color(235, 245, 255);
                fgColor = new Color(44, 62, 80);
                cardColor = Color.WHITE;
                primaryColor = new Color(41, 128, 185);
                secondaryColor = new Color(52, 152, 219);
                break;
            case "Green":
                bgColor = new Color(240, 248, 240);
                fgColor = new Color(44, 62, 80);
                cardColor = Color.WHITE;
                primaryColor = new Color(39, 174, 96);
                secondaryColor = new Color(46, 204, 113);
                break;
            default: // Light - already set as constants
                return; // Use default modern theme
        }
        
        // Apply theme colors to components recursively
        applyThemeToComponent(this.getContentPane(), bgColor, fgColor, cardColor, primaryColor, secondaryColor);
        repaint();
    }
    
    private void applyThemeToComponent(Container container, Color bgColor, Color fgColor, 
                                      Color cardColor, Color primaryColor, Color secondaryColor) {
        container.setBackground(bgColor);
        container.setForeground(fgColor);
        
        for (Component comp : container.getComponents()) {
            if (comp instanceof JPanel) {
                // Don't override card panel backgrounds
                if (!comp.getClass().getName().contains("$")) {
                    comp.setBackground(bgColor);
                }
                comp.setForeground(fgColor);
                if (comp instanceof Container) {
                    applyThemeToComponent((Container) comp, bgColor, fgColor, cardColor, primaryColor, secondaryColor);
                }
            } else if (comp instanceof JButton) {
                // Custom button styling is handled in createStyledButton
                comp.setForeground(fgColor);
            } else if (comp instanceof JLabel) {
                comp.setForeground(fgColor);
            } else if (comp instanceof JProgressBar) {
                comp.setBackground(bgColor);
                comp.setForeground(primaryColor);
            } else if (comp instanceof JTabbedPane) {
                comp.setBackground(bgColor);
                comp.setForeground(fgColor);
                if (comp instanceof Container) {
                    applyThemeToComponent((Container) comp, bgColor, fgColor, cardColor, primaryColor, secondaryColor);
                }
            } else if (comp instanceof Container) {
                applyThemeToComponent((Container) comp, bgColor, fgColor, cardColor, primaryColor, secondaryColor);
            }
        }
    }
}