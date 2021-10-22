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
        private const val TABLE_SETTINGS = "SettingsTable"
        private const val TABLE_ACTION_LOG = "ActionLogTable"
        private const val TABLE_CATEGORIES = "CategoriesTable"
        private const val TABLE_PLATFORMS = "PlatformsTable"
        private const val TABLE_ACCOUNTS = "AccountsTable"
        private const val TABLE_SAVED_PASS = "SavedPasswordTable"


        // TABLE_USER_INFO
        private const val KEY_USER_ID = "user_id"
        private const val KEY_FIRST_NAME = "first_name"
        private const val KEY_LAST_NAME = "last_name"
        private const val KEY_BIRTH_DATE = "birth_date"
        private const val KEY_EMAIL = "email"
        private const val KEY_MOBILE_NUMBER = "mobile_number"
        private const val KEY_LAST_UPDATE = "last_update"

        // TABLE_USER_ACC
//        private const val KEY_USER_ID = "user_id"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
        private const val KEY_MASTER_PIN = "master_pin"
        private const val KEY_ACCOUNT_STATUS = "account_status"
        private const val KEY_CREATION_DATE = "creation_date"
        private const val KEY_LAST_LOGIN = "last_login"

        // TABLE_SETTINGS
//        private const val KEY_USER_ID = "user_id"
        private const val KEY_SCREEN_CAPTURE = "screen_capture"
        private const val KEY_COPY = "copy"

        // TABLE_ACTION_LOG
        private const val KEY_ACTION_LOG_ID = "action_log_id"
        private const val KEY_ACTION_LOG_DESCRIPTION = "action_log_description"
        private const val KEY_ACTION_LOG_DATE = "action_log_date"

        // TABLE_CATEGORIES
        private const val KEY_CATEGORY_ID = "category_id"
        private const val KEY_CATEGORY_NAME = "category_name"

        // TABLE_PLATFORMS
        private const val KEY_PLATFORM_ID = "platform_id"
        private const val KEY_PLATFORM_NAME = "platform_name"
//        private const val KEY_CATEGORY_ID = "category_id"
//        private const val KEY_CATEGORY_NAME = "category_name"

        // TABLE_ACCOUNTS
        private const val KEY_ACCOUNT_ID = "account_id"
        private const val KEY_ACCOUNT_NAME = "account_name"
        private const val KEY_ACCOUNT_CREDENTIAL_FIELD = "account_credential_field"                 // Email, Mobile Number, Username, or Other
        private const val KEY_ACCOUNT_CREDENTIAL = "account_credential"
        private const val KEY_ACCOUNT_PASSWORD = "account_password"
        private const val KEY_ACCOUNT_WEBSITE_URL = "account_website_url"
        private const val KEY_ACCOUNT_DESCRIPTION = "account_description"
        private const val KEY_ACCOUNT_IS_FAVORITES = "account_is_favorites"
        private const val KEY_ACCOUNT_DELETED = "account_deleted"
        private const val KEY_ACCOUNT_DELETE_DATE = "account_delete_date"
        // KEY_CREATION_DATE = "creation_date"
        private const val KEY_PASSWORD_STATUS = "password_status"
