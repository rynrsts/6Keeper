package com.example.sixkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.text.InputFilter
import android.text.InputType
import android.util.Patterns
import android.view.Gravity
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

open class UserAccountEditProcessClass : Fragment() {
    private val args: UserAccountEditFragmentArgs by navArgs()
    private lateinit var viewId: String
    private lateinit var appCompatActivity: AppCompatActivity

    private lateinit var attActivity: Activity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encodingClass: EncodingClass
    private lateinit var encryptionClass: EncryptionClass

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

    private var masterPin: Int = 0

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

    fun setVariables() {
        viewId = args.userAccountEditId
        appCompatActivity = activity as AppCompatActivity

        databaseHandlerClass = DatabaseHandlerClass(attActivity)
        encodingClass = EncodingClass()
        encryptionClass = EncryptionClass()
    }

    fun getViewId(): String {
        return viewId
    }

    fun getAppCompatActivity(): AppCompatActivity {
        return appCompatActivity
    }

    fun isFirstNameToUsername(): Boolean {
        return viewId == "first name" || viewId == "last name" || viewId == "birth date" ||
                viewId == "email" || viewId == "mobile number" || viewId == "username"
    }

    private fun isEmailToUsername(): Boolean {
        return viewId == "email" || viewId == "mobile number" || viewId == "username"
    }

    fun setView1() {                                                                                // View for First Name to Username
        val clUserAccountEditContainer: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clUserAccountEditContainer)

        val layoutContainer = if (viewId == "mobile number") {
            layoutInflater.inflate(
                    R.layout.layout_user_edit_4, view as ViewGroup?,  false
            )
        } else {
            layoutInflater.inflate(
                    R.layout.layout_user_edit_1, view as ViewGroup?,  false
            )
        }
        clUserAccountEditContainer.addView(layoutContainer)

