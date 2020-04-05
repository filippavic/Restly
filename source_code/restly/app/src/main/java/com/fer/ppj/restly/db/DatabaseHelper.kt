package com.fer.ppj.restly.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.sql.Date
import java.sql.Timestamp


class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context,
        TABLE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTable =
            "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL2 + " INTEGER," +
                    COL3 + " INTEGER," +
                    COL4 + " TEXT)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addData(exercise_time: Int, total_time: Int, date_time: Date): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL2, exercise_time)
        contentValues.put(COL3, total_time)
        contentValues.put(COL4, date_time.time)
        Log.d(
            TAG,
            "addData: Adding $exercise_time, $total_time, $date_time to $TABLE_NAME"
        )
        db.insert(TABLE_NAME, null, contentValues)
        return true
    }

    /**
     * Returns all the data from database
     * @return
     */
    val data: Cursor
        get() {
            val db = this.writableDatabase
            val query = "SELECT * FROM $TABLE_NAME"
            return db.rawQuery(query, null)
        }

    companion object {
        private const val TAG = "DatabaseHelper"
        private const val TABLE_NAME = "activity_logger"
        private const val COL1 = "ID"
        private const val COL2 = "exercise_time"
        private const val COL3 = "total_time"
        private const val COL4 = "date"
    }
}