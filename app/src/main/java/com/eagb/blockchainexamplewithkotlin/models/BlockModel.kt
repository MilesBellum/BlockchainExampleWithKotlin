package com.eagb.blockchainexamplewithkotlin.models

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class BlockModel(
    val index: Int,
    val timestamp: Long,
    val previousHash: String?,
    val data: String?
    ) {

    private var nonce: Int
    private var hash: String? = null

    init {
        nonce = 0
        hash = calculateHash(this)
    }

    companion object {

        // This is the process to find a hash with the exact
        // number of leading zeros set by the user
        fun calculateHash(block: BlockModel?): String? {
            block?.let {
                val messageDigest: MessageDigest = try {
                    MessageDigest.getInstance("SHA-256")
                } catch (e: NoSuchAlgorithmException) {
                    return null
                }

                val txt = it.str()
                val bytes = messageDigest.digest(txt.toByteArray())
                val builder = StringBuilder()

                for (b in bytes) {
                    val hex = Integer.toHexString(0xff and b.toInt())

                    if (hex.length == 1) {
                        builder.append('0')
                    }

                    builder.append(hex)
                }

                return builder.toString()
            }

            return null
        }
    }

    fun getHash(): String? { return hash }

    private fun str(): String { return index.toString() + timestamp + previousHash + data + nonce }

    // Proof-of-Work (mining blocks).
    // Adding the number of zeros set by the user.
    // The more zeros at the beginning of the hash, the more difficult
    // it will be to find a hash with that request.
    // The calculations will be done by the CPU. This process is called 'mining'
    fun mineBlock(difficulty: Int) {
        nonce = 0

        while (hash!!.substring(0, difficulty) != addZeros(difficulty)) {
            nonce++
            hash = calculateHash(this)
        }
    }

    // Adding zeros in a String to set more difficulty
    private fun addZeros(length: Int): String {
        val builder = StringBuilder()

        for (i in 0 until length) {
            builder.append('0')
        }

        return builder.toString()
    }
}