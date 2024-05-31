package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

class AdditionalTasks extends JFrame {
    private final int selectedTask;
    private JLabel timerLabel;
    private Timer timer;
    private long startTime;
    private long elapsedTime = 0;
    private Button startTimer;
    private final Tasks tasks;

    public AdditionalTasks(int selectedTasks, Tasks tasks) {
        this.selectedTask = selectedTasks;
        this.tasks = tasks;

        setTitle("Моя работа");
        setSize(400, 200);

        startTimer = new Button("Запустить время");
        startTimer.addActionListener(e -> {
            if (timer == null) {
                startTime = System.currentTimeMillis();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        elapsedTime += System.currentTimeMillis() - startTime;
                        startTime = System.currentTimeMillis();
                        timerLabel.setText(Methods.formatTime(elapsedTime));
                        try {
                            Methods.saveElapsedTimeToDatabase(selectedTask, elapsedTime);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        tasks.refreshTable();
                    }
                }, 0, 1000);
                startTimer.setLabel("Остановить время");
            } else {
                timer.cancel();
                timer = null;
                startTimer.setLabel("Запустить время");
                tasks.refreshTable();
            }
        });

        Button editTasks = new Button("Редактировать работу");
        editTasks.addActionListener(e -> showEditDialog());

        Button deleteTasks = new Button("Удалить работу");
        deleteTasks.addActionListener(e -> {
            Methods.deleteTask(selectedTask);
            tasks.refreshTable();
            dispose();
        });

        timerLabel = new JLabel("00:00:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));

        try {
            elapsedTime = Methods.loadElapsedTimeFromDatabase(selectedTask);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        timerLabel.setText(Methods.formatTime(elapsedTime));

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

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                try {
                    Methods.saveElapsedTimeToDatabase(selectedTask, elapsedTime);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void showEditDialog() {
        new showEditDialog(selectedTask, tasks);
    }
}