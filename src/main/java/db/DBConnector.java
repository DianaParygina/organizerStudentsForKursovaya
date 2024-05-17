package db;

import org.example.Item;
import org.example.Task;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnector {
    public static final String PATH_TO_DB_FILE = "databasestates.db";
    public static final String URL = "jdbc:sqlite:" + PATH_TO_DB_FILE;
    public static Connection conn;

    public static Connection connection() {
        try {
            conn = DriverManager.getConnection(URL);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Драйвер: " + meta.getDriverName());
            }
        } catch (SQLException ex) {
            System.out.println("Ошибка подключения к БД: " + ex);
        }
        return conn;
    }


    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Closing connection error: " + e.getMessage());
            }
        }
    }

    public static void createDB() throws SQLException {
        Statement statmt = conn.createStatement();
        statmt.execute("CREATE TABLE if not exists 'Who' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'NameIndustry' TEXT);"); // Добавьте эту строку
    }
//
//    public static List<Item> getAllItem() throws SQLException {
//        Statement statement = conn.createStatement();
//        List<Item> list = new ArrayList<Item>();
//        ResultSet resultSet = statement.executeQuery("SELECT id, name, number_of_hours FROM item");
//        while (resultSet.next()) {
//            list.add(new Item(resultSet.getInt("id"),resultSet.getString("name"), resultSet.getString("number_of_hours")));
//        }
//        resultSet.close();
//        statement.close();
//        return list;
//    }
//
//    public static List<Task> getTasksForItem(int itemId) throws SQLException {
//        List<Task> tasks = new ArrayList<>();
//        String sql = "SELECT * FROM tasks WHERE item_id = ?";
//        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, itemId);
//            ResultSet rs = pstmt.executeQuery();
//            while (rs.next()) {
//                int id = rs.getInt("id");
//                String type = rs.getString("type");
//                String target = rs.getString("target");
//                String due_date = rs.getString("due_date");
//                boolean done = rs.getBoolean("done");
//                Task task = new Task(id, type, target, due_date, done);
//                tasks.add(task);
//            }
//        }
//        return tasks;
//    }

//    public static String searchStateByIdRepublic(int id, String type) {
//        String sqlrepublic = "SELECT name, satisfaction_of_citizens, parliament FROM republic WHERE id = ?";
//        try (PreparedStatement pstmt = connection().prepareStatement(sqlrepublic)) {
//            pstmt.setInt(1, id);
//            try (ResultSet rs = pstmt.executeQuery()) {
//                if (rs.next()) {
//                    String name = rs.getString("name");
//                    String satisfactionOfCitizens = rs.getString("satisfaction_of_citizens");
//                    String parliament = rs.getString("parliament");
//                    closeConnection();
//                    return String.format("Имя: %s.\nУдовлетворение граждан: %s.\nПарламент: %s.\n",
//                            name, satisfactionOfCitizens, parliament);
//                } else {
//                    closeConnection();
//                    return "Государство с таким ID не найдено.";
//                }
//            }
//        } catch (SQLException e) {
//            closeConnection();
//            e.printStackTrace();
//            return "Ошибка при выполнении запроса к базе данных.";
//        }
//    }
//
//
//    public static String deleteRepublic(int id, String type) throws SQLException {
//        String result = null;
//        String sqlRepublic = "DELETE FROM republic WHERE id = ?";
//        try (PreparedStatement pstmt = conn.prepareStatement(sqlRepublic)) {
//            pstmt.setInt(1, id);
//            int affectedRows = pstmt.executeUpdate();
//            if (affectedRows > 0) {
//                closeConnection();
//                return "Удалено!";
//            } else {
//                closeConnection();
//                return "Государство с таким ID не найдено.";
//            }
//        }  catch (SQLException e) {
//            closeConnection();
//            result = "Error deleting republic: " + e.getMessage();
//            e.printStackTrace();
//        }
//        return result;
//    }
}