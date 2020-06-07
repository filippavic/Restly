package com.fer.ppj.restly.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.sql.Date
import java.time.LocalDate

val DATABASE_NAME = "Sessions"
val TABLE_NAME = "Session"
val COL_EXERCISE_TIME = "exercise_time"
val COL_TOTAL_TIME = "total_time"
val COL_DATE = "date"

class DbHandler(var context: Context?) : SQLiteOpenHelper(
    context,
    DATABASE_NAME, null, 1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable =
            "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_EXERCISE_TIME + " INTEGER," +
                    COL_TOTAL_TIME + " INTEGER," +
                    COL_DATE + " TEXT)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun insertData(session: Session) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_EXERCISE_TIME, session.exercise_time)
        contentValues.put(COL_TOTAL_TIME, session.total_time)
        contentValues.put(COL_DATE, session.date.time)
        var result = db.insert(TABLE_NAME, null, contentValues)
        if (result == -1.toLong())
            Toast.makeText(context, "Failed inserting to db", Toast.LENGTH_SHORT).show()
    }

    fun readData(): MutableList<Session> {
        var list: MutableList<Session> = ArrayList()

        val db = this.readableDatabase
        val query = "SELECT * FROM " + TABLE_NAME
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                var session = Session()
                session.id = result.getInt(0)
                session.exercise_time = result.getInt(1)
                session.total_time = result.getInt(2)
                session.date = Date(result.getLong(3))
                list.add(session)
            } while (result.moveToNext())
        }

        result.close()
        db.close()

        return list
    }

    fun readDataTop10(): MutableList<Session> {
        var list: MutableList<Session> = ArrayList()

        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME ORDER BY $COL_DATE DESC LIMIT 7"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                var session = Session()
                session.id = result.getInt(0)
                session.exercise_time = result.getInt(1)
                session.total_time = result.getInt(2)
                session.date = Date(result.getLong(3))
                list.add(session)
            } while (result.moveToNext())
        }

        result.close()
        db.close()

        return list
    }

    fun readDataAfter10(): MutableList<Session> {
        var list: MutableList<Session> = ArrayList()

        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE ID NOT IN (SELECT ID FROM $TABLE_NAME ORDER BY $COL_DATE DESC LIMIT 7) ORDER BY $COL_DATE DESC"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                var session = Session()
                session.id = result.getInt(0)
                session.exercise_time = result.getInt(1)
                session.total_time = result.getInt(2)
                session.date = Date(result.getLong(3))
                list.add(session)
            } while (result.moveToNext())
        }

        result.close()
        db.close()

        return list
    }

    fun getDataCount(): Int {
        var count = 0

        val db = this.readableDatabase
        val query = "SELECT COUNT(ID) FROM $TABLE_NAME"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                count = result.getInt(0)
            } while (result.moveToNext())
        }

        result.close()
        db.close()
        return count

    }
}