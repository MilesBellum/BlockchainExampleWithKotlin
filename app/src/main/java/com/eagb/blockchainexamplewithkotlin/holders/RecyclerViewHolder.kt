package com.eagb.blockchainexamplewithkotlin.holders

import android.content.Context
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eagb.blockchainexamplewithkotlin.R
import com.eagb.blockchainexamplewithkotlin.databinding.ItemBlockDataBinding
import com.eagb.blockchainexamplewithkotlin.models.BlockModel
import java.util.*

/**
 * Describes each item view as a [BlockModel] and retrieves its data.
 *
 * * @param itemView: Binding item view as a [ItemBlockDataBinding].
 */
class RecyclerViewHolder(
    itemView: ItemBlockDataBinding,
) : RecyclerView.ViewHolder(itemView.root) {
    private var txtIndex: TextView = itemView.txtIndex
    private var txtPreviousHash: TextView = itemView.txtPreviousHash
    private var txtTimestamp: TextView = itemView.txtTimestamp
    private var txtData: TextView = itemView.txtData
    private var txtHash: TextView = itemView.txtHash

    /**
     * Collects data for each [BlockModel].
     *
     * @param context: [Context] to access the resources.
     * @param block: [BlockModel] is the model for each block data.
     */
    fun collectData(context: Context, block: BlockModel) {
        txtIndex.text = String.format(context.getString(R.string.title_block_number), block.index)
        txtPreviousHash.text = block.previousHash ?: "Null"
        txtTimestamp.text = Date(block.timestamp).toString()
        txtData.text = block.data
        txtHash.text = block.getHash()
    }
}
