package org.example;

import db.DBConnector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class Course extends JFrame {
    private final JTable courseTable;
    private final DefaultTableModel courseTableModel;

    private int selectedCourse = -1;

    public Course(int CourseId) {
        courseTableModel = new DefaultTableModel(new Object[]{"ID", "nameCourse"}, 0);
        courseTable = new JTable(courseTableModel);
        JScrollPane scrollPane = new JScrollPane(courseTable);
        add(scrollPane);
        setTitle("Список курсов");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 150);
        setLocationRelativeTo(null);
        setVisible(true);

        // Загрузка специальностей из БД
        loadSpecialtiesFromDatabase(CourseId);

        courseTable.setCellSelectionEnabled(false);
        courseTable.setDefaultEditor(Object.class, null);

        courseTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = courseTable.getSelectedRow();
                    if (selectedRow != -1) {
                        selectedCourse = (int) courseTable.getValueAt(selectedRow, 0); // Сохраняем выбранный ID
                        new Items(selectedCourse).setVisible(true); // Передаем ID в SpecialtySelection
                    }
                }
            }
        });
    }

    private void loadSpecialtiesFromDatabase(int CourseId) {
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, nameCourse FROM specialtyCourse WHERE idNameProgram = ?")) {

            preparedStatement.setInt(1, CourseId); // Устанавливаем ID отрасли
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


