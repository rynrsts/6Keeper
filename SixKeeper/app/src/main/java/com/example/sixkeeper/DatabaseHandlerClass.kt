package com.example.sixkeeper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandlerClass(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "SixKeeperDatabase"
        private const val TABLE_USER_INFO = "UserInformationTable"
        private const val TABLE_USER_ACC = "UserAccountTable"

        //private const val KEY_ID = "id"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_FIRST_NAME = "first_name"
        private const val KEY_LAST_NAME = "last_name"
        private const val KEY_BIRTH_DATE = "birth_date"
        private const val KEY_EMAIL = "email"
        private const val KEY_MOBILE_NUMBER = "mobile_number"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
        private const val KEY_MASTER_PIN = "master_pin"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createUserInfoTable = (
                "CREATE TABLE " + TABLE_USER_INFO + "(" +
                        KEY_USER_ID + " INTEGER PRIMARY KEY," +
                        KEY_FIRST_NAME + " TEXT," +
                        KEY_LAST_NAME + " TEXT," +
                        KEY_BIRTH_DATE + " TEXT" +
                        KEY_EMAIL + " TEXT" +
                        KEY_MOBILE_NUMBER + " INTEGER" +
                        ")"
                )
        val createUserAccTable = (
                "CREATE TABLE " + TABLE_USER_ACC + "(" +
                        KEY_USER_ID + " INTEGER PRIMARY KEY," +
                        KEY_USERNAME + " TEXT," +
                        KEY_PASSWORD + " TEXT" +
                        KEY_MASTER_PIN + " INTEGER" +
                        ")"
                )

        db?.execSQL(createUserInfoTable)
        db?.execSQL(createUserAccTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USER_INFO")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USER_ACC")
        onCreate(db)
    }

    // Add User Information
    fun addUserInfo(userInfo: UserInfoModelClass): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.apply {
            put(KEY_USER_ID, userInfo.userId)
            put(KEY_FIRST_NAME, userInfo.firstName)
            put(KEY_LAST_NAME, userInfo.lastName)
            put(KEY_BIRTH_DATE, userInfo.birthDate)
            put(KEY_EMAIL, userInfo.email)
            put(KEY_MOBILE_NUMBER, userInfo.mobileNumber)
        }

        val success = db.insert(TABLE_USER_INFO, null, contentValues)

        db.close()
        return success
    }

    // Add User Account
    fun addUserAcc(userAcc: UserAccModelClass): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.apply {
            put(KEY_USER_ID, userAcc.userId)
            put(KEY_USER_ID, userAcc.username)
            put(KEY_USER_ID, userAcc.password)
            put(KEY_USER_ID, userAcc.masterPin)
        }

        val success = db.insert(TABLE_USER_ACC, null, contentValues)

        db.close()
        return success
    }
}