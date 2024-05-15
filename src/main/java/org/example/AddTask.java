package org.example;

import db.DBConnector;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddTask {

    public void AddTasks(String type, String target, String due_date, boolean done, int itemId) {
        try {
            DBConnector.connection();
            DBConnector.createDB();
            String sql = "INSERT INTO tasks(type, target, due_date, done, item_id) VALUES(?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = DBConnector.conn.prepareStatement(sql)) {
                pstmt.setString(1, type);
                pstmt.setString(2, target);
                pstmt.setString(3, due_date);
                pstmt.setBoolean(4, done);
                pstmt.setInt(5, itemId);

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(null, "Задача добавлена");
                } else {
                    JOptionPane.showMessageDialog(null, "Ошибка добавления задачи");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка подключения к БД");
        } finally {
            DBConnector.closeConnection();
        }
    }
}