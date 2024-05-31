package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class TypeWorks extends JFrame {
    private final JTable typeWorksTable;
    private final DefaultTableModel typeWorksTableModel;
    private int selectedTypeWorks = -1;
    private JLabel totalTimeLabel;
    private JProgressBar progressBar;
    private JLabel progressLabel;

    public TypeWorks(int ItemsId, String itemName, int itemsHours) {
        Font headerFont = new Font("Arial", Font.BOLD, 16);
        Font tableFont = new Font("Arial", Font.PLAIN, 14);

        setTitle("Типы работ для " + itemName);

        typeWorksTableModel = new DefaultTableModel(new Object[]{"ID", "Тип работы"}, 0);
        typeWorksTable = new JTable(typeWorksTableModel);
        typeWorksTable.setFont(tableFont);
        typeWorksTable.setRowHeight(25);

        JLabel headerLabel = new JLabel(itemName + " (" + itemsHours + " часов)", SwingConstants.CENTER);
        headerLabel.setFont(headerFont);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        totalTimeLabel = new JLabel("Вы отзанимались: 00:00:00", SwingConstants.CENTER);
        totalTimeLabel.setFont(tableFont);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(91, 232, 93));
        progressBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        progressLabel = new JLabel("0%");
        progressLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel timeProgressPanel = new JPanel(new GridLayout(2, 1));
        timeProgressPanel.add(totalTimeLabel);
        timeProgressPanel.add(progressBar);

        JPanel headerTimeProgressPanel = new JPanel(new BorderLayout());
        headerTimeProgressPanel.add(headerLabel, BorderLayout.NORTH);
        headerTimeProgressPanel.add(timeProgressPanel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(typeWorksTable);

        setLayout(new BorderLayout());
        add(headerTimeProgressPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 280);
        setLocationRelativeTo(null);
        setVisible(true);

        Methods.loadTypeWorksFromDatabase(typeWorksTableModel, ItemsId);
        typeWorksTable.setCellSelectionEnabled(false);
        typeWorksTable.setDefaultEditor(Object.class, null);

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

        updateTotalTime(ItemsId);
        updateTotalProgress(ItemsId, itemsHours);
    }

    // Метод для обновления общего времени и прогресс бара
    public void updateTotalProgress(int ItemsId, int itemsHours) {
        long totalTime = Methods.calculateTotalTime(ItemsId);

        int progressPercent = (int) ((double) totalTime / (itemsHours * 60 * 60 * 1000) * 100);
        progressBar.setValue(progressPercent);
    }

    // Метод для обновления общего времени
    public void updateTotalTime(int ItemsId) {
        long totalTime = Methods.calculateTotalTime(ItemsId);
        totalTimeLabel.setText("Вы отзанимались: " + Methods.formatTime(totalTime));
    }
}