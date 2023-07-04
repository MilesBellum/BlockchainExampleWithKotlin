package com.eagb.blockchainexamplewithkotlin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.eagb.blockchainexamplewithkotlin.databinding.ItemBlockDataBinding
import com.eagb.blockchainexamplewithkotlin.holders.RecyclerViewHolder
import com.eagb.blockchainexamplewithkotlin.models.BlockModel

class BlockAdapter(
    private val  context: Context,
    private val blocks: List<BlockModel?>?
) : RecyclerView.Adapter<RecyclerViewHolder>() {

    private var lastPosition = -1

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        // Inflate the layout, initialize the View Holder
        val view = ItemBlockDataBinding.inflate(LayoutInflater.from(parent.context))
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerViewHolder, position: Int) {
        // Use the provided View Holder on the onCreateViewHolder method
        // to populate the current row on the RecyclerView
        blocks?.let {
            viewHolder.collectData(context, it[position]!!)
        }

        setAnimation(viewHolder.itemView, position)
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    // Return the size of list of data (invoked by the layout manager)
    override fun getItemCount(): Int = blocks?.size ?: 0

}