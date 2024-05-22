package org.example;

import db.DBConnector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class showEditDialog extends JDialog {
    private final int taskId;
    private final Tasks tasks;
    private JTextField tasksNameField;
    private JTextField qtyHoursField;

    public showEditDialog(int taskId, Tasks tasks) {
        this.taskId = taskId;
        this.tasks = tasks;

        setTitle("Редактирование работы");
        setSize(400, 150);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);

        JPanel p1 = new JPanel();
        p1.setLayout(new GridLayout(3, 2));
        FlowLayout layout = new FlowLayout();
        JPanel p2 = new JPanel();
        p2.setLayout(layout);
        JLabel one, two;

        one = new JLabel("Название работы:");
        two = new JLabel("Количество часов:");

        tasksNameField = new JTextField(16);
        qtyHoursField = new JTextField(16);

        // Загрузка данных из БД
        loadTaskData();

        Button transformButton = new Button("Изменить");

        transformButton.addActionListener(e -> {

            String newTarget = tasksNameField.getText();
            int newHours = Integer.parseInt(qtyHoursField.getText());
            try (Connection connection = DBConnector.connection();
                 PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Работы SET target = ?, hours = ? WHERE id = ?")) {
                preparedStatement.setString(1, newTarget);
                preparedStatement.setInt(2, newHours);
                preparedStatement.setInt(3, taskId);
                preparedStatement.executeUpdate();
                tasks.refreshTable(); // Обновляем таблицу в окне "Список работ"
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Ошибка при сохранении изменений.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }

            dispose();
        });

        p1.add(one);
        p1.add(tasksNameField);
        p1.add(two);
        p1.add(qtyHoursField);
        p2.add(transformButton);
        add(p1, "North");
        add(p2, "Center");

        // Обработчик закрытия окна
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadTaskData() {
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT target, hours FROM Работы WHERE id = ?")) {

            preparedStatement.setInt(1, taskId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                tasksNameField.setText(resultSet.getString("target"));
                qtyHoursField.setText(String.valueOf(resultSet.getInt("hours")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при загрузке данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
