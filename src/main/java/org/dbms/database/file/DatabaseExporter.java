package org.dbms.database.file;

import org.dbms.database.component.Column;
import org.dbms.database.component.Database;
import org.dbms.database.component.Row;
import org.dbms.database.component.Table;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DatabaseExporter {

    public static void exportDatabase(Database database, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(database.name);
            writer.newLine();
            writer.write(String.valueOf(database.tables.size()));
            writer.newLine();
            for (Table table : database.tables) {
                exportTable(writer, table);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void exportTable(BufferedWriter writer, Table table) throws IOException {
        writer.write(table.name);
        writer.newLine();
        writer.write(String.valueOf(table.columns.size()));
        writer.newLine();
        writer.write(String.valueOf(table.rows.size()));
        writer.newLine();

        for (Column column : table.columns) {
            writer.write(column.name + "\n" + column.type);
            writer.newLine();
        }

        for (Row row : table.rows) {
            for (int i = 0; i < table.columns.size(); i++) {
                writer.write(row.getAt(i));
                writer.newLine();
            }
        }
    }
}