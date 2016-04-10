package com.example.androidstudio.kalkulaator_ii.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.androidstudio.kalkulaator_ii.DoubleStatistic;
import com.example.androidstudio.kalkulaator_ii.model.DoubleType;
import com.example.androidstudio.kalkulaator_ii.model.Operand;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AndroidStudio on 02.04.2016.
 */
public class DoubleTypeDataSource {
    private SQLiteDatabase database;
    private List<String> tableChild = new ArrayList<String>();

    private String[] allColumns = MySQLiteHelper.ALL_COLUMNS_DOUBLETYPES;

    public DoubleTypeDataSource(SQLiteDatabase database) {
        this.database = database;
        tableChild.add(MySQLiteHelper.TABLE_DOUBLETYPES);
    }

    public DoubleTypeDataSource(SQLiteDatabase database, List<String> tableChild) {
        this.database = database;
        this.tableChild.addAll(tableChild);
        this.tableChild.add(MySQLiteHelper.TABLE_DOUBLETYPES);
    }

    public void create(DoubleType doubleType) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, doubleType.getName());
        long insertId = database.insert(MySQLiteHelper.TABLE_DOUBLETYPES, null, values);
        doubleType.setId(insertId);
    }

    public void createNotExist(DoubleType doubleType) {
        DoubleType doubleType1 = getByName(doubleType.getName());
        if (doubleType1 == null) {
            create(doubleType);
        } else {
            doubleType.setId(doubleType1.getId());
        }
    }

    public DoubleType getById(long id) {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_DOUBLETYPES,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + id, null,
                null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) return null;
        DoubleType doubleType = cursorToDoubleType(cursor);
        cursor.close();
        return doubleType;
    }

    public DoubleType getByName(String name) {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_DOUBLETYPES,
                allColumns, MySQLiteHelper.COLUMN_NAME + " = '" + name + "'", null,
                null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) return null;
        DoubleType doubleType = cursorToDoubleType(cursor);
        cursor.close();
        return doubleType;
    }

    private DoubleType cursorToDoubleType(Cursor cursor) {
        DoubleType doubleType = new DoubleType();
        doubleType.setId(cursor.getLong(0));
        doubleType.setName(cursor.getString(1));
        return doubleType;
    }
}
