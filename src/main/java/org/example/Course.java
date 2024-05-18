package org.example;

import db.DBConnector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class Course extends JFrame {

    private final JTable courseTable;
    private final DefaultTableModel courseTableModel;

    public Course(String title) {
        courseTableModel = new DefaultTableModel(new Object[]{"ID", "nameCourse"}, 0);
        courseTable = new JTable(courseTableModel);
        JScrollPane scrollPane = new JScrollPane(courseTable);
        add(scrollPane);
        setTitle(title);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);

        // Загрузка специальностей из БД
        loadCourseGeologyFromDatabase();

        courseTable.setCellSelectionEnabled(false);
        courseTable.setDefaultEditor(Object.class, null);

        courseTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // getClickCount() -  верный метод для определения двойного клика
                    int selectedRow = courseTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int specialtyId = (int) courseTableModel.getValueAt(selectedRow, 0);
                        if (specialtyId == 1) {
                            new TypeWorks("1 курс").setVisible(true);
                        } else if (specialtyId == 2) {
                            new TypeWorks("2 курс").setVisible(true);
                        }
                    }
                }
            }
        });
    }

    private void loadCourseGeologyFromDatabase() {
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM specialtyCourse WHERE idnameIndustry = ?")) {

            preparedStatement.setInt(1, 1); // 1 -  idNameIndustry для 1 курса
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nameCourse = resultSet.getString("nameCourse");
                courseTableModel.addRow(new Object[]{id, nameCourse});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при получении данных из базы данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}