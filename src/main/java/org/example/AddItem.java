package org.example;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static db.DBConnector.conn;

public class AddItem {

    public void AddItems(String name, String number_of_hours) {
        String SQL = "INSERT INTO item(name, number_of_hours) VALUES(?,?)";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setString(2, number_of_hours);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        // Пример использования сгенерированного id
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "Предмет успешно добавлен.");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
