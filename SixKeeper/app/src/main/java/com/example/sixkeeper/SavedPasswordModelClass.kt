package com.example.sixkeeper

class SavedPasswordModelClass {
    private var isSelected = false

    fun getSelected(): Boolean {
        return isSelected
    }

    fun setSelected(selected: Boolean) {
        isSelected = selected
    }
}