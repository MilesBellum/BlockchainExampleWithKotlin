package com.eagb.blockchainexamplewithkotlin.models

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class BlockModel(
    private val index: Int,
    private val timestamp: Long,
    private val previousHash: String?,
    private val data: String?) {

    private var nonce: Int
    private var hash: String? = null

    init {
        nonce = 0
        hash = calculateHash(this)
    }

    companion object {
        fun calculateHash(block: BlockModel?): String? {
            block?.let {
                val messageDigest: MessageDigest = try {
                    MessageDigest.getInstance("SHA-256")
                } catch (e: NoSuchAlgorithmException) {
                    return null
                }

                val txt = it.str()
                val bytes = messageDigest.digest(txt!!.toByteArray())
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

    fun getIndex(): Int { return index }

    fun getTimestamp(): Long { return timestamp }

    fun getHash(): String? { return hash }

    fun getPreviousHash(): String? { return previousHash }

    fun getData(): String? { return data }

    private fun str(): String? { return index.toString() + timestamp + previousHash + data + nonce }

    // Proof-of-Work (mining blocks)
    fun mineBlock(difficulty: Int) {
        nonce = 0

        while (getHash()!!.substring(0, difficulty) != addZeros(difficulty)) {
            nonce++
            hash = calculateHash(this)
        }
    }

    private fun addZeros(length: Int): String {
        val builder = StringBuilder()

        for (i in 0 until length) {
            builder.append('0')
        }

        return builder.toString()
    }
}