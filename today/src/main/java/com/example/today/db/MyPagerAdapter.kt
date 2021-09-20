package com.example.today.db

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.example.today.R

class MyPagerAdapter(var mcontext:Context,var list:List<String>) : PagerAdapter() {
    override fun getCount()=list.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view=View.inflate(mcontext,R.layout.today_vp_item,null)
        val text=view.findViewById<TextView>(R.id.today_vp_item_text)
        text.text=list[position]
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view==`object`
    }
}