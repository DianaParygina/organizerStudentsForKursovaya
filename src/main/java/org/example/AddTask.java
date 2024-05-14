package org.example;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;

import static db.DBConnector.conn;

public class AddTask {

    public void AddTasks(String type, String target, LocalDate due_date, boolean done) {
        String SQL = "INSERT INTO tasks(type, target, due_date, done) VALUES(?,?,?,?)";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, type);
            pstmt.setString(2, target);
            pstmt.setDate(1, Date.valueOf(due_date));
            pstmt.setBoolean(2, done);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        // Пример использования сгенерированного id
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "Работа успешно добавлена.");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
