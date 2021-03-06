package com.example.ktjob.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.ktjob.R
import com.example.ktjob.db.CityItem

class CityItemAdapter (var city: List<CityItem>, var context: Context) : BaseAdapter() {

    override fun getItem(index: Int): Any {
        return city.get(index)
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return city.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var viewHolder: ViewHolder
        var view: View
        if (convertView == null) {
            view = View.inflate(context, R.layout.layout_show_list, null)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        val item = getItem(position)
        if (item is CityItem) {
            viewHolder.name.text = item.cityName
            viewHolder.code.text = item.cityCode
        }
        return view
    }

    inner class ViewHolder (viewItem: View) {
        var name: TextView = viewItem.findViewById(R.id.name)
        var code: TextView = viewItem.findViewById(R.id.code)
    }
}