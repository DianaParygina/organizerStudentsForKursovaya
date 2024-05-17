package org.example;

import db.DBConnector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class WhoIndustry extends JFrame {
    private final JTable industryTable;
    private final DefaultTableModel tableModel;

    public WhoIndustry() {
        tableModel = new DefaultTableModel(new Object[]{"ID", "NameIndustry"}, 0);
        industryTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(industryTable);
        add(scrollPane);
        setTitle("Список отраслей");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 150);
        setLocationRelativeTo(null);
        setVisible(true);

        // Загрузка отраслей из БД
        loadIndustriesFromDatabase();

        industryTable.setCellSelectionEnabled(false);
        industryTable.setDefaultEditor(Object.class, null);

        industryTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = industryTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int specialtyId = (int) industryTable.getValueAt(selectedRow, 0);
                        if (specialtyId == 1) {
                            new SpecialtySelectionSystemGeologyWindow("Горное дело").setVisible(true);
                        } else if (specialtyId == 2) {
                            new SpecialtySelectionSystemISTWindow("ИСТ").setVisible(true);
                        }
                    }
                }
            }
        });
    }

    private void loadIndustriesFromDatabase() {
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, NameIndustry FROM who")) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nameIndustry = resultSet.getString("NameIndustry");
                tableModel.addRow(new Object[]{id, nameIndustry});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при получении данных из базы данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new WhoIndustry());
//    }
}