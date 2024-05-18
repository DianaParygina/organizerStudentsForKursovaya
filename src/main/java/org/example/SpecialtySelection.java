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

class SpecialtySelection extends JFrame {
    private final JTable specialtyTable;
    private final DefaultTableModel specialtyTableModel;

    private int selectedSpecialty = -1;

    public SpecialtySelection(int industryId) {
        specialtyTableModel = new DefaultTableModel(new Object[]{"ID", "nameProgram"}, 0);
        specialtyTable = new JTable(specialtyTableModel);
        JScrollPane scrollPane = new JScrollPane(specialtyTable);
        add(scrollPane);
        setTitle("Список специальностей");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 150);
        setLocationRelativeTo(null);
        setVisible(true);

        // Загрузка специальностей из БД
        loadSpecialtiesFromDatabase(industryId);

        specialtyTable.setCellSelectionEnabled(false);
        specialtyTable.setDefaultEditor(Object.class, null);

        specialtyTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = specialtyTable.getSelectedRow();
                    if (selectedRow != -1) {
                        selectedSpecialty = (int) specialtyTable.getValueAt(selectedRow, 0); // Сохраняем выбранный ID
                        new Course(selectedSpecialty).setVisible(true); // Передаем ID в SpecialtySelection
                    }
                }
            }
        });
    }

    private void loadSpecialtiesFromDatabase(int industryId) {
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, nameProgram FROM EducationalProgram WHERE idNameIndustry = ?")) {

            preparedStatement.setInt(1, industryId); // Устанавливаем ID отрасли
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


