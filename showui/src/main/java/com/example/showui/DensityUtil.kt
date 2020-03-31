package com.example.showui

import android.content.Context
import android.view.View

class DensityUtil {
    companion object {
        fun dp2px(context: Context, dpValue: Float): Int {
            return (context.resources.displayMetrics.density * dpValue + 0.5f).toInt()
        }

        fun px2dp(context: Context, pxValue: Float): Int {
            return (pxValue / context.resources.displayMetrics.density+ 0.5f).toInt()
        }

        fun sp2px(context: Context, spValue: Float): Int {
            return (context.resources.displayMetrics.scaledDensity * spValue + 0.5f).toInt()
        }

        fun px2sp(context: Context, pxValue: Float): Int {
            return (pxValue / context.resources.displayMetrics.scaledDensity+ 0.5f).toInt()
        }

        fun getStatusBarHeight(context: Context): Int {
            var result = 0
            var resourceId : Int = context.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = context.resources.getDimensionPixelSize(resourceId)
            }
            return result
        }
    }
}