package org.example;

import db.DBConnector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;

public class SecondWindow extends JFrame {
    AddTask tasks = new AddTask();
    List<Task> tasksList;

    public SecondWindow(Item selectedItem) {
        setTitle("Информация о предмете");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(3, 1));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20)); // Отступы

        JLabel nameLabel = new JLabel("Название: " + selectedItem.getName());
        JLabel hoursLabel = new JLabel("Часов: " + selectedItem.getNumberOfHours());
        JButton specificsButton = new JButton("Добавить работу");
        //specificsButton.setPreferredSize(new Dimension(150, 25)); // Задаем размер кнопки
        topPanel.add(nameLabel);
        topPanel.add(hoursLabel);
        topPanel.add(specificsButton);

        add(topPanel, BorderLayout.NORTH);

        specificsButton.addActionListener(e -> ThirdWindow(selectedItem));

        // Обработчик закрытия окна
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        // Получение данных из базы данных
        try {
            DBConnector.connection();
            tasksList = DBConnector.getAllTask();
            DBConnector.createDB();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при получении данных из БД", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } finally {
            DBConnector.closeConnection();
        }

        // Таблица с работами по предмету
        MyTableModelSpecificsTask tableModelTask = new MyTableModelSpecificsTask(tasksList);
        JTable taskTable = new JTable(tableModelTask);
        addSectionWithLabelAndTable("Работы по предмету", taskTable);

        setResizable(false);
        setVisible(true);
    }

    private void addSectionWithLabelAndTable(String labelTitle, JTable table) {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20)); // Отступы

        JLabel label = new JLabel(labelTitle);
        add(Box.createRigidArea(new Dimension(0, 10)));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        tablePanel.add(label);

        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane);

        getContentPane().add(Box.createVerticalGlue());
    }

    private void ThirdWindow(Item selectedItem) {
        new ThirdWindow(this, tasks, selectedItem);
    }
}