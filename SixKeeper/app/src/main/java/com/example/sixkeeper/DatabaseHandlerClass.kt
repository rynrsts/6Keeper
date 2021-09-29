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
        private const val TABLE_CATEGORIES = "CategoriesTable"
        private const val TABLE_PLATFORMS = "PlatformsTable"
        private const val TABLE_ACCOUNTS = "AccountsTable"

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

        // TABLE_CATEGORIES
        private const val KEY_CATEGORY_ID = "category_id"
        private const val KEY_CATEGORY_NAME = "category_name"

        // TABLE_PLATFORMS
        private const val KEY_PLATFORM_ID = "platform_id"
        private const val KEY_PLATFORM_NAME = "platform_name"
//        private const val KEY_CATEGORY_ID = "category_id"

        // TABLE_ACCOUNTS
        private const val KEY_ACCOUNT_ID = "account_id"
        private const val KEY_ACCOUNT_NAME = "account_name"
        private const val KEY_ACCOUNT_USERNAME = "account_username"
        private const val KEY_ACCOUNT_EMAIL = "account_email"
        private const val KEY_ACCOUNT_MOBILE_NUMBER = "account_mobile_number"
        private const val KEY_ACCOUNT_PASSWORD = "account_password"
        private const val KEY_ACCOUNT_APPLICATION = "account_application"
        private const val KEY_ACCOUNT_WEBSITE = "account_website"
        private const val KEY_ACCOUNT_DESCRIPTION = "account_description"
        private const val KEY_ACCOUNT_FAVORITES = "account_favorites"
