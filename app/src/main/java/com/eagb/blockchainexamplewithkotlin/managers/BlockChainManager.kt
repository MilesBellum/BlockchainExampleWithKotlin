package com.eagb.blockchainexamplewithkotlin.managers

import android.content.Context
import com.eagb.blockchainexamplewithkotlin.adapters.BlockAdapter
import com.eagb.blockchainexamplewithkotlin.models.BlockModel

class BlockChainManager(
    context: Context,
    private val difficulty: Int,
) {
    private var blocks: MutableList<BlockModel> = ArrayList()
    var adapter: BlockAdapter

    init {
        // Creating the 'Genesis block' (first block)
        val block =
            BlockModel(0, System.currentTimeMillis(), null, "Genesis Block")
        block.mineBlock(difficulty)
        blocks.add(block)

        // Setting list of blocks in the adapter
        adapter = BlockAdapter(context, blocks)
    }

    private fun latestBlock(): BlockModel {
        return blocks[blocks.size - 1]
    }

    /**
     * Broadcast block. Creating a new block.
     *
     * @param data is the data to broadcast.
     * @return new block.
     */
    fun newBlock(data: String?): BlockModel {
        val latestBlock = latestBlock()

        return BlockModel(
            latestBlock.index + 1,
            System.currentTimeMillis(),
            latestBlock.getHash(),
            data,
        )
    }

    /**
     * Requesting Proof-of-Work. Add block to the block chain.
     *
     * @param block is the block to add.
     */
    fun addBlock(block: BlockModel?) {
        block?.let {
            it.mineBlock(difficulty)
            blocks.add(it)
        }
    }

    /**
     * Validating first block. Validating first block.
     *
     * @return true if the first block is valid. Otherwise false.
     */
    private fun isFirstBlockValid(): Boolean {
        val firstBlock = blocks[0]

        if (firstBlock.index != 0) {
            return false
        }

        return if (firstBlock.previousHash != null) {
            false
        } else {
            firstBlock.getHash() != null &&
                BlockModel.calculateHash(firstBlock).equals(firstBlock.getHash())
        }
    }

    /**
     * Validate new block.
     *
     * @param newBlock is the new block to validate.
     * @param previousBlock is the previous block.
     * @return true if the new block is valid. Otherwise false.
     */
    private fun isValidNewBlock(
        newBlock: BlockModel?,
        previousBlock: BlockModel?,
    ): Boolean {
        if (newBlock != null && previousBlock != null) {
            if (previousBlock.index + 1 != newBlock.index) {
                return false
            }

            return if (newBlock.previousHash == null ||
                newBlock.previousHash != previousBlock.getHash()
            ) {
                return false
            } else {
                newBlock.getHash() != null &&
                    BlockModel.calculateHash(newBlock).equals(newBlock.getHash())
            }
        }

        return false
    }

    /**
     * Validating current block and block chain.
     *
     * @return true if the block chain is valid. Otherwise false.
     */
    fun isBlockChainValid(): Boolean {
        if (!isFirstBlockValid()) {
            return false
        }

        for (i in 1 until blocks.size) {
            val currentBlock = blocks[i]
            val previousBlock = blocks[i - 1]

            if (!isValidNewBlock(currentBlock, previousBlock)) {
                return false
            }
        }

        return true
    }
}
