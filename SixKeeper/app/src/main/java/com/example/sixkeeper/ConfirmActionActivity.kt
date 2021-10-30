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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey

class ConfirmActionActivity : ConfirmActionProcessClass(), LifecycleObserver {
    private val keyName = "yourKey"
    private lateinit var cipher: Cipher
    private lateinit var keyStore: KeyStore
    private lateinit var keyGenerator: KeyGenerator
    private lateinit var cryptoObject: FingerprintManager.CryptoObject
    private lateinit var fingerprintManager: FingerprintManager
    private lateinit var keyguardManager: KeyguardManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_action)

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        IndexActivity().setBackgroundDate()
        changeStatusBarColor()
        setVariables()
        fingerprint()
        blockCapture()
        setButtonOnClick()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        this.finish()
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
                    val helper = FingerprintHandler(this, "confirm action")
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
        val tvConfirmActionForgotPass: TextView = findViewById(R.id.tvConfirmActionForgotPass)

        getAcbConfirmActionButton1().setOnClickListener {
            pushNumber(1)
        }

        getAcbConfirmActionButton2().setOnClickListener {
            pushNumber(2)
        }

        getAcbConfirmActionButton3().setOnClickListener {
            pushNumber(3)
        }

        getAcbConfirmActionButton4().setOnClickListener {
            pushNumber(4)
        }

        getAcbConfirmActionButton5().setOnClickListener {
            pushNumber(5)
        }

        getAcbConfirmActionButton6().setOnClickListener {
            pushNumber(6)
        }

        getAcbConfirmActionButton7().setOnClickListener {
            pushNumber(7)
        }

        getAcbConfirmActionButton8().setOnClickListener {
            pushNumber(8)
        }

        getAcbConfirmActionButton9().setOnClickListener {
            pushNumber(9)
        }

        getAcbConfirmActionButton0().setOnClickListener {
            pushNumber(0)
        }

        getAcbConfirmActionButtonDelete().setOnClickListener {
            if (getPin().size > 0) {
                unShadePin()
                getPin().pop()
            }
        }

        getAcbConfirmActionButtonCancel().setOnClickListener {
            it.apply {
                getAcbConfirmActionButtonCancel().isClickable = false                               // Set button un-clickable for 1 second
                postDelayed(
                    {
                        getAcbConfirmActionButtonCancel().isClickable = true
                    }, 1000
                )
            }

            onBackPressed()
        }

        tvConfirmActionForgotPass.setOnClickListener {
            val goToForgotCredentialsActivity =
                    Intent(this, ForgotCredentialsActivity::class.java)
            goToForgotCredentialsActivity.putExtra("credential", "master pin")

            startActivity(goToForgotCredentialsActivity)
            overridePendingTransition(
                    R.anim.anim_enter_right_to_left_2,
                    R.anim.anim_exit_right_to_left_2
            )

            it.apply {
                tvConfirmActionForgotPass.isClickable = false                                       // Set button un-clickable for 1 second
                postDelayed(
                        {
                            tvConfirmActionForgotPass.isClickable = true
                        }, 1000
                )
            }
        }
    }
}