        tvUserEditLabel = appCompatActivity.findViewById(R.id.tvUserEditLabel)
        etUserEditTextBox = appCompatActivity.findViewById(R.id.etUserEditTextBox)
        ivUserEditIcon = appCompatActivity.findViewById(R.id.ivUserEditIcon)
        tvUserEditNote = appCompatActivity.findViewById(R.id.tvUserEditNote)
    }

    fun setViewButton() {                                                                           // View for button in the bottom
        clUserAccountEditButton = appCompatActivity.findViewById(R.id.clUserAccountEditButton)
        val layoutButton = layoutInflater.inflate(
                R.layout.layout_user_edit_button_1, view as ViewGroup?, false
        )
        clUserAccountEditButton.addView(layoutButton)
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

    fun setInfoContent() {                                                                          // Set user information data
        etUserEditTextBox.setText(viewUserInformation())
    }

    private fun viewUserInformation(): String {                                                     // View desired user information
        val userInfoList: List<UserInfoModelClass> = databaseHandlerClass.viewUserInfo()
        val userUsername: String = databaseHandlerClass.viewUsername()
        var returnValue = ""

        var firstName = ""
        var lastName = ""
        var birthDate = ""
        var email = ""
        var mobileNumber = ""
        val username = encodingClass.decodeData(userUsername)

        for (u in userInfoList) {
            firstName = encodingClass.decodeData(u.firstName)
            lastName = encodingClass.decodeData(u.lastName)
            birthDate = encodingClass.decodeData(u.birthDate)
            email = encodingClass.decodeData(u.email)
            mobileNumber = encodingClass.decodeData(u.mobileNumber)
        }

        when (viewId) {
            "first name" ->
                returnValue = firstName
            "last name" ->
                returnValue = lastName
            "birth date" ->
                returnValue = birthDate
            "email" ->
                returnValue = email
            "mobile number" ->
                returnValue = mobileNumber
            "username" ->
                returnValue = username
        }

        return returnValue
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

    fun setView3() {                                                                                // View for Master PIN
        val clUserAccountEditContainer: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clUserAccountEditContainer)
        val layoutContainer = layoutInflater.inflate(
                R.layout.layout_user_edit_3, view as ViewGroup?, false
        )
        clUserAccountEditContainer.addView(layoutContainer)
    }

    fun setEditOnClick() {
        when {
            isEmailToUsername() -> {
                val clUserEditEdit: ConstraintLayout =
                        getAppCompatActivity().findViewById(R.id.clUserEditEdit)

                clUserEditEdit.setOnClickListener {
                    previousData = etUserEditTextBox.text.toString()

                    when (viewId) {
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
                            R.anim.anim_enter_bottom_to_top_2,
                            R.anim.anim_0
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
                if (isEmailToUsername()) {
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
            if (isEmailToUsername()) {
                val text = etUserEditTextBox.text.toString()

                if (
                        (viewId == "email" && isEmailValid(text)) ||
                        (viewId == "mobile number" && text.length == 10) ||
                        (viewId == "username" && isUsernameValid(text))
                ) {
                    showSaveAlertDialog()

                    it.apply {
                        clUserEditSave.isClickable = false                                          // Set un-clickable for 1 second
                        postDelayed(
                                {
                                    clUserEditSave.isClickable = true
                                }, 1000
                        )
                    }
                }
            } else if (viewId == "password") {
                val currentPass = etUserEditCurrentPass.text.toString()
                val newPass = etUserEditNewPass.text.toString()
                val confirmPass = etUserEditConfirmPass.text.toString()

                if (
                        isPasswordValid(newPass) && confirmPass == newPass &&
                        validateUserAccPass(currentPass)
                ) {
                    showSaveAlertDialog()

                    it.apply {
                        clUserEditSave.isClickable = false                                          // Set un-clickable for 1 second
                        postDelayed(
                                {
                                    clUserEditSave.isClickable = true
                                }, 1000
                        )
                    }
                } else {
                    if (confirmPass == newPass) {
                        tvUserEditConfirmPassNote.text = ""
                    } else if (confirmPass != newPass) {
                        tvUserEditConfirmPassNote.setText(R.string.many_validate_confirm_pass)
                    }

                    if (
                            isPasswordValid(newPass) && confirmPass == newPass &&
                            !validateUserAccPass(currentPass)
                    ) {
                        val toast: Toast = Toast.makeText(
                                appCompatActivity.applicationContext,
                                R.string.user_edit_pass_mes,
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

    private fun showSaveAlertDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(getAppCompatActivity())
        builder.setMessage(R.string.user_edit_save_alert_mes)
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
            when (viewId) {
                "email" -> {
                    updateInfo("email")
                    setInfoContent()
                    goBackToViewMode()
                }
                "mobile number" -> {
                    updateInfo("mobile_number")
                    setInfoContent()
                    goBackToViewMode()
                }
                "username" -> {
                    updateAccUsername()
                    setInfoContent()
                    goBackToViewMode()
                    setUsernameInMenu()
                }
                "password" -> {
                    updateAccPassword()
                    view?.apply {
                        postDelayed(
                                {
                                    appCompatActivity.onBackPressed()
                                }, 250
                        )
                    }
                }
                "master pin" -> {
                    updateAccMasterPIN()
                    view?.apply {
                        postDelayed(
                                {
                                    appCompatActivity.onBackPressed()
                                }, 250
                        )
                    }
                }
            }

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

//    TODO: Back press on edit mode
//    override fun onCreate(savedInstanceState: Bundle?) {                                            // To override onBackPressed in fragment
//        super.onCreate(savedInstanceState)
//        activity?.onBackPressedDispatcher?.addCallback(
//                this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                if (editMode) {
//                    val builder: AlertDialog.Builder = AlertDialog.Builder(getAppCompatActivity())
//                    builder.setMessage(R.string.user_edit_cancel_alert_mes)
//                    builder.setCancelable(false)
//
//                    builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
//                        etUserEditTextBox.setText(previousData)
//                        goBackToViewMode()
//                    }
//                    builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
//                        dialog.cancel()
//                    }
//
//                    val alert: AlertDialog = builder.create()
//                    alert.setTitle(R.string.many_alert_title)
//                    alert.show()
//                }
//            }
//        })
//    }

    private fun isEmailValid(s: String): Boolean {                                                  // Accept valid email
        return Patterns.EMAIL_ADDRESS.matcher(s).matches()
    }

    private fun isUsernameValid(s: String): Boolean {                                               // Accept letters, numbers, (.), (_) and (-) only
        val exp = "[a-zA-Z0-9._-]{6,}"
        val pattern: Pattern = Pattern.compile(exp)
        return pattern.matcher(s).matches()
    }

    private fun validateUserAccPass(pass: String): Boolean {
        val userAccList: List<UserAccModelClass> = databaseHandlerClass.validateUserAcc()
        var bool = false

        val encodedPassword = encodingClass.encodeData(pass)
        val encryptedPassword = encryptionClass.hashData(encodedPassword)

        for (u in userAccList) {
            bool = encryptedPassword.contentEquals(u.password)
        }

        return bool
    }

    private fun isPasswordValid(s: String): Boolean {                                               // Accept 1 lowercase, uppercase, number, (.), (_) and (-) only
        val exp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9])(?=.*[._-])(?=\\S+\$)(?=.{8,})(^[a-zA-Z0-9._-]+\$)"
        val pattern = Pattern.compile(exp)
        return pattern.matcher(s).matches()
    }

    private fun updateInfo(field: String) {                                                         // Update desired information
        val input = etUserEditTextBox.text.toString()
        databaseHandlerClass.updateUserInfo(
                field, encodingClass.encodeData(input), getCurrentDate()
        )
    }

    private fun updateAccUsername() {                                                               // Update Username
        val input = etUserEditTextBox.text.toString()

        if (viewId == "username") {
            databaseHandlerClass.updateUserUsername(
                    encodingClass.encodeData(input), getCurrentDate()
            )
        }
    }

    private fun updateAccPassword() {                                                               // Update Password
        val input = etUserEditNewPass.text.toString()
        val encodedInput = encodingClass.encodeData(input)

        databaseHandlerClass.updateUserAcc(
                "password", encryptionClass.hashData(encodedInput), getCurrentDate()
        )
    }

    private fun updateAccMasterPIN() {                                                              // Update Master PIN
        val encodedInput = encodingClass.encodeData(masterPin.toString())

        databaseHandlerClass.updateUserAcc(
                "master_pin", encryptionClass.hashData(encodedInput), getCurrentDate()
        )
    }
    
    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate(): String {
        val calendar: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
        return dateFormat.format(calendar.time)
    }

    private fun goBackToViewMode() {                                                                // Go back to view mode
        etUserEditTextBox.apply {
            setBackgroundResource(R.drawable.layout_edit_text_2)
            isFocusableInTouchMode = false
            clearFocus()
        }
        tvUserEditNote.text = null

        val layoutButton = layoutInflater.inflate(
                R.layout.layout_user_edit_button_1, view as ViewGroup?, false
        )
        clUserAccountEditButton.apply {
            removeAllViews()
            addView(layoutButton)
        }

        val immKeyboard: InputMethodManager =
                appCompatActivity.getSystemService(Context.INPUT_METHOD_SERVICE)
                        as InputMethodManager
        when {
            immKeyboard.isActive ->
                immKeyboard.hideSoftInputFromWindow(                                                // Close keyboard
                        appCompatActivity.currentFocus?.windowToken,
                        0
                )
        }

        editMode = false
        setEditOnClick()
    }

    private fun setUsernameInMenu() {                                                               // Set update username in menu
        val userAccList: List<UserAccModelClass> = databaseHandlerClass.validateUserAcc()
        var username = ""

        for (u in userAccList) {
            username = encodingClass.decodeData(u.username)
        }

        val navigationView: NavigationView =
                appCompatActivity.findViewById(R.id.nvIndexNavigationView)
        val headerView = navigationView.getHeaderView(0)
        val tvNavigationHeaderUsername: TextView =
                headerView.findViewById(R.id.tvNavigationHeaderUsername)

        tvNavigationHeaderUsername.text = username
    }
}