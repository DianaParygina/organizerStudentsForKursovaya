package org.example;

import db.DBConnector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class GeologyLectures extends JFrame {

    private final JTable LKTable;
    private final DefaultTableModel LKTableModel;

    public GeologyLectures() {
        LKTableModel = new DefaultTableModel(new Object[]{"ID", "name", "number_of_hours"}, 0);
        LKTable = new JTable(LKTableModel);
        JScrollPane scrollPane = new JScrollPane(LKTable);
        add(scrollPane);
        setTitle("Лекции");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);

        // Загрузка специальностей из БД
        loadCourseGeologyFromDatabase();

        LKTable.setCellSelectionEnabled(false);
        LKTable.setDefaultEditor(Object.class, null);

        LKTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = LKTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int specialtyId = (int) LKTableModel.getValueAt(selectedRow, 0);
                        openLecturesForSpecialty(specialtyId);
                    }
                }
            }
        });
    }

    private void loadCourseGeologyFromDatabase() {
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ПредметыПервыйКурсСпециалитетГеологи")) { // Убираем условие WHERE id = ?

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int number_of_hours = resultSet.getInt("number_of_hours");
                LKTableModel.addRow(new Object[]{id, name, number_of_hours});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при получении данных из базы данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openLecturesForSpecialty(int specialtyId) {
        if (specialtyId == 1) {
            new GeologyLectures().setVisible(true); // Замените GeologyLectures на класс, который отображает лекции для геологии
        } else if (specialtyId == 2) {
            new ISTLectures().setVisible(true); // Замените ISTLectures на класс, который отображает лекции для ИСТ
        }
    }
}