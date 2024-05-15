package org.example;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class MyTableModelSpecificsTask extends AbstractTableModel {

    private List<Task> data;

    public MyTableModelSpecificsTask(List<Task> tasks){
        this.data = tasks;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Task tasks = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> tasks.getId();
            case 1 -> tasks.getType();
            case 2 -> tasks.getTarget();
            case 3 -> tasks.getDueDate();
            case 4 -> tasks.getDone();
            default -> "Unknown";
        };
    }

    @Override
    public String getColumnName ( int column){
        return switch (column) {
            case 0 -> "id";
            case 1 -> "Тип";
            case 2 -> "Задание";
            case 3 -> "Срок сдачи";
            case 4 -> "Готовность";
            default -> "";
        };
    }

    public void setTasks(List<Task> tasks) {
        this.data = tasks;
        fireTableDataChanged(); // Сообщает таблице о изменении данных
    }

}
