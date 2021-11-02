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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
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
    private lateinit var encodingClass: EncodingClass

    private lateinit var ivUserAccountPhoto: ImageView

    private var field = ""

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
        encodingClass = EncodingClass()
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
        val profilePhoto = databaseHandlerClass.viewProfilePhoto()

        if (profilePhoto.contentEquals("".toByteArray())) {
            ivUserAccountPhoto.setImageDrawable(null)
        } else {
            val imageDrawable: Drawable = BitmapDrawable(
                    resources,
                    BitmapFactory.decodeByteArray(
                            profilePhoto, 0, profilePhoto.size
                    )
            )

            ivUserAccountPhoto.setImageDrawable(imageDrawable)
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
        val clUserAccountExportData: ConstraintLayout =
                appCompatActivity.findViewById(R.id.clUserAccountExportData)

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
                    val tvProfilePhotoAdd: TextView = dialogView.findViewById(R.id.tvProfilePhotoAdd)
                    val llProfilePhotoRemove: LinearLayout =
                            dialogView.findViewById(R.id.llProfilePhotoRemove)
                    val profilePhoto = databaseHandlerClass.viewProfilePhoto()

                    if (profilePhoto.contentEquals("".toByteArray())) {
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
                            (activity as IndexActivity).setTimer(30)

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
                            updateProfilePhoto("removed!", "".toByteArray())
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
                            val backupDBPath = "/SixKeeper/SixKeeperDatabase"
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
                                                encodingClass.encodeData(getLastActionLogId().toString()),
                                                encodingClass.encodeData("Data was exported to the " +
                                                        "'SixKeeper' folder in the internal storage."),
                                                encodingClass.encodeData(getCurrentDate())
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
            requestCode == 135491 && resultCode == Activity.RESULT_OK -> {                          // If image was selected
                val selectedImage: Uri? = data?.data

                val contentResolver = appCompatActivity.contentResolver
                val type = contentResolver.getType(selectedImage!!)

                if (type!!.startsWith("image")) {                                                   // If selected is image
                    val imageByteArray = appCompatActivity.contentResolver.openInputStream(
                            selectedImage
                    )?.readBytes()

                    if (imageByteArray!!.size <= 1992294) {                                         // If image is less than 2 MB
                        val imageDrawable: Drawable = BitmapDrawable(
                                resources,
                                BitmapFactory.decodeByteArray(
                                        imageByteArray, 0, imageByteArray.size
                                )
                        )
                        val profilePhoto = databaseHandlerClass.viewProfilePhoto()

                        ivUserAccountPhoto.setImageDrawable(imageDrawable)

                        if (profilePhoto.contentEquals("".toByteArray())) {
                            updateProfilePhoto("added", imageByteArray)
                            addEventToActionLog("added")
                        } else {
                            updateProfilePhoto("modified", imageByteArray)
                            addEventToActionLog("modified")
                        }

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

    private fun updateProfilePhoto(action: String, image: ByteArray) {
        val profileStatus = databaseHandlerClass.updateProfilePhoto(                                // Update Profile Photo
                image
        )
        val message = "Profile Photo was $action!"

        if (profileStatus > -1) {
            val toast: Toast = Toast.makeText(
                    appCompatActivity, message, Toast.LENGTH_SHORT
            )
            toast.apply {
                setGravity(Gravity.CENTER, 0, 0)
                show()
            }
        }
    }

    private fun addEventToActionLog(action: String) {
        databaseHandlerClass.addEventToActionLog(                                                   // Add event to Action Log
                UserActionLogModelClass(
                        encodingClass.encodeData(getLastActionLogId().toString()),
                        encodingClass.encodeData("Profile Photo was $action."),
                        encodingClass.encodeData(getCurrentDate())
                )
        )
    }

    private fun getLastActionLogId(): Int {
        var actionLogId = 1000001
        val lastId = databaseHandlerClass.getLastIdOfActionLog()

        if (lastId.isNotEmpty()) {
            actionLogId = Integer.parseInt(encodingClass.decodeData(lastId)) + 1
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