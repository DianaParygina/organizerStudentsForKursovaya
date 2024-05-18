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

class TypeWorks extends JFrame {
    private final JTable typeWorksTable;
    private final DefaultTableModel typeWorksTableModel;

    private int selectedTypeWorks = -1;

    public TypeWorks(int TypeWorksId) {
        typeWorksTableModel = new DefaultTableModel(new Object[]{"ID", "nameType"}, 0);
        typeWorksTable = new JTable(typeWorksTableModel);
        JScrollPane scrollPane = new JScrollPane(typeWorksTable);
        add(scrollPane);
        setTitle("Список типов");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 150);
        setLocationRelativeTo(null);
        setVisible(true);

        // Загрузка специальностей из БД
        loadSpecialtiesFromDatabase();

        typeWorksTable.setCellSelectionEnabled(false);
        typeWorksTable.setDefaultEditor(Object.class, null);

        typeWorksTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = typeWorksTable.getSelectedRow();
                    if (selectedRow != -1) {
                        selectedTypeWorks = (int) typeWorksTable.getValueAt(selectedRow, 0); // Сохраняем выбранный ID
                        new Items(selectedTypeWorks).setVisible(true); // Передаем ID в SpecialtySelection
                    }
                }
            }
        });
    }

    private void loadSpecialtiesFromDatabase() {
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


}


