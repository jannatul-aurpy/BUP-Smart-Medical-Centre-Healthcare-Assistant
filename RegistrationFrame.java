package Medical;

import javax.swing.*;
import java.awt.*;

public class RegistrationFrame extends JFrame {
    public RegistrationFrame() {
        setTitle("Register Student - Smart Medical Centre");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        panel.setBackground(new Color(235, 255, 240)); // light greenish background
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        panel.setLayout(new GridLayout(6, 2, 10, 15));
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setFont(labelFont);
        JTextField idField = new JTextField();
        idField.setFont(fieldFont);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(labelFont);
        JTextField nameField = new JTextField();
        nameField.setFont(fieldFont);

        JLabel deptLabel = new JLabel("Department:");
        deptLabel.setFont(labelFont);
        JTextField deptField = new JTextField();
        deptField.setFont(fieldFont);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(labelFont);
        JPasswordField passField = new JPasswordField();
        passField.setFont(fieldFont);
        JButton registerBtn = new JButton("Register");
        registerBtn.setBackground(new Color(0, 153, 0)); // Green
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerBtn.setPreferredSize(new Dimension(100, 35));
        panel.add(idLabel); panel.add(idField);
        panel.add(nameLabel); panel.add(nameField);
        panel.add(deptLabel); panel.add(deptField);
        panel.add(passLabel); panel.add(passField);
        panel.add(new JLabel()); panel.add(registerBtn);
        add(panel);
        registerBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String dept = deptField.getText().trim();
            String pass = new String(passField.getPassword());

            if (id.isEmpty() || name.isEmpty() || dept.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Please fill in all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            GlobalTable.table.insert(new Student(id, name, dept, pass));
            JOptionPane.showMessageDialog(this, "✅ Registration Successful!");
            new LoginFrame().setVisible(true);
            dispose();
        });
    }
}
