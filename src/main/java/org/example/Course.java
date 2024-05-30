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

class Course extends JFrame {
    private final JTable courseTable;
    private final DefaultTableModel courseTableModel;
    private int selectedCourse = -1;

    public Course(int courseId, String currentUsername) {
        // Настройки шрифтов
        Font headerFont = new Font("Arial", Font.BOLD, 16);
        Font tableFont = new Font("Arial", Font.PLAIN, 14);
        // Заголовок окна
        setTitle("Выбор курса");

        // Модель таблицы
        courseTableModel = new DefaultTableModel(new Object[]{"ID", "Курс"}, 0);
        courseTable = new JTable(courseTableModel);
        courseTable.setFont(tableFont);
        courseTable.setRowHeight(25);

        // Заголовок
        JLabel headerLabel = new JLabel("Выберите курс", SwingConstants.CENTER);
        headerLabel.setFont(headerFont);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Панель для заголовка
        JPanel headerPanel = new JPanel(new GridLayout(1, 1));
        headerPanel.add(headerLabel);

        // Скролл для таблицы
        JScrollPane scrollPane = new JScrollPane(courseTable);

        // Размещение элементов на окне
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 200); // Увеличиваем размер окна
        setLocationRelativeTo(null);
        setVisible(true);

        // Загрузка данных
        loadSpecialtiesFromDatabase(courseId);
        courseTable.setCellSelectionEnabled(false);
        courseTable.setDefaultEditor(Object.class, null);

        // Обработчик двойного клика по строке таблицы
        courseTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = courseTable.getSelectedRow();
                    if (selectedRow != -1) {
                        selectedCourse = (int) courseTable.getValueAt(selectedRow, 0);
                        // Сохраняем id курса в базу данных, связав его с пользователем
                        try {
                            saveCourseIdToDatabase(selectedCourse, currentUsername);
                            new Items(selectedCourse).setVisible(true);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Ошибка сохранения данных", "Ошибка", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
    }

    private void loadSpecialtiesFromDatabase(int courseId) {
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, nameCourse FROM specialtyCourse WHERE idNameProgram = ?")) {
            preparedStatement.setInt(1, courseId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nameCourse = resultSet.getString("nameCourse");
                courseTableModel.addRow(new Object[]{id, nameCourse});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при получении данных из базы данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Метод для сохранения id курса в базу данных
    private void saveCourseIdToDatabase(int courseId, String currentUsername) throws SQLException {
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE вход SET idNeed = ? WHERE username = ?")) {
            preparedStatement.setInt(1, courseId);
            preparedStatement.setString(2, currentUsername);
            preparedStatement.executeUpdate();
        }
    }
}