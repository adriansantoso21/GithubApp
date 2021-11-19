package com.example.githubapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.githubapp.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.example.githubapp.db.DatabaseContract.UserColumns

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "dbgithubapp"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                " (${UserColumns.USERNAME} TEXT NOT NULL," +
                " ${UserColumns.FULLNAME} TEXT NOT NULL," +
                " ${UserColumns.AVATAR} TEXT NOT NULL," +
                " ${UserColumns.COMPANY} TEXT NOT NULL," +
                " ${UserColumns.LOCATION} TEXT NOT NULL," +
                " ${UserColumns.BIO} TEXT NOT NULL," +
                " ${UserColumns.FOLLOWER} TEXT NOT NULL," +
                " ${UserColumns.FOLLOWING} TEXT NOT NULL," +
                " ${UserColumns.REPOSITORY} TEXT NOT NULL," +
                " ${UserColumns.ISFAV} BOOLEAN NOT NULL)"

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}