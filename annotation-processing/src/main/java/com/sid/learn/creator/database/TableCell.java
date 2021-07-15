package com.sid.learn.creator.database;

import lombok.Data;

@Data
public class TableCell{
    private String columnName;
    private String columnSize;
    private int datatype;
    private boolean isNullable;
    private boolean isAutoIncrement;
    private boolean isPrimaryKey;
}
