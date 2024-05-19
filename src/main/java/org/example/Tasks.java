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

class Tasks extends JFrame {
    private final JTable tasksTable;
    private final DefaultTableModel tasksTableModel;

    private int selectedTasks = -1;

    public Tasks(int TypeWorksId, int ItemsId) {
        tasksTableModel = new DefaultTableModel(new Object[]{"ID", "target", "hours", "done"}, 0);
        tasksTable = new JTable(tasksTableModel);
        JScrollPane scrollPane = new JScrollPane(tasksTable);
        add(scrollPane);
        setTitle("Список работ");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 150);
        setLocationRelativeTo(null);
        setVisible(true);

        // Загрузка специальностей из БД
        loadSpecialtiesFromDatabase(TypeWorksId, ItemsId);

        tasksTable.setCellSelectionEnabled(false);
        tasksTable.setDefaultEditor(Object.class, null);

        tasksTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = tasksTable.getSelectedRow();
                    if (selectedRow != -1) {
                        selectedTasks = (int) tasksTable.getValueAt(selectedRow, 0); // Сохраняем выбранный ID
                        //new TypeWorks(selectedTasks).setVisible(true); // Передаем ID в SpecialtySelection
                    }
                }
            }
        });
    }

    private void loadSpecialtiesFromDatabase(int TypeWorksId, int ItemsId) {
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Работы WHERE idItem = ? AND idType = ?")) {

            preparedStatement.setInt(1, ItemsId); // Устанавливаем ID предмета
            preparedStatement.setInt(2, TypeWorksId); // Устанавливаем ID типа работы
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String target = resultSet.getString("target");
                int hours = resultSet.getInt("hours");
                int done = resultSet.getInt("done");
                tasksTableModel.addRow(new Object[]{id, target, hours, done});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при получении данных из базы данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }


}


