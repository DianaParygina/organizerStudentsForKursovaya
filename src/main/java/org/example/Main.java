package org.example;

import db.DBConnector;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        new LogWindow();

        if (DBConnector.connection() == null) {
            System.out.println("Не удалось установить соединение с БД.");
            return;
        }

        try {
            DBConnector.createDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
