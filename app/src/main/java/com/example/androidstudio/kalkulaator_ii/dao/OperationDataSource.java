package com.example.androidstudio.kalkulaator_ii.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.androidstudio.kalkulaator_ii.model.DoubleType;
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
public class OperationDataSource {
    private SQLiteDatabase database;
    private List<String> tableChild = new ArrayList<String>();

    public OperationDataSource(SQLiteDatabase database) {
        this.database = database;
        tableChild.add(MySQLiteHelper.TABLE_OPERATIONS);
    }

    public OperationDataSource(SQLiteDatabase database, List<String> tableChild) {
        this.database = database;
        this.tableChild.addAll(tableChild);
        this.tableChild.add(MySQLiteHelper.TABLE_OPERATIONS);
    }

    private String[] allColumns = MySQLiteHelper.ALL_COLUMNS_OPERATIONS;

    public void create(Operation operation) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_EXPRESSION_ID, operation.getExpressionId());
        if (operation.getOperandId() == 0 && operation.getOperand() != null) {
            Operand operand = operation.getOperand();
            OperandDataSource operandDataSource = new OperandDataSource(database);
            operandDataSource.createNotExist(operand);
            operation.setOperandId(operand.getId());
        }
        values.put(MySQLiteHelper.COLUMN_OPERAND_ID, operation.getOperandId());
        values.put(MySQLiteHelper.COLUMN_NUM1, operation.getNum1());
        values.put(MySQLiteHelper.COLUMN_NUM2, operation.getNum2());
        values.put(MySQLiteHelper.COLUMN_RESULT, operation.getResult());
        if (operation.getResultTypeId() == 0 && operation.getResultType() != null) {
            DoubleType doubleType = operation.getResultType();
            DoubleTypeDataSource doubleTypeDataSource = new DoubleTypeDataSource(database);
            doubleTypeDataSource.createNotExist(doubleType);
            operation.setResultTypeId(doubleType.getId());
        }
        values.put(MySQLiteHelper.COLUMN_RESULTTYPE_ID, operation.getResultTypeId());
        long insertId = database.insert(MySQLiteHelper.TABLE_OPERATIONS, null, values);
        operation.setId(insertId);
    }

    public Operation getById(long id) {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_OPERATIONS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + id, null,
                null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) return null;
        Operation operation = cursorToOperation(cursor);
        cursor.close();
        return operation;
    }

    public List<Operation> getByExpressionId(long expressionId) {
        List<Operation> operations = new ArrayList<Operation>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_OPERATIONS,
                allColumns, MySQLiteHelper.COLUMN_EXPRESSION_ID + " = " + expressionId,
                null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            operations.add(cursorToOperation(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return operations;
    }

    public List<Operation> getAll() {
        List<Operation> operations = new ArrayList<Operation>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_OPERATIONS,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            operations.add(cursorToOperation(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return operations;
    }

    private Operation cursorToOperation(Cursor cursor) {
        Operation operation = new Operation();
        operation.setId(cursor.getLong(0));
        operation.setExpressionId(cursor.getLong(1));
        if (tableChild.indexOf(MySQLiteHelper.TABLE_EXPRESSIONS) == -1) {
            ExpressionDataSource expressionDataSource = new ExpressionDataSource(database, tableChild);
            operation.setExpression(expressionDataSource.getById(operation.getExpressionId()));
        }
        operation.setOperandId(cursor.getLong(2));
        if (tableChild.indexOf(MySQLiteHelper.TABLE_OPERANDS) == -1) {
            OperandDataSource operandDataSource = new OperandDataSource(database, tableChild);
            operation.setOperand(operandDataSource.getById(operation.getOperandId()));
        }
        operation.setNum1(cursor.getFloat(3));
        operation.setNum2(cursor.getFloat(4));
        operation.setResult(cursor.getFloat(5));
        operation.setResultTypeId(cursor.getLong(6));
        if (tableChild.indexOf(MySQLiteHelper.TABLE_DOUBLETYPES) == -1) {
            DoubleTypeDataSource doubleTypeDataSource = new DoubleTypeDataSource(database, tableChild);
            operation.setResultType(doubleTypeDataSource.getById(operation.getResultTypeId()));
        }
        return operation;
    }
}
