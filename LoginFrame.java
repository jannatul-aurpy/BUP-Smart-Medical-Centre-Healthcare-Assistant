package Medical;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("Student Login - BUP Smart Medical Centre");
        setSize(450, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        GlobalTable.table.insert(new Student("23029", "Misha", "CSE", "pass029"));
        GlobalTable.table.insert(new Student("23123", "Shanjimom", "EEE", "abc456"));

        JPanel panel = new JPanel();
        panel.setBackground(new Color(240, 255, 240)); // Light green
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        URL logoURL = getClass().getResource("/Medical/bup logo.jpg");
        JLabel logoLabel = new JLabel();
        if (logoURL != null) {
            ImageIcon icon = new ImageIcon(logoURL);
            Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(img));
        } else {
            logoLabel.setText("🏥");
            logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 60));
        }
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel uniName = new JLabel("<html><div style='text-align: center;'>BANGLADESH UNIVERSITY OF PROFESSIONALS</div></html>");
        uniName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        uniName.setForeground(new Color(0, 100, 0));
        uniName.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel subTitle = new JLabel("<html><div style='text-align: center;'>Enter your Student ID and<br>Password to access the Medical Centre</div></html>");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel idLabel = new JLabel("Student ID:", SwingConstants.CENTER);
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField idField = new JTextField();
        idField.setMaximumSize(new Dimension(250, 30));
        idField.setHorizontalAlignment(JTextField.CENTER);
        JLabel passLabel = new JLabel("Password:", SwingConstants.CENTER);
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPasswordField passField = new JPasswordField();
        passField.setMaximumSize(new Dimension(250, 30));
        passField.setHorizontalAlignment(JTextField.CENTER);
        JButton loginBtn = new JButton("Login");
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setBackground(new Color(0, 153, 0)); // Green
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setMaximumSize(new Dimension(150, 35));
        panel.add(logoLabel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(uniName);
        panel.add(Box.createVerticalStrut(10));
        panel.add(subTitle);
        panel.add(Box.createVerticalStrut(20));

        panel.add(idLabel);
        panel.add(idField);
        panel.add(Box.createVerticalStrut(10));

        panel.add(passLabel);
        panel.add(passField);
        panel.add(Box.createVerticalStrut(20));
        panel.add(loginBtn);
        add(panel);
        loginBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String password = new String(passField.getPassword());

            Student student = GlobalTable.table.get(id, password);

            if (student != null) {
                new HomePage(student).setVisible(true);
                dispose();
            } else {
                int option = JOptionPane.showConfirmDialog(this,
                        "❌ Incorrect ID or Password.\nWould you like to register?",
                        "Login Failed", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    new RegistrationFrame().setVisible(true);
                    dispose();
                }
            }
        });
    }
}
