package Medical;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class HomePage extends JFrame {
    public HomePage(Student student) {
        setTitle("BUP Smart Medical System - Home");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // ==== 🔝 Top Panel ====
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(230, 255, 240)); // Light green
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        // Logo
        URL logoURL = getClass().getResource("/Medical/bup logo.jpg");
        JLabel logoLabel = new JLabel();
        if (logoURL != null) {
            ImageIcon icon = new ImageIcon(logoURL);
            Image img = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(img));
        } else {
            logoLabel.setText("🏥");
            logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 60));
        }
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(student.name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        nameLabel.setForeground(new Color(0, 100, 0));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel idLabel = new JLabel("ID: #" + student.id);
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        idLabel.setForeground(Color.DARK_GRAY);
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(logoLabel);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(nameLabel);
        topPanel.add(idLabel);
        topPanel.add(Box.createVerticalStrut(10));

        add(topPanel, BorderLayout.NORTH);

        // ==== 🔲 Center Card Panel ====
        JPanel cardPanel = new JPanel(new GridLayout(1, 2, 30, 20));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        cardPanel.setBackground(Color.WHITE);

        // Doctor Card
        JPanel doctorCard = createCard("Doctor Appointment UI", "/Medical/bup logo.jpg");
        doctorCard.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        doctorCard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JOptionPane.showMessageDialog(null, "👉 Opening Doctor Appointment System...");
                new DoctorAppointmentUI(HomePage.this, student).setVisible(true);
                setVisible(false);
            }
        });

        // Medicine Card
        JPanel medicineCard = createCard("Medicine System UI", "/Medical/image2.jpg");
        medicineCard.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        medicineCard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JOptionPane.showMessageDialog(null, "👉 Opening Medicine System...");
                new MedicineSystemUI(HomePage.this, student.name, student.dept).setVisible(true);
                setVisible(false);
            }
        });

        cardPanel.add(doctorCard);
        cardPanel.add(medicineCard);
        add(cardPanel, BorderLayout.CENTER);

        // ==== 🔚 Bottom Panel with Exit Button ====
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(240, 255, 240));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton exitBtn = new JButton("Exit");
        exitBtn.setBackground(new Color(200, 0, 0));
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        exitBtn.setFocusPainted(false);
        exitBtn.addActionListener(e -> System.exit(0));

        bottomPanel.add(exitBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createCard(String title, String iconPath) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(235, 255, 240)); // Light green shade
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 220, 200), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Icon
        URL iconURL = getClass().getResource(iconPath);
        if (iconURL != null) {
            ImageIcon icon = new ImageIcon(iconURL);
            Image scaled = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            JLabel iconLabel = new JLabel(new ImageIcon(scaled));
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(iconLabel);
        }

        // Title
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setForeground(new Color(0, 100, 0));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(Box.createVerticalStrut(15));
        card.add(label);

        return card;
    }
}
