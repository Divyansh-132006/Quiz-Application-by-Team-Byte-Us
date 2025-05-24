// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.*;
// import java.util.List;

// /**
//  * Enhanced rules screen with additional customization options
//  */
// public class Rules extends JFrame {
//     private UserProfile userProfile;
//     private String domain;
//     private String difficulty;
//     private QuestionBank questionBank;
//     private JSpinner questionCountSpinner;
//     private JSpinner timePerQuestionSpinner;
//     private JCheckBox reviewAnswersCheckbox;
//     private JCheckBox showExplanationsCheckbox;
//     private JCheckBox soundEffectsCheckbox;
    
//     public Rules(UserProfile userProfile, String domain, 
//                 String difficulty, QuestionBank questionBank) {
//         this.userProfile = userProfile;
//         this.domain = domain;
//         this.difficulty = difficulty;
//         this.questionBank = questionBank;
        
//         // Set up the frame
//         setTitle("Quiz Rules - " + domain + " (" + difficulty + ")");
//         setSize(600, 500);
//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         setLocationRelativeTo(null);
        
//         // Create main panel with gap borders
//         JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
//         mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
//         // Header panel
//         JPanel headerPanel = new JPanel(new BorderLayout());
//         JLabel titleLabel = new JLabel("Quiz Rules & Options", JLabel.CENTER);
//         titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
//         JLabel subtitleLabel = new JLabel("Customize your quiz experience", JLabel.CENTER);
//         subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        
//         JPanel titlePanel = new JPanel(new GridLayout(2, 1));
//         titlePanel.add(titleLabel);
//         titlePanel.add(subtitleLabel);
//         headerPanel.add(titlePanel, BorderLayout.CENTER);
        
//         // Welcome message
//         JPanel welcomePanel = new JPanel(new BorderLayout());
//         JLabel welcomeLabel = new JLabel("Welcome, " + userProfile.getUsername() + "!", JLabel.CENTER);
//         welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
//         welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
        
//         // Quiz information panel
//         JPanel infoPanel = new JPanel(new GridLayout(0, 1, 5, 10));
//         infoPanel.setBorder(BorderFactory.createTitledBorder("Quiz Information"));
        
//         infoPanel.add(new JLabel("Domain: " + domain));
//         infoPanel.add(new JLabel("Difficulty: " + difficulty));
        
//         // Question count setting
//         JPanel questionCountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//         questionCountPanel.add(new JLabel("Number of Questions: "));
//         SpinnerNumberModel questionModel = new SpinnerNumberModel(10, 5, 30, 1);
//         questionCountSpinner = new JSpinner(questionModel);
//         questionCountPanel.add(questionCountSpinner);
//         infoPanel.add(questionCountPanel);
        
//         // Time settings
//         JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//         timePanel.add(new JLabel("Time per Question (seconds): "));
//         SpinnerNumberModel timeModel = new SpinnerNumberModel(30, 10, 120, 5);
//         timePerQuestionSpinner = new JSpinner(timeModel);
//         timePanel.add(timePerQuestionSpinner);
//         infoPanel.add(timePanel);
        
//         // Options panel
//         JPanel optionsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
//         optionsPanel.setBorder(BorderFactory.createTitledBorder("Quiz Options"));
        
//         reviewAnswersCheckbox = new JCheckBox("Review answers after quiz", true);
//         showExplanationsCheckbox = new JCheckBox("Show explanations for answers", true);
//         soundEffectsCheckbox = new JCheckBox("Enable sound effects", true);
        
//         optionsPanel.add(reviewAnswersCheckbox);
//         optionsPanel.add(showExplanationsCheckbox);
//         optionsPanel.add(soundEffectsCheckbox);
        
//         // Rules panel
//         JPanel rulesPanel = new JPanel(new BorderLayout());
//         rulesPanel.setBorder(BorderFactory.createTitledBorder("Quiz Rules"));
        
