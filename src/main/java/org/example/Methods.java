package org.example;

import db.DBConnector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Methods {

    // Метод для проверки логина и пароля
    public static boolean authenticateUser(String username, String password) throws SQLException {
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM вход WHERE username = ? AND password = ?")) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }

    // Метод для получения idNeed из базы данных по имени пользователя
    public static int getIdNeed(String username) throws SQLException {
        int idNeed = -1;
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT idNeed FROM вход WHERE username = ?")) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                idNeed = resultSet.getInt("idNeed");
            }
        }
        return idNeed;
    }

    // Метод для загрузки данных курса из базы данных
    public static void loadCourseFromDatabase(DefaultTableModel courseTableModel, int courseId) {
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, nameCourse FROM specialtyCourse WHERE idNameProgram = ?")) {
            preparedStatement.setInt(1, courseId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nameCourse = resultSet.getString("nameCourse");
                courseTableModel.addRow(new Object[]{id, nameCourse});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при получении данных из базы данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Метод для сохранения id курса в базу данных
    public static void saveCourseIdToDatabase(int courseId, String currentUsername) throws SQLException {
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE вход SET idNeed = ? WHERE username = ?")) {
            preparedStatement.setInt(1, courseId);
            preparedStatement.setString(2, currentUsername);
            preparedStatement.executeUpdate();
        }
    }

    // Метод для загрузки данных предмета из базы данных
    public static void loadItemsFromDatabase(DefaultTableModel itemsTableModel, int itemsId) {
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Предметы WHERE idType = ?")) {
            preparedStatement.setInt(1, itemsId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int numberOfHours = resultSet.getInt("number_of_hours");
                itemsTableModel.addRow(new Object[]{id, name, numberOfHours});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при получении данных из базы данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Метод для загрузки данных отрасли из базы данных
    public static void loadIndustriesFromDatabase(DefaultTableModel industryTableModel) {
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, NameIndustry FROM who")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nameIndustry = resultSet.getString("NameIndustry");
                industryTableModel.addRow(new Object[]{id, nameIndustry});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при получении данных из базы данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Метод для загрузки данных специальности из базы данных
    public static void loadSpecialtiesFromDatabase(DefaultTableModel specialtyTableModel, int industryId) {
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, nameProgram FROM EducationalProgram WHERE idNameIndustry = ?")) {
            preparedStatement.setInt(1, industryId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nameProgram = resultSet.getString("nameProgram");
                specialtyTableModel.addRow(new Object[]{id, nameProgram});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при получении данных из базы данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Метод для загрузки данных задачи из базы данных
    public static void loadTasksFromDatabase(DefaultTableModel tasksTableModel, int TypeWorksId, int ItemsId) {
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Работы WHERE idItem = ? AND idType = ?")) {

            preparedStatement.setInt(1, ItemsId);
            preparedStatement.setInt(2, TypeWorksId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String target = resultSet.getString("target");
                int hours = resultSet.getInt("hours");
                boolean done = resultSet.getInt("done") == 1;
                int elapsedTimeSeconds = resultSet.getInt("elapsedTime");
                String dueDate = resultSet.getString("dueDate");

                String elapsedTimeFormatted = formatSecondsToHHMMSS(elapsedTimeSeconds);

                tasksTableModel.addRow(new Object[]{id, target, hours, dueDate, elapsedTimeFormatted, done});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при получении данных из базы данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Метод для загрузки данных типа работы из базы данных
    public static void loadTypeWorksFromDatabase(DefaultTableModel typeWorksTableModel, int ItemsId) {
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Тип")) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nameType = resultSet.getString("nameType");
                typeWorksTableModel.addRow(new Object[]{id, nameType});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при получении данных из базы данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Метод для форматирования времени
    public static String formatTime(long timeInMillis) {
        long seconds = timeInMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        seconds %= 60;
        minutes %= 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    // Метод для форматирования секунд в HH:mm:ss
    public static String formatSecondsToHHMMSS(long timeInMillis) {
        long seconds = timeInMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        seconds %= 60;
        minutes %= 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    // Метод для расчета общего времени
    public static long calculateTotalTime(int ItemsId) {
        long totalTime = 0;
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT elapsedTime FROM Работы WHERE idItem = ?")) {

            preparedStatement.setInt(1, ItemsId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                totalTime += resultSet.getLong("elapsedTime");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при получении данных из базы данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
        return totalTime;
    }

    // Метод для сохранения прошедшего времени в базу данных
    public static void saveElapsedTimeToDatabase(int taskId, long elapsedTime) throws SQLException {
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Работы SET elapsedTime = ? WHERE id = ?")) {
            preparedStatement.setLong(1, elapsedTime);
            preparedStatement.setInt(2, taskId);
            preparedStatement.executeUpdate();
        }
    }

    // Метод для загрузки прошедшего времени из базы данных
    public static long loadElapsedTimeFromDatabase(int taskId) throws SQLException {
        long elapsedTime = 0;
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT elapsedTime FROM Работы WHERE id = ?")) {
            preparedStatement.setInt(1, taskId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                elapsedTime = resultSet.getLong("elapsedTime");
            }
        }
        return elapsedTime;
    }

    // Метод для удаления задачи
    public static void deleteTask(int taskId) {
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Работы WHERE id = ?")) {
            preparedStatement.setInt(1, taskId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Метод для загрузки данных задачи для редактирования
    public static void loadTaskData(JTextField tasksNameField, JTextField qtyHoursField, JTextField dueDateField, int taskId) {
        try (Connection connection = DBConnector.connection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT target, hours, dueDate FROM Работы WHERE id = ?")) {

            preparedStatement.setInt(1, taskId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                tasksNameField.setText(resultSet.getString("target"));
                qtyHoursField.setText(String.valueOf(resultSet.getInt("hours")));
                dueDateField.setText(resultSet.getString("dueDate"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при загрузке данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Метод для обновления задачи
    public static void updateTask(int taskId, String newTarget, String newHours, String newDueDate, Tasks tasks) {
        boolean validInput = true;

        try {
            // Проверка, что количество часов является числом
            Integer.parseInt(newHours);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Количество часов должно быть числом", "Ошибка", JOptionPane.ERROR_MESSAGE);
            validInput = false;
        }

        if (!newDueDate.isEmpty()) {
            try {
                // Проверка, что срок сдачи является корректной датой
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateFormat.setLenient(false);
                dateFormat.parse(newDueDate);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, "Срок сдачи должен быть в формате yyyy-MM-dd.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                validInput = false;
            }
        }

        if (validInput) {
            try (Connection connection = DBConnector.connection();
                 PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Работы SET target = ?, hours = ?, dueDate = ? WHERE id = ?")) {
                preparedStatement.setString(1, newTarget);
                preparedStatement.setString(2, newHours);
                preparedStatement.setString(3, newDueDate.isEmpty() ? null : newDueDate);
                preparedStatement.setInt(4, taskId);
                preparedStatement.executeUpdate();
                tasks.refreshTable();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Ошибка при сохранении изменений.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Функция для конвертации HH:mm:ss в секунды
    public static long hhmmssToSeconds(String hhmmss) {
        String[] parts = hhmmss.split(":");
        long hours = Long.parseLong(parts[0]);
        long minutes = Long.parseLong(parts[1]);
        long seconds = Long.parseLong(parts[2]);
        return hours * 3600 + minutes * 60 + seconds;
    }

}