package com.example.androidstudio.kalkulaator_ii.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String COLUMN_ID = "_id";

    public static final String TABLE_DAYSTATISICS = "daystatistics";
    public static final String TABLE_DOUBLETYPES = "doubletypes";
    public static final String TABLE_EXPRESSIONS = "expressions";
    public static final String TABLE_OPERANDS = "operand";
    public static final String TABLE_OPERATIONS = "operations";

    public static final String COLUMN_OPERAND_ID = "operand_id";
    public static final String COLUMN_EXPRESSION_ID = "expression_id";
    public static final String COLUMN_RESULTTYPE_ID = "resulttype_id";
    public static final String COLUMN_CREATED = "created";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_COUNT = "count";
    public static final String COLUMN_DATA = "data";
    public static final String COLUMN_RESULT = "result";
    public static final String COLUMN_NUM1 = "num1";
    public static final String COLUMN_NUM2 = "num2";

    public static final String[] ALL_COLUMNS_DAYSTATISICS = {COLUMN_ID, COLUMN_OPERAND_ID, COLUMN_CREATED, COLUMN_COUNT};
    public static final String[] ALL_COLUMNS_DOUBLETYPES = {COLUMN_ID, COLUMN_NAME};
    public static final String[] ALL_COLUMNS_EXPRESSIONS = {COLUMN_ID, COLUMN_DATA, COLUMN_RESULT, COLUMN_CREATED};
    public static final String[] ALL_COLUMNS_OPERANDS = {COLUMN_ID, COLUMN_NAME};
    public static final String[] ALL_COLUMNS_OPERATIONS = {COLUMN_ID, COLUMN_EXPRESSION_ID, COLUMN_OPERAND_ID, COLUMN_NUM1, COLUMN_NUM2, COLUMN_RESULT, COLUMN_RESULTTYPE_ID};

    private static final String DATABASE_NAME = "calculator.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE_TABLE_DAYSTATISICS = "create table "
            + TABLE_DAYSTATISICS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_OPERAND_ID
            + " integer, " + COLUMN_CREATED + " DATETIME DEFAULT CURRENT_DATE, "
            + COLUMN_COUNT + " integer);";

    private static final String DATABASE_CREATE_TABLE_DOUBLETYPES = "create table "
            + TABLE_DOUBLETYPES + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null);";

    private static final String DATABASE_CREATE_TABLE_EXPRESSIONS = "create table "
            + TABLE_EXPRESSIONS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_DATA
            + " text not null, " + COLUMN_RESULT + " text not null, "
            + COLUMN_CREATED + " DATETIME DEFAULT CURRENT_TIMESTAMP)";

    private static final String DATABASE_CREATE_TABLE_OPERANDS = "create table "
            + TABLE_OPERANDS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null);";

    private static final String DATABASE_CREATE_TABLE_OPERATIONS = "create table "
            + TABLE_OPERATIONS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_EXPRESSION_ID
            + " integer, " + COLUMN_OPERAND_ID + " integer, "
            + COLUMN_NUM1 + " FLOAT, " + COLUMN_NUM2 + " FLOAT, "
            + COLUMN_RESULT + " FLOAT, " + COLUMN_RESULTTYPE_ID + " integer);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_TABLE_DAYSTATISICS);
        database.execSQL(DATABASE_CREATE_TABLE_DOUBLETYPES);
        database.execSQL(DATABASE_CREATE_TABLE_EXPRESSIONS);
        database.execSQL(DATABASE_CREATE_TABLE_OPERANDS);
        database.execSQL(DATABASE_CREATE_TABLE_OPERATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAYSTATISICS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOUBLETYPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPRESSIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPERANDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPERATIONS);
        onCreate(db);
    }

    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        java.util.Date date = new java.util.Date();
        return dateFormat.format(date);
    }

}