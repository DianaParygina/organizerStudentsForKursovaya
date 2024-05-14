package org.example;

import db.DBConnector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;

public class MainWindow extends JFrame{

    AddItem item = new AddItem();
    List<Item> items;

    public MainWindow() {

        setTitle("Главное окно");
        setSize(380, 350);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setLocationRelativeTo(null);

        FlowLayout layout = new FlowLayout();
        JPanel p3 = new JPanel();
        p3.setLayout(layout);
        JPanel p1 = new JPanel();
        p1.setLayout(new GridLayout(3, 2));
        JPanel p2 = new JPanel();
        p2.setLayout(layout);
        JLabel one, two, three;
        JTextField itemNameField, numberOfHoursField;

        one = new JLabel("Название предмета:");
        two = new JLabel("Часов в семестр:");
        three = new JLabel("Добавим пердмет?");

        itemNameField = new JTextField(16);
        numberOfHoursField = new JTextField(16);
        Button addButton = new Button("Добавить");
        //addButton.setPreferredSize(new Dimension(150, 25)); // Задаем размер кнопки

        addButton.addActionListener(e -> {

            setTitle("Окно добавления предмета");
            String itemName = itemNameField.getText();
            String numberOfHours = numberOfHoursField.getText();

            setLocationRelativeTo(null);
            if (itemName.matches(".*\\d.*")) {
                JOptionPane.showMessageDialog(null, "Название предмета не может содержать цифры");
                itemNameField.setText("");
                return;
            }

            if (!numberOfHours.matches("\\d+")) {
                JOptionPane.showMessageDialog(null, "Количество штатов должно быть числом");
                numberOfHoursField.setText("");
                return;
            }

            item.AddItems(itemName, numberOfHours);

            itemNameField.setText("");
            numberOfHoursField.setText("");

            //dispose();

        });

        p3.add(three);
        p1.add(one);
        p1.add(itemNameField);
        p1.add(two);
        p1.add(numberOfHoursField);
        p2.add(addButton);
        add(p3, "Center");
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(p1, "Center");
        add(p2, "Center");
        add(Box.createRigidArea(new Dimension(0, 30)));

        // Обработчик закрытия окна
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        try {
            DBConnector.connection();
            items = DBConnector.getAllItem();
            DBConnector.createDB();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при получении данных из БД", "Ошибка", JOptionPane.ERROR_MESSAGE);
        } finally {
            DBConnector.closeConnection();
        }

        MyTableModelItems tableModelItems = new MyTableModelItems(items);
        JTable tableItems = new JTable(tableModelItems);

        tableItems.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = tableItems.getSelectedRow();
                    if (selectedRow != -1) {
                        Item selectedItem = items.get(selectedRow);
                        SecondWindow secondWindow = new SecondWindow(selectedItem);
                        secondWindow.setVisible(true);
                    }
                }
            }
        });

        addSectionWithLabelAndTable("Мои предметы", tableItems);

        setResizable(false);
        setVisible(true);
    }

    private void addSectionWithLabelAndTable(String labelTitle, JTable table) {
        JLabel label = new JLabel(labelTitle);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        getContentPane().add(label);
        add(Box.createRigidArea(new Dimension(0, 10)));

        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane);
        getContentPane().add(Box.createVerticalGlue());
    }
}
