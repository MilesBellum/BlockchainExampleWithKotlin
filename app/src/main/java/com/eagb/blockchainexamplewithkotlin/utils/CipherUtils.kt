package com.eagb.blockchainexamplewithkotlin.utils

import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import javax.crypto.*
import javax.crypto.spec.DESKeySpec

class CipherUtils {

    companion object {
        private const val PASSWORD = "Th15-15-4-P455w0rd"
        private const val ALGORITHM = "DES"

        // This method is used to encrypt the message, but this feature is not necessary in Blockchain,
        // so I just added this to keep the example more interesting
        fun encryptIt(value: String): String? {
            try {
                val keySpec =
                    DESKeySpec(PASSWORD.toByteArray(StandardCharsets.UTF_8))
                val keyFactory =
                    SecretKeyFactory.getInstance(ALGORITHM)
                val key = keyFactory.generateSecret(keySpec)
                val clearText =
                    value.toByteArray(StandardCharsets.UTF_8)
                // Cipher is not thread safe
                val cipher = Cipher.getInstance(ALGORITHM)
                cipher.init(Cipher.ENCRYPT_MODE, key)
                return Base64.encodeToString(
                    cipher.doFinal(clearText),
                    Base64.DEFAULT
                )
            } catch (e: InvalidKeyException) {
                e.printStackTrace()
            } catch (e: InvalidKeySpecException) {
                e.printStackTrace()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: BadPaddingException) {
                e.printStackTrace()
            } catch (e: NoSuchPaddingException) {
                e.printStackTrace()
            } catch (e: IllegalBlockSizeException) {
                e.printStackTrace()
            }

            return value
        }

        // This method is not used in this app, but it's not bad to know how to decrypt the text
        fun decryptIt(value: String): String {
            try {
                val keySpec =
                    DESKeySpec(PASSWORD.toByteArray(StandardCharsets.UTF_8))
                val keyFactory =
                    SecretKeyFactory.getInstance(ALGORITHM)
                val key = keyFactory.generateSecret(keySpec)

                val encryptedPwdBytes =
                    Base64.decode(value, Base64.DEFAULT)
                // Cipher is not thread safe
                val cipher = Cipher.getInstance(ALGORITHM)
                cipher.init(Cipher.DECRYPT_MODE, key)
                val decryptedValueBytes = cipher.doFinal(encryptedPwdBytes)

                return String(decryptedValueBytes)
            } catch (e: InvalidKeyException) {
                e.printStackTrace()
            } catch (e: InvalidKeySpecException) {
                e.printStackTrace()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: BadPaddingException) {
                e.printStackTrace()
            } catch (e: NoSuchPaddingException) {
                e.printStackTrace()
            } catch (e: IllegalBlockSizeException) {
                e.printStackTrace()
            }

            return value
        }
    }
}