//         JTextArea rulesText = new JTextArea();
//         rulesText.setEditable(false);
//         rulesText.setLineWrap(true);
//         rulesText.setWrapStyleWord(true);
//         rulesText.setBackground(new Color(250, 250, 220));
//         rulesText.setText(
//             "1. Answer each question within the time limit.\n\n" +
//             "2. Each correct answer earns you points based on difficulty:\n" +
//             "   - Easy: 10 points\n" +
//             "   - Medium: 20 points\n" +
//             "   - Hard: 30 points\n\n" +
//             "3. Quick answers earn bonus points! Answering within half the time limit " +
//             "gives you a 50% bonus.\n\n" +
//             "4. No points are deducted for incorrect answers.\n\n" +
//             "5. You can earn achievement badges by completing quizzes with high scores " +
//             "or reaching milestones.\n\n" +
//             "6. Have fun and learn something new!"
//         );
        
//         JScrollPane rulesScroll = new JScrollPane(rulesText);
//         rulesScroll.setPreferredSize(new Dimension(500, 150));
//         rulesPanel.add(rulesScroll, BorderLayout.CENTER);
        
//         // Buttons panel
//         JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
//         JButton startButton = new JButton("Start Quiz");
//         JButton backButton = new JButton("Back to Login");
//         buttonPanel.add(backButton);
//         buttonPanel.add(startButton);
        
//         // Add action listeners
//         startButton.addActionListener(e -> startQuiz());
//         backButton.addActionListener(e -> goBackToLogin());
        
//         // Layout components on main panel
//         JPanel topPanel = new JPanel(new BorderLayout());
//         topPanel.add(headerPanel, BorderLayout.NORTH);
//         topPanel.add(welcomePanel, BorderLayout.CENTER);
        
//         JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
//         centerPanel.add(infoPanel, BorderLayout.NORTH);
//         centerPanel.add(optionsPanel, BorderLayout.CENTER);
        
//         mainPanel.add(topPanel, BorderLayout.NORTH);
//         mainPanel.add(centerPanel, BorderLayout.WEST);
//         mainPanel.add(rulesPanel, BorderLayout.CENTER);
//         mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
//         // Add to frame
//         add(mainPanel);
        
//         // Apply theme from user profile
//         applyTheme(userProfile.getPreferredTheme());
//     }
    
//     private void startQuiz() {
//         // Get quiz settings
//         int questionCount = (Integer) questionCountSpinner.getValue();
//         int timePerQuestion = (Integer) timePerQuestionSpinner.getValue();
//         boolean reviewAnswers = reviewAnswersCheckbox.isSelected();
//         boolean showExplanations = showExplanationsCheckbox.isSelected();
//         boolean soundEffects = soundEffectsCheckbox.isSelected();
        
//         // Hide rules screen
//         setVisible(false);
        
//         // Start the quiz with the specified settings
//         SwingUtilities.invokeLater(() -> {
//             Quiz quizScreen = new Quiz(
//                 userProfile, domain, difficulty, questionBank,
//                 questionCount, timePerQuestion, reviewAnswers, 
//                 showExplanations, soundEffects
//             );
//             quizScreen.startQuiz();
//         });
//     }
    
//     private void goBackToLogin() {
//         // Close this window
//         dispose();
        
//         // Show login screen
//         SwingUtilities.invokeLater(() -> {
//             new Login().setVisible(true);
//         });
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
//                 comp.setBackground(btnBgColor);
//                 comp.setForeground(btnFgColor);
//             } else if (comp instanceof JLabel) {
//                 comp.setForeground(fgColor);
//             } else if (comp instanceof JTextField || comp instanceof JComboBox || comp instanceof JSpinner) {
//                 comp.setBackground(Color.WHITE);
//                 comp.setForeground(Color.BLACK);
//             } else if (comp instanceof JCheckBox) {
//                 comp.setBackground(panelBgColor);
//                 comp.setForeground(fgColor);
//             } else if (comp instanceof Container) {
//                 applyThemeToComponent((Container) comp, bgColor, fgColor, btnBgColor, btnFgColor, panelBgColor);
//             }
//         }
//     }
// }


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.net.URL;

