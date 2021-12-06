package com.example.sixkeeper

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.*

class UserAccountFragment : Fragment() {
    private lateinit var attActivity: Activity
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var databaseHandlerClass: DatabaseHandlerClass
    private lateinit var encryptionClass: EncryptionClass
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userAccList: List<UserAccModelClass>

    private lateinit var ivUserAccountPhoto: ImageView

    private lateinit var button: Button

    private var field = ""
    private var userId: String = ""

    private lateinit var profilePhotoB: ByteArray

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setVariables()
        disableHeaderItem()
        closeKeyboard()
        setProfilePhoto()
        setOnClick()
    }

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activity
    }

    private fun setVariables() {
        appCompatActivity = activity as AppCompatActivity
        ivUserAccountPhoto = appCompatActivity.findViewById(R.id.ivUserAccountPhoto)

        databaseHandlerClass = DatabaseHandlerClass(attActivity)
        encryptionClass = EncryptionClass()
        firebaseDatabase = FirebaseDatabase.getInstance()

        button = Button(appCompatActivity)

        userAccList = databaseHandlerClass.validateUserAcc()

        for (u in userAccList) {
            userId = encryptionClass.decode(u.userId)
        }

        databaseReference = firebaseDatabase.getReference(userId)
    }

    private fun disableHeaderItem() {
        val navigationView: NavigationView =
                appCompatActivity.findViewById(R.id.nvIndexNavigationView)
        val headerView = navigationView.getHeaderView(0)
        val clNavigationHeader: ConstraintLayout = headerView.findViewById(R.id.clNavigationHeader)

        clNavigationHeader.isEnabled = false
        navigationView.menu.findItem(R.id.dashboardFragment).isEnabled = true
        navigationView.menu.findItem(R.id.accountsFragment).isEnabled = true
        navigationView.menu.findItem(R.id.favoritesFragment).isEnabled = true
        navigationView.menu.findItem(R.id.passwordGeneratorFragment).isEnabled = true
        navigationView.menu.findItem(R.id.recycleBinFragment).isEnabled = true
        navigationView.menu.findItem(R.id.settingsFragment).isEnabled = true
        navigationView.menu.findItem(R.id.aboutUsFragment).isEnabled = true
        navigationView.menu.findItem(R.id.termsConditionsFragment).isEnabled = true
        navigationView.menu.findItem(R.id.privacyPolicyFragment).isEnabled = true
        navigationView.menu.findItem(R.id.logoutFragment).isEnabled = true
    }

    private fun closeKeyboard() {
        val immKeyboard: InputMethodManager =
                appCompatActivity.getSystemService(Context.INPUT_METHOD_SERVICE)
                        as InputMethodManager

        if (immKeyboard.isActive) {
            immKeyboard.hideSoftInputFromWindow(                                                    // Close keyboard
                    appCompatActivity.currentFocus?.windowToken, 0
            )
        }
    }

    private fun setProfilePhoto() {
        val profilePhotoRef = databaseReference.child("profilePhoto")
        var profilePhoto = ""
        var count = 0

        profilePhotoRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                profilePhoto = dataSnapshot.getValue(String::class.java).toString()
                count++

                if (count == 1) {
                    button.performClick()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        button.setOnClickListener {
            profilePhotoB = encryptionClass.decodeBA(profilePhoto)

            if (profilePhotoB.contentEquals("".toByteArray())) {
                ivUserAccountPhoto.setImageDrawable(null)
            } else {
                val imageDrawable: Drawable = BitmapDrawable(
                        resources,
                        BitmapFactory.decodeByteArray(
                                profilePhotoB, 0, profilePhotoB.size
                        )
                )

                ivUserAccountPhoto.setImageDrawable(imageDrawable)
            }

            photoOnClick()
        }
    }

    @SuppressLint("InflateParams")
    private fun photoOnClick() {
        ivUserAccountPhoto.setOnClickListener {
            if (                                                                                    // Check if permission is granted
                    ActivityCompat.checkSelfPermission(
                            appCompatActivity, Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                            appCompatActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (InternetConnectionClass().isConnected()) {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(appCompatActivity)
                    val inflater = this.layoutInflater
                    val dialogView = inflater.inflate(
                            R.layout.layout_user_account_profile_photo, null
                    )
                    val removeProfilePhoto = inflater.inflate(
                            R.layout.layout_user_account_remove_photo, null
                    )
                    val llProfilePhotoAdd: LinearLayout =
                            dialogView.findViewById(R.id.llProfilePhotoAdd)
                    val tvProfilePhotoAdd: TextView =
                            dialogView.findViewById(R.id.tvProfilePhotoAdd)
                    val llProfilePhotoRemove: LinearLayout =
                            dialogView.findViewById(R.id.llProfilePhotoRemove)

                    if (profilePhotoB.contentEquals("".toByteArray())) {
                        tvProfilePhotoAdd.setText(R.string.user_change_profile_photo)
                    } else {
                        llProfilePhotoRemove.addView(removeProfilePhoto)
                        tvProfilePhotoAdd.setText(R.string.user_add_profile_photo)
                    }

                    builder.setView(dialogView)

                    val alert: AlertDialog = builder.create()
                    alert.apply {
                        window?.setBackgroundDrawable(
                                ContextCompat.getDrawable(context, R.drawable.layout_alert_dialog)
                        )
                        setTitle(R.string.user_profile_photo)
                        show()
                    }

                    llProfilePhotoAdd.setOnClickListener {
                        if (InternetConnectionClass().isConnected()) {
                            (activity as IndexActivity).setTimer(60)

                            val mediaStorage = Intent(
                                    Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )

                            @Suppress("DEPRECATION")
                            startActivityForResult(mediaStorage, 135491)
                        } else {
                            internetToast()
                        }

                        alert.cancel()
                    }

                    llProfilePhotoRemove.setOnClickListener {
                        if (InternetConnectionClass().isConnected()) {
                            ivUserAccountPhoto.setImageDrawable(null)
                            databaseReference.child("profilePhoto").setValue("")
                            profilePhotoB = "".toByteArray()
                            addEventToActionLog("removed")
                            setProfilePhotoInMenu(null)
                        } else {
                            internetToast()
                        }

                        alert.cancel()
                    }
                } else {
                    internetToast()
                }
            } else {
                ActivityCompat.requestPermissions(
                        appCompatActivity,
                        arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ), 52420
                )
            }
        }
    }

    @SuppressLint("SimpleDateFormat", "InflateParams")
    private fun setOnClick() {
        val clUserAccountFirstName: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clUserAccountFirstName)
        val clUserAccountLastName: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clUserAccountLastName)
        val clUserAccountBirthDate: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clUserAccountBirthDate)
        val clUserAccountEmail: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clUserAccountEmail)
        val clUserAccountMobileNum: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clUserAccountMobileNum)
        val clUserAccountUsername: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clUserAccountUsername)
        val clUserAccountPassword: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clUserAccountPassword)
        val clUserAccountMasterPIN: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clUserAccountMasterPIN)
        val clUserAccountDeleteAccount: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clUserAccountDeleteAccount)
        val clUserAccountExportData: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clUserAccountExportData)

        clUserAccountFirstName.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                field = "first name"
                val action = UserAccountFragmentDirections
                        .actionUserAccountFragmentToUserAccountEditFragment(field)
                findNavController().navigate(action)
            } else {
                internetToast()
            }
        }

        clUserAccountLastName.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                field = "last name"
                val action = UserAccountFragmentDirections
                        .actionUserAccountFragmentToUserAccountEditFragment(field)
                findNavController().navigate(action)
            } else {
                internetToast()
            }
        }

        clUserAccountBirthDate.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                field = "birth date"
                val action = UserAccountFragmentDirections
                        .actionUserAccountFragmentToUserAccountEditFragment(field)
                findNavController().navigate(action)
            } else {
                internetToast()
            }
        }

        clUserAccountEmail.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                field = "email"
                val action = UserAccountFragmentDirections
                        .actionUserAccountFragmentToUserAccountEditFragment(field)
                findNavController().navigate(action)
            } else {
                internetToast()
            }
        }

        clUserAccountMobileNum.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                field = "mobile number"
                val action = UserAccountFragmentDirections
                        .actionUserAccountFragmentToUserAccountEditFragment(field)
                findNavController().navigate(action)
            } else {
                internetToast()
            }
        }

        clUserAccountUsername.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                field = "username"
                val action = UserAccountFragmentDirections
                        .actionUserAccountFragmentToUserAccountEditFragment(field)
                findNavController().navigate(action)
            } else {
                internetToast()
            }
        }

        clUserAccountPassword.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                field = "password"
                val action = UserAccountFragmentDirections
                        .actionUserAccountFragmentToUserAccountEditFragment(field)
                findNavController().navigate(action)
            } else {
                internetToast()
            }
        }

        clUserAccountMasterPIN.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                field = "master pin"
                val action = UserAccountFragmentDirections
                        .actionUserAccountFragmentToUserAccountEditFragment(field)
                findNavController().navigate(action)
            } else {
                internetToast()
            }
        }

        clUserAccountDeleteAccount.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(appCompatActivity)
            builder.setMessage(R.string.user_deactivate_mes)
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
            alert.setTitle(R.string.many_alert_title)
            alert.show()
        }

        clUserAccountExportData.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(                                                 // Check if permission is granted
                            appCompatActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (InternetConnectionClass().isConnected()) {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(appCompatActivity)
                    builder.setMessage("Export data to 'SixKeeper' folder in the phone storage?")
                    builder.setCancelable(false)

                    builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                        if (InternetConnectionClass().isConnected()) {
                            @Suppress("DEPRECATION")
                            val directory = File(                                                   // Create folder if not existing
                                    Environment.getExternalStorageDirectory(), "/SixKeeper"
                            )
                            directory.mkdirs()

                            @Suppress("DEPRECATION")
                            val sd = Environment.getExternalStorageDirectory()
                            val data = Environment.getDataDirectory()
                            val source: FileChannel?
                            val destination: FileChannel?
                            val packageName = context?.packageName
                            val currentDBPath = "/data/$packageName/databases/SixKeeperDatabase"
                            val backupDBPath = "/SixKeeper/SixKeeperDatabase.skdb"
                            val currentDB = File(data, currentDBPath)
                            val backupDB = File(sd, backupDBPath)

                            try {
                                source = FileInputStream(currentDB).channel
                                destination = FileOutputStream(backupDB).channel
                                destination.transferFrom(source, 0, source.size())          // Save data to folder
                                source.close()
                                destination.close()

                                showToast("Data was exported to the 'SixKeeper' folder in the " +
                                        "internal storage!")

                                databaseHandlerClass.addEventToActionLog(                           // Add event to Action Log
                                        UserActionLogModelClass(
                                                encryptionClass.encrypt(
                                                        getLastActionLogId().toString(), userId
                                                ),
                                                encryptionClass.encrypt(
                                                        "Data was exported to the " +
                                                                "'SixKeeper' folder in the " +
                                                                "internal storage.", userId
                                                ),
                                                encryptionClass.encrypt(getCurrentDate(), userId)
                                        )
                                )
                            } catch (e: IOException) {
                                showToast("Cannot find folder")
                            }
                        } else {
                            internetToast()
                        }
                    }
                    builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                        dialog.cancel()
                    }

                    val alert: AlertDialog = builder.create()
                    alert.setTitle(R.string.many_alert_title_confirm)
                    alert.show()
                } else {
                    internetToast()
                }
            } else {
                ActivityCompat.requestPermissions(                                                  // Request permission
                        appCompatActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        52420
                )
            }

            it.apply {
                clUserAccountExportData.isClickable = false                                         // Set un-clickable for 1 second
                postDelayed({ clUserAccountExportData.isClickable = true }, 500)
            }
        }
    }

    private fun internetToast() {
        val toast: Toast = Toast.makeText(
                appCompatActivity.applicationContext,
                R.string.many_internet_connection,
                Toast.LENGTH_SHORT
        )
        toast.apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }

    @SuppressLint("Recycle")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == 16914 && resultCode == 16914 -> {                                        // If Master PIN is correct
                view?.apply {
                    postDelayed(
                            {
                                val goToConfirmWithCredentialsActivity = Intent(
                                        appCompatActivity,
                                        ConfirmWithCredentialsActivity::class.java
                                )

                                @Suppress("DEPRECATION")
                                startActivityForResult(
                                        goToConfirmWithCredentialsActivity, 451320
                                )
                                appCompatActivity.overridePendingTransition(
                                        R.anim.anim_enter_bottom_to_top_2, R.anim.anim_0
                                )
                            }, 250
                    )
                }
            }
            requestCode == 451320 && resultCode == 451320 -> {                                      // If Confirm With Credentials
                val firebaseUserAccountModelClass = FirebaseUserAccountModelClass()
                val newDatabaseReference = firebaseDatabase.getReference("D-$userId")
                var count = 0
                val button = Button(appCompatActivity)

                val usernameRef = databaseReference.child("username")
                val passwordRef = databaseReference.child("password")
                val masterPinRef = databaseReference.child("masterPin")
                val statusRef = databaseReference.child("status")
                val firstNameRef = databaseReference.child("firstName")
                val lastNameRef = databaseReference.child("lastName")
                val birthDateRef = databaseReference.child("birthDate")
                val emailRef = databaseReference.child("email")
                val mobileNumberRef = databaseReference.child("mobileNumber")
                val profilePhotoRef = databaseReference.child("profilePhoto")
                val pwWrongAttemptRef = databaseReference.child("pwWrongAttempt")
                val pwLockTimeRef = databaseReference.child("pwLockTime")
                val mpinWrongAttemptRef = databaseReference.child("mpinWrongAttempt")
                val fwrongAttempteRef = databaseReference.child("fwrongAttempt")
                val mpinLockTimeRef = databaseReference.child("mpinLockTime")
                val fnEditCountRef = databaseReference.child("fnEditCount")
                val lnEditCountRef = databaseReference.child("lnEditCount")
                val bdEditCountRef = databaseReference.child("bdEditCount")

                usernameRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val value = dataSnapshot.getValue(String::class.java).toString()
                        firebaseUserAccountModelClass.setUsername(value)
                        count++

                        button.performClick()
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })

                passwordRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val value = dataSnapshot.getValue(String::class.java).toString()
                        firebaseUserAccountModelClass.setPassword(value)
                        count++

                        button.performClick()
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })

                masterPinRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val value = dataSnapshot.getValue(String::class.java).toString()
                        firebaseUserAccountModelClass.setMasterPin(value)
                        count++

                        button.performClick()
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })

                statusRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val value = dataSnapshot.getValue(String::class.java).toString()
                        firebaseUserAccountModelClass.setStatus(value)
                        count++

                        button.performClick()
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })

                firstNameRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val value = dataSnapshot.getValue(String::class.java).toString()
                        firebaseUserAccountModelClass.setFirstName(value)
                        count++

                        button.performClick()
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })

                lastNameRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val value = dataSnapshot.getValue(String::class.java).toString()
                        firebaseUserAccountModelClass.setLastName(value)
                        count++

                        button.performClick()
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })

                birthDateRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val value = dataSnapshot.getValue(String::class.java).toString()
                        firebaseUserAccountModelClass.setBirthDate(value)
                        count++

                        button.performClick()
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })

                emailRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val value = dataSnapshot.getValue(String::class.java).toString()
                        firebaseUserAccountModelClass.setEmail(value)
                        count++

                        button.performClick()
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })

                mobileNumberRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val value = dataSnapshot.getValue(String::class.java).toString()
                        firebaseUserAccountModelClass.setMobileNumber(value)
                        count++

                        button.performClick()
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })

                profilePhotoRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val value = dataSnapshot.getValue(String::class.java).toString()
                        firebaseUserAccountModelClass.setProfilePhoto(value)
                        count++

                        button.performClick()
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })

                pwWrongAttemptRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val value = dataSnapshot.getValue(String::class.java).toString()
                        firebaseUserAccountModelClass.setPwWrongAttempt(value)
                        count++

                        button.performClick()
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })

                pwLockTimeRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val value = dataSnapshot.getValue(String::class.java).toString()
                        firebaseUserAccountModelClass.setPwLockTime(value)
                        count++

                        button.performClick()
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })

                mpinWrongAttemptRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val value = dataSnapshot.getValue(String::class.java).toString()
                        firebaseUserAccountModelClass.setMPinWrongAttempt(value)
                        count++

                        button.performClick()
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })

                fwrongAttempteRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val value = dataSnapshot.getValue(String::class.java).toString()
                        firebaseUserAccountModelClass.setFWrongAttempt(value)
                        count++

                        button.performClick()
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })

                mpinLockTimeRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val value = dataSnapshot.getValue(String::class.java).toString()
                        firebaseUserAccountModelClass.setMPinLockTime(value)
                        count++

                        button.performClick()
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })

                fnEditCountRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val value = dataSnapshot.getValue(String::class.java).toString()
                        firebaseUserAccountModelClass.setFnEditCount(value)
                        count++

                        button.performClick()
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })

                lnEditCountRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val value = dataSnapshot.getValue(String::class.java).toString()
                        firebaseUserAccountModelClass.setLnEditCount(value)
                        count++

                        button.performClick()
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })

                bdEditCountRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val value = dataSnapshot.getValue(String::class.java).toString()
                        firebaseUserAccountModelClass.setBdEditCount(value)
                        count++

                        button.performClick()
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })

                button.setOnClickListener {
                    if (count == 18) {
                        newDatabaseReference.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                newDatabaseReference.setValue(firebaseUserAccountModelClass)
                                databaseReference.removeValue()

                                databaseHandlerClass.truncateAllTables()

                                appCompatActivity.finish()
                            }
                            override fun onCancelled(error: DatabaseError) {}
                        })
                    }
                }
            }
            requestCode == 135491 && resultCode == Activity.RESULT_OK -> {                          // If image was selected
                val selectedImage: Uri? = data?.data

                val contentResolver = appCompatActivity.contentResolver
                val type = contentResolver.getType(selectedImage!!)

                if (type!!.startsWith("image")) {                                                   // If selected is image
                    val imageByteArray = appCompatActivity.contentResolver.openInputStream(
                            selectedImage
                    )?.readBytes()

                    if (imageByteArray!!.size <= 1900000) {                                         // If image is less than 2 MB
                        val imageDrawable: Drawable = BitmapDrawable(
                                resources,
                                BitmapFactory.decodeByteArray(
                                        imageByteArray, 0, imageByteArray.size
                                )
                        )
                        val imageString = encryptionClass.encodeS(imageByteArray)

                        ivUserAccountPhoto.setImageDrawable(imageDrawable)

                        if (profilePhotoB.contentEquals("".toByteArray())) {
                            addEventToActionLog("added")
                        } else {
                            addEventToActionLog("modified")
                        }

                        profilePhotoB = imageByteArray
                        databaseReference.child("profilePhoto").setValue(imageString)

                        setProfilePhotoInMenu(imageDrawable)
                    } else {
                        val toast: Toast = Toast.makeText(
                                appCompatActivity, R.string.user_image_size, Toast.LENGTH_SHORT
                        )
                        toast.apply {
                            setGravity(Gravity.CENTER, 0, 0)
                            show()
                        }
                    }
                } else {
                    val toast: Toast = Toast.makeText(
                            appCompatActivity, R.string.user_not_image, Toast.LENGTH_SHORT
                    )
                    toast.apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }
                }
            }
        }
    }

    private fun addEventToActionLog(action: String) {
        databaseHandlerClass.addEventToActionLog(                                                   // Add event to Action Log
                UserActionLogModelClass(
                        encryptionClass.encrypt(getLastActionLogId().toString(), userId),
                        encryptionClass.encrypt("Profile Photo was $action.", userId),
                        encryptionClass.encrypt(getCurrentDate(), userId)
                )
        )

        val message = "Profile Photo was $action!"

        val toast: Toast = Toast.makeText(
                appCompatActivity, message, Toast.LENGTH_SHORT
        )
        toast.apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }

    private fun getLastActionLogId(): Int {
        var actionLogId = 1000001
        val lastId = databaseHandlerClass.getLastIdOfActionLog()

        if (lastId.isNotEmpty()) {
            actionLogId = Integer.parseInt(encryptionClass.decrypt(lastId, userId)) + 1
        }

        return actionLogId
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate(): String {
        val calendar: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM-dd-yyyy HH:mm:ss")
        return dateFormat.format(calendar.time)
    }

    private fun setProfilePhotoInMenu(image: Drawable?) {
        val navigationView: NavigationView =
                appCompatActivity.findViewById(R.id.nvIndexNavigationView)
        val headerView = navigationView.getHeaderView(0)
        val ivNavigationHeaderPhoto: ImageView =
                headerView.findViewById(R.id.ivNavigationHeaderPhoto)

        ivNavigationHeaderPhoto.setImageDrawable(image)                                             // Set Profile Photo in menu
    }

    private fun showToast(message: String) {
        val toast: Toast = Toast.makeText(appCompatActivity, message, Toast.LENGTH_SHORT)
        toast.apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }
}