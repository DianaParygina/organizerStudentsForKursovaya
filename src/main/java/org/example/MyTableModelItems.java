//package org.example;
//
//import javax.swing.table.AbstractTableModel;
//import java.util.List;
//
//public class MyTableModelItems extends AbstractTableModel {
//
//    private List<Item> data;
//
//    public MyTableModelItems(List<Item> items){
//        this.data = items;
//    }
//
//    @Override
//    public int getRowCount() {
//        return data.size();
//    }
//
//    @Override
//    public int getColumnCount() {
//        return 3;
//    }
//
//    @Override
//    public Object getValueAt(int rowIndex, int columnIndex) {
//        Item items = data.get(rowIndex);
//        return switch (columnIndex) {
//            case 0 -> items.getId();
//            case 1 -> items.getName();
//            case 2 -> items.getNumberOfHours();
//            default -> "Unknown";
//        };
//    }
//
//    @Override
//    public String getColumnName ( int column){
//        return switch (column) {
//            case 0 -> "id";
//            case 1 -> "Название";
//            case 2 -> "Часов в семестр";
//            default -> "";
//        };
//    }
//
//}
