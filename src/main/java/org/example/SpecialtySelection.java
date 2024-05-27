package org.example;

import db.DBConnector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
        // Настройки шрифтов
        Font headerFont = new Font("Arial", Font.BOLD, 16);
        Font tableFont = new Font("Arial", Font.PLAIN, 14);

        // Заголовок окна
        setTitle("Выбор специальности");

        // Модель таблицы
        specialtyTableModel = new DefaultTableModel(new Object[]{"ID", "Специальность"}, 0);
        specialtyTable = new JTable(specialtyTableModel);
        specialtyTable.setFont(tableFont);
        specialtyTable.setRowHeight(25);

        // Заголовок
        JLabel headerLabel = new JLabel("Выберите специальность", SwingConstants.CENTER);
        headerLabel.setFont(headerFont);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Панель для заголовка
        JPanel headerPanel = new JPanel(new GridLayout(1, 1));
        headerPanel.add(headerLabel);

        // Скролл для таблицы
        JScrollPane scrollPane = new JScrollPane(specialtyTable);

        // Размещение элементов на окне
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 200); // Увеличиваем размер окна
        setLocationRelativeTo(null);
        setVisible(true);

        // Загрузка данных
        loadSpecialtiesFromDatabase(industryId);
        specialtyTable.setCellSelectionEnabled(false);
        specialtyTable.setDefaultEditor(Object.class, null);

        // Обработчик двойного клика по строке таблицы
        specialtyTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = specialtyTable.getSelectedRow();
                    if (selectedRow != -1) {
                        selectedSpecialty = (int) specialtyTable.getValueAt(selectedRow, 0);
                        new Course(selectedSpecialty).setVisible(true);
                    }
                }
            }
        });
    }

    private void loadSpecialtiesFromDatabase(int industryId) {
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, nameProgram FROM EducationalProgram WHERE idNameIndustry = ?")) {

            preparedStatement.setInt(1, industryId);
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