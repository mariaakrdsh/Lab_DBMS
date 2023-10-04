package org.dbms.database.file;

import org.dbms.database.ui.DBMS;
import org.dbms.database.DatabaseManager;
import org.dbms.database.component.Row;
import org.dbms.database.component.column.ColumnType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DatabaseImporter {

    public static void importDatabase(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String dbName = reader.readLine();
            DatabaseManager.getInstance().createDB(dbName);

            int numTables = Integer.parseInt(reader.readLine());

            for (int i = 0; i < numTables; i++) {
                String tableName = reader.readLine();
                DatabaseManager.getInstance().addTable(tableName);
                importTable(reader, i);
            }
            DBMS.getInstance().renderCells();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void importTable(BufferedReader reader, int tableIndex) throws IOException {

        int numColumns = Integer.parseInt(reader.readLine());
        int numRows = Integer.parseInt(reader.readLine());

        for (int i = 0; i < numColumns; i++) {
            String columnName = reader.readLine();
            String columnType = reader.readLine();
            ColumnType columnTypeEnum = ColumnType.valueOf(columnType);
            DatabaseManager.getInstance().addColumn(tableIndex,columnName,columnTypeEnum);
        }

        for (int i = 0; i < numRows; i++) {
            Row row = new Row();
            for (int j = 0; j < numColumns; j++){
                String data = reader.readLine();
                row.values.add(data);
            }
            DatabaseManager.getInstance().addRow(tableIndex,row);
        }
    }
}