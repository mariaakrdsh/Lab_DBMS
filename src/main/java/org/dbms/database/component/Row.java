package org.dbms.database.component;

import java.util.ArrayList;
import java.util.List;

public class Row {
    public List<String> values = new ArrayList<>();

    public String getAt(int index){
        return values.get(index);
    }

    public void setAt(int index, String content){
        values.set(index,content);
    }
}
