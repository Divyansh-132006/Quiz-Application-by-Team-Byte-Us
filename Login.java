
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class Login extends JFrame {
    private JTextField usernameField;
    private JComboBox<String> domainCombo;
    private JComboBox<String> difficultyCombo;
    private JComboBox<String> themeCombo;
    private JRadioButton newUserRadio;
    private JRadioButton existingUserRadio;
    private JComboBox<String> existingUsersCombo;
    private QuestionBank questionBank;
    

    private Font titleFont = new Font("Segoe UI", Font.BOLD, 28);
    private Font subtitleFont = new Font("Segoe UI", Font.PLAIN, 16);
    private Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
    private Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);
    
    public Login() {
      
        questionBank = new QuestionBank();
        
     
        setTitle("Quiz Master - Login");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
     
        setContentPane(createGradientPanel());
        
        JPanel mainPanel = createMainPanel();
        
      
        add(mainPanel);
        
 
        applyTheme("Light");
    }
    
    private JPanel createGradientPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
              
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(135, 206, 250), // Light blue
                    0, getHeight(), new Color(70, 130, 180) // Steel blue
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }
    
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
      
        JPanel headerPanel = createHeaderPanel();
        
       
        JPanel contentPanel = createContentPanel();
        
  
        JPanel buttonPanel = createButtonPanel();
    
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        
        JPanel logoTitlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        logoTitlePanel.setOpaque(false);
     
        JLabel logoLabel = createLogoLabel();
        logoTitlePanel.add(logoLabel);
        
       
        JPanel titleSection = new JPanel(new GridLayout(2, 1, 0, 5));
        titleSection.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Quiz Master");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        
        JLabel subtitleLabel = new JLabel("Test Your Knowledge & Learn");
        subtitleLabel.setFont(subtitleFont);
        subtitleLabel.setForeground(new Color(220, 220, 220));
        subtitleLabel.setHorizontalAlignment(JLabel.CENTER);
        
        titleSection.add(titleLabel);
        titleSection.add(subtitleLabel);
        logoTitlePanel.add(titleSection);
        
        headerPanel.add(logoTitlePanel, BorderLayout.CENTER);
        return headerPanel;
    }
    
    private JLabel createLogoLabel() {
        try {
         
            BufferedImage icon = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = icon.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2d.setColor(new Color(255, 215, 0)); 
            g2d.fillRoundRect(8, 12, 48, 40, 8, 8);
            
            g2d.setColor(new Color(255, 140, 0)); 
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(8, 12, 48, 40, 8, 8);
            
            g2d.setColor(new Color(70, 130, 180));
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            FontMetrics fm = g2d.getFontMetrics();
            String text = "?";
            int x = (64 - fm.stringWidth(text)) / 2;
            int y = (64 + fm.getAscent()) / 2 - 4;
            g2d.drawString(text, x, y);
            
            g2d.dispose();
            return new JLabel(new ImageIcon(icon));
        } catch (Exception e) {
          
            JLabel logoLabel = new JLabel("📚");
            logoLabel.setFont(new Font("Arial", Font.PLAIN, 48));
            return logoLabel;
        }
    }
    
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setOpaque(false);
        
    
        JPanel profileCard = createUserProfileCard();
        
       
        JPanel settingsCard = createQuizSettingsCard();
        
        contentPanel.add(profileCard);
        contentPanel.add(settingsCard);
        
        return contentPanel;
    }
    
    private JPanel createUserProfileCard() {
        JPanel card = createModernCard("👤 User Profile");
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        
       
        JPanel userTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userTypePanel.setOpaque(false);
        
        ButtonGroup userTypeGroup = new ButtonGroup();
        newUserRadio = createStyledRadioButton("🆕 New User");
        existingUserRadio = createStyledRadioButton("👨‍💼 Existing User");
        
        userTypeGroup.add(newUserRadio);
        userTypeGroup.add(existingUserRadio);
        newUserRadio.setSelected(true);
        
        userTypePanel.add(newUserRadio);
        userTypePanel.add(existingUserRadio);
        
  
        JPanel newUserPanel = createInputPanel("✏️ Username:", null);
        usernameField = new JTextField(15);
        styleTextField(usernameField);
        newUserPanel.add(usernameField);
    
        JPanel existingUserPanel = createInputPanel("👥 Select User:", null);
        existingUsersCombo = new JComboBox<>();
        styleComboBox(existingUsersCombo);
        refreshUserList();
        existingUserPanel.add(existingUsersCombo);
        existingUserPanel.setVisible(false);
      
        newUserRadio.addActionListener(e -> {
            newUserPanel.setVisible(true);
            existingUserPanel.setVisible(false);
            revalidate();
        });
        
        existingUserRadio.addActionListener(e -> {
            newUserPanel.setVisible(false);
            existingUserPanel.setVisible(true);
            refreshUserList();
            revalidate();
        });
        
        card.add(Box.createVerticalStrut(10));
        card.add(userTypePanel);
        card.add(Box.createVerticalStrut(15));
        card.add(newUserPanel);
        card.add(existingUserPanel);
        card.add(Box.createVerticalGlue());
        
        return card;
    }
    
    private JPanel createQuizSettingsCard() {
        JPanel card = createModernCard("⚙️ Quiz Settings");
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        
       
        JPanel domainPanel = createInputPanel("📚 Subject Domain:", null);
        domainCombo = new JComboBox<>(questionBank.getAvailableDomains().toArray(new String[0]));
        styleComboBox(domainCombo);
        domainPanel.add(domainCombo);
        
    
        JPanel difficultyPanel = createInputPanel("🎯 Difficulty Level:", null);
        difficultyCombo = new JComboBox<>(new String[]{"🟢 Easy", "🟡 Medium", "🔴 Hard"});
        styleComboBox(difficultyCombo);
        difficultyPanel.add(difficultyCombo);
        
  
        JPanel themePanel = createInputPanel("🎨 Theme:", null);
        themeCombo = new JComboBox<>(new String[]{"☀️ Light", "🌙 Dark", "💙 Blue", "💚 Green"});
        styleComboBox(themeCombo);
        themePanel.add(themeCombo);
        
        card.add(Box.createVerticalStrut(10));
        card.add(domainPanel);
        card.add(Box.createVerticalStrut(15));
        card.add(difficultyPanel);
        card.add(Box.createVerticalStrut(15));
        card.add(themePanel);
        card.add(Box.createVerticalGlue());
        
        return card;
    }
    
    private JPanel createModernCard(String title) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
               
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 15, 15);
                
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 15, 15);
                
                g2d.setColor(new Color(200, 200, 200));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 15, 15);
            }
        };
        
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
      
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(70, 130, 180));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(new JSeparator());
        
        return card;
    }
    
    private JPanel createInputPanel(String labelText, String iconPath) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        panel.setOpaque(false);
        
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        label.setForeground(new Color(60, 60, 60));
        label.setPreferredSize(new Dimension(140, 25));
        
        panel.add(label);
        return panel;
    }
    
    private JRadioButton createStyledRadioButton(String text) {
        JRadioButton radio = new JRadioButton(text);
        radio.setFont(labelFont);
        radio.setOpaque(false);
        radio.setForeground(new Color(60, 60, 60));
        radio.setFocusPainted(false);
        return radio;
    }
    
    private void styleTextField(JTextField field) {
        field.setFont(labelFont);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setPreferredSize(new Dimension(200, 35));
    }
    
    private void styleComboBox(JComboBox<?> combo) {
        combo.setFont(labelFont);
        combo.setPreferredSize(new Dimension(200, 35));
        combo.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        
        JButton startButton = createStyledButton("🚀 Start Quiz", new Color(34, 139, 34));
        JButton viewStatsButton = createStyledButton("📊 View Statistics", new Color(70, 130, 180));
        
    
        startButton.addActionListener(e -> startQuiz());
        viewStatsButton.addActionListener(e -> viewStatistics());
        
        buttonPanel.add(viewStatsButton);
        buttonPanel.add(startButton);
        
        return buttonPanel;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2d.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(bgColor.brighter());
                } else {
                    g2d.setColor(bgColor);
                }
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
              
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(getText(), x, y);
            }
        };
        
        button.setFont(buttonFont);
        button.setPreferredSize(new Dimension(180, 45));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private void refreshUserList() {
        existingUsersCombo.removeAllItems();
        List<String> usernames = UserProfile.getAllUsernames();
        for (String username : usernames) {
            existingUsersCombo.addItem(username);
        }
    }
    
    private void startQuiz() {
   
        String username;
        if (newUserRadio.isSelected()) {
            username = usernameField.getText().trim();
            if (username.isEmpty()) {
                showStyledMessage("Please enter a username", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            if (existingUsersCombo.getSelectedItem() == null) {
                showStyledMessage("No existing users found. Please create a new user.", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            username = existingUsersCombo.getSelectedItem().toString();
        }
        
     
        UserProfile profile = UserProfile.loadProfile(username);
        
      
        String selectedDifficulty = difficultyCombo.getSelectedItem().toString().replaceAll("🟢|🟡|🔴|\\s", "");
        String selectedTheme = themeCombo.getSelectedItem().toString().replaceAll("☀️|🌙|💙|💚|\\s", "");
        
        profile.setPreferredDifficulty(selectedDifficulty);
        profile.setPreferredTheme(selectedTheme);
        profile.saveProfile();
        
       
        String domain = domainCombo.getSelectedItem().toString();
        String difficulty = selectedDifficulty;
        
     
        setVisible(false);
        
       
        SwingUtilities.invokeLater(() -> {
            Rules rulesScreen = new Rules(profile, domain, difficulty, questionBank);
            rulesScreen.setVisible(true);
        });
    }
    
    private void viewStatistics() {
      
        String username;
        if (newUserRadio.isSelected()) {
            username = usernameField.getText().trim();
            if (username.isEmpty()) {
                showStyledMessage("Please enter a username", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
         
            boolean userExists = UserProfile.getAllUsernames().contains(username);
            if (!userExists) {
                showStyledMessage("User not found. Create a profile by starting a quiz first.", 
                    "User Not Found", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            if (existingUsersCombo.getSelectedItem() == null) {
                showStyledMessage("No existing users found. Please create a new user.", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            username = existingUsersCombo.getSelectedItem().toString();
        }
        
       
        UserProfile profile = UserProfile.loadProfile(username);
        showStyledStatisticsDialog(profile);
    }
    
    private void showStyledMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
    
    private void showStyledStatisticsDialog(UserProfile profile) {
        JDialog statsDialog = new JDialog(this, "📊 Quiz Statistics - " + profile.getUsername(), true);
        statsDialog.setSize(700, 500);
        statsDialog.setLocationRelativeTo(this);
       
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(240, 248, 255),
                    0, getHeight(), new Color(176, 196, 222)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        backgroundPanel.setLayout(new BorderLayout(15, 15));
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        // Header with user icon
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("📈 Statistics for " + profile.getUsername());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(25, 25, 112));
        headerPanel.add(titleLabel);
        
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        contentPanel.setOpaque(false);
        
     
        JPanel achievementsCard = createModernCard("🏆 Achievements");
        JPanel badgesPanel = new JPanel();
        badgesPanel.setLayout(new BoxLayout(badgesPanel, BoxLayout.Y_AXIS));
        badgesPanel.setOpaque(false);
        
        JLabel pointsLabel = new JLabel("🎯 Achievement Points: " + profile.getAchievementPoints());
        pointsLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        pointsLabel.setForeground(new Color(255, 140, 0));
        badgesPanel.add(pointsLabel);
        badgesPanel.add(Box.createVerticalStrut(10));
        
        JLabel badgesTitle = new JLabel("🏅 Earned Badges:");
        badgesTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        badgesPanel.add(badgesTitle);
        badgesPanel.add(Box.createVerticalStrut(5));
        
        for (String badge : profile.getEarnedBadges()) {
            JLabel badgeLabel = new JLabel("⭐ " + badge);
            badgeLabel.setFont(labelFont);
            badgeLabel.setForeground(new Color(50, 50, 50));
            badgesPanel.add(badgeLabel);
        }
        
        if (profile.getEarnedBadges().isEmpty()) {
            JLabel noBadgesLabel = new JLabel("💡 Complete quizzes to earn badges!");
            noBadgesLabel.setFont(labelFont);
            noBadgesLabel.setForeground(new Color(100, 100, 100));
            badgesPanel.add(noBadgesLabel);
        }
        
        achievementsCard.add(new JScrollPane(badgesPanel));
        
        
        JPanel statsCard = createModernCard("📊 Quiz Results");
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setOpaque(false);
        
        JLabel totalQuizzesLabel = new JLabel("📝 Total Quizzes: " + profile.getTotalQuizzesTaken());
        totalQuizzesLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalQuizzesLabel.setForeground(new Color(70, 130, 180));
        resultsPanel.add(totalQuizzesLabel);
        resultsPanel.add(Box.createVerticalStrut(15));
        
  
        if (profile.getTotalQuizzesTaken() > 0) {
            JLabel performanceTitle = new JLabel("📈 Domain Performance:");
            performanceTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
            resultsPanel.add(performanceTitle);
            resultsPanel.add(Box.createVerticalStrut(8));
            
            for (String domain : questionBank.getAvailableDomains()) {
                List<QuizResult> domainResults = profile.getResultsForDomain(domain);
                
                if (!domainResults.isEmpty()) {
                    double totalScore = 0;
                    for (QuizResult result : domainResults) {
                        totalScore += result.getPercentageScore();
                    }
                    double averageScore = totalScore / domainResults.size();
                    
                    JLabel domainLabel = new JLabel(String.format("📚 %s: %.1f%% avg (%d quizzes)", 
                        domain, averageScore, domainResults.size()));
                    domainLabel.setFont(labelFont);
                    domainLabel.setForeground(new Color(60, 60, 60));
                    resultsPanel.add(domainLabel);
                }
            }
        } else {
            JLabel noResultsLabel = new JLabel("🎯 Take some quizzes to see your stats!");
            noResultsLabel.setFont(labelFont);
            noResultsLabel.setForeground(new Color(100, 100, 100));
            resultsPanel.add(noResultsLabel);
        }
        
        statsCard.add(new JScrollPane(resultsPanel));
        
        contentPanel.add(achievementsCard);
        contentPanel.add(statsCard);
     
        JButton closeButton = createStyledButton("✖️ Close", new Color(220, 20, 60));
        closeButton.addActionListener(e -> statsDialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(closeButton);
        
        backgroundPanel.add(headerPanel, BorderLayout.NORTH);
        backgroundPanel.add(contentPanel, BorderLayout.CENTER);
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        statsDialog.add(backgroundPanel);
        statsDialog.setVisible(true);
    }
    
    private void applyTheme(String theme) {
       
        applyThemeToComponent(this.getContentPane(), theme);
    }
    
    private void applyThemeToComponent(Container container, String theme) {
        
        Color bgColor, fgColor, btnBgColor, btnFgColor, panelBgColor;
        
        switch (theme) {
            case "Dark":
                bgColor = new Color(50, 50, 50);
                fgColor = new Color(220, 220, 220);
                btnBgColor = new Color(80, 80, 80);
                btnFgColor = new Color(220, 220, 220);
                panelBgColor = new Color(60, 60, 60);
                break;
            case "Blue":
                bgColor = new Color(220, 230, 255);
                fgColor = new Color(20, 20, 70);
                btnBgColor = new Color(100, 150, 220);
                btnFgColor = Color.WHITE;
                panelBgColor = new Color(200, 210, 240);
                break;
            case "Green":
                bgColor = new Color(230, 250, 230);
                fgColor = new Color(20, 70, 20);
                btnBgColor = new Color(100, 200, 100);
                btnFgColor = Color.WHITE;
                panelBgColor = new Color(210, 240, 210);
                break;
            default: // Light
                bgColor = new Color(240, 240, 240);
                fgColor = Color.BLACK;
                btnBgColor = new Color(230, 230, 230);
                btnFgColor = Color.BLACK;
                panelBgColor = new Color(250, 250, 250);
                break;
        }
        
        for (Component comp : container.getComponents()) {
            if (comp instanceof JPanel && comp.isOpaque()) {
                comp.setBackground(panelBgColor);
                comp.setForeground(fgColor);
                applyThemeToComponent((Container) comp, theme);
            } else if (comp instanceof JLabel) {
                comp.setForeground(fgColor);
            } else if (comp instanceof JTextField || comp instanceof JComboBox) {
                comp.setBackground(Color.WHITE);
                comp.setForeground(Color.BLACK);
            } else if (comp instanceof JRadioButton) {
                if (comp instanceof JRadioButton && ((JRadioButton) comp).isOpaque()) {
                    comp.setBackground(panelBgColor);
                }
                comp.setForeground(fgColor);
            } else if (comp instanceof Container) {
                applyThemeToComponent((Container) comp, theme);
            }
        }
    }
    
    public static void main(String[] args) {
    
        try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            
            System.setProperty("awt.useSystemAAFontSettings", "on"); 
            System.setProperty("swing.aatext", "true");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}
