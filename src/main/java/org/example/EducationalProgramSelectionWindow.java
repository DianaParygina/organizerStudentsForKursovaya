//package org.example;
//
//import db.DBConnector;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class EducationalProgramSelectionWindow extends JFrame {
//    private final JTable specialtyTable;
//    private final DefaultTableModel specialtyTableModel;
//
//    public EducationalProgramSelectionWindow(int industryId) {
//        specialtyTableModel = new DefaultTableModel(new Object[]{"ID", "nameProgram"}, 0);
//        specialtyTable = new JTable(specialtyTableModel);
//        JScrollPane scrollPane = new JScrollPane(specialtyTable);
//        add(scrollPane);
//        setTitle("Список систем обучения");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(400, 300);
//        setLocationRelativeTo(null);
//        setVisible(true);
//
//        // Загрузка специальностей из БД
//        loadSpecialtiesFromDatabase(industryId);
//
//        specialtyTable.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if (e.getClickCount() == 2) {
//                    int selectedRow = specialtyTable.getSelectedRow();
//                    if (selectedRow != -1) {
//                        int specialtyId = (int) specialtyTableModel.getValueAt(selectedRow, 0);
//                        if (specialtyId == 1) {
//                            new SpecialtySelectionSystemGeologyWindow("Специальность 1").setVisible(true);
//                        } else if (specialtyId == 2) {
//                            new SpecialtySelectionSystemISTWindow("Специальность 2").setVisible(true);
//                        }
//                    }
//                }
//            }
//        });
//    }
//
//    private void loadSpecialtiesFromDatabase(int industryId) {
//        try (Connection connection = DBConnector.connection();
//             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, nameProgram FROM Educationalprogram WHERE idNameIndustry = ?")) {
//
//            preparedStatement.setInt(1, industryId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()) {
//                int id = resultSet.getInt("id");
//                String nameIndustry = resultSet.getString("nameProgram");
//                specialtyTableModel.addRow(new Object[]{id, nameIndustry});
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Ошибка при получении данных из базы данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//}
//
////// Класс для окна специальности (при необходимости)
////class SpecialtySelectionSystemWindow extends JFrame {
////    public SpecialtySelectionSystemWindow(String title) {
////        // ... конструктор для окна специальности
////        //  - Добавьте нужный контент
////        //  - Установите заголовок `title`
////    }
////}