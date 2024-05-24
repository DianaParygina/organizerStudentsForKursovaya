package org.example;

import db.DBConnector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class TypeWorks extends JFrame {
    private final JTable typeWorksTable;
    private final DefaultTableModel typeWorksTableModel;
    private int selectedTypeWorks = -1;
    private JLabel totalTimeLabel; // Метка для отображения общего времени

    public TypeWorks(int ItemsId, String itemName, int itemsHours) {
        typeWorksTableModel = new DefaultTableModel(new Object[]{"ID", "nameType"}, 0);
        typeWorksTable = new JTable(typeWorksTableModel);

        // Создаем панель для заголовка
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel headerLabel = new JLabel(itemName + " (" + itemsHours + " часов)");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headerPanel.add(headerLabel);

        //headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        // Добавляем метку для общего времени
        totalTimeLabel = new JLabel("Общее время: 00:00:00");
        headerPanel.add(totalTimeLabel);

        JScrollPane scrollPane = new JScrollPane(typeWorksTable);

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setTitle("Типы работ");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setSize(500, 200);
        setLocationRelativeTo(null);
        setVisible(true);

        // Загрузка специальностей из БД
        loadSpecialtiesFromDatabase(ItemsId);

        typeWorksTable.setCellSelectionEnabled(false);
        typeWorksTable.setDefaultEditor(Object.class, null);

        typeWorksTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = typeWorksTable.getSelectedRow();
                    if (selectedRow != -1) {
                        selectedTypeWorks = (int) typeWorksTable.getValueAt(selectedRow, 0); // Сохраняем выбранный ID
                        new Tasks(selectedTypeWorks, ItemsId, TypeWorks.this).setVisible(true); // Передаем ID в SpecialtySelection
                    }
                }
            }
        });

        // Обновляем общее время при открытии окна
        updateTotalTime(ItemsId);
    }

    private void loadSpecialtiesFromDatabase(int ItemsId) {
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Тип")) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nameType = resultSet.getString("nameType");
                typeWorksTableModel.addRow(new Object[]{id, nameType});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при получении данных из базы данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Метод для обновления общего времени
    public void updateTotalTime(int ItemsId) {
        long totalTime = calculateTotalTime(ItemsId);
        totalTimeLabel.setText("Общее время: " + formatTime(totalTime));
    }


    // Метод для расчета общего времени
    private long calculateTotalTime(int ItemsId) {
        long totalTime = 0;
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT elapsedTime FROM Работы WHERE idItem = ?")) {

            preparedStatement.setInt(1, ItemsId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                totalTime += resultSet.getLong("elapsedTime");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при получении данных из базы данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
        return totalTime;
    }

    // Метод для форматирования времени (скопируйте из класса AdditionalTasks)
    private String formatTime(long timeInMillis) {
        long seconds = timeInMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        seconds %= 60;
        minutes %= 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}


