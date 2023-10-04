package org.dbms.database.component;

public abstract class Column {
    public String name;
    public String type;

    public Column(String name){
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract boolean validate(String data);
}
