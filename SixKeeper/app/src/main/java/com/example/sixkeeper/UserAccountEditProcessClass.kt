package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.util.Patterns
import android.view.Gravity
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

open class UserAccountEditProcessClass : Fragment() {
    private val args: UserAccountEditFragmentArgs by navArgs()
    private lateinit var viewId: String
    private lateinit var appCompatActivity: AppCompatActivity

    private lateinit var attActivity: Activity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encryptionClass: EncryptionClass
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var clUserAccountEditButton: ConstraintLayout

    private lateinit var tvUserEditLabel: TextView
    private lateinit var etUserEditTextBox: EditText
    private lateinit var ivUserEditIcon: ImageView
    private lateinit var tvUserEditNote: TextView

    private lateinit var etUserEditCurrentPass: EditText
    private lateinit var etUserEditNewPass: EditText
    private lateinit var tvUserEditNewPassNote: TextView
    private lateinit var etUserEditConfirmPass: EditText
    private lateinit var tvUserEditConfirmPassNote: TextView

    private lateinit var ivUserEditCurrentTogglePass: ImageView
    private lateinit var ivUserEditNewTogglePass: ImageView
    private lateinit var ivUserEditConfirmTogglePass: ImageView

    private var previousData: String = ""
    private var editMode: Boolean = false
    private var editCount: Int = 0

    private lateinit var key: ByteArray
    private var userId: String = ""
    private var password = ""
    private var masterPin: Int = 0

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

    fun getViewId(): String {
        return viewId
    }

    fun getAppCompatActivity(): AppCompatActivity {
        return appCompatActivity
    }

    fun getEtUserEditCurrentPass(): EditText {
        return etUserEditCurrentPass
    }

    fun getEtUserEditNewPass(): EditText {
        return etUserEditNewPass
    }

    fun getEtUserEditConfirmPass(): EditText {
        return etUserEditConfirmPass
    }

    fun getIvUserEditCurrentTogglePass(): ImageView {
        return ivUserEditCurrentTogglePass
    }

    fun getIvUserEditNewTogglePass(): ImageView {
        return ivUserEditNewTogglePass
    }

    fun getIvUserEditConfirmTogglePass(): ImageView {
        return ivUserEditConfirmTogglePass
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {                                                    // Override back press
                if (editMode) {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(getAppCompatActivity())
                    builder.setMessage(R.string.user_edit_cancel_alert_mes)
                    builder.setCancelable(false)

                    builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                        etUserEditTextBox.setText(previousData)
                        goBackToViewMode()
                    }
                    builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                        dialog.cancel()
                    }

                    val alert: AlertDialog = builder.create()
                    alert.setTitle(R.string.many_alert_title)
                    alert.show()
                } else {
                    val controller = Navigation.findNavController(view!!)
                    controller.popBackStack(R.id.userAccountEditFragment, true)
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    fun setVariables() {
        viewId = args.userAccountEditId
        appCompatActivity = activity as AppCompatActivity

        databaseHandlerClass = DatabaseHandlerClass(attActivity)
        encryptionClass = EncryptionClass()
        firebaseDatabase = FirebaseDatabase.getInstance()

        val userAccList: List<UserAccModelClass> = databaseHandlerClass.validateUserAcc()

        for (u in userAccList) {
            userId = encryptionClass.decode(u.userId)
        }

        key = (userId + userId + userId.substring(0, 2)).toByteArray()
        databaseReference = firebaseDatabase.getReference(userId)
    }

    fun isFirstNameToUsername(): Boolean {
        return viewId == "first name" || viewId == "last name" || viewId == "birth date" ||
                viewId == "email" || viewId == "mobile number" || viewId == "username"
    }

    private fun isFirstNameToBirthDate(): Boolean {
        return viewId == "first name" || viewId == "last name" || viewId == "birth date"
    }

    private fun isEmailToUsername(): Boolean {
        return viewId == "email" || viewId == "mobile number" || viewId == "username"
    }

    fun setView1() {                                                                                // View for First Name to Username
        val clUserAccountEditContainer: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clUserAccountEditContainer)

        val layoutContainer = if (viewId == "mobile number") {
            layoutInflater.inflate(
                    R.layout.layout_user_edit_4, view as ViewGroup?, false
            )
        } else {
            layoutInflater.inflate(
                    R.layout.layout_user_edit_1, view as ViewGroup?, false
            )
        }
        clUserAccountEditContainer.addView(layoutContainer)

        tvUserEditLabel = appCompatActivity.findViewById(R.id.tvUserEditLabel)
        etUserEditTextBox = appCompatActivity.findViewById(R.id.etUserEditTextBox)
        ivUserEditIcon = appCompatActivity.findViewById(R.id.ivUserEditIcon)
        tvUserEditNote = appCompatActivity.findViewById(R.id.tvUserEditNote)
    }