//        private const val KEY_PLATFORM_ID = "platform_id"
//        private const val KEY_PLATFORM_NAME = "platform_name"
//        private const val KEY_CATEGORY_NAME = "category_name"

        // TABLE_SAVED_PASS
        private const val KEY_PASS_ID = "pass_id"
        private const val KEY_SAVED_PASS = "saved_password"

        // KEY_CREATION_DATE = "creation_date"
        private const val KEY_PASS_DELETED = "pass_deleted"
        private const val KEY_PASS_DELETE_DATE = "pass_delete_date"
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
        val createSettingsTable = (
                "CREATE TABLE " + TABLE_SETTINGS + "(" +
                        KEY_USER_ID + " TEXT," +
                        KEY_SCREEN_CAPTURE + " TEXT," +
                        KEY_COPY + " TEXT" +
                        ")"
                )
        val createActionLogTable = (
                "CREATE TABLE " + TABLE_ACTION_LOG + "(" +
                        KEY_ACTION_LOG_ID + " TEXT," +
                        KEY_ACTION_LOG_DESCRIPTION + " TEXT," +
                        KEY_ACTION_LOG_DATE + " TEXT" +
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
                        KEY_CATEGORY_ID + " TEXT," +
                        KEY_CATEGORY_NAME + " TEXT" +
                        ")"
                )
        val createAccountsTable = (
                "CREATE TABLE " + TABLE_ACCOUNTS + "(" +
                        KEY_ACCOUNT_ID + " TEXT," +
                        KEY_ACCOUNT_NAME + " TEXT," +
                        KEY_ACCOUNT_CREDENTIAL_FIELD + " TEXT," +
                        KEY_ACCOUNT_CREDENTIAL + " TEXT," +
                        KEY_ACCOUNT_PASSWORD + " TEXT," +
                        KEY_ACCOUNT_WEBSITE_URL + " TEXT," +
                        KEY_ACCOUNT_DESCRIPTION + " TEXT," +
                        KEY_ACCOUNT_IS_FAVORITES + " TEXT," +
                        KEY_ACCOUNT_DELETED + " TEXT," +
                        KEY_ACCOUNT_DELETE_DATE + " TEXT," +
                        KEY_CREATION_DATE + " TEXT," +
                        KEY_PASSWORD_STATUS + " TEXT," +
                        KEY_PLATFORM_ID + " TEXT," +
                        KEY_PLATFORM_NAME + " TEXT," +
                        KEY_CATEGORY_NAME + " TEXT" +
                        ")"
                )
        val createSavedPassTable = (
                "CREATE TABLE " + TABLE_SAVED_PASS + "(" +
                        KEY_PASS_ID + " TEXT," +
                        KEY_SAVED_PASS + " TEXT," +
                        KEY_CREATION_DATE + " TEXT," +
                        KEY_PASS_DELETED + " TEXT," +
                        KEY_PASS_DELETE_DATE + " TEXT" +
                        ")"
                )

        db?.execSQL(createUserInfoTable)
        db?.execSQL(createUserAccTable)
        db?.execSQL(createSettingsTable)
        db?.execSQL(createActionLogTable)
        db?.execSQL(createCategoriesTable)
        db?.execSQL(createPlatformsTable)
        db?.execSQL(createAccountsTable)
        db?.execSQL(createSavedPassTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_USER_INFO")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER_ACC")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SETTINGS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ACTION_LOG")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PLATFORMS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ACCOUNTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SAVED_PASS")
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

    fun addSettings(userSettings: UserSettingsModelClass): Long {                                   // Add Settings
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.apply {
            put(KEY_USER_ID, userSettings.userId)
            put(KEY_SCREEN_CAPTURE, userSettings.screenCapture)
            put(KEY_COPY, userSettings.copy)
        }

        val success = db.insert(TABLE_SETTINGS, null, contentValues)

        db.close()
        return success
    }

    fun addEventToActionLog(userActionLog: UserActionLogModelClass): Long {                         // Add event to Action Log
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.apply {
            put(KEY_ACTION_LOG_ID, userActionLog.actionLogId)
            put(KEY_ACTION_LOG_DESCRIPTION, userActionLog.actionLogDescription)
            put(KEY_ACTION_LOG_DATE, userActionLog.actionLogDate)
        }

        val success = db.insert(TABLE_ACTION_LOG, null, contentValues)

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
            put(KEY_CATEGORY_NAME, userPlatform.categoryName)
        }

        val success = db.insert(TABLE_PLATFORMS, null, contentValues)

        db.close()
        return success
    }

    fun addAccount(userAccount: UserAccountModelClass): Long {                                      // Add Account
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.apply {
            put(KEY_ACCOUNT_ID, userAccount.accountId)
            put(KEY_ACCOUNT_NAME, userAccount.accountName)
            put(KEY_ACCOUNT_CREDENTIAL_FIELD, userAccount.accountCredentialField)
            put(KEY_ACCOUNT_CREDENTIAL, userAccount.accountCredential)
            put(KEY_ACCOUNT_PASSWORD, userAccount.accountPassword)
            put(KEY_ACCOUNT_WEBSITE_URL, userAccount.accountWebsiteURL)
            put(KEY_ACCOUNT_DESCRIPTION, userAccount.accountDescription)
            put(KEY_ACCOUNT_IS_FAVORITES, userAccount.accountIsFavorites)
            put(KEY_ACCOUNT_DELETED, userAccount.accountDeleted)
            put(KEY_ACCOUNT_DELETE_DATE, userAccount.accountDeleteDate)
            put(KEY_CREATION_DATE, userAccount.creationDate)
            put(KEY_PASSWORD_STATUS, userAccount.passwordStatus)
            put(KEY_PLATFORM_ID, userAccount.platformId)
            put(KEY_PLATFORM_NAME, userAccount.platformName)
            put(KEY_CATEGORY_NAME, userAccount.categoryName)
        }

        val success = db.insert(TABLE_ACCOUNTS, null, contentValues)

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
            put(KEY_PASS_DELETED, userSavedPass.passDeleted)
            put(KEY_PASS_DELETE_DATE, userSavedPass.passDeleteDate)
        }

        val success = db.insert(TABLE_SAVED_PASS, null, contentValues)

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
    fun viewAccStatus(): String {                                                                   // View Account Status
        val selectQuery = "SELECT * FROM $TABLE_USER_ACC"
        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return String.toString()
        }

        var userStatus = ""

        if (cursor.moveToFirst()) {
            do {
                userStatus = cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_STATUS))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return userStatus
    }

    @SuppressLint("Recycle")
    fun viewUserInfo(): List<UserInfoModelClass> {                                                  // View User Information
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

    fun getLastIdOfActionLog(): String {                                                            // Get last Id of of Platforms
        val selectQuery = "SELECT $KEY_ACTION_LOG_ID FROM $TABLE_ACTION_LOG"
        val db = this.readableDatabase
        val cursor: Cursor?
        var lastId = ""

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ""
        }

        if (cursor.moveToLast()) {
            lastId = cursor.getString(0)
        }

        cursor.close()
        db.close()
        return lastId
    }

    @SuppressLint("Recycle")
    fun viewActionLog(): List<UserActionLogModelClass> {                                            // View Action Log
        val userActionLogList: ArrayList<UserActionLogModelClass> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_ACTION_LOG"
        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var actionLogId: String
        var actionLogDescription: String
        var actionLogDate: String

        if (cursor.moveToLast()) {
            var limit = 0

            do {
                limit++

                actionLogId = cursor.getString(cursor.getColumnIndex(KEY_ACTION_LOG_ID))
                actionLogDescription =
                        cursor.getString(cursor.getColumnIndex(KEY_ACTION_LOG_DESCRIPTION))
                actionLogDate = cursor.getString(cursor.getColumnIndex(KEY_ACTION_LOG_DATE))

                val user = UserActionLogModelClass(
                        actionLogId = actionLogId,
                        actionLogDescription = actionLogDescription,
                        actionLogDate = actionLogDate
                )
                userActionLogList.add(user)

                if (limit == 100) {
                    break
                }
            } while (cursor.moveToPrevious())
        }

        cursor.close()
        db.close()
        return userActionLogList
    }

    fun viewTotalNumberDashboard1(tableName: String): Int {                                         // View total number
        val selectQuery = "SELECT COUNT(*) FROM $tableName"
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

    fun viewTotalNumberDashboard2(
            tableName: String,
            columnDeleted: String,
            deleted: String,
            favorites: String
    ): Int {                                                                                        // View total number
        var selectQuery = "SELECT COUNT(*) FROM $tableName WHERE $columnDeleted = '$deleted'"
        val db = this.readableDatabase
        val cursor: Cursor?

        if (favorites.isNotEmpty()) {
            selectQuery = "SELECT COUNT(*) FROM $tableName " +
                    "WHERE $columnDeleted = '$deleted' AND $KEY_ACCOUNT_IS_FAVORITES = '$favorites'"
        }

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

    fun viewTotalNumberOfWeakPasswords(deleted: String, status: String): Int {                      // View total number of weak passwords
        val selectQuery = "SELECT COUNT(*) FROM $TABLE_ACCOUNTS " +
                "WHERE $KEY_ACCOUNT_DELETED = '$deleted' AND $KEY_PASSWORD_STATUS = '$status'"
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
    fun viewWeakAccounts(weak: String): List<UserAccountModelClass> {                               // View Weak Accounts
        val userAccountList: ArrayList<UserAccountModelClass> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_ACCOUNTS WHERE $KEY_PASSWORD_STATUS = '$weak'"
        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var accountId: String
        var accountName: String
        var platformId: String
        var platformName: String
        var categoryName: String

        if (cursor.moveToFirst()) {
            do {
                accountId = cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_ID))
                accountName = cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_NAME))
                platformId = cursor.getString(cursor.getColumnIndex(KEY_PLATFORM_ID))
                platformName = cursor.getString(cursor.getColumnIndex(KEY_PLATFORM_NAME))
                categoryName = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_NAME))

                val user = UserAccountModelClass(
                        accountId = accountId,
                        accountName = accountName,
                        accountCredentialField = "",
                        accountCredential = "",
                        accountPassword = "",
                        accountWebsiteURL = "",
                        accountDescription = "",
                        accountIsFavorites = "",
                        accountDeleted = "",
                        accountDeleteDate = "",
                        creationDate = "",
                        passwordStatus = "",
                        platformId = platformId,
                        platformName = platformName,
                        categoryName = categoryName
                )
                userAccountList.add(user)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return userAccountList
    }

    fun viewTotalNumberOfDuplicatePasswords(deleted: String): Int {                                 // View total number of duplicate passwords
        val selectQuery =
                "SELECT COUNT(*) FROM $TABLE_ACCOUNTS WHERE $KEY_ACCOUNT_DELETED = '$deleted' " +
                        "GROUP BY $KEY_ACCOUNT_PASSWORD HAVING COUNT(*) > 1"
        val db = this.readableDatabase
        val cursor: Cursor?
        var num = 0

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return 0
        }

        if (cursor.moveToFirst()) {
            do {
                num += cursor.getInt(0)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return num
    }

    @SuppressLint("Recycle")
    fun getDuplicateAccountsCount(): List<UserAccountDuplicateModelClass> {                                      // View Duplicate Accounts
        val userDuplicateCountList: ArrayList<UserAccountDuplicateModelClass> = ArrayList()
        val selectQuery = "SELECT $KEY_ACCOUNT_PASSWORD, COUNT (*) AS 'count' " +
                "FROM $TABLE_ACCOUNTS GROUP BY $KEY_ACCOUNT_PASSWORD HAVING COUNT(*) > 1"
        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var accountPassword: String
        var count: String

        if (cursor.moveToFirst()) {
            do {
                accountPassword = cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_PASSWORD))
                count = cursor.getString(cursor.getColumnIndex("count"))

                val user = UserAccountDuplicateModelClass(
                        accountPassword = accountPassword,
                        count = count
                )
                userDuplicateCountList.add(user)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return userDuplicateCountList
    }

    @SuppressLint("Recycle")
    fun viewCategory(field: String, id: String): List<UserCategoryModelClass> {                     // View Categories
        val userCategoryList: ArrayList<UserCategoryModelClass> = ArrayList()
        var selectQuery = "SELECT * FROM $TABLE_CATEGORIES"
        val db = this.readableDatabase
        val cursor: Cursor?

        if (field == "category") {
            selectQuery = "SELECT * FROM $TABLE_CATEGORIES WHERE $KEY_CATEGORY_ID = '$id'"
        }

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
        val selectQuery =
                "SELECT COUNT(*) FROM $TABLE_PLATFORMS WHERE $KEY_CATEGORY_ID = '$categoryId'"
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
    fun viewPlatform(field: String, id: String): List<UserPlatformModelClass> {                                    // View Platforms
        val userPlatformList: ArrayList<UserPlatformModelClass> = ArrayList()
        var selectQuery = "SELECT * FROM $TABLE_PLATFORMS WHERE $KEY_CATEGORY_ID = '$id'"
        val db = this.readableDatabase
        val cursor: Cursor?

        if (field == "platform") {
            selectQuery = "SELECT * FROM $TABLE_PLATFORMS WHERE $KEY_PLATFORM_ID = '$id'"
        }

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var platformId: String
        var platformName: String
        var categoryId: String
        var categoryName: String

        if (cursor.moveToFirst()) {
            do {
                platformId = cursor.getString(cursor.getColumnIndex(KEY_PLATFORM_ID))
                platformName = cursor.getString(cursor.getColumnIndex(KEY_PLATFORM_NAME))
                categoryId = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_ID))
                categoryName = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_NAME))

                val user = UserPlatformModelClass(
                        platformId = platformId,
                        platformName = platformName,
                        categoryId = categoryId,
                        categoryName = categoryName
                )
                userPlatformList.add(user)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return userPlatformList
    }

    fun viewNumberOfAccounts(deleted: String, platformId: String): Int {                            // View number of accounts in a platform
        val selectQuery = "SELECT COUNT(*) FROM $TABLE_ACCOUNTS " +
                "WHERE $KEY_ACCOUNT_DELETED = '$deleted' AND $KEY_PLATFORM_ID = '$platformId'"
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

    fun getLastIdOfPlatform(): String {                                                             // Get last Id of of Platforms
        val selectQuery = "SELECT $KEY_PLATFORM_ID FROM $TABLE_PLATFORMS"
        val db = this.readableDatabase
        val cursor: Cursor?
        var lastId = ""

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ""
        }

        if (cursor.moveToLast()) {
            lastId = cursor.getString(0)
        }

        cursor.close()
        db.close()
        return lastId
    }

    @SuppressLint("Recycle")
    fun viewAccount(
            conditionField: String,
            idOrIsFavorites: String,
            deleted: String
    ): List<UserAccountModelClass> {                                                                // View Specific Account
        val userAccountList: ArrayList<UserAccountModelClass> = ArrayList()
        var selectQuery = ""
        val db = this.readableDatabase
        val cursor: Cursor?

        when (conditionField) {
            "platformId" -> {
                selectQuery = "SELECT * FROM $TABLE_ACCOUNTS " +
                        "WHERE $KEY_ACCOUNT_DELETED = '$deleted' " +
                        "AND $KEY_PLATFORM_ID = '$idOrIsFavorites'"
            }
            "accountIsFavorites" -> {
                selectQuery = "SELECT * FROM $TABLE_ACCOUNTS " +
                        "WHERE $KEY_ACCOUNT_IS_FAVORITES = '$idOrIsFavorites' " +
                        "AND $KEY_ACCOUNT_DELETED = '$deleted'"
            }
            "deleted" -> {
                selectQuery = "SELECT * FROM $TABLE_ACCOUNTS " +
                        "WHERE $KEY_ACCOUNT_DELETED = '$deleted'"
            }
            "password" -> {
                selectQuery = "SELECT * FROM $TABLE_ACCOUNTS " +
                        "WHERE $KEY_ACCOUNT_DELETED = '$deleted' " +
                        "AND $KEY_ACCOUNT_PASSWORD = '$idOrIsFavorites'"
            }
        }

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var accountId: String
        var accountName: String
        var accountCredentialField: String
        var accountCredential: String
        var accountPassword: String
        var accountWebsiteURL: String
        var accountDescription: String
        var accountIsFavorites: String
        var accountDeleted: String
        var accountDeleteDate: String
        var creationDate: String
        var passwordStatus: String
        var platformId: String
        var platformName: String
        var categoryName: String

        if (cursor.moveToFirst()) {
            do {
                accountId = cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_ID))
                accountName = cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_NAME))
                accountCredentialField =
                        cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_CREDENTIAL_FIELD))
                accountCredential = cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_CREDENTIAL))
                accountPassword = cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_PASSWORD))
                accountWebsiteURL = cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_WEBSITE_URL))
                accountDescription =
                        cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_DESCRIPTION))
                accountIsFavorites =
                        cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_IS_FAVORITES))
                accountDeleted = cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_DELETED))
                accountDeleteDate = cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_DELETE_DATE))
                creationDate = cursor.getString(cursor.getColumnIndex(KEY_CREATION_DATE))
                passwordStatus = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD_STATUS))
                platformId = cursor.getString(cursor.getColumnIndex(KEY_PLATFORM_ID))
                platformName = cursor.getString(cursor.getColumnIndex(KEY_PLATFORM_NAME))
                categoryName = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_NAME))

                val user = UserAccountModelClass(
                        accountId = accountId,
                        accountName = accountName,
                        accountCredentialField = accountCredentialField,
                        accountCredential = accountCredential,
                        accountPassword = accountPassword,
                        accountWebsiteURL = accountWebsiteURL,
                        accountDescription = accountDescription,
                        accountIsFavorites = accountIsFavorites,
                        accountDeleted = accountDeleted,
                        accountDeleteDate = accountDeleteDate,
                        creationDate = creationDate,
                        passwordStatus = passwordStatus,
                        platformId = platformId,
                        platformName = platformName,
                        categoryName = categoryName
                )
                userAccountList.add(user)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return userAccountList
    }

    fun getLastIdOfAccount(): String {                                                              // Get last Id of of Accounts
        val selectQuery = "SELECT $KEY_ACCOUNT_ID FROM $TABLE_ACCOUNTS"
        val db = this.readableDatabase
        val cursor: Cursor?
        var lastId = ""

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ""
        }

        if (cursor.moveToLast()) {
            lastId = cursor.getString(0)
        }

        cursor.close()
        db.close()
        return lastId
    }

    @SuppressLint("Recycle")
    fun viewSavedPass(deleted: String): List<UserSavedPassModelClass> {                             // View Saved Password
        val userSavedPassList: ArrayList<UserSavedPassModelClass> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_SAVED_PASS WHERE $KEY_PASS_DELETED = '$deleted'"
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
        var passDeleted: String
        var passDeleteDate: String

        if (cursor.moveToLast()) {
            do {
                passId = cursor.getString(cursor.getColumnIndex(KEY_PASS_ID))
                passPassword = cursor.getString(cursor.getColumnIndex(KEY_SAVED_PASS))
                passCreationDate = cursor.getString(cursor.getColumnIndex(KEY_CREATION_DATE))
                passDeleted = cursor.getString(cursor.getColumnIndex(KEY_PASS_DELETED))
                passDeleteDate = cursor.getString(cursor.getColumnIndex(KEY_PASS_DELETE_DATE))

                val user = UserSavedPassModelClass(
                        passId = passId,
                        generatedPassword = passPassword,
                        creationDate = passCreationDate,
                        passDeleted = passDeleted,
                        passDeleteDate = passDeleteDate
                )
                userSavedPassList.add(user)
            } while (cursor.moveToPrevious())
        }

        cursor.close()
        db.close()
        return userSavedPassList
    }

    fun getLastIdOfSavedPasswords(): String {                                                       // Get last Id of of Saved Passwords
        val selectQuery = "SELECT $KEY_PASS_ID FROM $TABLE_SAVED_PASS"
        val db = this.readableDatabase
        val cursor: Cursor?
        var lastId = ""

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ""
        }

        if (cursor.moveToLast()) {
            lastId = cursor.getString(0)
        }

        cursor.close()
        db.close()
        return lastId
    }

    fun viewSettings(): List<UserSettingsModelClass> {                                              // View Settings
        val userSettingsList: ArrayList<UserSettingsModelClass> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_SETTINGS"
        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var accountId: String
        var screenCapture: String
        var copy: String

        if (cursor.moveToFirst()) {
            do {
                accountId = cursor.getString(cursor.getColumnIndex(KEY_USER_ID))
                screenCapture = cursor.getString(cursor.getColumnIndex(KEY_SCREEN_CAPTURE))
                copy = cursor.getString(cursor.getColumnIndex(KEY_COPY))

                val user = UserSettingsModelClass(
                        userId = accountId,
                        screenCapture = screenCapture,
                        copy = copy
                )
                userSettingsList.add(user)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return userSettingsList
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

    fun updateUserInfo(field: String, updatedData: String, lastUpdate: String): Int {               // Update User Information
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

    fun updateUserUsername(updatedData: String, lastUpdate: String): Int {                          // Update Username
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

    fun updateUserAcc(field: String, updatedData: ByteArray, lastUpdate: String): Int {             // Update User Account
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

    fun updateSettings(field: String, value: String): Int {                                         // Update Settings
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.apply {
            put(field, value)
        }

        val success = db.update(TABLE_SETTINGS, contentValues, "", null)

        db.close()
        return success
    }

    fun updateCategory(userCategory: UserCategoryModelClass): Int {                                 // Update Specific Category
        val db = this.writableDatabase
        val contentValues = ContentValues()
        val categoryId = userCategory.categoryId

        contentValues.apply {
            put(KEY_CATEGORY_NAME, userCategory.categoryName)
        }

        val success = db.update(
                TABLE_CATEGORIES,
                contentValues,
                "$KEY_CATEGORY_ID='$categoryId'",
                null
        )
        db.update(
                TABLE_PLATFORMS,
                contentValues,
                "$KEY_CATEGORY_ID='$categoryId'",
                null
        )

        db.close()
        return success
    }

    fun updatePlatform(userPlatform: UserPlatformModelClass): Int {                                 // Update Specific Platform
        val db = this.writableDatabase
        val contentValues = ContentValues()
        val platformId = userPlatform.platformId

        contentValues.apply {
            put(KEY_PLATFORM_NAME, userPlatform.platformName)
        }

        val success = db.update(
                TABLE_PLATFORMS,
                contentValues,
                "$KEY_PLATFORM_ID='$platformId'",
                null
        )
        db.update(
                TABLE_ACCOUNTS,
                contentValues,
                "$KEY_PLATFORM_ID='$platformId'",
                null
        )

        db.close()
        return success
    }

    fun updateIsFavorites(accountId: String, isFavorites: String): Int {                            // Update Specific Account
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.apply {
            put(KEY_ACCOUNT_IS_FAVORITES, isFavorites)
        }

        val success = db.update(
                TABLE_ACCOUNTS,
                contentValues,
                "$KEY_ACCOUNT_ID='$accountId'",
                null
        )

        db.close()
        return success
    }

    fun updateDeleteAccount(
            id: String,
            deleted: String,
            deleteDate: String,
            tableName: String,
            fieldId: String,
            fieldDelete: String,
            fieldDate: String
    ): Int {                                                                                        // Update Specific Account
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.apply {
            put(fieldDelete, deleted)
            put(fieldDate, deleteDate)
        }

        val success = db.update(
                tableName,
                contentValues,
                "$fieldId='$id'",
                null
        )

        db.close()
        return success
    }

    fun updateDeleteMultipleAccount(
            id: Array<String?>,
            deleted: String,
            deleteDate: String,
            tableName: String,
            fieldId: String,
            fieldDelete: String,
            fieldDate: String
    ): Int {                                                                                        // Update Specific Account
        val db = this.writableDatabase
        val contentValues = ContentValues()
        var questionMark = ""

        for (i in 1..id.size) {
            questionMark += "?,"
        }
        questionMark = questionMark.dropLast(1)

        contentValues.apply {
            put(fieldDelete, deleted)
            put(fieldDate, deleteDate)
        }

        val success = db.update(
                tableName,
                contentValues,
                "$fieldId IN ($questionMark)",
                id
        )

        db.close()
        return success
    }

    fun updateAccount(userAccount: UserAccountModelClass): Int {                                    // Update Specific Account
        val db = this.writableDatabase
        val contentValues = ContentValues()
        val accountId = userAccount.accountId

        contentValues.apply {
            put(KEY_ACCOUNT_NAME, userAccount.accountName)
            put(KEY_ACCOUNT_CREDENTIAL_FIELD, userAccount.accountCredentialField)
            put(KEY_ACCOUNT_CREDENTIAL, userAccount.accountCredential)
            put(KEY_ACCOUNT_PASSWORD, userAccount.accountPassword)
            put(KEY_ACCOUNT_WEBSITE_URL, userAccount.accountWebsiteURL)
            put(KEY_ACCOUNT_DESCRIPTION, userAccount.accountDescription)
            put(KEY_ACCOUNT_IS_FAVORITES, userAccount.accountIsFavorites)
            put(KEY_CREATION_DATE, userAccount.creationDate)
            put(KEY_PASSWORD_STATUS, userAccount.passwordStatus)
        }

        val success = db.update(
                TABLE_ACCOUNTS,
                contentValues,
                "$KEY_ACCOUNT_ID='$accountId'",
                null
        )

        db.close()
        return success
    }

    fun updateAccountPath(
            accountId: String,
            deleted: String,
            deleteDate: String,
            platformId: String,
            platformName: String,
            categoryName: String
    ): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.apply {
            put(KEY_ACCOUNT_DELETED, deleted)
            put(KEY_ACCOUNT_DELETE_DATE, deleteDate)
        }

        if (platformId.isNotEmpty()) {
            contentValues.put(KEY_PLATFORM_ID, platformId)
        }
        if (platformName.isNotEmpty()) {
            contentValues.put(KEY_PLATFORM_NAME, platformName)
        }
        if (categoryName.isNotEmpty()) {
            contentValues.put(KEY_CATEGORY_NAME, categoryName)
        }

        val success = db.update(
                TABLE_ACCOUNTS,
                contentValues,
                "$KEY_ACCOUNT_ID='$accountId'",
                null
        )

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
        db.delete(TABLE_ACTION_LOG, "", null)
        db.delete(TABLE_CATEGORIES, "", null)
        db.delete(TABLE_PLATFORMS, "", null)
        db.delete(TABLE_ACCOUNTS, "", null)
        db.delete(TABLE_SETTINGS, "", null)

        db.close()
        return success
    }

    fun removeCatPlatAcc(table: String, field: String, fieldId: String): Int {                      // Delete Category, Platform and Account
        val db = this.writableDatabase

        val success = db.delete(
                table,
                "$field='$fieldId'",
                null
        )

        db.close()
        return success
    }

    fun removeRecycleBin(id: Array<String?>, tableName: String, field: String): Int {               // Delete selected items from Recycle Bin
        val db = this.writableDatabase
        var questionMark = ""

        for (i in 1..id.size) {
            questionMark += "?,"
        }
        questionMark = questionMark.dropLast(1)

        val success = db.delete(
                tableName,
                "$field IN ($questionMark)",
                id
        )

        db.close()
        return success
    }
}