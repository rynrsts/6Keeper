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
        private const val TABLE_SAVED_PASS = "SavedPasswordTable"

        private const val KEY_USER_ID = "user_id"

        // TABLE_USER_INFO
        private const val KEY_FIRST_NAME = "first_name"
        private const val KEY_LAST_NAME = "last_name"
        private const val KEY_BIRTH_DATE = "birth_date"
        private const val KEY_EMAIL = "email"
        private const val KEY_MOBILE_NUMBER = "mobile_number"
        private const val KEY_LAST_UPDATE = "last_update"

        // TABLE_USER_ACC
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
        private const val KEY_MASTER_PIN = "master_pin"
        private const val KEY_ACCOUNT_STATUS = "account_status"
        private const val KEY_CREATION_DATE = "creation_date"
        private const val KEY_LAST_LOGIN = "last_login"

        // TABLE_SAVED_PASS
        private const val KEY_PASS_ID = "pass_id"
        private const val KEY_SAVED_PASS = "saved_password"
        // KEY_CREATION_DATE = "creation_date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createUserInfoTable = (
                "CREATE TABLE " + TABLE_USER_INFO + "(" +
                        KEY_USER_ID + " TEXT," +
                        KEY_FIRST_NAME + " TEXT," +
                        KEY_LAST_NAME + " TEXT," +
                        KEY_BIRTH_DATE + " TEXT," +
                        KEY_EMAIL + " TEXT," +
                        KEY_MOBILE_NUMBER + " TEXT," +
                        KEY_LAST_UPDATE + " TEXT" +
                        ")"
                )
        val createUserAccTable = (
                "CREATE TABLE " + TABLE_USER_ACC + "(" +
                        KEY_USER_ID + " TEXT," +
                        KEY_USERNAME + " TEXT," +
                        KEY_PASSWORD + " BLOB," +
                        KEY_MASTER_PIN + " BLOB," +
                        KEY_ACCOUNT_STATUS + " TEXT," +
                        KEY_CREATION_DATE + " TEXT," +
                        KEY_LAST_LOGIN + " TEXT" +
                        ")"
                )
        val createSavedPassTable = (
                "CREATE TABLE " + TABLE_SAVED_PASS + "(" +
                        KEY_PASS_ID + " TEXT," +
                        KEY_SAVED_PASS + " TEXT," +
                        KEY_CREATION_DATE + " TEXT" +
                        ")"
                )

        db?.execSQL(createUserInfoTable)
        db?.execSQL(createUserAccTable)
        db?.execSQL(createSavedPassTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_USER_INFO")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER_ACC")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SAVED_PASS")
        onCreate(db)
    }

    /*
     *******************************************************************************************
     *******************************************************************************************    ADD
     *******************************************************************************************
     */

    fun addUserInfo(userInfo: UserInfoModelClass): Long {                                           // Add User Information
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.apply {
            put(KEY_USER_ID, userInfo.userId)
            put(KEY_FIRST_NAME, userInfo.firstName)
            put(KEY_LAST_NAME, userInfo.lastName)
            put(KEY_BIRTH_DATE, userInfo.birthDate)
            put(KEY_EMAIL, userInfo.email)
            put(KEY_MOBILE_NUMBER, userInfo.mobileNumber)
            put(KEY_LAST_UPDATE, userInfo.lastUpdate)
        }

        val success = db.insert(TABLE_USER_INFO, null, contentValues)

        db.close()
        return success
    }

    fun addUserAcc(userAcc: UserAccModelClass): Long {                                              // Add User Account
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.apply {
            put(KEY_USER_ID, userAcc.userId)
            put(KEY_USERNAME, userAcc.username)
            put(KEY_PASSWORD, userAcc.password)
            put(KEY_MASTER_PIN, userAcc.masterPin)
            put(KEY_ACCOUNT_STATUS, userAcc.accountStatus)
            put(KEY_CREATION_DATE, userAcc.creationDate)
            put(KEY_LAST_LOGIN, userAcc.lastLogin)
        }

        val success = db.insert(TABLE_USER_ACC, null, contentValues)

        db.close()
        return success
    }

    fun addGeneratedPass(userSavedPass: UserSavedPassModelClass): Long {                            // Add Generated Password
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.apply {
            put(KEY_PASS_ID, userSavedPass.passId)
            put(KEY_SAVED_PASS, userSavedPass.generatedPassword)
            put(KEY_CREATION_DATE, userSavedPass.creationDate)
        }

        val success = db.insert(TABLE_SAVED_PASS, null, contentValues)

        db.close()
        return success
    }

    /*
     *******************************************************************************************
     *******************************************************************************************    VIEW
     *******************************************************************************************
     */

    @SuppressLint("Recycle")
    fun validateUserAcc(): List<UserAccModelClass> {                                                // Validate Username, Password, and/or Master PIN
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

        var userId: String
        var userUsername: String
        var userPassword: ByteArray
        var userMasterPIN: ByteArray
        var userAccountStatus: String
        var userCreationDate: String
        var userLastLogin: String

        if (cursor.moveToFirst()) {
            do {
                userId = cursor.getString(cursor.getColumnIndex("user_id"))
                userUsername = cursor.getString(cursor.getColumnIndex("username"))
                userPassword = cursor.getBlob(cursor.getColumnIndex("password"))
                userMasterPIN = cursor.getBlob(cursor.getColumnIndex("master_pin"))
                userAccountStatus =
                        cursor.getString(cursor.getColumnIndex("account_status"))
                userCreationDate =
                        cursor.getString(cursor.getColumnIndex("creation_date"))
                userLastLogin =
                        cursor.getString(cursor.getColumnIndex("last_login"))

                val user = UserAccModelClass(
                        userId = userId,
                        username = userUsername,
                        password = userPassword,
                        masterPin = userMasterPIN,
                        accountStatus = userAccountStatus,
                        creationDate = userCreationDate,
                        lastLogin = userLastLogin
                )
                userAccList.add(user)
            } while (cursor.moveToNext())
        }

        db.close()
        return userAccList
    }

    @SuppressLint("Recycle")
    fun viewUserInfo(): List<UserInfoModelClass> {                                            // View user information
        val userInfoList: ArrayList<UserInfoModelClass> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_USER_INFO"
        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var userId: String
        var userFirstName: String
        var userLastName: String
        var userBirthDate: String
        var userEmail: String
        var userMobileNumber: String
        var userLastUpdate: String

        if (cursor.moveToFirst()) {
            do {
                userId = cursor.getString(cursor.getColumnIndex("user_id"))
                userFirstName = cursor.getString(cursor.getColumnIndex("first_name"))
                userLastName = cursor.getString(cursor.getColumnIndex("last_name"))
                userBirthDate = cursor.getString(cursor.getColumnIndex("birth_date"))
                userEmail = cursor.getString(cursor.getColumnIndex("email"))
                userMobileNumber = cursor.getString(cursor.getColumnIndex("mobile_number"))
                userLastUpdate = cursor.getString(cursor.getColumnIndex("last_update"))

                val user = UserInfoModelClass(
                        userId = userId,
                        firstName = userFirstName,
                        lastName = userLastName,
                        birthDate = userBirthDate,
                        email = userEmail,
                        mobileNumber = userMobileNumber,
                        lastUpdate = userLastUpdate
                )
                userInfoList.add(user)
            } while (cursor.moveToNext())
        }

        db.close()
        return userInfoList
    }

    @SuppressLint("Recycle")
    fun viewSavedPass(): List<UserSavedPassModelClass> {                                            // View saved password
        val userSavedPassList: ArrayList<UserSavedPassModelClass> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_SAVED_PASS"
        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var passId: String
        var passPassword: String
        var passCreationDate: String

        if (cursor.moveToFirst()) {
            do {
                passId = cursor.getString(cursor.getColumnIndex("pass_id"))
                passPassword = cursor.getString(cursor.getColumnIndex("saved_password"))
                passCreationDate = cursor.getString(cursor.getColumnIndex("creation_date"))

                val user = UserSavedPassModelClass(
                        passId = passId,
                        generatedPassword = passPassword,
                        creationDate = passCreationDate
                )
                userSavedPassList.add(user)
            } while (cursor.moveToNext())
        }

        db.close()
        return userSavedPassList
    }

    /*
     *******************************************************************************************
     *******************************************************************************************    UPDATE
     *******************************************************************************************
     */

    fun updateUserStatus(id: String, accStatus: String): Int {                                      // Update User Status, 0 or 1
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.apply {
            put(KEY_ACCOUNT_STATUS, accStatus)
        }

        val success = db.update(
                TABLE_USER_ACC,
                contentValues,
                "user_id='$id'",
                null
        )

        db.close()
        return success
    }

    fun updateLastLogin(id: String, lastLogin: String): Int {                                          // Update last login
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.apply {
            put(KEY_LAST_LOGIN, lastLogin)
        }

        val success = db.update(
                TABLE_USER_ACC,
                contentValues,
                "user_id='$id'",
                null
        )

        db.close()
        return success
    }

    /*
     *******************************************************************************************
     *******************************************************************************************    DELETE
     *******************************************************************************************
     */

    fun truncateAllTables(): Int {                                                                  // Delete all data in all tables
        val db = this.writableDatabase

        val success = db.delete(TABLE_USER_ACC, "", null)
        db.delete(TABLE_USER_INFO, "", null)
        db.delete(TABLE_SAVED_PASS, "", null)

        db.close()
        return success
    }
}