package org.dbms.database.component;

import java.util.ArrayList;
import java.util.List;

public class Table {
    public String name;
    public List<Row> rows = new ArrayList<>();
    public List<Column> columns = new ArrayList<>();

    public Table(String name){
        this.name = name;
    }

    public void addRow(Row row) {
        rows.add(row);
    }

    public void deleteRow(int rowIndex) {
        rows.remove(rowIndex);
    }

    public void deleteColumn(int columnIndex) {
        columns.remove(columnIndex);
        for (Row row: rows) {
            row.values.remove(columnIndex);
        }
    }

    public void addColumn(Column column) {
        columns.add(column);
    }

    public void setName(String name) {
        this.name = name;
    }
}
