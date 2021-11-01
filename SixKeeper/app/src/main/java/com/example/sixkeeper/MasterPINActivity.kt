@file:Suppress("DEPRECATION")

package com.example.sixkeeper

import android.Manifest
import android.app.KeyguardManager
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.lang.NullPointerException
import java.security.*
import java.security.cert.CertificateException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey

class MasterPINActivity : MasterPINProcessClass() {
    private val keyName = "yourKey"
    private lateinit var cipher: Cipher
    private lateinit var keyStore: KeyStore
    private lateinit var keyGenerator: KeyGenerator
    private lateinit var cryptoObject: FingerprintManager.CryptoObject
    private lateinit var fingerprintManager: FingerprintManager
    private lateinit var keyguardManager: KeyguardManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master_p_i_n)

        changeStatusBarColor()
        setVariables()
        fingerprint()
        blockCapture()
        setButtonOnClick()
    }

    override fun onStart() {
        super.onStart()
        fingerprint()
    }

    private fun fingerprint() {                                                                     // Fingerprint code start
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
                fingerprintManager = getSystemService(FINGERPRINT_SERVICE) as FingerprintManager

                if (!fingerprintManager.isHardwareDetected) {
                    setFingerprintOff()
                    return
                }

                if (
                        ActivityCompat.checkSelfPermission(
                                this,
                                Manifest.permission.USE_FINGERPRINT
                        ) != PackageManager.PERMISSION_GRANTED
                ) {
                    setFingerprintOff()
                    return
                }

                if (!fingerprintManager.hasEnrolledFingerprints()) {
                    setFingerprintOff()
                    return
                }

                if (!keyguardManager.isKeyguardSecure) {
                    setFingerprintOff()
                } else {
                    val ivMasterPINFingerprintIcon: ImageView =
                            findViewById(R.id.ivMasterPINFingerprintIcon)

                    if (getFingerprintStatus() == 1) {
                        ivMasterPINFingerprintIcon.setImageResource(R.drawable.ic_fingerprint_green)

                        ivMasterPINFingerprintIcon.setOnClickListener {
                            val toast: Toast = Toast.makeText(
                                    applicationContext,
                                    R.string.many_fingerprint_available,
                                    Toast.LENGTH_SHORT
                            )
                            toast.apply {
                                setGravity(Gravity.CENTER, 0, 0)
                                show()
                            }
                        }

                        try {
                            generateKey()
                        } catch (e: FingerprintException) {
                            e.printStackTrace()
                        }

                        if (initCipher()) {
                            cryptoObject = FingerprintManager.CryptoObject(cipher)
                            val helper = FingerprintHandlerClass(this, "login")
                            helper.startAuth(fingerprintManager, cryptoObject)
                        }
                    } else {
                        ivMasterPINFingerprintIcon.isEnabled = false
                    }
                }
            } catch (e: NullPointerException) {
                setFingerprintOff()
            }
        } else {
            setFingerprintOff()
        }
    }

    @Throws(FingerprintException::class)
    private fun generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                "AndroidKeyStore"
            )
            keyStore.load(null)
            keyGenerator.init(
                KeyGenParameterSpec.Builder(
                    keyName,
                    KeyProperties.PURPOSE_ENCRYPT or  KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build()
            )
            keyGenerator.generateKey()
        } catch (e: KeyStoreException) {
            e.printStackTrace()
            throw FingerprintException(e)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            throw FingerprintException(e)
        } catch (e: NoSuchProviderException) {
            e.printStackTrace()
            throw FingerprintException(e)
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
            throw FingerprintException(e)
        } catch (e: CertificateException) {
            e.printStackTrace()
            throw FingerprintException(e)
        } catch (e: IOException) {
            e.printStackTrace()
            throw FingerprintException(e)
        }
    }

    private fun initCipher(): Boolean {
        try {
            cipher = Cipher.getInstance(
                KeyProperties.KEY_ALGORITHM_AES + "/" +
                        KeyProperties.BLOCK_MODE_CBC + "/" +
                        KeyProperties.ENCRYPTION_PADDING_PKCS7
            )
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to get Cipher", e)
        } catch (e: NoSuchPaddingException) {
            throw RuntimeException("Failed to get Cipher", e)
        }

        try {
            keyStore.load(null)
            val key: SecretKey = keyStore.getKey(keyName, null) as SecretKey
            cipher.init(Cipher.ENCRYPT_MODE, key)

            return true
        } catch (e: KeyPermanentlyInvalidatedException) {
            return false
        } catch (e: KeyStoreException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: CertificateException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: UnrecoverableKeyException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: IOException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: InvalidKeyException) {
            throw RuntimeException("Failed to init Cipher", e)
        }
    }

    private class FingerprintException(e: Exception?) : Exception(e)                                // Fingerprint code end

    private fun setButtonOnClick() {
        val llMasterPinLogout: LinearLayout = findViewById(R.id.llMasterPinLogout)
        val tvMasterPINForgotPass: TextView = findViewById(R.id.tvMasterPINForgotPass)

        getAcbMasterPINButton1().setOnClickListener {
            if (!locked()) {
                pushNumber(1, it)
            }
        }

        getAcbMasterPINButton2().setOnClickListener {
            if (!locked()) {
                pushNumber(2, it)
            }
        }

        getAcbMasterPINButton3().setOnClickListener {
            if (!locked()) {
                pushNumber(3, it)
            }
        }

        getAcbMasterPINButton4().setOnClickListener {
            if (!locked()) {
                pushNumber(4, it)
            }
        }

        getAcbMasterPINButton5().setOnClickListener {
            if (!locked()) {
                pushNumber(5, it)
            }
        }

        getAcbMasterPINButton6().setOnClickListener {
            if (!locked()) {
                pushNumber(6, it)
            }
        }

        getAcbMasterPINButton7().setOnClickListener {
            if (!locked()) {
                pushNumber(7, it)
            }
        }

        getAcbMasterPINButton8().setOnClickListener {
            if (!locked()) {
                pushNumber(8, it)
            }
        }

        getAcbMasterPINButton9().setOnClickListener {
            if (!locked()) {
                pushNumber(9, it)
            }
        }

        getAcbMasterPINButton0().setOnClickListener {
            if (!locked()) {
                pushNumber(0, it)
            }
        }

        getAcbMasterPINButtonDelete().setOnClickListener {
            if (!locked()) {
                if (getPin().size > 0) {
                    unShadePin()
                    getPin().pop()
                }
            }
        }

        getAcbMasterPINButtonCancel().setOnClickListener {
            it.apply {
                getAcbMasterPINButtonCancel().isClickable = false                                   // Set button un-clickable for 1 second
                postDelayed(
                        {
                            getAcbMasterPINButtonCancel().isClickable = true
                        }, 1000
                )
            }

            onBackPressed()
        }

        llMasterPinLogout.setOnClickListener {
            it.apply {
                llMasterPinLogout.isClickable = false                                               // Set button un-clickable for 1 second
                postDelayed(
                        {
                            llMasterPinLogout.isClickable = true
                        }, 1000
                )
            }

            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setMessage(R.string.many_logout_mes)
            builder.setCancelable(false)

            builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                updateUserStatus()
                updateLastLogin()
                goToLoginActivity()
            }
            builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                dialog.cancel()
            }

            val alert: AlertDialog = builder.create()
            alert.setTitle(R.string.many_alert_title_confirm)
            alert.show()
        }

        tvMasterPINForgotPass.setOnClickListener {
            val goToForgotCredentialsActivity =
                    Intent(this, ForgotCredentialsActivity::class.java)
            goToForgotCredentialsActivity.putExtra("credential", "master pin")

            startActivity(goToForgotCredentialsActivity)
            overridePendingTransition(
                    R.anim.anim_enter_right_to_left_2,
                    R.anim.anim_exit_right_to_left_2
            )

            it.apply {
                tvMasterPINForgotPass.isClickable = false                                           // Set button un-clickable for 1 second
                postDelayed(
                        {
                            tvMasterPINForgotPass.isClickable = true
                        }, 1000
                )
            }
        }
    }
}