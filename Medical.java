package Medical;

import javax.swing.*;

public class Medical{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WelcomeFrame().setVisible(true));
    }
}