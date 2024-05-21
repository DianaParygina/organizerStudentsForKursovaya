package org.example;

import javax.swing.*;
import java.awt.*;

class PieChart extends JPanel {

    private int hours; // Количество часов для отображения

    public PieChart(int hours) {
        this.hours = hours;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();
        int diameter = Math.min(width, height);

        // Определяем центр круга
        int centerX = width / 2;
        int centerY = height / 2;

        // Рисуем кольцо
        g.setColor(Color.LIGHT_GRAY); // Цвет кольца
        g.drawOval(centerX - diameter / 2, centerY - diameter / 2, diameter, diameter);

        // Рисуем заполненную часть кольца
        g.setColor(Color.BLUE); // Цвет заполненной части
        g.fillArc(centerX - diameter / 2, centerY - diameter / 2, diameter, diameter, 0, (int) (360 * hours / 100)); // 100 - максимальное количество часов
    }
}