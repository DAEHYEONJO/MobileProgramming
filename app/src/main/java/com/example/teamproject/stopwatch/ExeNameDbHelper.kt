package com.example.teamproject.stopwatch

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class ExeNameDbHelper(val context : Context) : SQLiteOpenHelper(context, DB_NAME,null, DB_VERSION) {
    companion object{
        val DB_VERSION = 1
        val DB_NAME = "exedb.db"
        val TABLE_NAME = "namelist"
        val EXE_NAME = "name"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val create_table = "create table if not exists $TABLE_NAME ("+
                "$EXE_NAME text); "
        db?.execSQL(create_table)
    }

    fun getAll():ArrayList<String>{
        Log.d("fetch","dbgetAll")
        val exeNameList = ArrayList<String>()
        val sql = "select * from $TABLE_NAME"
        val db = readableDatabase
        val cursor = db.rawQuery(sql,null)
        val flag = cursor.count!=0
        if (flag){
            cursor.moveToFirst()
            do {
                Log.d("fetch","name : ${cursor.getString(0)}")
                exeNameList.add(cursor.getString(0))
            }while (cursor.moveToNext())
        }else{
            Log.d("fetch","한개도없음")
        }
        return exeNameList
    }

    fun checkDuplicate(name : String): Boolean {
        val sql = "select * from $TABLE_NAME where $EXE_NAME='$name';"
        val db = readableDatabase
        val flag = db.rawQuery(sql,null).count!=0
        Log.d("fetch","name ,flag : $flag")
        return flag
    }

    fun insertName(name:String){
        Log.d("fetch","dbinsertName")
        if (checkDuplicate(name)) return

        val values = ContentValues()
        values.put(EXE_NAME,name)
        val db = readableDatabase
        db.insert(TABLE_NAME,null,values)
        db.close()

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val delete_table = "drop table if exists $TABLE_NAME"
        db?.execSQL(delete_table)
        onCreate(db)
    }
}