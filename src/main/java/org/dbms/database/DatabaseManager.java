package org.dbms.database;

import org.dbms.database.file.DatabaseExporter;
import org.dbms.database.file.DatabaseImporter;
import org.dbms.database.ui.DBMS;
import org.dbms.database.ui.CustomTable;
import org.dbms.database.ui.CustomTableModel;
import org.dbms.database.component.column.*;
import org.dbms.database.component.Column;
import org.dbms.database.component.Database;
import org.dbms.database.component.Row;
import org.dbms.database.component.Table;

import javax.swing.*;

public class DatabaseManager {
    private static DatabaseManager instance;
    public static DBMS instanceCSW;

    private DatabaseManager(){
    }

    public static DatabaseManager getInstance(){
        if (instance == null){
            instance = new DatabaseManager();
            instanceCSW = DBMS.getInstance();
        }
        return instance;
    }

    public static Database database;

    public void openDB(String path){
        DatabaseImporter.importDatabase(path);
    }

    public void renameDB(String name){
        if (name != null && !name.isEmpty()) {
            database.setName(name);
            instanceCSW.databaseLabel.setText(database.name);
        }
    }

    public void saveDB(String path) {
        DatabaseExporter.exportDatabase(database, path);
    }

    public void deleteDB() {
        database = null;
        while (instanceCSW.tabbedPane.getTabCount() > 0) {
            instanceCSW.tabbedPane.removeTabAt(0);
        }
    }

    public void createDB(String name) {
        database = new Database(name);
        instanceCSW.databaseLabel.setText(database.name);
    }

    public boolean existDB(){
        return database != null;
    }

    public void addTable(String name){
        if (name != null && !name.isEmpty()) {
            JPanel tablePanel = instanceCSW.createTablePanel();

            DBMS.getInstance().tabbedPane.addTab(name, tablePanel);
            Table table = new Table(name);
            database.addTable(table);
        }
    }

    public void renameTable(int tableIndex, String name){
        if (name != null && !name.isEmpty() && tableIndex != -1) {
            instanceCSW.tabbedPane.setTitleAt(tableIndex,name);
            database.tables.get(tableIndex).setName(name);
        }
    }

    public void deleteTable(int tableIndex){

        if (tableIndex != -1) {
            instanceCSW.tabbedPane.removeTabAt(tableIndex);

            database.deleteTable(tableIndex);
        }
    }

    public void addColumn(int tableIndex, String columnName, ColumnType columnType){
        if (columnName != null && !columnName.isEmpty()) {
            if (tableIndex != -1) {
                JPanel tablePanel = (JPanel) instanceCSW.tabbedPane.getComponentAt(tableIndex);
                JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
                JTable table = (JTable) scrollPane.getViewport().getView();
                CustomTableModel tableModel = (CustomTableModel) table.getModel();

                tableModel.addColumn(columnName + " (" + columnType.name() + ")");

                switch (columnType) {
                    case INT -> {
                        Column columnInt = new IntegerColumn(columnName);
                        database.tables.get(tableIndex).addColumn(columnInt);
                    }
                    case REAL -> {
                        Column columnReal = new RealColumn(columnName);
                        database.tables.get(tableIndex).addColumn(columnReal);
                    }
                    case STRING -> {
                        Column columnStr = new StringColumn(columnName);
                        database.tables.get(tableIndex).addColumn(columnStr);
                    }
                    case CHAR -> {
                        Column columnChar = new CharColumn(columnName);
                        database.tables.get(tableIndex).addColumn(columnChar);
                    }
                    case MONEY -> {
                        Column columnMoney = new MoneyColumn(columnName);
                        database.tables.get(tableIndex).addColumn(columnMoney);
                    }
                    case MONEY_INVL -> {
                        Column columnMoneyInvl = new MoneyInvlColumn(columnName);
                        database.tables.get(tableIndex).addColumn(columnMoneyInvl);
                    }
                }
                for (Row row: database.tables.get(tableIndex).rows) {
                    row.values.add("");
                }

            }
        }
    }

    public void renameColumn(int tableIndex, int columnIndex, String columnName, JTable table){
        if (columnName != null && !columnName.isEmpty()) {
            if (tableIndex != -1 && columnIndex != -1) {
                table.getColumnModel().getColumn(columnIndex).setHeaderValue(columnName + " (" + database.tables.get(tableIndex).columns.get(columnIndex).type + ")");
                table.getTableHeader().repaint();

                database.tables.get(tableIndex).columns.get(columnIndex).setName(columnName);
            }
        }
    }

