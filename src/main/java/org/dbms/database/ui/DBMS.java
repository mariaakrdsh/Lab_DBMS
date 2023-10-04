package org.dbms.database.ui;

import org.dbms.database.DatabaseManager;
import org.dbms.database.component.column.ColumnType;
import org.dbms.database.component.Row;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DBMS {

    private static DBMS instance;
    private static String databaseName = "Відкрийте або створіть базу данних";
    JFrame frame;
    public JTabbedPane tabbedPane;
    public JMenuBar menuBar;

    public JMenuItem renameDatabaseMenuItem;
    public JMenuItem deleteTableMenuItem;
    public JMenuItem addRowMenuItem;
    public JMenuItem addColumnMenuItem;
    public JMenuItem deleteRowMenuItem;
    public JMenuItem deleteColumnMenuItem;
    public JMenuItem createTableMenuItem;
    public JMenuItem renameTableMenuItem;
    public JMenuItem renameColumnMenuItem;
    public JMenuItem changeColumnTypeMenuItem;
    public JMenuItem loadDB;
    public JMenuItem deleteRowDuplicates;
    public JMenuItem createDB;
    public JMenuItem deleteDB;
    public JMenuItem saveDB;

    public JMenu tableMenu = new JMenu("Таблиця");
    public JMenu columnMenu = new JMenu("Колонка");
    public JMenu rowMenu = new JMenu("Рядок");

    public JLabel databaseLabel;


    public static DBMS getInstance(){
        if (instance == null){
            instance = new DBMS();

            instance.frame = new JFrame("DBMS");
            instance.menuBar = new JMenuBar();

            instance.tabbedPane = new JTabbedPane();

            instance.renameDatabaseMenuItem = new JMenuItem("Перейменувати");
            instance.deleteTableMenuItem = new JMenuItem("Видалити");
            instance.addRowMenuItem = new JMenuItem("Додати");
            instance.addColumnMenuItem = new JMenuItem("Додати");
            instance.deleteRowMenuItem = new JMenuItem("Видалити");
            instance.deleteColumnMenuItem = new JMenuItem("Видалити");
            instance.createTableMenuItem = new JMenuItem("Створити");
            instance.renameTableMenuItem = new JMenuItem("Перейменувати");
            instance.renameColumnMenuItem = new JMenuItem("Перейменувати");
            instance.changeColumnTypeMenuItem = new JMenuItem("Змінити тип");
            instance.loadDB = new JMenuItem("Відкрити");
            instance.createDB = new JMenuItem("Створити");
            instance.deleteDB = new JMenuItem("Видалити");
            instance.deleteRowDuplicates = new JMenuItem("Видалити дублікати");
            instance.saveDB = new JMenuItem("Зберегти");
        }
        return instance;
    }

    public static void main(String[] args) {
        DBMS instance = DBMS.getInstance();
        instance.frame.setSize(800, 600);
        instance.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        instance.setMenusVisibility(false);

        JMenu dbMenu = new JMenu("БД");
        dbMenu.add(instance.createDB);
        dbMenu.add(instance.deleteDB);
        dbMenu.add(instance.saveDB);
        dbMenu.add(instance.loadDB);
        dbMenu.add(instance.renameDatabaseMenuItem);

        instance.tableMenu.add(instance.createTableMenuItem);
        instance.tableMenu.add(instance.deleteTableMenuItem);
        instance.tableMenu.add(instance.renameTableMenuItem);


        instance.columnMenu.add(instance.addColumnMenuItem);
        instance.columnMenu.add(instance.deleteColumnMenuItem);
        instance.columnMenu.add(instance.renameColumnMenuItem);
        instance.columnMenu.add(instance.changeColumnTypeMenuItem);

        instance.rowMenu.add(instance.addRowMenuItem);
        instance.rowMenu.add(instance.deleteRowMenuItem);
        instance.rowMenu.add(instance.deleteRowDuplicates);

        instance.menuBar.add(dbMenu);
        instance.menuBar.add(instance.tableMenu);
        instance.menuBar.add(instance.columnMenu);
        instance.menuBar.add(instance.rowMenu);

        DBMS.instance.frame.setJMenuBar(DBMS.instance.menuBar);

        instance.frame.getContentPane().add(DBMS.getInstance().tabbedPane, BorderLayout.CENTER);

        instance.databaseLabel = new JLabel(databaseName, SwingConstants.CENTER);
        instance.frame.getContentPane().add(instance.databaseLabel, BorderLayout.NORTH);

        instance.frame.setLocationRelativeTo(null);

        instance.frame.setVisible(true);

        instance.loadDB.addActionListener(e -> {
            String strPath = JOptionPane.showInputDialog(instance.frame, "Введіть шлях:");
            if (strPath != null) {
                Path path = Paths.get(strPath);
                if (Files.exists(path)){
                    DatabaseManager.getInstance().openDB(strPath);
                    instance.setMenusVisibility(true);
                } else {
                    JOptionPane.showMessageDialog(DBMS.instance.frame, "Ви викликали Фічу 1");
                }
            }
        });

        instance.renameDatabaseMenuItem.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog(instance.frame, "Введіть нову назву бази даних:");
            DatabaseManager.getInstance().renameDB(newName);
        });

        instance.createTableMenuItem.addActionListener(e -> {
            String tableName = JOptionPane.showInputDialog(instance.frame, "Введіть назву нової таблиці:");
            DatabaseManager.getInstance().addTable(tableName);
        });

        instance.deleteTableMenuItem.addActionListener(e -> {
            int selectedIndex = instance.tabbedPane.getSelectedIndex();
            DatabaseManager.getInstance().deleteTable(selectedIndex);
        });

        instance.addColumnMenuItem.addActionListener(e -> {
            int selectedTab = instance.tabbedPane.getSelectedIndex();
            if (selectedTab != -1) {
                String newColumnName = JOptionPane.showInputDialog(instance.frame, "Введіть назву нової колонки:");

                if (newColumnName != null && !newColumnName.isEmpty()) {
                    ColumnType selectedDataType = (ColumnType) JOptionPane.showInputDialog(
                            instance.frame,
                            "Оберіть тип нової колонки:",
                            "Додати Колонку",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            ColumnType.values(),
                            ColumnType.INT
                    );

                    if (selectedDataType != null) {
                        DatabaseManager.getInstance().addColumn(selectedTab, newColumnName, selectedDataType);
                    }
                }
            }
        });

        instance.deleteColumnMenuItem.addActionListener(e -> {
            int selectedTab = instance.tabbedPane.getSelectedIndex();
            if (selectedTab != -1) {
                JPanel tablePanel = (JPanel) instance.tabbedPane.getComponentAt(selectedTab);
                JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
                JTable table = (JTable) scrollPane.getViewport().getView();
                CustomTableModel tableModel = (CustomTableModel) table.getModel();

                int selectedColumn = table.getSelectedColumn();
                DatabaseManager.getInstance().deleteColumn(selectedTab, selectedColumn, tableModel);
            }
        });

        instance.addRowMenuItem.addActionListener(e -> {
            int selectedTab = instance.tabbedPane.getSelectedIndex();
            DatabaseManager.getInstance().addRow(selectedTab, new Row());
        });

        instance.deleteRowMenuItem.addActionListener(e -> {
            int selectedTab = instance.tabbedPane.getSelectedIndex();
            if (selectedTab != -1) {
                JPanel tablePanel = (JPanel) instance.tabbedPane.getComponentAt(selectedTab);
                JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
                JTable table = (JTable) scrollPane.getViewport().getView();
                CustomTableModel tableModel = (CustomTableModel) table.getModel();

                int selectedRow = table.getSelectedRow();
                DatabaseManager.getInstance().deleteRow(selectedTab, selectedRow, tableModel);
            }
        });

        instance.renameTableMenuItem.addActionListener(e -> {
            int selectedTab = instance.tabbedPane.getSelectedIndex();
            if (selectedTab != -1) {
                JPanel tablePanel = (JPanel) instance.tabbedPane.getComponentAt(selectedTab);
                String newName = JOptionPane.showInputDialog(instance.frame, "Введіть нову назву таблиці:");
                DatabaseManager.getInstance().renameTable(selectedTab, newName);
            }
        });

        instance.renameColumnMenuItem.addActionListener(e -> {

            int selectedTab = instance.tabbedPane.getSelectedIndex();
            if (selectedTab != -1) {
                String newColumnName = JOptionPane.showInputDialog(instance.frame, "Введіть нову назву колонки:");

                JPanel tablePanel = (JPanel) instance.tabbedPane.getComponentAt(selectedTab);
                JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
                JTable table = (JTable) scrollPane.getViewport().getView();

                int selectedColumn = table.getSelectedColumn();
                DatabaseManager.getInstance().renameColumn(selectedTab, selectedColumn, newColumnName, table);
            }

        });
        instance.changeColumnTypeMenuItem.addActionListener(e -> {

            int selectedTab = instance.tabbedPane.getSelectedIndex();
            if (selectedTab != -1) {
                ColumnType selectedDataType = (ColumnType) JOptionPane.showInputDialog(
                        instance.frame,
                        "Оберіть новий тип колонки:",
                        "Редагувати Колонку",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        ColumnType.values(),
                        ColumnType.INT
                );

                if (selectedDataType != null) {

                    JPanel tablePanel = (JPanel) instance.tabbedPane.getComponentAt(selectedTab);
                    JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
                    JTable table = (JTable) scrollPane.getViewport().getView();
                    int selectedColumn = table.getSelectedColumn();

                    DatabaseManager.getInstance().changeColumnType(selectedTab, selectedColumn, selectedDataType, table);
                }
            }
        });

        instance.createDB.addActionListener(e -> {
            if (!DatabaseManager.getInstance().existDB()) {
                String name = JOptionPane.showInputDialog(instance.frame, "Введіть назву бази даних:");
                if (name != null && !name.isEmpty()) {
                    DatabaseManager.getInstance().createDB(name);
                    instance.setMenusVisibility(true);
                }
            } else {
                int result = JOptionPane.showConfirmDialog(instance.frame, "База даних вже існує. Хочете створити нову?", "Підтвердити створення бази даних", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    String name = JOptionPane.showInputDialog(instance.frame, "Введіть назву бази даних:");
                    if (name != null && !name.isEmpty()) {
                        DatabaseManager.getInstance().deleteDB();
                        DatabaseManager.getInstance().createDB(name);
                    }
                }
            }
        });
        instance.deleteDB.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(instance.frame, "Точно треба видалити???", "Підтвердити видалення бази даних", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                DatabaseManager.getInstance().deleteDB();
                instance.databaseLabel.setText("Відкрийте або створіть базу данних");
                instance.setMenusVisibility(false);

            }
        });

        instance.saveDB.addActionListener(e -> {
            String path = JOptionPane.showInputDialog(instance.frame, "Введіть шлях:");
            System.out.println(path);
            DatabaseManager.getInstance().saveDB(path);
        });
        instance.deleteRowDuplicates.addActionListener(e -> {
            int selectedTab = instance.tabbedPane.getSelectedIndex();
            if (selectedTab != -1) {
                JPanel tablePanel = (JPanel) instance.tabbedPane.getComponentAt(selectedTab);
                JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
                JTable table = (JTable) scrollPane.getViewport().getView();
                CustomTableModel tableModel = (CustomTableModel) table.getModel();

                DatabaseManager.getInstance().deleteDuplicateRows(selectedTab, tableModel);
                JOptionPane.showMessageDialog(DBMS.instance.frame, "Вітаю! Дублікати рядків видалено");
            }
        });
    }

    public JPanel createTablePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();

        CustomTable table = new CustomTable(model);

        DefaultCellEditor cellEditor = new DefaultCellEditor(new JTextField());

        cellEditor.addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                int selectedRow = table.getSelectedRow();
                int selectedColumn = table.getSelectedColumn();
                Object updatedValue = table.getValueAt(selectedRow, selectedColumn);
            }

            @Override
            public void editingCanceled(ChangeEvent e) {
            }
        });

        for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
            table.getColumnModel().getColumn(columnIndex).setCellEditor(cellEditor);
        }

        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(scrollPane, BorderLayout.CENTER);

        CustomTableModel tableModel = new CustomTableModel ();

        table.setModel(tableModel);

        return panel;
    }
    private void setMenusVisibility(boolean visible) {
        renameDatabaseMenuItem.setVisible(visible);
        instance.tableMenu.setVisible(visible);
        instance.columnMenu.setVisible(visible);
        instance.rowMenu.setVisible(visible);
        instance.deleteDB.setVisible(visible);
        instance.saveDB.setVisible(visible);
    }

    public void renderCells(){
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            JPanel tablePanel = (JPanel) tabbedPane.getComponentAt(i);
            JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(0);
            CustomTable table = (CustomTable) scrollPane.getViewport().getView();
            for (int j = 0; j < DatabaseManager.database.tables.get(i).rows.size(); j++) {
                for (int k = 0; k < DatabaseManager.database.tables.get(i).columns.size(); k++) {
                    String data = DatabaseManager.database.tables.get(i).rows.get(j).getAt(k);
                    table.setValueAt(data, j, k);
                }
            }
        }
    }
}