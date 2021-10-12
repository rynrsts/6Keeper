package com.example.sixkeeper

class RecycleBinModelClass {
    private var isSelected = false
    private var id = 0

    private var accountName = ""
    private var platformName = ""
    private var categoryName = ""

    fun setSelected(selected: Boolean) {
        isSelected = selected
    }

    fun getSelected(): Boolean {
        return isSelected
    }

    fun setId(i: Int) {
        id = i
    }

    fun getId(): Int {
        return id
    }

    fun setAccountName(name: String) {
        accountName = name
    }

    fun getAccountName(): String {
        return accountName
    }

    fun setPlatformName(name: String) {
        platformName = name
    }

    fun getPlatformName(): String {
        return platformName
    }

    fun setCategoryName(name: String) {
        categoryName = name
    }

    fun getCategoryName(): String {
        return categoryName
    }
}