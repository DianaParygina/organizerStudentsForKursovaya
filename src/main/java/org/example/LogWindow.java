package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

class LogWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LogWindow() {
        // Настройки шрифтов
        Font headerFont = new Font("Arial", Font.BOLD, 16);
        Font labelFont = new Font("Arial", Font.PLAIN, 14);

        // Заголовок окна
        setTitle("Авторизация");

        // Текстовое поле для ввода логина
        JLabel usernameLabel = new JLabel("Логин:");
        usernameLabel.setFont(labelFont);
        usernameField = new JTextField();

        // Поле для ввода пароля
        JLabel passwordLabel = new JLabel("Пароль:");
        passwordLabel.setFont(labelFont);
        passwordField = new JPasswordField();

        // Кнопка входа
        JButton loginButton = new JButton("Войти");
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try {
                if (Methods.authenticateUser(username, password)) {
                    int idNeed = Methods.getIdNeed(username);
                    if (idNeed != -1) {
                        dispose();
                        new Items(idNeed).setVisible(true);
                    } else {
                        dispose();
                        new WhoIndustry(username).setVisible(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Неверный логин или пароль.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Ошибка при проверке данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Размещение элементов на окне
        JPanel contentPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.add(usernameLabel);
        contentPanel.add(usernameField);
        contentPanel.add(passwordLabel);
        contentPanel.add(passwordField);
        contentPanel.add(new JLabel());
        contentPanel.add(loginButton);

        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}