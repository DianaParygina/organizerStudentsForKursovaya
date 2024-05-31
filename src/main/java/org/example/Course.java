package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        Methods.loadCourseFromDatabase(courseTableModel, courseId);
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
                            Methods.saveCourseIdToDatabase(selectedCourse, currentUsername);
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
}