/**
 * Enhanced rules screen with attractive UI, images, and improved typography
 */
public class Rules extends JFrame {
    private UserProfile userProfile;
    private String domain;
    private String difficulty;
    private QuestionBank questionBank;
    private JSpinner questionCountSpinner;
    private JSpinner timePerQuestionSpinner;
    private JCheckBox reviewAnswersCheckbox;
    private JCheckBox showExplanationsCheckbox;
    private JCheckBox soundEffectsCheckbox;
    
    // Custom fonts
    private Font titleFont;
    private Font headingFont;
    private Font bodyFont;
    private Font buttonFont;
    
    public Rules(UserProfile userProfile, String domain, 
                String difficulty, QuestionBank questionBank) {
        this.userProfile = userProfile;
        this.domain = domain;
        this.difficulty = difficulty;
        this.questionBank = questionBank;
        
        // Initialize custom fonts
        initializeFonts();
        
        // Set up the frame
        setTitle("Quiz Rules - " + domain + " (" + difficulty + ")");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create main panel with gradient background
        JPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        // Header panel with icon
        JPanel headerPanel = createHeaderPanel();
        
        // Welcome message with user avatar
        JPanel welcomePanel = createWelcomePanel();
        
        // Quiz information panel with enhanced styling
        JPanel infoPanel = createInfoPanel();
        
        // Options panel with icons
        JPanel optionsPanel = createOptionsPanel();
        
        // Rules panel with better formatting
        JPanel rulesPanel = createRulesPanel();
        
        // Buttons panel with styled buttons
        JPanel buttonPanel = createButtonPanel();
        
        // Layout components on main panel
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(welcomePanel, BorderLayout.CENTER);
        
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setOpaque(false);
        centerPanel.add(infoPanel, BorderLayout.NORTH);
        centerPanel.add(optionsPanel, BorderLayout.CENTER);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.WEST);
        mainPanel.add(rulesPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add to frame
        add(mainPanel);
        
        // Apply theme from user profile
        applyTheme(userProfile.getPreferredTheme());
    }
    
    private void initializeFonts() {
        try {
            // Try to use system fonts, fallback to default if not available
            titleFont = new Font("Segoe UI", Font.BOLD, 28);
            headingFont = new Font("Segoe UI", Font.BOLD, 16);
            bodyFont = new Font("Segoe UI", Font.PLAIN, 14);
            buttonFont = new Font("Segoe UI", Font.BOLD, 14);
        } catch (Exception e) {
            // Fallback fonts
            titleFont = new Font("Arial", Font.BOLD, 28);
            headingFont = new Font("Arial", Font.BOLD, 16);
            bodyFont = new Font("Arial", Font.PLAIN, 14);
            buttonFont = new Font("Arial", Font.BOLD, 14);
        }
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(15, 10));
        headerPanel.setOpaque(false);
        
        // Quiz icon
        JLabel iconLabel = new JLabel();
        try {
            // Create a quiz icon using Unicode or simple graphics
            iconLabel.setText("🎯");
            iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
            iconLabel.setHorizontalAlignment(JLabel.CENTER);
        } catch (Exception e) {
            iconLabel.setText("QUIZ");
            iconLabel.setFont(new Font("Arial", Font.BOLD, 24));
            iconLabel.setHorizontalAlignment(JLabel.CENTER);
        }
        