    fun setInfoContent() {                                                                          // Set user information data
        val button = Button(appCompatActivity)
        var childRef = ""
        var countChildRef = ""
        var returnedValue = ""
        var returnedEditCount = ""
        var count = 0

        when (viewId) {
            "first name" -> {
                childRef = "firstName"
                countChildRef = "fnEditCount"
            }
            "last name" -> {
                childRef = "lastName"
                countChildRef = "lnEditCount"
            }
            "birth date" -> {
                childRef = "birthDate"
                countChildRef = "bdEditCount"
            }
            "email" -> {
                childRef = "email"
            }
            "mobile number" -> {
                childRef = "mobileNumber"
            }
            "username" -> {
                childRef = "username"
            }
        }

        if (viewId == "first name" || viewId == "last name" || viewId == "birth date") {
            val countChildReference = databaseReference.child(countChildRef)

            countChildReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val value = dataSnapshot.getValue(String::class.java).toString()
                    returnedEditCount = encryptionClass.decrypt(value, key)
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }

        val childReference = databaseReference.child(childRef)

        childReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(String::class.java).toString()
                returnedValue = encryptionClass.decrypt(value, key)
                count++

                if (count == 1) {
                    button.performClick()
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })

        button.setOnClickListener {
            if (returnedEditCount.isNotEmpty()) {
                editCount = Integer.parseInt(returnedEditCount)
            }

            etUserEditTextBox.setText(returnedValue)

            if (editCount == 0) {
                setViewButton()
            }

            setEditOnClick()
        }
    }

    fun setViewButton() {                                                                           // View for button in the bottom
        clUserAccountEditButton = appCompatActivity.findViewById(R.id.clUserAccountEditButton)
        val layoutButton = layoutInflater.inflate(
                R.layout.layout_user_edit_button_1, view as ViewGroup?, false
        )
        clUserAccountEditButton.addView(layoutButton)
    }

    private fun setEditOnClick() {
        when {
            isFirstNameToUsername() -> {
                if (isFirstNameToBirthDate() && editCount > 0) {
                    return
                }

                val clUserEditEdit: ConstraintLayout =
                        getAppCompatActivity().findViewById(R.id.clUserEditEdit)

                clUserEditEdit.setOnClickListener {
                    previousData = etUserEditTextBox.text.toString()

                    when (viewId) {
                        "first name" -> {
                            tvUserEditNote.setText(R.string.many_letters_only_message)
                            enterEditMode()
                        }
                        "last name" -> {
                            tvUserEditNote.setText(R.string.many_letters_only_message)
                            enterEditMode()
                        }
                        "birth date" -> {
                            tvUserEditNote.setText(R.string.many_birth_date_format_message)
                            enterEditMode()
                        }
                        "email" -> {
                            tvUserEditNote.setText(R.string.many_validate_email)
                            enterEditMode()
                        }
                        "mobile number" -> {
                            tvUserEditNote.setText(R.string.many_validate_mobile_num)
                            enterEditMode()
                        }
                        "username" -> {
                            tvUserEditNote.setText(R.string.many_validate_username)
                            enterEditMode()
                        }
                    }
                }
            }
            viewId == "password" -> {
                setEditModeOnClick()
            }
            viewId == "master pin" -> {
                val acbUserEditMasterPIN: Button =
                        appCompatActivity.findViewById(R.id.acbUserEditMasterPIN)

                acbUserEditMasterPIN.setOnClickListener {
                    val goToCreateMasterPINActivity = Intent(
                            appCompatActivity,
                            CreateMasterPINActivity::class.java
                    )

                    @Suppress("DEPRECATION")
                    startActivityForResult(goToCreateMasterPINActivity, 14523)

                    getAppCompatActivity().overridePendingTransition(
                            R.anim.anim_enter_bottom_to_top_2, R.anim.anim_0
                    )

                    it.apply {
                        acbUserEditMasterPIN.isClickable = false                                    // Set button un-clickable for 1 second
                        postDelayed(
                                {
                                    acbUserEditMasterPIN.isClickable = true
                                }, 1000
                        )
                    }
                }
            }
        }
    }

    private fun enterEditMode() {                                                                   // Enter edit mode
        etUserEditTextBox.apply {
            setBackgroundResource(R.drawable.layout_edit_text)
            isFocusableInTouchMode = true
            requestFocus()
            setSelection(etUserEditTextBox.text.toString().length)
        }

        val layoutButton = layoutInflater.inflate(
                R.layout.layout_user_edit_button_2, view as ViewGroup?, false
        )
        clUserAccountEditButton.apply {
            removeAllViews()
            addView(layoutButton)
        }

        editMode = true
        setEditModeOnClick()
    }

    private fun setEditModeOnClick() {
        val clUserEditCancel: ConstraintLayout =
                getAppCompatActivity().findViewById(R.id.clUserEditCancel)
        val clUserEditSave: ConstraintLayout =
                getAppCompatActivity().findViewById(R.id.clUserEditSave)

        clUserEditCancel.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(getAppCompatActivity())
            builder.setMessage(R.string.user_edit_cancel_alert_mes)
            builder.setCancelable(false)

            builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                if (isFirstNameToUsername()) {
                    etUserEditTextBox.setText(previousData)
                    goBackToViewMode()
                } else if (viewId == "password") {
                    appCompatActivity.onBackPressed()
                }
            }
            builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                dialog.cancel()
            }

            val alert: AlertDialog = builder.create()
            alert.setTitle(R.string.many_alert_title)
            alert.show()

            it.apply {
                clUserEditCancel.isClickable = false                                                // Set un-clickable for 1 second
                postDelayed(
                        {
                            clUserEditCancel.isClickable = true
                        }, 1000
                )
            }
        }

        clUserEditSave.setOnClickListener {
            if (isFirstNameToUsername()) {
                val text = etUserEditTextBox.text.toString()

                if (
                        (viewId == "first name" && isNameValid(text)) ||
                        (viewId == "last name" && isNameValid(text)) ||
                        (viewId == "birth date" && isBirthDateValid(text)) ||
                        (viewId == "email" && isEmailValid(text)) ||
                        (viewId == "mobile number" && text.length == 10) ||
                        (viewId == "username" && isUsernameValid(text))
                ) {
                    val input = etUserEditTextBox.text.toString()

                    if (input != previousData) {
                        showSaveAlertDialog()
                    } else {
                        val toast: Toast = Toast.makeText(
                                appCompatActivity.applicationContext,
                                R.string.user_edit_new_previous_mes, Toast.LENGTH_SHORT
                        )
                        toast.apply {
                            setGravity(Gravity.CENTER, 0, 0)
                            show()
                        }
                    }

                    it.apply {
                        clUserEditSave.isClickable = false                                          // Set un-clickable for 1 second
                        postDelayed({ clUserEditSave.isClickable = true }, 1000)
                    }
                }
            } else if (viewId == "password") {
                val currentPass = etUserEditCurrentPass.text.toString()
                val newPass = etUserEditNewPass.text.toString()
                val confirmPass = etUserEditConfirmPass.text.toString()

                val encryptedNewPass = encryptionClass.encrypt(newPass, key)

                val usernameRef = databaseReference.child("username")
                var usernameVal = ""
                var count = 0
                val button = Button(appCompatActivity)

                usernameRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        usernameVal = dataSnapshot.getValue(String::class.java).toString()
                        count++

                        if (count == 1) {
                            button.performClick()
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })

                button.setOnClickListener {
                    if (
                            isPasswordValid(newPass) && confirmPass == newPass &&
                            currentPass != newPass && usernameVal != encryptedNewPass
                    ) {
                        showSaveAlertDialog()

                        clUserEditSave.apply {
                            clUserEditSave.isClickable = false                                      // Set un-clickable for 1 second
                            postDelayed({ clUserEditSave.isClickable = true }, 1000)
                        }
                    } else {
                        if (confirmPass == newPass) {
                            tvUserEditConfirmPassNote.text = ""
                        } else if (confirmPass != newPass) {
                            tvUserEditConfirmPassNote.setText(R.string.many_validate_confirm_pass)
                        }

                        if (
                                isPasswordValid(newPass) && confirmPass == newPass &&
                                currentPass == newPass
                        ) {
                            val toast: Toast = Toast.makeText(
                                    appCompatActivity.applicationContext,
                                    R.string.user_edit_pass_not_the_same, Toast.LENGTH_SHORT
                            )
                            toast.apply {
                                setGravity(Gravity.CENTER, 0, 0)
                                show()
                            }
                        } else if (
                                isPasswordValid(newPass) && confirmPass == newPass &&
                                currentPass != newPass && usernameVal == encryptedNewPass
                        ) {
                            val toast: Toast = Toast.makeText(
                                    appCompatActivity.applicationContext,
                                    R.string.user_edit_password_mes_2, Toast.LENGTH_SHORT
                            )
                            toast.apply {
                                setGravity(Gravity.CENTER, 0, 0)
                                show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun isNameValid(s: String): Boolean {                                                   // Accept letters, (.) and (-) only
        val exp = "[a-zA-Z .-]+"
        val pattern: Pattern = Pattern.compile(exp)
        return pattern.matcher(s).matches()
    }

    private fun isBirthDateValid(s: String): Boolean {                                              // Accept MM//DD/YYYY format only
        val exp = "^(0[0-9]|1[0-2])/([0-2][0-9]|3[0-1])/(19[5-9][0-9]|20[0-1][0-9])$"
        val pattern: Pattern = Pattern.compile(exp)
        return pattern.matcher(s).matches()
    }

    private fun isEmailValid(s: String): Boolean {                                                  // Accept valid email
        return Patterns.EMAIL_ADDRESS.matcher(s).matches()
    }

    private fun isUsernameValid(s: String): Boolean {                                               // Accept letters, numbers, (.), (_) and (-) only
        val exp = "[a-zA-Z0-9._-]{6,}"
        val pattern: Pattern = Pattern.compile(exp)
        return pattern.matcher(s).matches()
    }

    private fun goBackToViewMode() {                                                                // Go back to view mode
        etUserEditTextBox.apply {
            setBackgroundResource(R.drawable.layout_edit_text_2)
            isFocusableInTouchMode = false
            clearFocus()
        }
        tvUserEditNote.text = null

        if (editCount == 0) {
            val layoutButton = layoutInflater.inflate(
                    R.layout.layout_user_edit_button_1, view as ViewGroup?, false
            )
            clUserAccountEditButton.apply {
                removeAllViews()
                addView(layoutButton)
            }

            setEditOnClick()
        } else {
            clUserAccountEditButton.removeAllViews()
        }

        val immKeyboard: InputMethodManager =
                appCompatActivity.getSystemService(Context.INPUT_METHOD_SERVICE)
                        as InputMethodManager
        when {
            immKeyboard.isActive ->
                immKeyboard.hideSoftInputFromWindow(                                                // Close keyboard
                        appCompatActivity.currentFocus?.windowToken, 0
                )
        }

        editMode = false
    }

    private fun showSaveAlertDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(getAppCompatActivity())

        if (isFirstNameToBirthDate()) {
            builder.setMessage(R.string.user_edit_save_alert_mes_2)
        } else if (isEmailToUsername() || viewId == "password" || viewId == "master pin") {
            builder.setMessage(R.string.user_edit_save_alert_mes)
        }

        builder.setCancelable(false)

        builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            val goToConfirmActivity = Intent(
                    appCompatActivity, ConfirmActionActivity::class.java
            )

            @Suppress("DEPRECATION")
            startActivityForResult(goToConfirmActivity, 16914)
            appCompatActivity.overridePendingTransition(
                    R.anim.anim_enter_bottom_to_top_2, R.anim.anim_0
            )
        }
        builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
            dialog.cancel()
        }

        val alert: AlertDialog = builder.create()
        alert.setTitle(R.string.many_alert_title_confirm)
        alert.show()
    }

    private fun isPasswordValid(s: String): Boolean {                                               // Accept 1 lowercase, uppercase, number, (.), (_) and (-) only
        val exp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9])(?=.*[._-])(?=\\S+\$)(?=.{8,})(^[a-zA-Z0-9._-]+\$)"
        val pattern = Pattern.compile(exp)
        return pattern.matcher(s).matches()
    }

    fun setFirstName() {                                                                            // View for First Name
        tvUserEditLabel.setText(R.string.many_first_name)
        etUserEditTextBox.apply {
            inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            filters = arrayOf<InputFilter>(InputFilter.LengthFilter(30))
        }
        ivUserEditIcon.setImageResource(R.drawable.ic_person_gray)
    }

    fun setLastName() {                                                                             // View for Last Name
        tvUserEditLabel.setText(R.string.many_last_name)
        etUserEditTextBox.apply {
            inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            filters = arrayOf<InputFilter>(InputFilter.LengthFilter(30))
        }
        ivUserEditIcon.setImageResource(R.drawable.ic_person_gray)
    }

    fun setBirthDate() {                                                                            // View for Birth Date
        tvUserEditLabel.setText(R.string.many_birth_date)
        etUserEditTextBox.apply {
            inputType = InputType.TYPE_CLASS_TEXT
            filters = arrayOf<InputFilter>(InputFilter.LengthFilter(10))
        }
        ivUserEditIcon.setImageResource(R.drawable.ic_date_range_gray)
    }

    fun setEmail() {                                                                                // View for Email
        tvUserEditLabel.setText(R.string.many_email)
        etUserEditTextBox.apply {
            inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            filters = arrayOf<InputFilter>(InputFilter.LengthFilter(60))
        }
        ivUserEditIcon.setImageResource(R.drawable.ic_email_gray)
    }

    fun setMobileNumber() {                                                                         // View for Mobile Number
        tvUserEditLabel.setText(R.string.many_mobile_number)
        etUserEditTextBox.apply {
            inputType = InputType.TYPE_CLASS_NUMBER
            filters = arrayOf<InputFilter>(InputFilter.LengthFilter(10))
        }
        ivUserEditIcon.setImageResource(R.drawable.ic_phone_gray)
    }

    fun setUsername() {                                                                             // View for Username
        tvUserEditLabel.setText(R.string.many_username)
        etUserEditTextBox.apply {
            inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            filters = arrayOf<InputFilter>(InputFilter.LengthFilter(30))
        }
        ivUserEditIcon.setImageResource(R.drawable.ic_person_gray)
    }

    fun setView2() {                                                                                // View for Password
        val clUserAccountEditContainer: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clUserAccountEditContainer)
        val layoutContainer = layoutInflater.inflate(
                R.layout.layout_user_edit_2, view as ViewGroup?, false
        )
        clUserAccountEditContainer.addView(layoutContainer)

        clUserAccountEditButton = appCompatActivity.findViewById(R.id.clUserAccountEditButton)
        val layoutButton = layoutInflater.inflate(
                R.layout.layout_user_edit_button_2, view as ViewGroup?, false
        )
        clUserAccountEditButton.addView(layoutButton)

        etUserEditCurrentPass = appCompatActivity.findViewById(R.id.etUserEditCurrentPass)
        etUserEditNewPass = appCompatActivity.findViewById(R.id.etUserEditNewPass)
        tvUserEditNewPassNote = appCompatActivity.findViewById(R.id.tvUserEditNewPassNote)
        etUserEditConfirmPass = appCompatActivity.findViewById(R.id.etUserEditConfirmPass)
        tvUserEditConfirmPassNote = appCompatActivity.findViewById(R.id.tvUserEditConfirmPassNote)

        ivUserEditCurrentTogglePass =
                appCompatActivity.findViewById(R.id.ivUserEditCurrentTogglePass)
        ivUserEditNewTogglePass = appCompatActivity.findViewById(R.id.ivUserEditNewTogglePass)
        ivUserEditConfirmTogglePass =
                appCompatActivity.findViewById(R.id.ivUserEditConfirmTogglePass)

        getPassword()
        setEditOnClick()
    }

    private fun getPassword() {
        val passwordRef = databaseReference.child("password")

        passwordRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                password = dataSnapshot.getValue(String::class.java).toString()
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun setView3() {                                                                                // View for Master PIN
        val clUserAccountEditContainer: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clUserAccountEditContainer)
        val layoutContainer = layoutInflater.inflate(
                R.layout.layout_user_edit_3, view as ViewGroup?, false
        )
        clUserAccountEditContainer.addView(layoutContainer)

        setEditOnClick()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 14523 && resultCode == 14523) {
            if (viewId == "master pin") {
                masterPin = data?.getIntExtra("masterPin", 0)!!
                showSaveAlertDialog()
            }
        }

        if (requestCode == 16914 && resultCode == 16914) {                                          // If Master PIN is correct
            val button = Button(appCompatActivity)

            when (viewId) {
                "first name" -> {
                    updateInfo("firstName")
                    updateEditCount("fnEditCount")
                    goBackToViewMode()
                    changesSavedToast()
                }
                "last name" -> {
                    updateInfo("lastName")
                    updateEditCount("lnEditCount")
                    goBackToViewMode()
                    changesSavedToast()
                }
                "birth date" -> {
                    updateInfo("birthDate")
                    updateEditCount("bdEditCount")
                    goBackToViewMode()
                    changesSavedToast()
                }
                "email" -> {
                    updateInfo("email")
                    goBackToViewMode()
                    changesSavedToast()
                }
                "mobile number" -> {
                    updateInfo("mobileNumber")
                    goBackToViewMode()
                    changesSavedToast()
                }
                "username" -> {
                    val passwordRef = databaseReference.child("password")
                    var passwordVal = ""
                    var count = 0
                    val input = etUserEditTextBox.text.toString()
                    val encryptedInput = encryptionClass.encrypt(input, key)
                    val hashedInput = encryptionClass.hash(input)

                    passwordRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            passwordVal = dataSnapshot.getValue(String::class.java).toString()
                            count++

                            if (count == 1) {
                                button.performClick()
                            }
                        }
                        override fun onCancelled(databaseError: DatabaseError) {}
                    })

                    button.setOnClickListener {
                        if (!passwordVal.contentEquals(hashedInput)) {
                            updateAccUsername(input, encryptedInput)
                            goBackToViewMode()
                            setUsernameInMenu(input)
                            changesSavedToast()
                        } else {
                            val toast: Toast = Toast.makeText(
                                    appCompatActivity.applicationContext,
                                    R.string.user_edit_username_mes,
                                    Toast.LENGTH_SHORT
                            )
                            toast.apply {
                                setGravity(Gravity.CENTER, 0, 0)
                                show()
                            }
                        }
                    }
                }
                "password" -> {
                    val currentPass = etUserEditCurrentPass.text.toString()
                    val encryptedCurrentPass = encryptionClass.hash(currentPass)

                    if (encryptedCurrentPass.contentEquals(password)) {
                        updateAccPassword()
                        view?.apply {
                            postDelayed(
                                    {
                                        appCompatActivity.onBackPressed()
                                    }, 250
                            )
                        }
                        changesSavedToast()
                    } else {
                        val toast: Toast = Toast.makeText(
                                appCompatActivity.applicationContext,
                                R.string.user_edit_pass_mes, Toast.LENGTH_SHORT
                        )
                        toast.apply {
                            setGravity(Gravity.CENTER, 0, 0)
                            show()
                        }
                    }
                }
                "master pin" -> {
                    val masterPinRef = databaseReference.child("masterPin")
                    var masterPinVal = ""
                    var count = 0
                    val encryptedInput = encryptionClass.hash(masterPin.toString())

                    masterPinRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            masterPinVal = dataSnapshot.getValue(String::class.java).toString()
                            count++

                            if (count == 1) {
                                button.performClick()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })

                    button.setOnClickListener {
                        if (!masterPinVal.contentEquals(encryptedInput)) {
                            updateAccMasterPIN(encryptedInput)
                            changesSavedToast()
                            view?.apply {
                                postDelayed(
                                        {
                                            appCompatActivity.onBackPressed()
                                        }, 250
                                )
                            }
                        } else {
                            val toast: Toast = Toast.makeText(
                                    appCompatActivity.applicationContext,
                                    R.string.user_edit_master_pin_mes,
                                    Toast.LENGTH_SHORT
                            )
                            toast.apply {
                                setGravity(Gravity.CENTER, 0, 0)
                                show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateInfo(field: String) {                                                         // Update desired information
        databaseHandlerClass.addEventToActionLog(                                                   // Add event to Action Log
                UserActionLogModelClass(
                        encryptionClass.encrypt(getLastActionLogId().toString(), key),
                        encryptionClass.encrypt("App account $viewId was modified.", key),
                        encryptionClass.encrypt(getCurrentDate(), key)
                )
        )

        val input = etUserEditTextBox.text.toString()
        val encryptedInput = encryptionClass.encrypt(input, key)

        databaseReference.child(field).setValue(encryptedInput)
    }

    private fun getLastActionLogId(): Int {
        var actionLogId = 1000001
        val lastId = databaseHandlerClass.getLastIdOfActionLog()

        if (lastId.isNotEmpty()) {
            actionLogId = Integer.parseInt(encryptionClass.decrypt(lastId, key)) + 1
        }

        return actionLogId
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate(): String {
        val calendar: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
        return dateFormat.format(calendar.time)
    }

    private fun updateEditCount(field: String) {                                                    // Update edit count
        editCount += 1
        clUserAccountEditButton.removeAllViews()

        val encryptedInput = encryptionClass.encrypt(editCount.toString(), key)

        databaseReference.child(field).setValue(encryptedInput)
    }

    private fun updateAccUsername(input: String, encryptedInput: String) {                            // Update Username
        databaseHandlerClass.addEventToActionLog(                                                   // Add event to Action Log
                UserActionLogModelClass(
                        encryptionClass.encrypt(getLastActionLogId().toString(), key),
                        encryptionClass.encrypt("App account username '$previousData'" +
                                " was modified to '$input'.", key),
                        encryptionClass.encrypt(getCurrentDate(), key)
                )
        )

        databaseReference.child("username").setValue(encryptedInput)
    }

    private fun setUsernameInMenu(input: String) {                                                  // Set update username in menu
        val navigationView: NavigationView =
                appCompatActivity.findViewById(R.id.nvIndexNavigationView)
        val headerView = navigationView.getHeaderView(0)
        val tvNavigationHeaderUsername: TextView =
                headerView.findViewById(R.id.tvNavigationHeaderUsername)

        tvNavigationHeaderUsername.text = input
    }

    private fun updateAccPassword() {                                                               // Update Password
        val input = etUserEditNewPass.text.toString()
        val encryptedInput =  encryptionClass.hash(input)

        databaseHandlerClass.addEventToActionLog(                                                   // Add event to Action Log
                UserActionLogModelClass(
                        encryptionClass.encrypt(getLastActionLogId().toString(), key),
                        encryptionClass.encrypt("App account password was modified.", key),
                        encryptionClass.encrypt(getCurrentDate(), key)
                )
        )

        databaseReference.child("password").setValue(encryptedInput)
    }

    private fun updateAccMasterPIN(inputString: String) {                                           // Update Master PIN
        databaseHandlerClass.addEventToActionLog(                                                   // Add event to Action Log
                UserActionLogModelClass(
                        encryptionClass.encrypt(getLastActionLogId().toString(), key),
                        encryptionClass.encrypt("App account master pin was modified.", key),
                        encryptionClass.encrypt(getCurrentDate(), key)
                )
        )

        databaseReference.child("masterPin").setValue(inputString)
    }

     private fun changesSavedToast() {
         val toast: Toast = Toast.makeText(
                 appCompatActivity.applicationContext,
                 R.string.many_changes_saved,
                 Toast.LENGTH_SHORT
         )
         toast.apply {
             setGravity(Gravity.CENTER, 0, 0)
             show()
         }
     }
}