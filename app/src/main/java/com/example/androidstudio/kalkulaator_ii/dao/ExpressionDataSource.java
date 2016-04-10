package com.example.androidstudio.kalkulaator_ii.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.androidstudio.kalkulaator_ii.model.DayStatistic;
import com.example.androidstudio.kalkulaator_ii.model.Expression;
import com.example.androidstudio.kalkulaator_ii.model.Operand;
import com.example.androidstudio.kalkulaator_ii.model.Operation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by AndroidStudio on 02.04.2016.
 */
public class ExpressionDataSource {
    private SQLiteDatabase database;
    List<String> tableChild = new ArrayList<String>();

    public ExpressionDataSource(SQLiteDatabase database) {
        this.database = database;
        tableChild.add(MySQLiteHelper.TABLE_EXPRESSIONS);
    }

    public ExpressionDataSource(SQLiteDatabase database, List<String> tableChild) {
        this.database = database;
        for (String table : tableChild) {
            this.tableChild.add(table);
        }
        this.tableChild.add(MySQLiteHelper.TABLE_EXPRESSIONS);
    }

    private String[] allColumns = MySQLiteHelper.ALL_COLUMNS_EXPRESSIONS;

    public void create(Expression expression) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_DATA, expression.getData());
        values.put(MySQLiteHelper.COLUMN_RESULT, expression.getResult());
        values.put(MySQLiteHelper.COLUMN_CREATED, MySQLiteHelper.getDateTime());
        long insertId = database.insert(MySQLiteHelper.TABLE_EXPRESSIONS, null, values);
        if (expression.getOperations() != null) {
            OperationDataSource operationDataSource = new OperationDataSource(database);
            for (Operation operation : expression.getOperations()) {
                operation.setExpressionId(insertId);
                operationDataSource.create(operation);
            }
        }
        expression.setId(insertId);
    }

    public Expression getById(long id) {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_EXPRESSIONS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + id, null,
                null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) return null;
        Expression expression = cursorToExpression(cursor);
        cursor.close();
        return expression;
    }

    public List<Expression> getAll() {
        List<Expression> expressions = new ArrayList<Expression>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_EXPRESSIONS,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            expressions.add(cursorToExpression(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return expressions;
    }

    private Expression cursorToExpression(Cursor cursor) {
        Expression expression = new Expression();
        expression.setId(cursor.getLong(0));
        expression.setData(cursor.getString(1));
        expression.setResult(cursor.getString(2));
        if (tableChild.indexOf(MySQLiteHelper.TABLE_OPERATIONS) == -1) {
            OperationDataSource operationDataSource = new OperationDataSource(database, tableChild);
            expression.setOperations(operationDataSource.getByExpressionId(expression.getId()));
        }
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            expression.setCreated(ft.parse(cursor.getString(3)));
        } catch (Exception e) {

        }
        return expression;
    }
}
