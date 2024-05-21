package org.example;

import db.DBConnector;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
        tasksTableModel = new DefaultTableModel(new Object[]{"ID", "target", "hours", "done"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Только столбец "done" редактируемый
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) {
                    return Boolean.class; // Тип данных столбца "done" - boolean
                }
                return super.getColumnClass(columnIndex);
            }
        };
        tasksTable = new JTable(tasksTableModel);
        JScrollPane scrollPane = new JScrollPane(tasksTable);
        add(scrollPane);
        setTitle("Список работ");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 200);
        setLocationRelativeTo(null);
        setVisible(true);

        // Загрузка специальностей из БД
        loadSpecialtiesFromDatabase(TypeWorksId, ItemsId);

        tasksTable.setCellSelectionEnabled(false);





        // Добавляем обработчик изменения значения в таблице
        tasksTable.getModel().addTableModelListener(e -> {
            // Обновление значения в базе данных
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (column == 3) { // Изменение в столбце "done"
                int id = (int) tasksTable.getValueAt(row, 0);
                boolean done = (boolean) tasksTable.getValueAt(row, column);
                try (Connection connection = DBConnector.connection();
                     PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Работы SET done = ? WHERE id = ?")) {
                    preparedStatement.setInt(1, done ? 1 : 0);
                    preparedStatement.setInt(2, id);
                    preparedStatement.executeUpdate();

                    // Уведомляем таблицу об обновлении одной строки
                    tasksTableModel.fireTableRowsUpdated(row, row);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Ошибка при обновлении данных в базе данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        });




//        tasksTable.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if (e.getClickCount() == 2) {
//                    int selectedRow = tasksTable.getSelectedRow();
//                    if (selectedRow != -1) {
//                        selectedTasks = (int) tasksTable.getValueAt(selectedRow, 0); // Сохраняем выбранный ID
//                        //new TypeWorks(selectedTasks).setVisible(true); // Передаем ID в SpecialtySelection
//                    }
//                }
//            }
//        });





        // Устанавливаем рендерер для всей строки
        tasksTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if ((boolean) tasksTable.getValueAt(row, 3)) {
                    c.setBackground(Color.GREEN);
                } else {
                    c.setBackground(table.getBackground());
                }
                return c;
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
                boolean done = resultSet.getInt("done") == 1;
                tasksTableModel.addRow(new Object[]{id, target, hours, done});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при получении данных из базы данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}