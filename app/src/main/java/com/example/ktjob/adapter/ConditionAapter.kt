package com.example.ktjob.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ktjob.R
import com.example.ktjob.model.ConditionItem

class ConditionAapter : RecyclerView.Adapter<ConditionAapter.ViewHolder> {

    private var mConditions : List<ConditionItem>

    constructor(items : List<ConditionItem>) {
        mConditions = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).
            inflate(R.layout.layout_condition_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cond = mConditions.get(position)
        holder.mImage.setImageResource(cond.id)
        holder.mCond.setText(cond.name)
    }

    override fun getItemCount(): Int {
        return mConditions.size
    }

    inner class ViewHolder : RecyclerView.ViewHolder {
        var mImage : ImageView
        var mCond : TextView
        constructor(view: View) : super(view) {
            mImage = view.findViewById(R.id.icon)
            mCond = view.findViewById(R.id.description)
        }
    }
}