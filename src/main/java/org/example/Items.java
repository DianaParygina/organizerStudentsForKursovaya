package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class Items extends JFrame {
    private final JTable itemsTable;
    private final DefaultTableModel itemsTableModel;
    private int selectedItemsId = -1;
    private String selectedItemsName = "";
    private int selectedItemsHours = 0;

    public Items(int itemsId) {
        // Настройки шрифтов
        Font headerFont = new Font("Arial", Font.BOLD, 16);
        Font tableFont = new Font("Arial", Font.PLAIN, 14);
        // Заголовок окна
        setTitle("Список предметов");

        // Модель таблицы
        itemsTableModel = new DefaultTableModel(new Object[]{"ID", "Название", "Количество часов"}, 0);
        itemsTable = new JTable(itemsTableModel);
        itemsTable.setFont(tableFont);
        itemsTable.setRowHeight(25);

        // Заголовок
        JLabel headerLabel = new JLabel("Выберите предмет", SwingConstants.CENTER);
        headerLabel.setFont(headerFont);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Панель для заголовка
        JPanel headerPanel = new JPanel(new GridLayout(1, 1));
        headerPanel.add(headerLabel);

        // Скролл для таблицы
        JScrollPane scrollPane = new JScrollPane(itemsTable);

        // Размещение элементов на окне
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 200); // Увеличиваем размер окна
        setLocationRelativeTo(null);
        setVisible(true);

        // Загрузка данных
        Methods.loadItemsFromDatabase(itemsTableModel, itemsId);
        itemsTable.setCellSelectionEnabled(false);
        itemsTable.setDefaultEditor(Object.class, null);

        // Обработчик двойного клика по строке таблицы
        itemsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = itemsTable.getSelectedRow();
                    if (selectedRow != -1) {
                        selectedItemsId = (int) itemsTable.getValueAt(selectedRow, 0);
                        selectedItemsName = (String) itemsTable.getValueAt(selectedRow, 1);
                        selectedItemsHours = (int) itemsTable.getValueAt(selectedRow, 2);
                        new TypeWorks(selectedItemsId, selectedItemsName, selectedItemsHours).setVisible(true);
                    }
                }
            }
        });

        // Создаем панель для кнопки
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Размещение по центру

        // Кнопка начала
        JButton StartButton = new JButton("К отраслям");
        StartButton.addActionListener(e -> {
            new WhoIndustry("").setVisible(true);
        });

        // Добавляем кнопку на панель
        buttonPanel.add(StartButton);

        // Добавляем панель на окно
        add(buttonPanel, BorderLayout.SOUTH); // Размещаем панель снизу
    }
}