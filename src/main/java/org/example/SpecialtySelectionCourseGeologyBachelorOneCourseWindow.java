package org.example;

import db.DBConnector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpecialtySelectionCourseGeologyBachelorOneCourseWindow extends JFrame {

    private final JTable bachelorGeologyTable;
    private final DefaultTableModel bachelorGeologyTableModel;
    AddItem item = new AddItem();
    List<Item> items;

    public SpecialtySelectionCourseGeologyBachelorOneCourseWindow(String title) {
        bachelorGeologyTableModel = new DefaultTableModel(new Object[]{"ID", "name", "number_of_hours"}, 0);
        bachelorGeologyTable = new JTable(bachelorGeologyTableModel);
        JScrollPane scrollPane = new JScrollPane(bachelorGeologyTable);
        add(scrollPane);
        setTitle(title);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);

        // Загрузка специальностей из БД
        loadCourseGeologyFromDatabase();

        bachelorGeologyTable.setCellSelectionEnabled(false);
        bachelorGeologyTable.setDefaultEditor(Object.class, null);

        bachelorGeologyTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // getClickCount() -  верный метод для определения двойного клика
                    int selectedRow = bachelorGeologyTable.getSelectedRow();
                    if (selectedRow != -1) {
                        Item selectedItem = items.get(selectedRow);
                        SecondWindow secondWindow = new SecondWindow(selectedItem);
                        secondWindow.setVisible(true);
                    }
                }
            }
        });
    }

    private void loadCourseGeologyFromDatabase() {
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Items WHERE idCourse = ?")) {

            preparedStatement.setInt(1, 1);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int number_of_hours = resultSet.getInt("number_of_hours");
                bachelorGeologyTableModel.addRow(new Object[]{id, name, number_of_hours});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при получении данных из базы данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}