package com.sid.learn.creator.database;

import lombok.Data;
import lombok.Getter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TableMetaData {

    @Getter
    private String tableName;

    @Getter
    private List<TableCell> tableCells;

    private Connection connection;

    public TableMetaData(String tableName) throws SQLException, ClassNotFoundException {
        this.tableName = tableName;
        this.tableCells = new ArrayList<>();
        makeDBConnection();
        setTableCells();
    }

    private void makeDBConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/lazy-wallet?sslmode=disable","postgres","54321");

    }

    private void setTableCells() throws SQLException {
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet columns = databaseMetaData.getColumns(null,null, tableName, null);
        while(columns.next()) {
            TableCell tableCell = new TableCell();
            tableCell.setColumnName(columns.getString("COLUMN_NAME"));
            tableCell.setColumnSize(columns.getString("COLUMN_SIZE"));
            tableCell.setDatatype(columns.getInt("DATA_TYPE"));
            tableCell.setNullable(columns.getBoolean("IS_NULLABLE"));
            tableCell.setAutoIncrement(columns.getBoolean("IS_AUTOINCREMENT"));
            tableCells.add(tableCell);
        }

        ResultSet primaryKeys = databaseMetaData.getPrimaryKeys(null, null, tableName);
        while(primaryKeys.next()){
            String primaryKeyColumnName = primaryKeys.getString("COLUMN_NAME");
            String primaryKeyName = primaryKeys.getString("PK_NAME");
            TableCell tableCell = tableCells.stream().
                    filter(cell-> cell.getColumnName().equalsIgnoreCase(primaryKeyColumnName)).
                    findFirst().get();
            tableCell.setPrimaryKey(true);
        }
    }

}