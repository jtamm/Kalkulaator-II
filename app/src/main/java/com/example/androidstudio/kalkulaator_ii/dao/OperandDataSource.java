package com.example.androidstudio.kalkulaator_ii.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.androidstudio.kalkulaator_ii.model.DayStatistic;
import com.example.androidstudio.kalkulaator_ii.model.Operand;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AndroidStudio on 02.04.2016.
 */
public class OperandDataSource {
    private SQLiteDatabase database;
    private List<String> tableChild = new ArrayList<String>();

    public OperandDataSource(SQLiteDatabase database) {
        this.database = database;
        tableChild.add(MySQLiteHelper.TABLE_OPERANDS);
    }

    public OperandDataSource(SQLiteDatabase database, List<String> tableChild) {
        this.database = database;
        this.tableChild.addAll(tableChild);
        this.tableChild.add(MySQLiteHelper.TABLE_OPERANDS);
    }

    private String[] allColumns = MySQLiteHelper.ALL_COLUMNS_OPERANDS;

    public void create(Operand operand) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, operand.getName());
        long insertId = database.insert(MySQLiteHelper.TABLE_OPERANDS, null, values);
        operand.setId(insertId);
    }

    public void createNotExist(Operand operand) {
        Operand operand1 = getByName(operand.getName());
        if (operand1 == null) {
            create(operand);
        } else {
            operand.setId(operand1.getId());
        }
    }

    public Operand getById(long id) {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_OPERANDS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + id, null,
                null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) return null;
        Operand operand = cursorTOperand(cursor);
        cursor.close();
        return operand;
    }

    public Operand getByName(String name) {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_OPERANDS,
                allColumns, MySQLiteHelper.COLUMN_NAME + " = '" + name + "'", null,
                null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) return null;
        Operand operand = cursorTOperand(cursor);
        cursor.close();
        return operand;
    }

    private Operand cursorTOperand(Cursor cursor) {
        Operand operand = new Operand();
        operand.setId(cursor.getLong(0));
        operand.setName(cursor.getString(1));
        return operand;
    }
}
