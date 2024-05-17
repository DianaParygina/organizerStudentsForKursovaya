//package org.example;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
//public class ThirdWindow extends JDialog {
//
//    AddTask tasks = new AddTask();
//    SecondWindow secondWindow;
//
//    public ThirdWindow(SecondWindow secondWindow, AddTask tasks, Item selectedItem) {
//        this.secondWindow = secondWindow;
//        setTitle("Добавить работу");
//        setSize(400, 200);
//        setLocationRelativeTo(null);
//        setLayout(new FlowLayout());
//
//        FlowLayout layout = new FlowLayout();
//        JPanel p1 = new JPanel();
//        p1.setLayout(new GridLayout(3, 2));
//        JPanel p2 = new JPanel();
//        p2.setLayout(layout);
//        Button specificsButton = new Button("Добавить работу");
//        // данные из selectedItem для заполнения второй формы
//        JLabel nameLabel = new JLabel("Название: " + selectedItem.getName());
//        JLabel hoursLabel = new JLabel("Часов: " + selectedItem.getNumberOfHours());
//
//        JTextField typeTaskField, targetTaskField, due_dateTaskField, doneTaskField;
//
//        JLabel typeTaskLabel = new JLabel("Тип работы:");
//        JLabel targetTaskLabel = new JLabel("Цель работы:");
//        JLabel due_dateTaskLabel = new JLabel("Срок сдачи:");
//
//        typeTaskField = new JTextField(16);
//        targetTaskField = new JTextField(16);
//        due_dateTaskField = new JTextField(16);
//        doneTaskField = new JTextField(16);
//
//        specificsButton.addActionListener(e -> {
//
//            String typeTask = typeTaskField.getText();
//            String targetTask = targetTaskField.getText();
//            String due_dateTask = due_dateTaskField.getText();
//            String doneTask = doneTaskField.getText();
//            boolean isDone = Boolean.parseBoolean(doneTask);
//
//            setLocationRelativeTo(null);
//            if (typeTask.matches(".*\\d.*")) {
//                JOptionPane.showMessageDialog(null, "Тип предмета не может содержать цифры");
//                typeTaskField.setText("");
//                return;
//            }
//
////            if (!due_dateTask.matches("\\d+")) {
////                JOptionPane.showMessageDialog(null, "Количество штатов должно быть числом");
////                due_dateTaskField.setText("");
////                return;
////            }
//
//            tasks.AddTasks(typeTask, targetTask, due_dateTask, isDone, selectedItem.getId());
//
//            typeTaskField.setText("");
//            targetTaskField.setText("");
//            due_dateTaskField.setText("");
//            doneTaskField.setText("");
//
//            dispose();
//            secondWindow.updateTable();
//
//        });
//
//        p1.add(typeTaskLabel);
//        p1.add(typeTaskField);
//        p1.add(targetTaskLabel);
//        p1.add(targetTaskField);
//        p1.add(due_dateTaskLabel);
//        p1.add(due_dateTaskField);
//        p2.add(specificsButton);
//        add(Box.createRigidArea(new Dimension(0, 10)));
//        add(p1, "Center");
//        add(p2, "Center");
//        add(Box.createRigidArea(new Dimension(0, 30)));
//
//        // Обработчик закрытия окна
//        addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent e) {
//                dispose();
//            }
//        });
//
//        setResizable(false);
//        setVisible(true);
//    }
//}
