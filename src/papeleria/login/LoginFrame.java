package papeleria.login;



import papeleria.AppMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Inicio de Sesion");
        setSize(300, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();

        JLabel passLabel = new JLabel("Contraseña:");
        JPasswordField passField = new JPasswordField();

        JButton loginButton = new JButton("Iniciar Sesion");
        JLabel statusLabel = new JLabel("");

        add(emailLabel);
        add(emailField);
        add(passLabel);
        add(passField);
        add(new JLabel(""));
        add(loginButton);
        add(new JLabel(""));
        add(statusLabel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String contrasena = new String(passField.getPassword());

                LoginManager.RolUsuario rol = LoginManager.autenticar(email, contrasena);

                switch (rol) {
                    case VENDEDOR:
                        dispose(); // Cierra la ventana de login
                        AppMain.iniciarComoVendedor();

                        break;
                    case GERENTE:
                        dispose();
                        JOptionPane.showMessageDialog(null, "Pantalla de gerente no implementada");

                        break;
                    default:
                        statusLabel.setText("Usuario o contraseña incorrectos");
                }
            }
        });

        setVisible(true);
    }

    // Clase simulada para prueba
    static class AppVendedor {
        public static void iniciar() {
            JFrame frame = new JFrame("Pantalla Vendedor");
            frame.setSize(300, 200);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new JLabel("Bienvenido Vendedor"), BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }
}