//        private const val KEY_PLATFORM_ID = "platform_id"
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

        val createCategoriesTable = (
                "CREATE TABLE " + TABLE_CATEGORIES + "(" +
                        KEY_CATEGORY_ID + " TEXT," +
                        KEY_CATEGORY_NAME + " TEXT" +
                        ")"
                )

        val createPlatformsTable = (
                "CREATE TABLE " + TABLE_PLATFORMS + "(" +
                        KEY_PLATFORM_ID + " TEXT," +
                        KEY_PLATFORM_NAME + " TEXT," +
                        KEY_CATEGORY_ID + " TEXT" +
                        ")"
                )

        val createAccountsTable = (
                "CREATE TABLE " + TABLE_ACCOUNTS + "(" +
                        KEY_ACCOUNT_ID + " TEXT," +
                        KEY_ACCOUNT_NAME + " TEXT," +
                        KEY_ACCOUNT_USERNAME + " TEXT," +
                        KEY_ACCOUNT_EMAIL + " TEXT," +
                        KEY_ACCOUNT_MOBILE_NUMBER + " TEXT," +
                        KEY_ACCOUNT_PASSWORD + " TEXT," +
                        KEY_ACCOUNT_APPLICATION + " TEXT," +
                        KEY_ACCOUNT_WEBSITE + " TEXT," +
                        KEY_ACCOUNT_DESCRIPTION + " TEXT," +
                        KEY_ACCOUNT_FAVORITES + " TEXT," +
                        KEY_PLATFORM_ID + " TEXT" +
                        ")"
                )

        db?.execSQL(createUserInfoTable)
        db?.execSQL(createUserAccTable)
        db?.execSQL(createSavedPassTable)
        db?.execSQL(createCategoriesTable)
        db?.execSQL(createPlatformsTable)
        db?.execSQL(createAccountsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_USER_INFO")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER_ACC")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SAVED_PASS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PLATFORMS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ACCOUNTS")
        onCreate(db)
    }

    /*
     *******************************************************************************************
     *******************************************************************************************
     *******************************************************************************************    ADD
     *******************************************************************************************
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

    fun addCategory(userCategory: UserCategoryModelClass): Long {                                   // Add Category
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.apply {
            put(KEY_CATEGORY_ID, userCategory.categoryId)
            put(KEY_CATEGORY_NAME, userCategory.categoryName)
        }

        val success = db.insert(TABLE_CATEGORIES, null, contentValues)

        db.close()
        return success
    }

    fun addPlatform(userPlatform: UserPlatformModelClass): Long {                                   // Add Platform
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.apply {
            put(KEY_PLATFORM_ID, userPlatform.platformId)
            put(KEY_PLATFORM_NAME, userPlatform.platformName)
            put(KEY_CATEGORY_ID, userPlatform.categoryId)
        }

        val success = db.insert(TABLE_PLATFORMS, null, contentValues)

        db.close()
        return success
    }

    /*
     *******************************************************************************************
     *******************************************************************************************
     *******************************************************************************************    VIEW
     *******************************************************************************************
     *******************************************************************************************
     */

    @SuppressLint("Recycle")
    fun validateUserAcc(): List<UserAccModelClass> {                                                // Validate Username, Password, and/or Master PIN
        val userAccList: ArrayList<UserAccModelClass> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_USER_ACC"
        val db = this.readableDatabase
        val cursor: Cursor?

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
                userId = cursor.getString(cursor.getColumnIndex(KEY_USER_ID))
                userUsername = cursor.getString(cursor.getColumnIndex(KEY_USERNAME))
                userPassword = cursor.getBlob(cursor.getColumnIndex(KEY_PASSWORD))
                userMasterPIN = cursor.getBlob(cursor.getColumnIndex(KEY_MASTER_PIN))
                userAccountStatus =
                        cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_STATUS))
                userCreationDate =
                        cursor.getString(cursor.getColumnIndex(KEY_CREATION_DATE))
                userLastLogin =
                        cursor.getString(cursor.getColumnIndex(KEY_LAST_LOGIN))

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

        cursor.close()
        db.close()
        return userAccList
    }

    @SuppressLint("Recycle")
    fun viewUserInfo(): List<UserInfoModelClass> {                                                  // View user information
        val userInfoList: ArrayList<UserInfoModelClass> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_USER_INFO"
        val db = this.readableDatabase
        val cursor: Cursor?

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
                userId = cursor.getString(cursor.getColumnIndex(KEY_USER_ID))
                userFirstName = cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME))
                userLastName = cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME))
                userBirthDate = cursor.getString(cursor.getColumnIndex(KEY_BIRTH_DATE))
                userEmail = cursor.getString(cursor.getColumnIndex(KEY_EMAIL))
                userMobileNumber = cursor.getString(cursor.getColumnIndex(KEY_MOBILE_NUMBER))
                userLastUpdate = cursor.getString(cursor.getColumnIndex(KEY_LAST_UPDATE))

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

        cursor.close()
        db.close()
        return userInfoList
    }

    @SuppressLint("Recycle")
    fun viewUsername(): String {                                                                    // View Username
        val selectQuery = "SELECT * FROM $TABLE_USER_ACC"
        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return String.toString()
        }

        var userUsername = ""

        if (cursor.moveToFirst()) {
            do {
                userUsername = cursor.getString(cursor.getColumnIndex(KEY_USERNAME))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return userUsername
    }

    @SuppressLint("Recycle")
    fun viewSavedPass(): List<UserSavedPassModelClass> {                                            // View saved password
        val userSavedPassList: ArrayList<UserSavedPassModelClass> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_SAVED_PASS"
        val db = this.readableDatabase
        val cursor: Cursor?

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
                passId = cursor.getString(cursor.getColumnIndex(KEY_PASS_ID))
                passPassword = cursor.getString(cursor.getColumnIndex(KEY_SAVED_PASS))
                passCreationDate = cursor.getString(cursor.getColumnIndex(KEY_CREATION_DATE))

                val user = UserSavedPassModelClass(
                        passId = passId,
                        generatedPassword = passPassword,
                        creationDate = passCreationDate
                )
                userSavedPassList.add(user)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return userSavedPassList
    }

    @SuppressLint("Recycle")
    fun viewCategory(): List<UserCategoryModelClass> {                                              // View categories
        val userCategoryList: ArrayList<UserCategoryModelClass> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_CATEGORIES"
        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var categoryId: String
        var categoryName: String

        if (cursor.moveToFirst()) {
            do {
                categoryId = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_ID))
                categoryName = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_NAME))

                val user = UserCategoryModelClass(
                        categoryId = categoryId,
                        categoryName = categoryName
                )
                userCategoryList.add(user)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return userCategoryList
    }

    fun viewNumberOfPlatforms(categoryId: String): Int {                                            // View number of platforms in a category
        val selectQuery = "SELECT COUNT(*) FROM $TABLE_PLATFORMS WHERE $KEY_CATEGORY_ID = '$categoryId'"
        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return 0
        }

        cursor.moveToFirst()
        val num = cursor.getInt(0)

        cursor.close()
        db.close()
        return num
    }

    @SuppressLint("Recycle")
    fun viewPlatform(id: String): List<UserPlatformModelClass> {                      // View platforms
        val userPlatformList: ArrayList<UserPlatformModelClass> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_PLATFORMS WHERE $KEY_CATEGORY_ID = '$id'"
        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var platformId: String
        var platformName: String
        var categoryId: String

        if (cursor.moveToFirst()) {
            do {
                platformId = cursor.getString(cursor.getColumnIndex(KEY_PLATFORM_ID))
                platformName = cursor.getString(cursor.getColumnIndex(KEY_PLATFORM_NAME))
                categoryId = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_ID))

                val user = UserPlatformModelClass(
                        platformId = platformId,
                        platformName = platformName,
                        categoryId = categoryId
                )
                userPlatformList.add(user)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return userPlatformList
    }

    fun viewNumberOfAccounts(platformId: String): Int {                                             // View number of accounts in a platform
        val selectQuery = "SELECT COUNT(*) FROM $TABLE_ACCOUNTS WHERE $KEY_PLATFORM_ID = '$platformId'"
        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return 0
        }

        cursor.moveToFirst()
        val num = cursor.getInt(0)

        cursor.close()
        db.close()
        return num
    }

    /*
     *******************************************************************************************
     *******************************************************************************************
     *******************************************************************************************    UPDATE
     *******************************************************************************************
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
                "$KEY_USER_ID='$id'",
                null
        )

        db.close()
        return success
    }

    fun updateLastLogin(id: String, lastLogin: String): Int {                                       // Update last login
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.apply {
            put(KEY_LAST_LOGIN, lastLogin)
        }

        val success = db.update(
                TABLE_USER_ACC,
                contentValues,
                "$KEY_USER_ID='$id'",
                null
        )

        db.close()
        return success
    }

    fun updateUserInfo(field: String, updatedData: String, lastUpdate: String): Int {               // Update user information
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.apply {
            put(field, updatedData)
            put(KEY_LAST_UPDATE, lastUpdate)
        }

        val success = db.update(TABLE_USER_INFO, contentValues, null, null)

        db.close()
        return success
    }

    fun updateUserUsername(updatedData: String, lastUpdate: String): Int {                          // Update user account
        val db = this.writableDatabase
        val contentValues1 = ContentValues()
        val contentValues2 = ContentValues()

        contentValues1.apply {
            put(KEY_USERNAME, updatedData)
        }
        contentValues2.apply {
            put(KEY_LAST_UPDATE, lastUpdate)
        }

        val success = db.update(TABLE_USER_ACC, contentValues1, null, null)
        db.update(TABLE_USER_INFO, contentValues2, null, null)

        db.close()
        return success
    }

    fun updateUserAcc(field: String, updatedData: ByteArray, lastUpdate: String): Int {             // Update user account
        val db = this.writableDatabase
        val contentValues1 = ContentValues()
        val contentValues2 = ContentValues()

        contentValues1.apply {
            put(field, updatedData)
        }
        contentValues2.apply {
            put(KEY_LAST_UPDATE, lastUpdate)
        }

        val success = db.update(TABLE_USER_ACC, contentValues1, null, null)
        db.update(TABLE_USER_INFO, contentValues2, null, null)

        db.close()
        return success
    }

    /*
     *******************************************************************************************
     *******************************************************************************************
     *******************************************************************************************    DELETE
     *******************************************************************************************
     *******************************************************************************************
     */

    fun truncateAllTables(): Int {                                                                  // Delete all data in all tables
        val db = this.writableDatabase

        val success = db.delete(TABLE_USER_ACC, "", null)
        db.delete(TABLE_USER_INFO, "", null)
        db.delete(TABLE_SAVED_PASS, "", null)
        db.delete(TABLE_CATEGORIES, "", null)
        db.delete(TABLE_PLATFORMS, "", null)
        db.delete(TABLE_ACCOUNTS, "", null)

        db.close()
        return success
    }
}