package com.example.sudoku.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COL_1 + " TEXT," + COL_2 + " TEXT," + COL_3 + " INTEGER, " + COL_4 + " INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun addData(name : String, time : String, errors : Int, score : Int){

        val values = ContentValues()
        values.put(COL_1, name)
        values.put(COL_2, time)
        values.put(COL_3, errors)
        values.put(COL_4, score)

        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
    }

    fun getTable(): Cursor? {

        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_4 + " DESC, " + COL_2 + " DESC",null)

    }


    /*fun updateTable(name: String?, time: String?, errors: Int?): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_1, name)
        values.put(COL_2, time)
        values.put(COL_3, errors)
        values.put(COL_4, score)
        db.update(TABLE_NAME, values, "NAME = ?", arrayOf(name))
        return true
    }*/

    fun deleteFromTable() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM " + TABLE_NAME)
    }

    companion object{

        private val DATABASE_NAME = "SudokuDatabase"
        private val DATABASE_VERSION = 1
        val TABLE_NAME = "LEADERBOARD"
        val COL_1 = "NAME"
        val COL_2 = "TIME"
        val COL_3 = "ERRORS"
        val COL_4 = "SCORE"
    }
}
