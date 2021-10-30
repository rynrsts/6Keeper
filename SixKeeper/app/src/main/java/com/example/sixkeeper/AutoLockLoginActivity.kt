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
import android.widget.TextView
import androidx.core.app.ActivityCompat
import java.io.IOException
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

    private fun fingerprint() {                                                                     // Fingerprint code start
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
            fingerprintManager = getSystemService(FINGERPRINT_SERVICE) as FingerprintManager

            if (!fingerprintManager.isHardwareDetected) {
//                textView.text = "Your device doesn't support fingerprint authentication"
            }

            if (
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.USE_FINGERPRINT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
//                textView.text = "Please enable the fingerprint permission"
            }

            if (!fingerprintManager.hasEnrolledFingerprints()) {
//                textView.text = "No fingerprint configured. Please register at least one fingerprint in your device's Settings"
            }

            if (!keyguardManager.isKeyguardSecure) {
//                textView.text = "Please enable lockscreen security in your device's Settings"
            } else {
                try {
                    generateKey()
                } catch (e: FingerprintException) {
                    e.printStackTrace()
                }

                if (initCipher()) {
                    cryptoObject = FingerprintManager.CryptoObject(cipher)
                    val helper = FingerprintHandler(this, "auto lock")
                    helper.startAuth(fingerprintManager, cryptoObject)
                }
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
        val tvAutoLockLoginForgotPass: TextView = findViewById(R.id.tvAutoLockLoginForgotPass)

        getAcbAutoLockLoginButton1().setOnClickListener {
            pushNumber(1)
        }

        getAcbAutoLockLoginButton2().setOnClickListener {
            pushNumber(2)
        }

        getAcbAutoLockLoginButton3().setOnClickListener {
            pushNumber(3)
        }

        getAcbAutoLockLoginButton4().setOnClickListener {
            pushNumber(4)
        }

        getAcbAutoLockLoginButton5().setOnClickListener {
            pushNumber(5)
        }

        getAcbAutoLockLoginButton6().setOnClickListener {
            pushNumber(6)
        }

        getAcbAutoLockLoginButton7().setOnClickListener {
            pushNumber(7)
        }

        getAcbAutoLockLoginButton8().setOnClickListener {
            pushNumber(8)
        }

        getAcbAutoLockLoginButton9().setOnClickListener {
            pushNumber(9)
        }

        getAcbAutoLockLoginButton0().setOnClickListener {
            pushNumber(0)
        }

        getAcbAutoLockLoginButtonDelete().setOnClickListener {
            if (getPin().size > 0) {
                unShadePin()
                getPin().pop()
            }
        }

        getAcbAutoLockLoginButtonCancel().setOnClickListener {
            it.apply {
                getAcbAutoLockLoginButtonCancel().isClickable = false                               // Set button un-clickable for 1 second
                postDelayed(
                    {
                        getAcbAutoLockLoginButtonCancel().isClickable = true
                    }, 1000
                )
            }

            onBackPressed()
        }

        tvAutoLockLoginForgotPass.setOnClickListener {
            val goToForgotCredentialsActivity =
                Intent(this, ForgotCredentialsActivity::class.java)
            goToForgotCredentialsActivity.putExtra("credential", "master pin")

            startActivity(goToForgotCredentialsActivity)
            overridePendingTransition(
                R.anim.anim_enter_right_to_left_2,
                R.anim.anim_exit_right_to_left_2
            )

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
}