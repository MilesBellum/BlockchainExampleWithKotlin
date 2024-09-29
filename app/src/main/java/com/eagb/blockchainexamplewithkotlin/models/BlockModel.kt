package com.eagb.blockchainexamplewithkotlin.models

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Block model constructor.
 *
 * @param index is the index of the block.
 * @param timestamp is the timestamp of the block.
 * @param previousHash is the hash of the previous block.
 * @param data is the data of the block.
 */
class BlockModel(
    val index: Int,
    val timestamp: Long,
    val previousHash: String?,
    val data: String?,
) {
    private var nonce: Int
    private var hash: String? = null

    init {
        nonce = 0
        hash = calculateHash(this)
    }

    companion object {
        /**
         * Calculate the hash of the block. This is the process to find a hash with the exact
         * number of leading zeros set by the user.
         *
         * @param block is the block to calculate the hash.
         * @return the hash of the block.
         */
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

    /**
     * Getter of the hash.
     *
     * @return the hash of the block.
     */
    fun getHash(): String? { return hash }

    /**
     * Getter of the nonce.
     *
     * @return the nonce of the block.
     */
    private fun str(): String { return index.toString() + timestamp + previousHash + data + nonce }

    /**
     * Mine the block. Proof-of-Work (mining blocks). Adding the number of zeros set by the user.
     * The more zeros at the beginning of the hash, the more difficult it will be to find a hash
     * with that request. The calculations will be done by the CPU. This process is called 'mining'.
     *
     * @param difficulty is the number of zeros at the beginning of the hash.
     */
    fun mineBlock(difficulty: Int) {
        nonce = 0

        while (hash!!.substring(0, difficulty) != addZeros(difficulty)) {
            nonce++
            hash = calculateHash(this)
        }
    }

    /**
     * Add zeros in a String to the hash to set more difficulty.
     *
     * @param length is the number of zeros to add.
     * @return the hash with the added zeros.
     */
    private fun addZeros(length: Int): String {
        val builder = StringBuilder()

        for (i in 0 until length) {
            builder.append('0')
        }

        return builder.toString()
    }
}
