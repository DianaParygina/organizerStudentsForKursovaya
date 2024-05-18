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

public class SpecialtySelection extends JFrame  {

    private final JTable specialtyTable;
    private final DefaultTableModel specialtyTableModel;


    public SpecialtySelection(String title) {

        specialtyTableModel = new DefaultTableModel(new Object[]{"ID", "nameProgram"}, 0);
        specialtyTable = new JTable(specialtyTableModel);
        JScrollPane scrollPane = new JScrollPane(specialtyTable);
        add(scrollPane);
        setTitle(title);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);

        // Загрузка специальностей из БД
        loadCourseFromDatabase();

        specialtyTable.setCellSelectionEnabled(false);
        specialtyTable.setDefaultEditor(Object.class, null);

        specialtyTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // getClickCount() -  верный метод для определения двойного клика
                    int selectedRow = specialtyTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int specialtyId = (int) specialtyTable.getValueAt(selectedRow, 0);
                        if (specialtyId == 1) {
                            new Course("Специалитет").setVisible(true);
                        } else if (specialtyId == 2) {
                            new Course("Бакалавриат").setVisible(true);
                        }
                    }
                }
            }
        });
    }

    private void loadCourseFromDatabase () {
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, nameProgram FROM EducationalProgram WHERE idNameIndustry = ?")) { // Добавляем idNameIndustry

            // Замените 1 на фактическое значение idNameIndustry для геологии
            preparedStatement.setInt(1, 1); // 1 -  idNameIndustry для геологии
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nameProgram = resultSet.getString("nameProgram");
                specialtyTableModel.addRow(new Object[]{id, nameProgram});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при получении данных из базы данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

}
