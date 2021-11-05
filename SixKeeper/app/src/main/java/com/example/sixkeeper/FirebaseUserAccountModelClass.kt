package com.example.sixkeeper

class FirebaseUserAccountModelClass {
    private var username: String? = null
    private var password: String? = null
    private var masterPin: String? = null
    private var status: String? = null
    private var firstName: String? = null
    private var lastName: String? = null
    private var birthDate: String? = null
    private var email: String? = null
    private var mobileNumber: String? = null
    private var profilePhoto: String? = null
    private var pwWrongAttempt: String? = null
    private var pwLockTime: String? = null
    private var mPinWrongAttempt: String? = null
    private var fWrongAttempt: String? = null
    private var mPinLockTime: String? = null
    private var fnEditCount: String? = null
    private var lnEditCount: String? = null
    private var bdEditCount: String? = null

    fun setUsername(s: String?) {
        username = s
    }

    fun getUsername(): String? {
        return username
    }

    fun setPassword(s: String?) {
        password = s
    }

    fun getPassword(): String? {
        return password
    }

    fun setMasterPin(s: String?) {
        masterPin = s
    }

    fun getMasterPin(): String? {
        return masterPin
    }

    fun setStatus(s: String?) {
        status = s
    }

    fun getStatus(): String? {
        return status
    }

    fun setFirstName(s: String?) {
        firstName = s
    }

    fun getFirstName(): String? {
        return firstName
    }

    fun setLastName(s: String?) {
        lastName = s
    }

    fun getLastName(): String? {
        return lastName
    }

    fun setBirthDate(s: String?) {
        birthDate = s
    }

    fun getBirthDate(): String? {
        return birthDate
    }

    fun setEmail(s: String?) {
        email = s
    }

    fun getEmail(): String? {
        return email
    }

    fun setMobileNumber(s: String?) {
        mobileNumber = s
    }

    fun getMobileNumber(): String? {
        return mobileNumber
    }

    fun setProfilePhoto(s: String?) {
        profilePhoto = s
    }

    fun getProfilePhoto(): String? {
        return profilePhoto
    }

    fun setPwWrongAttempt(s: String?) {
        pwWrongAttempt = s
    }

    fun getPwWrongAttempt(): String? {
        return pwWrongAttempt
    }

    fun setPwLockTime(s: String?) {
        pwLockTime = s
    }

    fun getPwLockTime(): String? {
        return pwLockTime
    }

    fun setMPinWrongAttempt(s: String?) {
        mPinWrongAttempt = s
    }

    fun getMPinWrongAttempt(): String? {
        return mPinWrongAttempt
    }

    fun setFWrongAttempt(s: String?) {
        fWrongAttempt = s
    }

    fun getFWrongAttempt(): String? {
        return fWrongAttempt
    }

    fun setMPinLockTime(s: String?) {
        mPinLockTime = s
    }

    fun getMPinLockTime(): String? {
        return mPinLockTime
    }

    fun setFnEditCount(s: String?) {
        fnEditCount = s
    }

    fun getFnEditCount(): String? {
        return fnEditCount
    }

    fun setLnEditCount(s: String?) {
        lnEditCount = s
    }

    fun getLnEditCount(): String? {
        return lnEditCount
    }

    fun setBdEditCount(s: String?) {
        bdEditCount = s
    }

    fun getBdEditCount(): String? {
        return bdEditCount
    }
}