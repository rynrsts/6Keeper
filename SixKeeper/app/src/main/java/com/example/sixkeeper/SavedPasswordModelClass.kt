package com.example.sixkeeper

class SavedPasswordModelClass {
    private var isSelected = false
    private var id = 0
    private var password = ""

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

    fun setPassword(pw: String) {
        password = pw
    }

    fun getPassword(): String {
        return password
    }
}