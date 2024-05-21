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

class Items extends JFrame {
    private final JTable ItemsTable;
    private final DefaultTableModel ItemsTableModel;

    private int selectedItems = -1;
    private int selectedItemsHours = 0; // Переменная для хранения количества часов

    public Items(int ItemsId) {
        ItemsTableModel = new DefaultTableModel(new Object[]{"ID", "name", "number_of_hours"}, 0);
        ItemsTable = new JTable(ItemsTableModel);
        JScrollPane scrollPane = new JScrollPane(ItemsTable);
        add(scrollPane);
        setTitle("Список предметов");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 150);
        setLocationRelativeTo(null);
        setVisible(true);

        // Загрузка специальностей из БД
        loadSpecialtiesFromDatabase(ItemsId);

        ItemsTable.setCellSelectionEnabled(false);
        ItemsTable.setDefaultEditor(Object.class, null);

        ItemsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = ItemsTable.getSelectedRow();
                    if (selectedRow != -1) {
                        selectedItems = (int) ItemsTable.getValueAt(selectedRow, 0); // Сохраняем выбранный ID
                        selectedItemsHours = (int) ItemsTable.getValueAt(selectedRow, 2); // Получаем количество часов
                        new TypeWorks(selectedItems).setVisible(true); // Передаем ID в SpecialtySelection
                    }
                }
            }
        });
    }

    private void loadSpecialtiesFromDatabase(int ItemsId) {
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Предметы WHERE idType = ?")) {

            preparedStatement.setInt(1, ItemsId); // Устанавливаем ID отрасли
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int number_of_hours = resultSet.getInt("number_of_hours");
                ItemsTableModel.addRow(new Object[]{id, name, number_of_hours});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при получении данных из базы данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }


}


