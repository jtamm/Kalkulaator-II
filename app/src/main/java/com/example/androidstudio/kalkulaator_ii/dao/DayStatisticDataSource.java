package com.example.androidstudio.kalkulaator_ii.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.androidstudio.kalkulaator_ii.model.DayStatistic;
import com.example.androidstudio.kalkulaator_ii.model.Operand;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by AndroidStudio on 02.04.2016.
 */
public class DayStatisticDataSource {
    private SQLiteDatabase database;
    private List<String> tableChild = new ArrayList<String>();

    private String[] allColumns = MySQLiteHelper.ALL_COLUMNS_DAYSTATISICS;

    public DayStatisticDataSource(SQLiteDatabase database) {
        this.database = database;
        this.tableChild.add(MySQLiteHelper.TABLE_DAYSTATISICS);
    }

    public DayStatisticDataSource(SQLiteDatabase database, List<String> tableChild) {
        this.database = database;
        this.tableChild.addAll(tableChild);
        this.tableChild.add(MySQLiteHelper.TABLE_DAYSTATISICS);
    }

    public void create(DayStatistic dayStatistic) {
        ContentValues values = new ContentValues();
        if (dayStatistic.getOperandId() == 0 && dayStatistic.getOperand() != null) {
            Operand operand = dayStatistic.getOperand();
            OperandDataSource operandDataSource = new OperandDataSource(database);
            operandDataSource.createNotExist(operand);
            dayStatistic.setOperandId(operand.getId());
        }
        values.put(MySQLiteHelper.COLUMN_OPERAND_ID, dayStatistic.getOperandId());
        values.put(MySQLiteHelper.COLUMN_CREATED, MySQLiteHelper.getDateTime());
        values.put(MySQLiteHelper.COLUMN_COUNT, dayStatistic.getCount());
        long insertId = database.insert(MySQLiteHelper.TABLE_DAYSTATISICS, null, values);
        dayStatistic.setId(insertId);
    }

    public void update(DayStatistic dayStatistic) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_OPERAND_ID, dayStatistic.getOperandId());
        values.put(MySQLiteHelper.COLUMN_COUNT, dayStatistic.getCount());
        database.update(MySQLiteHelper.TABLE_DAYSTATISICS, values, MySQLiteHelper.COLUMN_ID + "="
                + dayStatistic.getId(), null);
    }

    public DayStatistic getById(long id) {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_DAYSTATISICS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + id, null,
                null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) return null;
        DayStatistic d = cursorToDayStatistic(cursor);
        cursor.close();
        return d;
    }

    public DayStatistic getByCreatedDateOperand(java.util.Date date, Operand operand) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        String dateData = dateFormat.format(date);
        if (operand == null) return null;
        if (operand.getId() == 0) {
            OperandDataSource operandDataSource = new OperandDataSource(database);
            Operand operand1 = operandDataSource.getByName(operand.getName());
            if (operand1 == null) return null;
            operand.setId(operand1.getId());
        }
        Cursor cursor = database.query(MySQLiteHelper.TABLE_DAYSTATISICS,
                allColumns, "strftime(\"%Y-%m-%d\", " + MySQLiteHelper.COLUMN_CREATED + ") = '"
                        + dateData + "' AND " + MySQLiteHelper.COLUMN_OPERAND_ID + " = " + operand.getId(), null,
                null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) return null;
        DayStatistic d = cursorToDayStatistic(cursor);
        cursor.close();
        return d;
    }

    public void addCountByCreatedDateOperand(java.util.Date date, Operand operand) {
        DayStatistic dayStatistic = getByCreatedDateOperand(date, operand);
        if (dayStatistic == null) {
            //Create
        } else {
            dayStatistic.setCount(dayStatistic.getCount() + 1);
            update(dayStatistic);
        }
    }

    public void addCountByCreatedNowOperand(Operand operand) {
        DayStatistic dayStatistic = getByCreatedDateOperand(new java.util.Date(), operand);
        if (dayStatistic == null) {
            dayStatistic = new DayStatistic();
            dayStatistic.setOperand(operand);
            dayStatistic.setCount(1);
            create(dayStatistic);
        } else {
            dayStatistic.setCount(dayStatistic.getCount() + 1);
            update(dayStatistic);
        }
    }

    public List<DayStatistic> getAll() {
        List<DayStatistic> dayStatistics = new ArrayList<DayStatistic>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_DAYSTATISICS,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            dayStatistics.add(cursorToDayStatistic(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return dayStatistics;
    }

    private DayStatistic cursorToDayStatistic(Cursor cursor) {
        DayStatistic dayStatistic = new DayStatistic();
        dayStatistic.setId(cursor.getLong(0));
        dayStatistic.setOperandId(cursor.getLong(1));
        if (tableChild.indexOf(MySQLiteHelper.TABLE_OPERANDS) == -1) {
            OperandDataSource operandDataSource = new OperandDataSource(database);
            dayStatistic.setOperand(operandDataSource.getById(dayStatistic.getOperandId()));
        }
        String[] s = cursor.getString(2).split(" ");
        dayStatistic.setCreated(Date.valueOf(s[0]));
        dayStatistic.setCount(cursor.getInt(3));
        return dayStatistic;
    }
}
