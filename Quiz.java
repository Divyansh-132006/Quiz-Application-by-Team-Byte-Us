

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


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
  
    private JLabel questionNumberLabel;
    private JLabel questionTextLabel;
    private JPanel optionsPanel;
    private JButton[] optionButtons;
    private JLabel timerLabel;
    private JLabel scoreLabel;
    private JProgressBar progressBar;
    private JButton nextButton;
    

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
       
        this.currentQuestionIndex = 0;
        this.correctAnswers = 0;
        this.totalScore = 0;
        
    
        this.questions = questionBank.getQuestionsForDomain(domain, difficulty, questionCount);
        
        
        setTitle("Quiz Master - " + domain + " (" + difficulty + ")");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(BACKGROUND_COLOR);
        
       
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
        
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
             
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(74, 144, 226, 20),
                    0, getHeight(), new Color(155, 89, 182, 20)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
       
        JPanel headerPanel = createCardPanel();
        headerPanel.setLayout(new BorderLayout(10, 15));
        headerPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        
     
        JPanel topHeaderPanel = new JPanel(new BorderLayout());
        topHeaderPanel.setOpaque(false);
        
        questionNumberLabel = new JLabel("Question 1 of " + questionCount);
        questionNumberLabel.setFont(SUBTITLE_FONT);
        questionNumberLabel.setForeground(TEXT_PRIMARY);
        
        
        JPanel scorePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        scorePanel.setOpaque(false);
        JLabel scoreIcon = new JLabel("🏆");
        scoreIcon.setFont(new Font("Arial", Font.PLAIN, 16));
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(SUBTITLE_FONT);
        scoreLabel.setForeground(SUCCESS_COLOR);
        scorePanel.add(scoreIcon);
        scorePanel.add(scoreLabel);
      
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
        
        
        progressBar = new JProgressBar(0, questionCount) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
               
                g2d.setColor(new Color(236, 240, 241));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
             
                int progressWidth = (int) ((double) getValue() / getMaximum() * getWidth());
                GradientPaint gradient = new GradientPaint(
                    0, 0, PRIMARY_COLOR,
                    progressWidth, 0, SECONDARY_COLOR
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, progressWidth, getHeight(), 10, 10);
                
          
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
        
        
        JPanel questionPanel = createCardPanel();
        questionPanel.setLayout(new BorderLayout(15, 20));
        questionPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        
   
        questionTextLabel = new JLabel();
        questionTextLabel.setFont(BODY_FONT);
        questionTextLabel.setForeground(TEXT_PRIMARY);
        questionTextLabel.setBorder(new EmptyBorder(10, 0, 20, 0));
        
        JScrollPane questionScrollPane = new JScrollPane(questionTextLabel);
        questionScrollPane.setBorder(null);
        questionScrollPane.setOpaque(false);
        questionScrollPane.getViewport().setOpaque(false);
        questionPanel.add(questionScrollPane, BorderLayout.NORTH);
        
      
        optionsPanel = new JPanel(new GridLayout(0, 1, 0, 12));
        optionsPanel.setOpaque(false);
        optionsPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
    
        optionButtons = new JButton[6];
        for (int i = 0; i < optionButtons.length; i++) {
            final int optionIndex = i;
            optionButtons[i] = createStyledButton("", false);
            optionButtons[i].setFont(BODY_FONT);
            optionButtons[i].addActionListener(e -> selectAnswer(optionIndex));
            optionButtons[i].setHorizontalAlignment(SwingConstants.LEFT);
            optionButtons[i].setBorder(new EmptyBorder(15, 20, 15, 20));
            
           
            addHoverEffect(optionButtons[i]);
            
            optionsPanel.add(optionButtons[i]);
        }
        
        JScrollPane optionsScrollPane = new JScrollPane(optionsPanel);
        optionsScrollPane.setBorder(null);
        optionsScrollPane.setOpaque(false);
        optionsScrollPane.getViewport().setOpaque(false);
        questionPanel.add(optionsScrollPane, BorderLayout.CENTER);
        
    
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        nextButton = createStyledButton("Next Question →", true);
        nextButton.setFont(BUTTON_FONT);
        nextButton.addActionListener(e -> nextQuestion());
        nextButton.setEnabled(false);
        nextButton.setPreferredSize(new Dimension(150, 45));
        
        buttonPanel.add(nextButton);
        

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(questionPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
   
        add(mainPanel);
    }
    
    private JPanel createCardPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
             
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 15, 15);
                
              
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
        
 
        questionNumberLabel.setText("Question " + (currentQuestionIndex + 1) + " of " + questionCount);
        
       
        progressBar.setValue(currentQuestionIndex + 1);
        progressBar.setString((currentQuestionIndex + 1) + " / " + questionCount);
        
        String questionHtml = String.format(
            "<html><body style='width: 600px; font-family: Segoe UI; color: rgb(%d, %d, %d); line-height: 1.4;'>" +
            "<div style='margin: 10px 0;'>%s</div></body></html>",
            TEXT_PRIMARY.getRed(), TEXT_PRIMARY.getGreen(), TEXT_PRIMARY.getBlue(),
            currentQuestion.getText()
        );
        questionTextLabel.setText(questionHtml);
        
      
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
        
      
        for (JButton button : optionButtons) {
            if (button.isVisible()) {
                button.setEnabled(false);
            }
        }
     
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
                
                
                if (j == question.getCorrectOptionIndex()) {
                    optionPanel.setOpaque(true);
                    optionPanel.setBackground(new Color(212, 237, 218));
                    optionPanel.setBorder(new LineBorder(SUCCESS_COLOR, 2, true));
                    optionLabel.setForeground(new Color(21, 87, 36));
                 
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
       
    }
    
    private void applyModernTheme() {
        
        try {
           UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (Exception e) {
            
        }
        
      
        UIManager.put("Button.font", BUTTON_FONT);
        UIManager.put("Label.font", BODY_FONT);
        UIManager.put("TabbedPane.font", BUTTON_FONT);
        UIManager.put("TabbedPane.selected", PRIMARY_COLOR);
        UIManager.put("ProgressBar.font", SMALL_FONT);
        
       
        applyTheme(userProfile.getPreferredTheme());
    }
    
    private void applyTheme(String theme) {
       
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
            default: 
                return;
        }
      
        applyThemeToComponent(this.getContentPane(), bgColor, fgColor, cardColor, primaryColor, secondaryColor);
        repaint();
    }
    
    private void applyThemeToComponent(Container container, Color bgColor, Color fgColor, 
                                      Color cardColor, Color primaryColor, Color secondaryColor) {
        container.setBackground(bgColor);
        container.setForeground(fgColor);
        
        for (Component comp : container.getComponents()) {
            if (comp instanceof JPanel) {
               
                if (!comp.getClass().getName().contains("$")) {
                    comp.setBackground(bgColor);
                }
                comp.setForeground(fgColor);
                if (comp instanceof Container) {
                    applyThemeToComponent((Container) comp, bgColor, fgColor, cardColor, primaryColor, secondaryColor);
                }
            } else if (comp instanceof JButton) {
               
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
