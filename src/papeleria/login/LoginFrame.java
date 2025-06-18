package papeleria.login;

import papeleria.AppMain;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Inicio de Sesión");
        setSize(300, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 5, 5));

        JTextField emailField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginButton = new JButton("Iniciar Sesión");

        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Contraseña:"));
        add(passField);
        add(new JLabel(""));
        add(loginButton);

        loginButton.addActionListener((ActionEvent e) -> {
            String email = emailField.getText().trim();
            String contrasena = new String(passField.getPassword()).trim();

            if (email.isEmpty() || contrasena.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (LoginManager.autenticar(email, contrasena)) {
                dispose();
                AppMain.iniciarAplicacion();
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}