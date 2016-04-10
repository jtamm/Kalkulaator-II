package com.example.androidstudio.kalkulaator_ii.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by AndroidStudio on 02.04.2016.
 */
public class DataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    public DayStatisticDataSource dayStatistics;
    public OperandDataSource operandDataSource;
    public DoubleTypeDataSource doubleTypeDataSource;
    public ExpressionDataSource expressionDataSource;
    public OperationDataSource operationDataSource;

    public DataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        dayStatistics = new DayStatisticDataSource(database);
        operandDataSource = new OperandDataSource(database);
        doubleTypeDataSource = new DoubleTypeDataSource(database);
        expressionDataSource = new ExpressionDataSource(database);
        operationDataSource = new OperationDataSource(database);
    }

    public void close() {
        dbHelper.close();
    }

}