        JLabel titleLabel = new JLabel("Quiz Rules & Configuration", JLabel.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(new Color(45, 55, 72));
        
        JLabel subtitleLabel = new JLabel("Customize your learning experience", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        subtitleLabel.setForeground(new Color(74, 85, 104));
        
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 5));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        
        headerPanel.add(iconLabel, BorderLayout.WEST);
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createWelcomePanel() {
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        welcomePanel.setOpaque(false);
        welcomePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        welcomePanel.setBackground(new Color(255, 255, 255, 200));
        
        // User avatar
        JLabel avatarLabel = new JLabel("👤");
        avatarLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        
        JLabel welcomeLabel = new JLabel("Welcome back, " + userProfile.getUsername() + "!");
        welcomeLabel.setFont(headingFont);
        welcomeLabel.setForeground(new Color(45, 55, 72));
        
        welcomePanel.add(avatarLabel);
        welcomePanel.add(welcomeLabel);
        
        return welcomePanel;
    }
    
    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 8, 12));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createRaisedBevelBorder(),
                "📊 Quiz Information",
                0, 0, headingFont, new Color(45, 55, 72)
            ),
            BorderFactory.createEmptyBorder(10, 15, 15, 15)
        ));
        infoPanel.setBackground(new Color(255, 255, 255, 230));
        
        // Domain info with icon
        JPanel domainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        domainPanel.setOpaque(false);
        JLabel domainIcon = new JLabel("📚 ");
        domainIcon.setFont(bodyFont);
        JLabel domainLabel = new JLabel("Subject: " + domain);
        domainLabel.setFont(bodyFont);
        domainLabel.setForeground(new Color(45, 55, 72));
        domainPanel.add(domainIcon);
        domainPanel.add(domainLabel);
        infoPanel.add(domainPanel);
        
        // Difficulty info with icon
        JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        difficultyPanel.setOpaque(false);
        String diffIcon = difficulty.equals("Easy") ? "🟢" : difficulty.equals("Medium") ? "🟡" : "🔴";
        JLabel difficultyIcon = new JLabel(diffIcon + " ");
        difficultyIcon.setFont(bodyFont);
        JLabel difficultyLabel = new JLabel("Difficulty: " + difficulty);
        difficultyLabel.setFont(bodyFont);
        difficultyLabel.setForeground(new Color(45, 55, 72));
        difficultyPanel.add(difficultyIcon);
        difficultyPanel.add(difficultyLabel);
        infoPanel.add(difficultyPanel);
        
        // Question count setting
        JPanel questionCountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        questionCountPanel.setOpaque(false);
        JLabel questionIcon = new JLabel("❓ ");
        questionIcon.setFont(bodyFont);
        JLabel questionLabel = new JLabel("Number of Questions: ");
        questionLabel.setFont(bodyFont);
        questionLabel.setForeground(new Color(45, 55, 72));
        SpinnerNumberModel questionModel = new SpinnerNumberModel(10, 5, 30, 1);
        questionCountSpinner = new JSpinner(questionModel);
        questionCountSpinner.setFont(bodyFont);
        ((JSpinner.DefaultEditor) questionCountSpinner.getEditor()).getTextField().setFont(bodyFont);
        questionCountPanel.add(questionIcon);
        questionCountPanel.add(questionLabel);
        questionCountPanel.add(questionCountSpinner);
        infoPanel.add(questionCountPanel);
        
        // Time settings
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        timePanel.setOpaque(false);
        JLabel timeIcon = new JLabel("⏱️ ");
        timeIcon.setFont(bodyFont);
        JLabel timeLabel = new JLabel("Time per Question (seconds): ");
        timeLabel.setFont(bodyFont);
        timeLabel.setForeground(new Color(45, 55, 72));
        SpinnerNumberModel timeModel = new SpinnerNumberModel(30, 10, 120, 5);
        timePerQuestionSpinner = new JSpinner(timeModel);
        timePerQuestionSpinner.setFont(bodyFont);
        ((JSpinner.DefaultEditor) timePerQuestionSpinner.getEditor()).getTextField().setFont(bodyFont);
        timePanel.add(timeIcon);
        timePanel.add(timeLabel);
        timePanel.add(timePerQuestionSpinner);
        infoPanel.add(timePanel);
        
        return infoPanel;
    }
    
    private JPanel createOptionsPanel() {
        JPanel optionsPanel = new JPanel(new GridLayout(0, 1, 8, 8));
        optionsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createRaisedBevelBorder(),
                "⚙️ Quiz Options",
                0, 0, headingFont, new Color(45, 55, 72)
            ),
            BorderFactory.createEmptyBorder(10, 15, 15, 15)
        ));
        optionsPanel.setBackground(new Color(255, 255, 255, 230));
        
        reviewAnswersCheckbox = new JCheckBox("📝 Review answers after quiz", true);
        reviewAnswersCheckbox.setFont(bodyFont);
        reviewAnswersCheckbox.setOpaque(false);
        reviewAnswersCheckbox.setForeground(new Color(45, 55, 72));
        
        showExplanationsCheckbox = new JCheckBox("💡 Show detailed explanations", true);
        showExplanationsCheckbox.setFont(bodyFont);
        showExplanationsCheckbox.setOpaque(false);
        showExplanationsCheckbox.setForeground(new Color(45, 55, 72));
        
        soundEffectsCheckbox = new JCheckBox("🔊 Enable sound effects", true);
        soundEffectsCheckbox.setFont(bodyFont);
        soundEffectsCheckbox.setOpaque(false);
        soundEffectsCheckbox.setForeground(new Color(45, 55, 72));
        
        optionsPanel.add(reviewAnswersCheckbox);
        optionsPanel.add(showExplanationsCheckbox);
        optionsPanel.add(soundEffectsCheckbox);
        
        return optionsPanel;
    }
    
    private JPanel createRulesPanel() {
        JPanel rulesPanel = new JPanel(new BorderLayout());
        rulesPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createRaisedBevelBorder(),
                "📋 Quiz Rules & Scoring",
                0, 0, headingFont, new Color(45, 55, 72)
            ),
            BorderFactory.createEmptyBorder(10, 15, 15, 15)
        ));
        rulesPanel.setBackground(new Color(255, 255, 255, 230));
        
        JTextArea rulesText = new JTextArea();
        rulesText.setEditable(false);
        rulesText.setLineWrap(true);
        rulesText.setWrapStyleWord(true);
        rulesText.setFont(bodyFont);
        rulesText.setBackground(new Color(248, 250, 252));
        rulesText.setForeground(new Color(45, 55, 72));
        rulesText.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        rulesText.setText(
            "🎯 GAME RULES:\n\n" +
            "⏰ Time Limit: Answer each question within the allocated time\n\n" +
            "🏆 SCORING SYSTEM:\n" +
            "   • Easy Questions: 10 points\n" +
            "   • Medium Questions: 20 points  \n" +
            "   • Hard Questions: 30 points\n\n" +
            "⚡ SPEED BONUS: Answer within half the time limit for 50% bonus points!\n\n" +
            "✅ No penalty for wrong answers - learn without fear!\n\n" +
            "🏅 ACHIEVEMENTS: Unlock badges by:\n" +
            "   • Scoring 80%+ (Quiz Master)\n" +
            "   • Perfect scores (Perfectionist)\n" +
            "   • Speed bonuses (Lightning Fast)\n\n" +
            "📈 Track your progress and compete with yourself!\n\n" +
            "🎉 Most importantly: Have fun while learning!"
        );
        
        JScrollPane rulesScroll = new JScrollPane(rulesText);
        rulesScroll.setPreferredSize(new Dimension(500, 180));
        rulesScroll.setBorder(BorderFactory.createLoweredBevelBorder());
        rulesScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rulesScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rulesPanel.add(rulesScroll, BorderLayout.CENTER);
        
        return rulesPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setOpaque(false);
        
        // Styled buttons
        JButton backButton = createStyledButton("⬅️ Back to Login", new Color(108, 117, 125), Color.WHITE);
        JButton startButton = createStyledButton("🚀 Start Quiz", new Color(40, 167, 69), Color.WHITE);
        
        // Add action listeners
        startButton.addActionListener(e -> startQuiz());
        backButton.addActionListener(e -> goBackToLogin());
        
        buttonPanel.add(backButton);
        buttonPanel.add(startButton);
        
        return buttonPanel;
    }
    
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(buttonFont);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(160, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    // Custom gradient panel for background
    private class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            
            Color color1 = new Color(240, 248, 255);
            Color color2 = new Color(230, 240, 250);
            GradientPaint gradient = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
    
    private void startQuiz() {
        // Get quiz settings
        int questionCount = (Integer) questionCountSpinner.getValue();
        int timePerQuestion = (Integer) timePerQuestionSpinner.getValue();
        boolean reviewAnswers = reviewAnswersCheckbox.isSelected();
        boolean showExplanations = showExplanationsCheckbox.isSelected();
        boolean soundEffects = soundEffectsCheckbox.isSelected();
        
        // Hide rules screen
        setVisible(false);
        
        // Start the quiz with the specified settings
        SwingUtilities.invokeLater(() -> {
            Quiz quizScreen = new Quiz(
                userProfile, domain, difficulty, questionBank,
                questionCount, timePerQuestion, reviewAnswers, 
                showExplanations, soundEffects
            );
            quizScreen.startQuiz();
        });
    }
    
    private void goBackToLogin() {
        // Close this window
        dispose();
        
        // Show login screen
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
    
    private void applyTheme(String theme) {
        Color bgColor, fgColor, btnBgColor, btnFgColor, panelBgColor;
        
        switch (theme) {
            case "Dark":
                bgColor = new Color(45, 55, 72);
                fgColor = new Color(237, 242, 247);
                btnBgColor = new Color(74, 85, 104);
                btnFgColor = new Color(237, 242, 247);
                panelBgColor = new Color(68, 77, 86, 200);
                break;
            case "Blue":
                bgColor = new Color(219, 234, 254);
                fgColor = new Color(30, 58, 138);
                btnBgColor = new Color(59, 130, 246);
                btnFgColor = Color.WHITE;
                panelBgColor = new Color(191, 219, 254, 200);
                break;
            case "Green":
                bgColor = new Color(236, 253, 245);
                fgColor = new Color(22, 101, 52);
                btnBgColor = new Color(34, 197, 94);
                btnFgColor = Color.WHITE;
                panelBgColor = new Color(209, 250, 229, 200);
                break;
            default: // Light
                bgColor = new Color(248, 250, 252);
                fgColor = new Color(45, 55, 72);
                btnBgColor = new Color(229, 231, 235);
                btnFgColor = new Color(45, 55, 72);
                panelBgColor = new Color(255, 255, 255, 230);
                break;
        }
        
        // Apply theme to all components recursively
        applyThemeToComponent(this.getContentPane(), bgColor, fgColor, btnBgColor, btnFgColor, panelBgColor);
    }
    
    private void applyThemeToComponent(Container container, Color bgColor, Color fgColor, 
                                      Color btnBgColor, Color btnFgColor, Color panelBgColor) {
        if (container instanceof GradientPanel) {
            return; // Don't override gradient panel
        }
        
        container.setBackground(bgColor);
        container.setForeground(fgColor);
        
        for (Component comp : container.getComponents()) {
            if (comp instanceof JPanel && !(comp instanceof GradientPanel)) {
                if (comp.getBackground().getAlpha() < 255) {
                    // Keep transparent backgrounds
                    comp.setBackground(new Color(panelBgColor.getRed(), 
                                               panelBgColor.getGreen(), 
                                               panelBgColor.getBlue(), 
                                               comp.getBackground().getAlpha()));
                } else {
                    comp.setBackground(panelBgColor);
                }
                comp.setForeground(fgColor);
                applyThemeToComponent((Container) comp, bgColor, fgColor, btnBgColor, btnFgColor, panelBgColor);
            } else if (comp instanceof JButton && !comp.getName().equals("styled")) {
                comp.setBackground(btnBgColor);
                comp.setForeground(btnFgColor);
            } else if (comp instanceof JLabel) {
                comp.setForeground(fgColor);
            } else if (comp instanceof JTextField || comp instanceof JComboBox || comp instanceof JSpinner) {
                comp.setBackground(Color.WHITE);
                comp.setForeground(Color.BLACK);
            } else if (comp instanceof JComponent) {
    ((JComponent) comp).setOpaque(false);

            } else if (comp instanceof Container) {
                applyThemeToComponent((Container) comp, bgColor, fgColor, btnBgColor, btnFgColor, panelBgColor);
            }
        }
    }
}