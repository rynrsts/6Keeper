@file:Suppress("DEPRECATION")

package com.example.sixkeeper

import android.Manifest
import android.app.KeyguardManager
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.lang.NullPointerException
import java.security.*
import java.security.cert.CertificateException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey

class AutoLockLoginActivity : AutoLockLoginProcessClass() {
    private val keyName = "yourKey"
    private lateinit var cipher: Cipher
    private lateinit var keyStore: KeyStore
    private lateinit var keyGenerator: KeyGenerator
    private lateinit var cryptoObject: FingerprintManager.CryptoObject
    private lateinit var fingerprintManager: FingerprintManager
    private lateinit var keyguardManager: KeyguardManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_lock_login)

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
        fingerprintHandlerClass = FingerprintHandlerClass(this, "auto lock")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
                fingerprintManager = getSystemService(FINGERPRINT_SERVICE) as FingerprintManager

                if (!fingerprintManager.isHardwareDetected) {
                    return
                }

                if (
                        ActivityCompat.checkSelfPermission(
                                this,
                                Manifest.permission.USE_FINGERPRINT
                        ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }

                if (!fingerprintManager.hasEnrolledFingerprints()) {
                    return
                }

                if (!keyguardManager.isKeyguardSecure) {
                    return
                } else {
                    val ivAutoLockFingerprintIcon: ImageView =
                            findViewById(R.id.ivAutoLockFingerprintIcon)

                    if (getFingerprintStatus() == 1) {
                        ivAutoLockFingerprintIcon.setImageResource(R.drawable.ic_fingerprint_green)

                        ivAutoLockFingerprintIcon.setOnClickListener {
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
                            fingerprintHandlerClass.startAuth(fingerprintManager, cryptoObject)
                        }
                    } else {
                        ivAutoLockFingerprintIcon.isEnabled = false
                    }
                }
            } catch (e: NullPointerException) {
                return
            }
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
                            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
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
        val tvAutoLockLoginForgotPass: TextView = findViewById(R.id.tvAutoLockLoginForgotPass)

        getAcbAutoLockLoginButton1().setOnClickListener {
            clickEffect(it)
            pushNumber(1, it)
        }

        getAcbAutoLockLoginButton2().setOnClickListener {
            clickEffect(it)
            pushNumber(2, it)
        }

        getAcbAutoLockLoginButton3().setOnClickListener {
            clickEffect(it)
            pushNumber(3, it)
        }

        getAcbAutoLockLoginButton4().setOnClickListener {
            clickEffect(it)
            pushNumber(4, it)
        }

        getAcbAutoLockLoginButton5().setOnClickListener {
            clickEffect(it)
            pushNumber(5, it)
        }

        getAcbAutoLockLoginButton6().setOnClickListener {
            clickEffect(it)
            pushNumber(6, it)
        }

        getAcbAutoLockLoginButton7().setOnClickListener {
            clickEffect(it)
            pushNumber(7, it)
        }

        getAcbAutoLockLoginButton8().setOnClickListener {
            clickEffect(it)
            pushNumber(8, it)
        }

        getAcbAutoLockLoginButton9().setOnClickListener {
            clickEffect(it)
            pushNumber(9, it)
        }

        getAcbAutoLockLoginButton0().setOnClickListener {
            clickEffect(it)
            pushNumber(0, it)
        }

        getAcbAutoLockLoginButtonDelete().setOnClickListener {
            clickEffect(it)

            if (getPin().size > 0) {
                unShadePin()
                getPin().pop()
            }
        }

        getAcbAutoLockLoginButtonCancel().setOnClickListener {
            clickEffect(it)

            it.apply {
                getAcbAutoLockLoginButtonCancel().isClickable = false                               // Set button un-clickable for 1 second
                postDelayed(
                        {
                            getAcbAutoLockLoginButtonCancel().isClickable = true
                        }, 1000
                )
            }

            onBackPressed()
            fingerprintHandlerClass.stopFingerAuth()
        }

        tvAutoLockLoginForgotPass.setOnClickListener {
            if (InternetConnectionClass().isConnected()) {
                val goToForgotCredentialsActivity =
                        Intent(this, ForgotCredentialsActivity::class.java)
                goToForgotCredentialsActivity.putExtra("credential", "master pin")

                startActivity(goToForgotCredentialsActivity)
                overridePendingTransition(
                        R.anim.anim_enter_right_to_left_2,
                        R.anim.anim_exit_right_to_left_2
                )
            } else {
                internetToast()
            }

            fingerprintHandlerClass.stopFingerAuth()

            it.apply {
                tvAutoLockLoginForgotPass.isClickable = false                                       // Set button un-clickable for 1 second
                postDelayed(
                        {
                            tvAutoLockLoginForgotPass.isClickable = true
                        }, 1000
                )
            }
        }
    }

    private fun clickEffect(view: View) {
        view.apply {
            view.setBackgroundResource(R.drawable.layout_button_light_gray_quadrilateral)
            postDelayed(
                    {
                        view.setBackgroundResource(R.drawable.layout_button_white_quadrilateral)
                    }, 100
            )
        }
    }
}