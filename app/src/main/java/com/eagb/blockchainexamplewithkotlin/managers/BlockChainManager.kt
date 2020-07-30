package com.eagb.blockchainexamplewithkotlin.managers

import android.content.Context
import com.eagb.blockchainexamplewithkotlin.adapters.BlockAdapter
import com.eagb.blockchainexamplewithkotlin.models.BlockModel

class BlockChainManager(
    context: Context,
    private val difficulty: Int
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

    // Broadcast block
    fun newBlock(data: String?): BlockModel? {
        val latestBlock = latestBlock()

        return BlockModel(
            latestBlock.getIndex() + 1, System.currentTimeMillis(),
            latestBlock.getHash(), data
        )
    }

    // Requesting Proof-of-Work
    fun addBlock(block: BlockModel?) {
        block?.let {
            it.mineBlock(difficulty)
            blocks.add(it)
        }
    }

    // Validating first block
    private fun isFirstBlockValid(): Boolean {
        val firstBlock = blocks[0]

        if (firstBlock.getIndex() != 0) {
            return false
        }

        return if (firstBlock.getPreviousHash() != null) {
            false
        } else firstBlock.getHash() != null &&
                BlockModel.calculateHash(firstBlock).equals(firstBlock.getHash())
    }

    // Validate new block
    private fun isValidNewBlock(
        newBlock: BlockModel?,
        previousBlock: BlockModel?
    ): Boolean {
        if (newBlock != null && previousBlock != null) {
            if (previousBlock.getIndex() + 1 != newBlock.getIndex()) {
                return false
            }

            return if (newBlock.getPreviousHash() == null ||
                !newBlock.getPreviousHash().equals(previousBlock.getHash())
            ) {
                return false
            } else newBlock.getHash() != null &&
                    BlockModel.calculateHash(newBlock).equals(newBlock.getHash())
        }

        return false
    }

    // Validating current block
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