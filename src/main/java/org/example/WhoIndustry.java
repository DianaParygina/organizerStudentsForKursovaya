package org.example;

import db.DBConnector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

class WhoIndustry extends JFrame {
    private final JTable industryTable;
    private final DefaultTableModel industryTableModel;
    private int selectedIndustryId = -1;

    public WhoIndustry(String currentUsername) {
        // Настройки шрифтов
        Font headerFont = new Font("Arial", Font.BOLD, 16);
        Font tableFont = new Font("Arial", Font.PLAIN, 14);
        // Заголовок окна
        setTitle("Выбор отрасли");

        // Модель таблицы
        industryTableModel = new DefaultTableModel(new Object[]{"ID", "Отрасль"}, 0);
        industryTable = new JTable(industryTableModel);
        industryTable.setFont(tableFont);
        industryTable.setRowHeight(25);

        // Заголовок
        JLabel headerLabel = new JLabel("Выберите отрасль", SwingConstants.CENTER);
        headerLabel.setFont(headerFont);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Панель для заголовка
        JPanel headerPanel = new JPanel(new GridLayout(1, 1));
        headerPanel.add(headerLabel);

        // Скролл для таблицы
        JScrollPane scrollPane = new JScrollPane(industryTable);

        // Размещение элементов на окне
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 200); // Увеличиваем размер окна
        setLocationRelativeTo(null);
        setVisible(true);

        // Загрузка данных
        Methods.loadIndustriesFromDatabase(industryTableModel);
        industryTable.setCellSelectionEnabled(false);
        industryTable.setDefaultEditor(Object.class, null);

        // Обработчик двойного клика по строке таблицы
        industryTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = industryTable.getSelectedRow();
                    if (selectedRow != -1) {
                        selectedIndustryId = (int) industryTable.getValueAt(selectedRow, 0);
                        new SpecialtySelection(selectedIndustryId, currentUsername).setVisible(true);
                    }
                }
            }
        });
    }
}