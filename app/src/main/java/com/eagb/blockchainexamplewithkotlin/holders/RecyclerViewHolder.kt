package com.eagb.blockchainexamplewithkotlin.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eagb.blockchainexamplewithkotlin.R

class RecyclerViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    var txtIndex: TextView = itemView.findViewById(R.id.txt_index)
    var txtPreviousHash: TextView = itemView.findViewById(R.id.txt_previous_hash)
    var txtTimestamp: TextView = itemView.findViewById(R.id.txt_timestamp)
    var txtData: TextView = itemView.findViewById(R.id.txt_data)
    var txtHash: TextView = itemView.findViewById(R.id.txt_hash)
}