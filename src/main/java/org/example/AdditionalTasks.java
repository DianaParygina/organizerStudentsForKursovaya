package org.example;

import db.DBConnector;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

class AdditionalTasks extends JFrame {

    private int selectedTask;
    private JLabel timerLabel;
    private Timer timer;
    private long startTime;
    private long elapsedTime = 0; // Переменная для хранения прошедшего времени
    private Button startTimer; // Сделаем кнопку доступной для изменения
    private Tasks tasks;

    public AdditionalTasks(int selectedTasks, Tasks tasks) {

        this.selectedTask = selectedTasks;
        this.tasks = tasks;

        setTitle("Моя работа");
        setSize(400, 200);

        // Кнопка запуска таймера
        startTimer = new Button("Запустить время");
        startTimer.addActionListener(e -> {
            if (timer == null) {
                // Запуск таймера
                startTime = System.currentTimeMillis();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        elapsedTime += System.currentTimeMillis() - startTime; // Добавляем прошедшее время
                        startTime = System.currentTimeMillis(); // Обновляем начальное время
                        timerLabel.setText(formatTime(elapsedTime));
                        // Сохраняем текущее время в базе данных
                        saveElapsedTimeToDatabase();
                    }
                }, 0, 1000); // Обновление каждые 1000 миллисекунд (1 секунда)
                startTimer.setLabel("Остановить время"); // Меняем текст на кнопке
            } else {
                // Остановка таймера
                timer.cancel();
                timer = null;
                startTimer.setLabel("Запустить время"); // Возвращаем текст на кнопке
            }
        });

        // Кнопка редактирования задачи
        Button editTasks = new Button("Редактировать работу");
        editTasks.addActionListener(e ->  showEditDialog());

        // Кнопка удаления задачи
        Button deleteTasks = new Button("Удалить работу");
        deleteTasks.addActionListener(e -> {
            deleteTask(selectedTask);
            tasks.refreshTable();
            dispose();
        });

        // Метка для отображения таймера
        timerLabel = new JLabel("00:00:00"); // Инициализируй timerLabel
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));

        // Загружаем прошедшее время из базы данных при запуске
        loadElapsedTimeFromDatabase();

        // Панель с кнопками и меткой
        Panel panel = new Panel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(timerLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(startTimer);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(editTasks);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(deleteTasks);

        add(panel);

        setLocationRelativeTo(null);
        setVisible(true);

        // Закрытие окна:
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                // Сохраняем прошедшее время в базе данных перед закрытием окна
                saveElapsedTimeToDatabase();
            }
        });

    }


    // Добавление слушателя на нажатие кнопки редактирования
    private void showEditDialog() {
        new showEditDialog();
    }

    private void deleteTask(int taskId) {
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Работы WHERE id = ?")) {
            preparedStatement.setInt(1, taskId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Форматирование времени
    private String formatTime(long timeInMillis) {
        long seconds = timeInMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        seconds %= 60;
        minutes %= 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void loadElapsedTimeFromDatabase() {
        try {
            elapsedTime = DBConnector.loadElapsedTimeFromDatabase(selectedTask);
            // Обновляем текст метки таймера
            timerLabel.setText(formatTime(elapsedTime));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Метод для сохранения прошедшего времени в базе данных
    private void saveElapsedTimeToDatabase() {
        try {
            DBConnector.saveElapsedTimeToDatabase(selectedTask, elapsedTime);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}