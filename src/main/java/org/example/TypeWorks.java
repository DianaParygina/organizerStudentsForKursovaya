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
class TypeWorks extends JFrame {
    private final JTable typeWorksTable;
    private final DefaultTableModel typeWorksTableModel;
    private int selectedTypeWorks = -1;
    private JLabel totalTimeLabel;
    private JProgressBar progressBar; // Полоса загрузки
    private JLabel progressLabel; // Лейбл для процентов

    public TypeWorks(int ItemsId, String itemName, int itemsHours) {
        // Настройки шрифтов
        Font headerFont = new Font("Arial", Font.BOLD, 16);
        Font tableFont = new Font("Arial", Font.PLAIN, 14);

        // Заголовок окна
        setTitle("Типы работ для " + itemName);

        // Модель таблицы
        typeWorksTableModel = new DefaultTableModel(new Object[]{"ID", "Тип работы"}, 0);
        typeWorksTable = new JTable(typeWorksTableModel);
        typeWorksTable.setFont(tableFont);
        typeWorksTable.setRowHeight(25); // Увеличиваем высоту строк

        // Заголовок с информацией о предмете
        JLabel headerLabel = new JLabel(itemName + " (" + itemsHours + " часов)", SwingConstants.CENTER);
        headerLabel.setFont(headerFont);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Метка для общего времени
        totalTimeLabel = new JLabel("Вы отзанимались: 00:00:00", SwingConstants.CENTER);
        totalTimeLabel.setFont(tableFont);

        // Полоса загрузки
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(91, 232, 93)); // Цвет полосы
        progressBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Лейбл для процентов
        progressLabel = new JLabel("0%");
        progressLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Панель для метки общего времени и прогресс бара
        JPanel timeProgressPanel = new JPanel(new GridLayout(2, 1));
        timeProgressPanel.add(totalTimeLabel);
        timeProgressPanel.add(progressBar);

        // Панель для заголовка, времени/прогресса и лейбла процентов
        JPanel headerTimeProgressPanel = new JPanel(new BorderLayout());
        headerTimeProgressPanel.add(headerLabel, BorderLayout.NORTH);
        headerTimeProgressPanel.add(timeProgressPanel, BorderLayout.CENTER);

        // Скролл для таблицы
        JScrollPane scrollPane = new JScrollPane(typeWorksTable);

        // Размещение элементов на окне
        setLayout(new BorderLayout());
        add(headerTimeProgressPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 280);
        setLocationRelativeTo(null);
        setVisible(true);

        // Загрузка данных
        loadSpecialtiesFromDatabase(ItemsId);
        typeWorksTable.setCellSelectionEnabled(false);
        typeWorksTable.setDefaultEditor(Object.class, null);

        // Обработчик двойного клика по строке таблицы
        typeWorksTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = typeWorksTable.getSelectedRow();
                    if (selectedRow != -1) {
                        selectedTypeWorks = (int) typeWorksTable.getValueAt(selectedRow, 0);
                        new Tasks(selectedTypeWorks, ItemsId, TypeWorks.this).setVisible(true);
                    }
                }
            }
        });

        // Обновляем общее время при открытии окна
        updateTotalTime(ItemsId);
        updateTotalProgress(ItemsId, itemsHours);
    }

    private void loadSpecialtiesFromDatabase(int ItemsId) {
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Тип")) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nameType = resultSet.getString("nameType");
                typeWorksTableModel.addRow(new Object[]{id, nameType});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при получении данных из базы данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Метод для обновления общего времени и прогресс бара
    public void updateTotalProgress(int ItemsId, int itemsHours) {
        long totalTime = calculateTotalTime(ItemsId);
        //totalTimeLabel.setText("Вы отзанимались: " + formatTime(totalTime));

        // Расчет процентов и обновление прогресс бара
        int progressPercent = (int) ((double) totalTime / (itemsHours * 60 * 60 * 1000) * 100);
        progressBar.setValue(progressPercent);
    }

    // Метод для обновления общего времени
    public void updateTotalTime(int ItemsId) {
        long totalTime = calculateTotalTime(ItemsId);
        totalTimeLabel.setText("Вы отзанимались: " + formatTime(totalTime));
    }

    // Метод для расчета общего времени
    private long calculateTotalTime(int ItemsId) {
        long totalTime = 0;
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT elapsedTime FROM Работы WHERE idItem = ?")) {

            preparedStatement.setInt(1, ItemsId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                totalTime += resultSet.getLong("elapsedTime");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при получении данных из базы данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
        return totalTime;
    }

    // Метод для форматирования времени
    private String formatTime(long timeInMillis) {
        long seconds = timeInMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        seconds %= 60;
        minutes %= 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}