package com.example.ktjob.ui

import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ktjob.R
import com.example.ktjob.adapter.ConditionAapter
import com.example.ktjob.model.ConditionItem

class ConditionActivity : AppCompatActivity() {
    private val tag = ConditionActivity::class.simpleName

    private val perLineSize = 4

    private lateinit var mList: RecyclerView;

    private var mConds = mutableListOf<ConditionItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weahter_condition)
        mList = findViewById(R.id.condition_icon_list)
        initConditionItems()
        mList.adapter = ConditionAapter(mConds.toList())
        mList.layoutManager = GridLayoutManager(this, perLineSize)
        //mList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }


    private fun getDrawableResId(context: Context, imageName: String): Int {
        var appInfo: ApplicationInfo = context.applicationInfo
        val name = "cond_"+imageName
        Log.i(tag, "pic name = " + name)
        return context.resources.getIdentifier(name, "drawable", appInfo.packageName)
    }

    private fun initConditionItems() {
        mConds.add(ConditionItem(getDrawableResId(this, "100"), "晴"))
        mConds.add(ConditionItem(getDrawableResId(this, "100n"), "晴(夜)"))
        mConds.add(ConditionItem(getDrawableResId(this, "101"), "多云"))
        mConds.add(ConditionItem(getDrawableResId(this, "102"), "少云"))
        mConds.add(ConditionItem(getDrawableResId(this, "103"), "晴间多云"))
        mConds.add(ConditionItem(getDrawableResId(this, "103n"), "晴间多云(夜)"))
        mConds.add(ConditionItem(getDrawableResId(this, "104"), "阴"))
        mConds.add(ConditionItem(getDrawableResId(this, "104"), "阴(夜)"))
        mConds.add(ConditionItem(getDrawableResId(this, "200"), "有风"))
        mConds.add(ConditionItem(getDrawableResId(this, "201"), "平静"))
        mConds.add(ConditionItem(getDrawableResId(this, "202"), "微风"))
        mConds.add(ConditionItem(getDrawableResId(this, "203"), "和风"))
        mConds.add(ConditionItem(getDrawableResId(this, "204"), "清风"))
        mConds.add(ConditionItem(getDrawableResId(this, "205"), "强风/劲风"))
        mConds.add(ConditionItem(getDrawableResId(this, "206"), "疾风"))
        mConds.add(ConditionItem(getDrawableResId(this, "207"), "大风"))
        mConds.add(ConditionItem(getDrawableResId(this, "208"), "烈风"))
        mConds.add(ConditionItem(getDrawableResId(this, "209"), "风暴"))
        mConds.add(ConditionItem(getDrawableResId(this, "210"), "狂爆风"))
        mConds.add(ConditionItem(getDrawableResId(this, "211"), "飓风"))
        mConds.add(ConditionItem(getDrawableResId(this, "212"), "龙卷风"))
        mConds.add(ConditionItem(getDrawableResId(this, "213"), "热带风暴"))
        mConds.add(ConditionItem(getDrawableResId(this, "300"), "阵雨"))
        mConds.add(ConditionItem(getDrawableResId(this, "300n"), "阵雨(夜)"))
        mConds.add(ConditionItem(getDrawableResId(this, "301"), "强阵雨"))
        mConds.add(ConditionItem(getDrawableResId(this, "301n"), "强阵雨(夜)"))
        mConds.add(ConditionItem(getDrawableResId(this, "302"), "雷阵雨"))
        mConds.add(ConditionItem(getDrawableResId(this, "303"), "强雷阵雨"))
        mConds.add(ConditionItem(getDrawableResId(this, "304"), "雷阵雨伴有冰雹"))
        mConds.add(ConditionItem(getDrawableResId(this, "305"), "小雨"))
        mConds.add(ConditionItem(getDrawableResId(this, "306"), "中雨"))
        mConds.add(ConditionItem(getDrawableResId(this, "307"), "大雨"))
        //mConds.add(ConditionItem(getDrawableResId(this, "308"), "极端降雨"))
        mConds.add(ConditionItem(getDrawableResId(this, "309"), "毛毛雨/细雨"))
        mConds.add(ConditionItem(getDrawableResId(this, "310"), "暴雨"))
        mConds.add(ConditionItem(getDrawableResId(this, "311"), "大暴雨"))
        mConds.add(ConditionItem(getDrawableResId(this, "312"), "特大暴雨"))
        mConds.add(ConditionItem(getDrawableResId(this, "313"), "冻雨"))
        mConds.add(ConditionItem(getDrawableResId(this, "314"), "小到中雨"))
        mConds.add(ConditionItem(getDrawableResId(this, "315"), "中到大雨"))
        mConds.add(ConditionItem(getDrawableResId(this, "316"), "大到暴雨"))
        mConds.add(ConditionItem(getDrawableResId(this, "317"), "暴雨到大暴雨"))
        mConds.add(ConditionItem(getDrawableResId(this, "318"), "大暴雨到特大暴雨"))
        mConds.add(ConditionItem(getDrawableResId(this, "399"), "雨"))
        mConds.add(ConditionItem(getDrawableResId(this, "400"), "小雪"))
        mConds.add(ConditionItem(getDrawableResId(this, "401"), "中雪"))
        mConds.add(ConditionItem(getDrawableResId(this, "402"), "大雪"))
        mConds.add(ConditionItem(getDrawableResId(this, "403"), "暴雪"))
        mConds.add(ConditionItem(getDrawableResId(this, "404"), "雨夹雪"))
        mConds.add(ConditionItem(getDrawableResId(this, "405"), "雨雪天气"))
        mConds.add(ConditionItem(getDrawableResId(this, "406"), "阵雨夹雪"))
        mConds.add(ConditionItem(getDrawableResId(this, "406n"), "阵雨夹雪(夜)"))
        mConds.add(ConditionItem(getDrawableResId(this, "407"), "阵雪"))
        mConds.add(ConditionItem(getDrawableResId(this, "407n"), "阵雪(夜)"))
        mConds.add(ConditionItem(getDrawableResId(this, "408"), "小到中雪"))
        mConds.add(ConditionItem(getDrawableResId(this, "409"), "中到大雪"))
        mConds.add(ConditionItem(getDrawableResId(this, "410"), "大到暴雪"))
        mConds.add(ConditionItem(getDrawableResId(this, "499"), "雪"))
        mConds.add(ConditionItem(getDrawableResId(this, "500"), "薄雾"))
        mConds.add(ConditionItem(getDrawableResId(this, "501"), "雾"))
        mConds.add(ConditionItem(getDrawableResId(this, "502"), "霾"))
        mConds.add(ConditionItem(getDrawableResId(this, "503"), "扬沙"))
        mConds.add(ConditionItem(getDrawableResId(this, "504"), "浮尘"))
        mConds.add(ConditionItem(getDrawableResId(this, "507"), "沙尘暴"))
        mConds.add(ConditionItem(getDrawableResId(this, "508"), "强沙尘暴"))
        mConds.add(ConditionItem(getDrawableResId(this, "509"), "浓雾"))
        mConds.add(ConditionItem(getDrawableResId(this, "510"), "强浓雾"))
        mConds.add(ConditionItem(getDrawableResId(this, "511"), "中度霾"))
        mConds.add(ConditionItem(getDrawableResId(this, "512"), "重度霾"))
        mConds.add(ConditionItem(getDrawableResId(this, "513"), "严重霾"))
        mConds.add(ConditionItem(getDrawableResId(this, "514"), "大雾"))
        mConds.add(ConditionItem(getDrawableResId(this, "515"), "特强浓雾"))
        mConds.add(ConditionItem(getDrawableResId(this, "900"), "热"))
        mConds.add(ConditionItem(getDrawableResId(this, "901"), "冷"))
        mConds.add(ConditionItem(getDrawableResId(this, "999"), "未知"))
    }
}
