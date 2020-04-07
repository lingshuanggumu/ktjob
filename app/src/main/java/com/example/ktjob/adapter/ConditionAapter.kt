package com.example.ktjob.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ktjob.R
import com.example.ktjob.model.ConditionItem

class ConditionAapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private val tag = ConditionAapter::class.simpleName

    companion object {
        val TYPE_ITEM = 0

        val TYPE_HEADER = 1

        val TYPE_FOOTER = 2

        val PULLUP_LOAD_MORE = 0

        val LOADING_MORE = 1
    }

    private var mLoadingStatus = PULLUP_LOAD_MORE

    private var mConditions : MutableList<ConditionItem>

    private lateinit var mContext : Context

    constructor(items : MutableList<ConditionItem>) {
        mConditions = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var view: View
        mContext = parent.context
        if (viewType == TYPE_ITEM) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.layout_condition_item, parent, false)
            return ItemViewHolder(view)
        } else if(viewType == TYPE_FOOTER) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.layout_condition_footer, parent, false)
            return FootViewHolder(view)
        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.layout_condition_footer, parent, false)
            return HeadViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val cond = mConditions.get(position)
            holder.mImage.setImageResource(cond.id)
            holder.mCond.setText(cond.name)
        } else if (holder is FootViewHolder) {
            when (mLoadingStatus) {
                PULLUP_LOAD_MORE ->
                    holder.mText.setText(mContext.getText(R.string.pullup_loading))
                LOADING_MORE ->
                    holder.mText.setText(mContext.getText(R.string.loading_more))
            }
        } else if (holder is HeadViewHolder) {
            when (mLoadingStatus) {
                PULLUP_LOAD_MORE ->
                    holder.mText.setText(mContext.getText(R.string.pullup_loading))
                LOADING_MORE ->
                    holder.mText.setText(mContext.getText(R.string.loading_more))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return TYPE_HEADER
        }

        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER
        }

        return TYPE_ITEM
    }

    override fun getItemCount(): Int {
        return mConditions.size + 1
    }

    fun addItem(items: MutableList<ConditionItem>) {
        mConditions.removeAll(mConditions)
        mConditions.addAll(items)
        notifyDataSetChanged()
    }

    fun addMoreItem(items: MutableList<ConditionItem>) {
        Log.i(tag, "addMoreItem")
        mConditions.addAll(items)
        notifyDataSetChanged()
    }

    fun changeMoreStatus(status: Int) {
        mLoadingStatus = status
        notifyDataSetChanged()
    }

    inner class ItemViewHolder : RecyclerView.ViewHolder {
        var mImage : ImageView
        var mCond : TextView
        constructor(view: View) : super(view) {
            mImage = view.findViewById(R.id.icon)
            mCond = view.findViewById(R.id.description)
        }
    }

    inner class HeadViewHolder : RecyclerView.ViewHolder {
        var mText : TextView
        constructor(view: View) : super(view) {
            mText = view.findViewById(R.id.loading_status)
        }
    }

    inner class FootViewHolder : RecyclerView.ViewHolder {
        var mText : TextView
        constructor(view: View) : super(view) {
            mText = view.findViewById(R.id.loading_status)
        }
    }
}