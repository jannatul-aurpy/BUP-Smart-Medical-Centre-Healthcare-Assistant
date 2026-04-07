package Medical;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class WelcomeFrame extends JFrame {
    public WelcomeFrame() {
        setTitle("Welcome to Smart Medical Centre");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 250, 245)); // soft background

        // ===== 🔝 Top Panel (Logo + Welcome + Title) =====
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // 🔗 Load logo
        URL logoURL = getClass().getResource("/Medical/bup logo.jpg");
        JLabel logoLabel;
        if (logoURL != null) {
            ImageIcon logoIcon = new ImageIcon(logoURL);
            Image logoImage = logoIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            logoLabel = new JLabel(new ImageIcon(logoImage));
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        } else {
            logoLabel = new JLabel("🏥");
            logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 60));
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        JLabel welcome = new JLabel("Welcome!");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 34));
        welcome.setForeground(new Color(0, 64, 0));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("BUP Smart Medical Centre & Healthcare Assistant");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(0, 85, 0));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        topPanel.add(logoLabel);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(welcome);
        topPanel.add(title);

        add(topPanel, BorderLayout.NORTH);

        // ===== 🎯 Center Panel: 4 Buttons =====
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton loginBtn = createGreenButton("Login");
        JButton registerBtn = createGreenButton("Register");
        JButton helpBtn = createGreenButton("Help");
        JButton aboutBtn = createGreenButton("About");

        // Add spacing and buttons
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(loginBtn);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(registerBtn);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(helpBtn);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(aboutBtn);

        add(centerPanel, BorderLayout.CENTER);

        // ===== 📞 Contact Info Panel =====
        JPanel contactPanel = new JPanel();
        contactPanel.setOpaque(false);
        contactPanel.setLayout(new BoxLayout(contactPanel, BoxLayout.Y_AXIS));
        contactPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        JLabel contact = new JLabel("📞 Helpline: 09666790799    ✉ Email: info@bup.edu.bd", SwingConstants.CENTER);
        contact.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contact.setForeground(Color.DARK_GRAY);
        contact.setAlignmentX(Component.CENTER_ALIGNMENT);

        contactPanel.add(contact);
        add(contactPanel, BorderLayout.SOUTH);

        // ===== Actions =====
        loginBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        registerBtn.addActionListener(e -> {
            new RegistrationFrame().setVisible(true);
            dispose();
        });

        helpBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "📘 Help - How to Use the Medical System\n\n" +
                            "1. Click on 'Register' to sign up using your Student ID.\n" +
                            "2. Once registered, log in to access services.\n" +
                            "3. Inside, you can:\n" +
                            "   • Book appointments based on symptoms or doctor\n" +
                            "   • Check which doctors are available\n" +
                            "   • View your patient history & prescriptions\n" +
                            "   • Search and request medicines\n\n" +
                            "For help, contact 09666790799 or email info@bup.edu.bd",
                    "Help", JOptionPane.INFORMATION_MESSAGE);
        });

        aboutBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "ℹ️ About the BUP Medical System\n\n" +
                            "This Smart Medical System is designed to provide healthcare\n" +
                            "support to BUP students in a fast, accessible way.\n\n" +
                            "Features include:\n" +
                            "✅ Doctor Appointments\n" +
                            "✅ Emergency Support\n" +
                            "✅ Medicine Availability & Expiry Check\n" +
                            "✅ Prescription History\n\n" +
                            "Created by: Your Team | Bangladesh University of Professionals\n" +
                            "© 2025 All rights reserved.",
                    "About", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    // 🔘 Reusable green button creator
    private JButton createGreenButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        btn.setBackground(new Color(0, 153, 0));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(220, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
