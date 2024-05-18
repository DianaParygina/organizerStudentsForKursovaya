package org.example;

import db.DBConnector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.List;

public class TypeWorks extends JFrame {

    private final JTable typeWorksTable;
    private final DefaultTableModel typeWorksTableModel;

    public TypeWorks(String title) {
        typeWorksTableModel = new DefaultTableModel(new Object[]{"ID", "nameType"}, 0);
        typeWorksTable = new JTable(typeWorksTableModel);
        JScrollPane scrollPane = new JScrollPane(typeWorksTable);
        add(scrollPane);
        setTitle(title);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);

        // Загрузка специальностей из БД
        loadCourseGeologyFromDatabase();

        typeWorksTable.setCellSelectionEnabled(false);
        typeWorksTable.setDefaultEditor(Object.class, null);

        typeWorksTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // getClickCount() -  верный метод для определения двойного клика
                    int selectedRow = typeWorksTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int specialtyId = (int) typeWorksTableModel.getValueAt(selectedRow, 0);
                        if (specialtyId == 1) {
                            new LK("ЛК").setVisible(true);
                        } else if (specialtyId == 2) {
                           // new SpecialtySelectionCourseGeologyBachelorWindow("ЛР").setVisible(true);
                        } else if (specialtyId == 3) {
                            // SpecialtySelectionCourseGeologyBachelorWindow("ПК").setVisible(true);
                        }
                    }
                }
            }
        });
    }

    private void loadCourseGeologyFromDatabase() {
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ПредметыПервыйКурсСпециалитетГеологиТип WHERE idCourse = ?")) {

            preparedStatement.setInt(1, 1); // 1 -  idNameIndustry для 1 курса
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
}