package com.fer.ppj.restly.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import java.sql.Timestamp


val DATABASE_NAME = "Sessions"
val TABLE_NAME = "Session"
val COL_EXERCISE_TIME = "exercise_time"
val COL_TOTAL_TIME = "total_time"
val COL_DATE = "date"

class DbHandler(var context: Context?) : SQLiteOpenHelper(context,
    DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable =
            "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_EXERCISE_TIME + " INTEGER," +
                    COL_TOTAL_TIME + " INTEGER," +
                    COL_DATE + " INTEGER)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun insertData(session: Session){
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_EXERCISE_TIME, session.exercise_time)
        contentValues.put(COL_TOTAL_TIME, session.total_time)
        contentValues.put(COL_DATE, session.date.time)
        var result = db.insert(TABLE_NAME, null, contentValues)
        if(result == -1.toLong())
            Toast.makeText(context, "Failed inserting to db", Toast.LENGTH_SHORT).show()
        else Toast.makeText(context, "Succeeded inserting to db: " + session.total_time + "s", Toast.LENGTH_SHORT).show()
    } 

    fun readData() : MutableList<Session>{
        var list : MutableList<Session> = ArrayList()

        val db = this.readableDatabase
        val query = "SELECT * FROM " + TABLE_NAME
        val result = db.rawQuery(query, null)
        if(result.moveToFirst()){
            do{
                var session = Session()
                session.id = result.getInt(0)
                session.exercise_time = result.getInt(1)
                session.total_time = result.getInt(2)
                session.date = Timestamp(result.getLong(3))
                list.add(session)
            }while (result.moveToNext())
        }

        result.close()
        db.close()

        return list
    }
}