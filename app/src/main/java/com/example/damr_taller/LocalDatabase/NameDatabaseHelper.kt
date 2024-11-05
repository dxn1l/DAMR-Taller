package com.example.damr_taller.LocalDatabase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NameDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "names.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAMES = "names"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_NAMES ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_NAME TEXT)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAMES")
        onCreate(db)
    }

    fun addName(name: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        return db.insert(TABLE_NAMES, null, values)
    }

    fun getAllNames(): List<String> {
        val names = mutableListOf<String>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAMES", null)
        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                names.add(name)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return names
    }

    fun deleteAllNames() {
        val db = this.writableDatabase
        db.delete(TABLE_NAMES, null, null)
    }

    fun nameExists(name: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAMES WHERE $COLUMN_NAME = '$name'", null)
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
}