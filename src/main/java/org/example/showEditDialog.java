package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class showEditDialog extends JDialog {
    private final int taskId;
    private final Tasks tasks;
    private JTextField tasksNameField;
    private JTextField qtyHoursField;
    private JTextField dueDateField;

    public showEditDialog(int taskId, Tasks tasks) {
        this.taskId = taskId;
        this.tasks = tasks;

        setTitle("Редактирование работы");
        setSize(400, 170);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);

        JPanel p1 = new JPanel();
        p1.setLayout(new GridLayout(3, 2));
        FlowLayout layout = new FlowLayout();
        JPanel p2 = new JPanel();
        p2.setLayout(layout);
        JLabel one, two, three;

        one = new JLabel("Название работы:");
        two = new JLabel("Количество часов:");
        three = new JLabel("Срок сдачи работы:");

        tasksNameField = new JTextField(16);
        qtyHoursField = new JTextField(16);
        dueDateField = new JTextField(16);

        Methods.loadTaskData(tasksNameField, qtyHoursField, dueDateField, taskId);

        Button transformButton = new Button("Изменить");

        transformButton.addActionListener(e -> {
            String newTarget = tasksNameField.getText();
            String newHours = qtyHoursField.getText();
            String newDueDate = dueDateField.getText();

            Methods.updateTask(taskId, newTarget, newHours, newDueDate, tasks);
            dispose();
        });

        p1.add(one);
        p1.add(tasksNameField);
        p1.add(two);
        p1.add(qtyHoursField);
        p1.add(three);
        p1.add(dueDateField);
        p2.add(transformButton);
        add(p1, "North");
        add(p2, "Center");

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }
}