package db;

//import org.example.Item;
//import org.example.Task;
//import org.example.Item;

import java.sql.*;

public class DBConnector {
    public static final String PATH_TO_DB_FILE = "databasestates.db";
    public static final String URL = "jdbc:sqlite:" + PATH_TO_DB_FILE;
    public static Connection conn;

    public static Connection connection() {
        try {
            conn = DriverManager.getConnection(URL);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
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
        statmt.execute("CREATE TABLE if not exists 'Предметы' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' TEXT, 'number_of_hours' integer, 'idType' INTEGER, FOREIGN KEY (idType) REFERENCES specialtyCourse (id));");
        statmt.execute("CREATE TABLE if not exists 'Работы' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'target' TEXT, 'hours' TEXT, 'done' boolean, 'elapsedTime' INTEGER, 'dueDate' TEXT, 'idItem' INTEGER, 'idType' INTEGER, FOREIGN KEY (idItem) REFERENCES Предметы (id), FOREIGN KEY (idType) REFERENCES Тип (id));");
        statmt.execute("CREATE TABLE if not exists 'Who' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'NameIndustry' TEXT);");
        statmt.execute("CREATE TABLE if not exists 'Тип' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'nameType' text);");
        statmt.execute("CREATE TABLE if not exists 'specialtyCourse' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'type' text, 'nameCourse' text, 'idNameProgram' INTEGER, FOREIGN KEY (idNameProgram) REFERENCES EducationalProgram (id));");
        statmt.execute("CREATE TABLE if not exists 'EducationalProgram' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'NameProgram' TEXT, 'idNameIndustry' integer, FOREIGN KEY (idNameIndustry) REFERENCES Who (id));");
        statmt.execute("CREATE TABLE if not exists 'Вход' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'username' TEXT, 'password' TEXT, 'idNeed' INTEGER, FOREIGN KEY (idNeed) REFERENCES specialtyCourse (id));");
        closeConnection();
    }

    // Метод для сохранения прошедшего времени в базу данных
    public static void saveElapsedTimeToDatabase(int taskId, long elapsedTime) throws SQLException {
        try (Connection connection = connection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Работы SET elapsedTime = ? WHERE id = ?")) {
            preparedStatement.setLong(1, elapsedTime);
            preparedStatement.setInt(2, taskId);
            preparedStatement.executeUpdate();
        }
    }

    // Метод для загрузки прошедшего времени из базы данных
    public static long loadElapsedTimeFromDatabase(int taskId) throws SQLException {
        long elapsedTime = 0;
        try (Connection connection = connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT elapsedTime FROM Работы WHERE id = ?")) {
            preparedStatement.setInt(1, taskId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                elapsedTime = resultSet.getLong("elapsedTime");
            }
        }
        return elapsedTime;
    }
}
