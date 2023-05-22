package com.jecna.task.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.jecna.task.model.TaskModel;

//https://www.digitalocean.com/community/tutorials/android-sqlite-database-example-tutorial
public class DBManager {
    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(TaskModel taskModel) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.NAME, taskModel.getName());
        contentValue.put(DatabaseHelper.DESCRIPTION, taskModel.getDescription());
        contentValue.put(DatabaseHelper.DATEFROM, taskModel.getDateFrom().toString());
        contentValue.put(DatabaseHelper.DATETO, taskModel.getDateTo().toString());
        contentValue.put(DatabaseHelper.PRIORITY, taskModel.getPriority().getValue());
        contentValue.put(DatabaseHelper.STATUS, taskModel.getStatus().getValue());
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.NAME, DatabaseHelper.DESCRIPTION,DatabaseHelper.DATEFROM,DatabaseHelper.DATETO,DatabaseHelper.PRIORITY,DatabaseHelper.STATUS };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(TaskModel taskModel) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.NAME, taskModel.getName());
        contentValue.put(DatabaseHelper.DESCRIPTION, taskModel.getDescription());
        contentValue.put(DatabaseHelper.DATEFROM, taskModel.getDateFrom().toString());
        contentValue.put(DatabaseHelper.DATETO, taskModel.getDateTo().toString());
        contentValue.put(DatabaseHelper.PRIORITY, taskModel.getPriority().getValue());
        contentValue.put(DatabaseHelper.STATUS, taskModel.getStatus().getValue());
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValue, DatabaseHelper._ID + " = " + taskModel.getId(), null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }
}
