package org.dbms.database.component.column;

import org.dbms.database.component.Column;

public class StringColumn extends Column {

    public StringColumn(String name){
        super(name);
        this.type = ColumnType.STRING.name();
    }
    @Override
    public boolean validate(String data) {
        return true;
    }
}
