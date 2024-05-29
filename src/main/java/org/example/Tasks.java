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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class Tasks extends JFrame {
    private final JTable tasksTable;
    private final DefaultTableModel tasksTableModel;
    private int TypeWorksId;
    private int ItemsId;
    private int selectedTasks = -1;
    private final TypeWorks parentTypeWorks; // Ссылка на родительское окно

    public Tasks(int TypeWorksId, int ItemsId, TypeWorks parentTypeWorks) {
        this.TypeWorksId = TypeWorksId;
        this.ItemsId = ItemsId;
        this.parentTypeWorks = parentTypeWorks;

        // Настройки шрифтов
        Font headerFont = new Font("Arial", Font.BOLD, 16);
        Font tableFont = new Font("Arial", Font.PLAIN, 14);

        // Заголовок окна
        setTitle("Список работ");

        // Модель таблицы
        tasksTableModel = new DefaultTableModel(new Object[]{"ID", "Задача", "Часы", "Срок", "Затрачено", "Готово"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 5) {
                    return Boolean.class;
                } else if (columnIndex == 3 || columnIndex == 4) {
                    return String.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };
        tasksTable = new JTable(tasksTableModel);
        tasksTable.setFont(tableFont);
        tasksTable.setRowHeight(25);

        // Заголовок
        JLabel headerLabel = new JLabel("Задачи", SwingConstants.CENTER);
        headerLabel.setFont(headerFont);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Панель для заголовка
        JPanel headerPanel = new JPanel(new GridLayout(1, 1));
        headerPanel.add(headerLabel);

        // Скролл для таблицы
        JScrollPane scrollPane = new JScrollPane(tasksTable);

        // Размещение элементов на окне
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 280);
        setLocationRelativeTo(null);
        setVisible(true);

        loadSpecialtiesFromDatabase(TypeWorksId, ItemsId);
        tasksTable.setCellSelectionEnabled(false);


        // Добавляем обработчик изменения значения в таблице
        tasksTable.getModel().addTableModelListener(e -> {
            // Обновление значения в базе данных
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (column == 5) { // Изменение в столбце "done"
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
            }else if (column == 3) { // Изменение в столбце "dueDate"
                int id = (int) tasksTable.getValueAt(row, 0);
                String dueDateString = (String) tasksTable.getValueAt(row, 3);
                try (Connection connection = DBConnector.connection();
                     PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Работы SET dueDate = ? WHERE id = ?")) {
                    preparedStatement.setString(1, dueDateString);
                    preparedStatement.setInt(2, id);
                    preparedStatement.executeUpdate();
                    tasksTableModel.fireTableRowsUpdated(row, row);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Ошибка при обновлении данных в базе данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }

            }

        });

        tasksTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = tasksTable.getSelectedRow();
                    if (selectedRow != -1) {
                        selectedTasks = (int) tasksTable.getValueAt(selectedRow, 0); // Сохраняем выбранный ID
                        new AdditionalTasks(selectedTasks, Tasks.this).setVisible(true); // Передаем ID в SpecialtySelection
                    }
                }
            }
        });

        // Устанавливаем рендерер для всей строки
        tasksTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Проверка на elapsedTime >= hours
                String elapsedTimeString = (String) table.getValueAt(row, 4);
                int hours = (int) table.getValueAt(row, 2);
                if (elapsedTimeString != null && !elapsedTimeString.isEmpty()) {
                    long elapsedSeconds = hhmmssToSeconds(elapsedTimeString);
                    if (elapsedSeconds >= hours * 3600) {
                        if ((boolean) table.getValueAt(row, 5)) { // Проверяем значение "done" только если время превышено
                            c.setBackground(Color.GREEN);
                        } else {
                            c.setBackground(Color.YELLOW);
                        }
                        return c; // Выходим после установки цвета
                    }
                }

                // Если elapsedTime < hours, проверяем done и dueDate
                if ((boolean) table.getValueAt(row, 5)) {
                    c.setBackground(Color.GREEN);
                } else {
                    String dueDateDateString = (String) table.getValueAt(row, 3);
                    if (dueDateDateString != null && !dueDateDateString.isEmpty()) {
                        try {
                            Date dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(dueDateDateString);
                            Date currentDate = new Date();
                            if (dueDate.before(currentDate)) {
                                c.setBackground(Color.RED);
                            } else {
                                c.setBackground(table.getBackground());
                            }
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                            c.setBackground(table.getBackground());
                        }
                    } else {
                        c.setBackground(table.getBackground());
                    }
                }
                return c;
            }
        });
    }

    public void refreshTable() {
        // Очистить существующие данные
        tasksTableModel.setRowCount(0);

        // Загрузить обновленные данные из базы данных
        loadSpecialtiesFromDatabase(TypeWorksId, ItemsId); // Подставьте ваши значения

        // Обновить отображение таблицы
        tasksTable.revalidate();
        tasksTable.repaint();
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
                int elapsedTimeSeconds = resultSet.getInt("elapsedTime"); // Получение времени в секундах
                String dueDate = resultSet.getString("dueDate");

                // Преобразование времени в формат HH:mm:ss
                String elapsedTimeFormatted = formatSecondsToHHMMSS(elapsedTimeSeconds);

                tasksTableModel.addRow(new Object[]{id, target, hours, dueDate, elapsedTimeFormatted, done});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при получении данных из базы данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatSecondsToHHMMSS(long timeInMillis) {
        long seconds = timeInMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        seconds %= 60;
        minutes %= 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    // Метод для обновления общего времени в родительском окне
    public void refreshParentTotalTime() {
        if (parentTypeWorks != null) {
            parentTypeWorks.updateTotalTime(ItemsId);
        }
    }

    // Функция для конвертации HH:mm:ss в секунды
    private long hhmmssToSeconds(String hhmmss) {
        String[] parts = hhmmss.split(":");
        long hours = Long.parseLong(parts[0]);
        long minutes = Long.parseLong(parts[1]);
        long seconds = Long.parseLong(parts[2]);
        return hours * 3600 + minutes * 60 + seconds;
    }
}