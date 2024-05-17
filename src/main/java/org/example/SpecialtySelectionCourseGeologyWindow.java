package org.example;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SpecialtySelectionCourseGeologyWindow extends JFrame {
    private final JTable courseTable;
    private final DefaultTableModel courseTableModel;

    public SpecialtySelectionCourseGeologyWindow(String title) {
        courseTableModel = new DefaultTableModel(new Object[]{"ID", "nameCourse"}, 0);
        courseTable = new JTable(courseTableModel);
        JScrollPane scrollPane = new JScrollPane(courseTable);
        add(scrollPane);
        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);

        // Загрузите данные о курсах из базы данных
        loadCoursesFromDatabase(); // Добавьте метод для загрузки данных

//        courseTable.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if (e.getClickCount() == 2) {
//                    int selectedRow = courseTable.getSelectedRow();
//                    if (selectedRow != -1) {
//                        int courseId = (int) courseTableModel.getValueAt(selectedRow, 0);
//
//                        // Открывайте окно с информацией о выбранном курсе
//                        new CourseInfoWindow(courseId).setVisible(true);
//                    }
//                }
//            }
//        });
    }

    private void loadCoursesFromDatabase() {
        // Загрузите данные о курсах (специалитет, бакалавриат) из базы данных
        // Например:
        // try (Connection connection = DBConnector.connection();
        //      PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, nameCourse FROM Courses")) {
        //
        //     ResultSet resultSet = preparedStatement.executeQuery();
        //     while (resultSet.next()) {
        //         int id = resultSet.getInt("id");
        //         String nameCourse = resultSet.getString("nameCourse");
        //         courseTableModel.addRow(new Object[]{id, nameCourse});
        //     }
        // } catch (SQLException e) {
        //     e.printStackTrace();
        //     JOptionPane.showMessageDialog(this, "Ошибка при получении данных из базы данных.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        // }
    }
}