    public void changeColumnType(int tableIndex, int columnIndex, ColumnType columnType, JTable table){
        if (tableIndex != -1 && columnIndex != -1) {
            table.getColumnModel().getColumn(columnIndex).setHeaderValue(database.tables.get(tableIndex).columns.get(columnIndex).name + " (" + columnType.name() + ")");
            table.getTableHeader().repaint();

            switch (columnType) {
                case INT -> {
                    Column columnInt = new IntegerColumn(database.tables.get(tableIndex).columns.get(columnIndex).name);
                    database.tables.get(tableIndex).columns.set(columnIndex, columnInt);
                }
                case REAL -> {
                    Column columnReal = new RealColumn(database.tables.get(tableIndex).columns.get(columnIndex).name);
                    database.tables.get(tableIndex).columns.set(columnIndex, columnReal);
                }
                case STRING -> {
                    Column columnStr = new StringColumn(database.tables.get(tableIndex).columns.get(columnIndex).name);
                    database.tables.get(tableIndex).columns.set(columnIndex, columnStr);
                }
                case CHAR -> {
                    Column columnChar = new CharColumn(database.tables.get(tableIndex).columns.get(columnIndex).name);
                    database.tables.get(tableIndex).columns.set(columnIndex, columnChar);
                }
                case MONEY -> {
                    Column columnMoney = new MoneyColumn(database.tables.get(tableIndex).columns.get(columnIndex).name);
                    database.tables.get(tableIndex).columns.set(columnIndex, columnMoney);
                }
                case MONEY_INVL -> {
                    Column columnMoneyInvl = new MoneyInvlColumn(database.tables.get(tableIndex).columns.get(columnIndex).name);
                    database.tables.get(tableIndex).columns.set(columnIndex, columnMoneyInvl);
                }
            }
            for (Row row: database.tables.get(tableIndex).rows) {
                row.values.set(columnIndex,"");
            }
            for (int i = 0; i < database.tables.get(tableIndex).rows.size(); i++) {
                table.setValueAt("", i, columnIndex);
            }
        }
    }

    public void deleteColumn(int tableIndex, int columnIndex, CustomTableModel tableModel){

        if (columnIndex != -1) {
            tableModel.removeColumn(columnIndex);
            database.tables.get(tableIndex).deleteColumn(columnIndex);
        }
    }

    public void addRow(int tableIndex, Row row){

        if (tableIndex != -1) {
            JPanel tablePanel = (JPanel) instanceCSW.tabbedPane.getComponentAt(tableIndex);
            JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
            JTable table = (JTable) scrollPane.getViewport().getView();
            CustomTableModel tableModel = (CustomTableModel) table.getModel();
            tableModel.addRow(new Object[tableModel.getColumnCount()]);

            database.tables.get(tableIndex).addRow(row);
            for (int i = 0; i < database.tables.get(tableIndex).columns.size(); i++){
                row.values.add("");
            }
        }
    }

    public void deleteRow(int tableIndex, int rowIndex, CustomTableModel tableModel){

        if (rowIndex != -1) {
            tableModel.removeRow(rowIndex);

            database.tables.get(tableIndex).deleteRow(rowIndex);
        }
    }

    public void updateCellValue(String value, int tableIndex, int columnIndex, int rowIndex, CustomTable table){
        if (database.tables.get(tableIndex).columns.get(columnIndex).validate(value)){
            database.tables.get(tableIndex).rows.get(rowIndex).setAt(columnIndex,value.trim());
        }
        else {
            String data = database.tables.get(tableIndex).rows.get(rowIndex).getAt(columnIndex);
            if (data != null){
                table.setValueAt(data, rowIndex, columnIndex);
            }
            else {
                table.setValueAt("", rowIndex, columnIndex);
            }

            JFrame frame = new JFrame("Помилка!!!");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JOptionPane.showMessageDialog(
                    frame,
                    "Введено некоректне значення",
                    "Помилка!!!",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    public void deleteDuplicateRows(int tableIndex, CustomTableModel tableModel) {
        int i = 0;
        boolean flag = true;
        while(i<database.tables.get(tableIndex).rows.size()){
            flag = true;
            for (int j = i+1; j < database.tables.get(tableIndex).rows.size(); j++) {
                if (database.tables.get(tableIndex).rows.get(i).values.equals(database.tables.get(tableIndex).rows.get(j).values)) {
                    deleteRow(tableIndex, i, tableModel);
                    flag = false;
                    break;
                }
            }
            if (flag){
                i++;
            }
        }
    }
}
