package dev.voleum.speedruncom

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import java.security.Key
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec

const val AndroidKeyStore = "AndroidKeyStore"
const val AES_MODE = "AES/GCM/NoPadding"
const val KEY_ALIAS = "ApiKey"
const val FIXED_IV = "SSSPPPDDRRNN"

val keyStore: KeyStore = KeyStore.getInstance(AndroidKeyStore)

@RequiresApi(Build.VERSION_CODES.M)
private fun createKey() {
    keyStore.load(null)
//    if (!keyStore.containsAlias(KEY_ALIAS)) {
        val keyGenerator =
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, AndroidKeyStore)
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setRandomizedEncryptionRequired(false)
            .build()
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
//    }
}

@RequiresApi(Build.VERSION_CODES.M)
fun encrypt(data: String): String {
    keyStore.load(null)
    val cipher = Cipher.getInstance(AES_MODE)
    cipher.init(
        Cipher.ENCRYPT_MODE,
        getSecretKey(),
        GCMParameterSpec(128, FIXED_IV.toByteArray())
    )
    val base64bytes = Base64.encode(data.toByteArray(), Base64.DEFAULT)
    val encodedBytes = cipher.doFinal(Base64.decode(base64bytes, Base64.DEFAULT))
    val encryptedBase64Encoded = Base64.encodeToString(encodedBytes, Base64.DEFAULT)
    Log.d("encr", "put $encryptedBase64Encoded")
    return encryptedBase64Encoded
}

@RequiresApi(Build.VERSION_CODES.M)
fun decrypt(encrypted: String): ByteArray {
    keyStore.load(null)
    val cipher = Cipher.getInstance(AES_MODE)
    cipher.init(
        Cipher.DECRYPT_MODE,
        getSecretKey(),
        GCMParameterSpec(128, FIXED_IV.toByteArray())
    )
    val decodedBytes = cipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT))
    Log.d("encr", "get $decodedBytes")
    Log.d("encr", "get ${String(decodedBytes)}")
    return decodedBytes
}

@RequiresApi(Build.VERSION_CODES.M)
fun getSecretKey(): Key {
    if (!keyStore.containsAlias(KEY_ALIAS)) createKey()
    return keyStore.getKey(KEY_ALIAS, null)
}
