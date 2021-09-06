package com.example.sixkeeper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
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
        private const val KEY_ACCOUNT_STATUS = "account_status"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createUserInfoTable = (
                "CREATE TABLE " + TABLE_USER_INFO + "(" +
                        KEY_USER_ID + " INTEGER PRIMARY KEY," +
                        KEY_FIRST_NAME + " TEXT," +
                        KEY_LAST_NAME + " TEXT," +
                        KEY_BIRTH_DATE + " TEXT," +
                        KEY_EMAIL + " TEXT," +
                        KEY_MOBILE_NUMBER + " INTEGER" +
                        ")"
                )
        val createUserAccTable = (
                "CREATE TABLE " + TABLE_USER_ACC + "(" +
                        KEY_USER_ID + " INTEGER PRIMARY KEY," +
                        KEY_USERNAME + " TEXT," +
                        KEY_PASSWORD + " TEXT," +
                        KEY_MASTER_PIN + " INTEGER," +
                        KEY_ACCOUNT_STATUS + " INTEGER" +
                        ")"
                )

        db?.execSQL(createUserInfoTable)
        db?.execSQL(createUserAccTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_USER_INFO")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER_ACC")
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
            put(KEY_USERNAME, userAcc.username)
            put(KEY_PASSWORD, userAcc.password)
            put(KEY_MASTER_PIN, userAcc.masterPin)
            put(KEY_ACCOUNT_STATUS, userAcc.accountStatus)
        }

        val success = db.insert(TABLE_USER_ACC, null, contentValues)

        db.close()
        return success
    }

    @SuppressLint("Recycle")
    fun validateUserAcc(): List<UserAccModelClass> {
        val userAccList: ArrayList<UserAccModelClass> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_USER_ACC"
        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var userId: Int
        var userUsername: String
        var userPassword: String
        var userMasterPIN: Int
        var userAccountStatus: Int

        if (cursor.moveToFirst()) {
            do {
                userId = cursor.getInt(cursor.getColumnIndex("user_id"))
                userUsername = cursor.getString(cursor.getColumnIndex("username"))
                userPassword = cursor.getString(cursor.getColumnIndex("password"))
                userMasterPIN = cursor.getInt(cursor.getColumnIndex("master_pin"))
                userAccountStatus = cursor.getInt(cursor.getColumnIndex("account_status"))

                val user = UserAccModelClass(
                        userId = userId,
                        username = userUsername,
                        password = userPassword,
                        masterPin = userMasterPIN,
                        accountStatus = userAccountStatus
                )
                userAccList.add(user)
            } while (cursor.moveToNext())
        }

        return userAccList
    }

    fun updateUserStatus(userAcc: UserAccModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.apply {
            put(KEY_ACCOUNT_STATUS, userAcc.accountStatus)
        }

        // Updating Row
        val success = db.update(
                TABLE_USER_ACC,
                contentValues,
                "user_id=" + userAcc.userId,
                null
        )
        // 2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return success
    }
}