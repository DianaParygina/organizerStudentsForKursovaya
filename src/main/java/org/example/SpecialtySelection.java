package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class SpecialtySelection extends JFrame {
    private final JTable specialtyTable;
    private final DefaultTableModel specialtyTableModel;
    private int selectedSpecialty = -1;

    public SpecialtySelection(int industryId, String currentUsername) {
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
        Methods.loadSpecialtiesFromDatabase(specialtyTableModel, industryId);
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
                        new Course(selectedSpecialty, currentUsername).setVisible(true);
                    }
                }
            }
        });
    }
}