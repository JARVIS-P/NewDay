package com.example.today.db

import android.annotation.SuppressLint
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.example.baselibs.LikeClass
import com.example.baselibs.Utility
import com.example.today.R
import kotlin.math.log

class MyRecyclerAdapter(var mContext:Context?, var list:List<RecyclerItem>) : RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>()   {

    private val cm:ClipboardManager=mContext?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    private var pos=0
    /**
     * RecyclerView中只有ViewHolder中的控件每次都是新的
     * 适配器中定义在其他地方的控件实质上只有一个
     */
    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val moonText=view.findViewById<TextView>(R.id.today_text_moon)
        val dayText=view.findViewById<TextView>(R.id.today_text_day)
        val weekText=view.findViewById<TextView>(R.id.today_text_week)
        val viewPager=view.findViewById<ViewPager>(R.id.today_view_pager)
        val like=view.findViewById<ImageView>(R.id.today_like)

        val imageView1=view.findViewById<ImageView>(R.id.today_cir1)
        val imageView2=view.findViewById<ImageView>(R.id.today_cir2)
        val imageView3=view.findViewById<ImageView>(R.id.today_cir3)
    }

    override fun getItemCount(): Int=list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=LayoutInflater.from(parent.context).inflate(R.layout.today_recycler_item,parent,false)
        val viewHolder=ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recyclerItem=list[position]
        holder.dayText.text=recyclerItem.day
        holder.moonText.text=recyclerItem.moon
        holder.weekText.text=recyclerItem.week
        pos=0

        if(recyclerItem.isLike[pos]==true){
            holder.like.visibility=View.VISIBLE
        }else {
            holder.like.visibility=View.GONE
        }

        val adapter=MyPagerAdapter(mContext!!,recyclerItem.talk)

        val mGestureDetector=GestureDetector(object : GestureDetector.SimpleOnGestureListener(){//双击将其文字加入剪切板
            override fun onDoubleTap(e: MotionEvent?): Boolean {
                recyclerItem.isLike[pos]=true
                holder.like.visibility=View.VISIBLE
                val edit=mContext?.getSharedPreferences("Today",Context.MODE_PRIVATE)?.edit()
                val flag=judgeIndex(edit!!,position)
                edit.apply()
                if(flag){
                    val temp=LikeClass(Utility.getUser().phone,recyclerItem.talk[pos],"毒鸡汤")
                    temp.save(object : SaveListener<String>(){
                        override fun done(t: String?, e: BmobException?) {
                        }
                    })
                }

                Toast.makeText(mContext,"已添加至收藏",Toast.LENGTH_SHORT).show()
                return super.onDoubleTap(e)
            }

            override fun onLongPress(e: MotionEvent?) {
                cm.text=recyclerItem.talk[pos]
                Toast.makeText(mContext,"复制文字成功",Toast.LENGTH_SHORT).show()
                super.onLongPress(e)
            }
        })

        holder.viewPager.setOnTouchListener(object : View.OnTouchListener{//给ViewPager设置双击事件
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return mGestureDetector.onTouchEvent(event)
            }
        })

        holder.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                when(position){
                    0->{
                        pos=0
                        changeBig(holder.imageView1)
                        changSmall(holder.imageView2)
                        changSmall(holder.imageView3)
                        if(recyclerItem.isLike[pos]==true){
                            holder.like.visibility=View.VISIBLE
                        }else {
                            holder.like.visibility=View.GONE
                        }
                    }
                    1->{
                        pos=1
                        changeBig(holder.imageView2)
                        changSmall(holder.imageView1)
                        changSmall(holder.imageView3)
                        if(recyclerItem.isLike[pos]==true){
                            holder.like.visibility=View.VISIBLE
                        }else {
                            holder.like.visibility=View.GONE
                        }
                    }
                    2->{
                        pos=2
                        changeBig(holder.imageView3)
                        changSmall(holder.imageView1)
                        changSmall(holder.imageView2)
                        if(recyclerItem.isLike[pos]==true){
                            holder.like.visibility=View.VISIBLE
                        }else {
                            holder.like.visibility=View.GONE
                        }
                    }
                }
            }
        })
        holder.viewPager.adapter=adapter
    }

    private fun changeBig(imageView: ImageView){
        val pra=imageView.layoutParams
        pra.height=40
        pra.width=40
        imageView.layoutParams=pra

        imageView.setImageResource(R.drawable.today_big)
    }

    private fun changSmall(imageView: ImageView){
        val pra=imageView.layoutParams
        pra.height=23
        pra.width=23
        imageView.layoutParams=pra

        imageView.setImageResource(R.drawable.today_small)
    }

    private fun judgeIndex(edit: SharedPreferences.Editor,position: Int) :Boolean{
        val sp=mContext?.getSharedPreferences("Today",Context.MODE_PRIVATE)
        when(position){
            0->{
                when(list.size){
                    1->{
                        when(pos){
                            0->{
                                if(sp?.getBoolean("curDay_like1",false)!=true){
                                    edit.putBoolean("curDay_like1",true)
                                    return true
                                }else return false
                            }
                            1->{
                                if(sp?.getBoolean("curDay_like2",false)!=true){
                                    edit.putBoolean("curDay_like2",true)
                                    return true
                                }else return false
                            }
                            2->{
                                if(sp?.getBoolean("curDay_like3",false)!=true){
                                    edit.putBoolean("curDay_like3",true)
                                    return true
                                }else return false
                            }
                        }
                    }
                    2->{
                        when(pos){
                            0->{
                                if(sp?.getBoolean("yesterday_like1",false)!=true){
                                    edit.putBoolean("yesterday_like1",true)
                                    return true
                                }else return false
                            }
                            1->{
                                if(sp?.getBoolean("yesterday_like2",false)!=true){
                                    edit.putBoolean("yesterday_like2",true)
                                    return true
                                }else return false
                            }
                            2->{
                                if(sp?.getBoolean("yesterday_like3",false)!=true){
                                    edit.putBoolean("yesterday_like3",true)
                                    return true
                                }else return false
                            }
                        }
                    }
                    3->{
                        when(pos){
                            0->{
                                if(sp?.getBoolean("beforeDay_like1",false)!=true){
                                    edit.putBoolean("beforeDay_like1",true)
                                    return true
                                }else return false
                            }
                            1->{
                                if(sp?.getBoolean("beforeDay_like2",false)!=true){
                                    edit.putBoolean("beforeDay_like2",true)
                                    return true
                                }else return false
                            }
                            2->{
                                if(sp?.getBoolean("beforeDay_like3",false)!=true){
                                    edit.putBoolean("beforeDay_like3",true)
                                    return true
                                }else return false
                            }
                        }
                    }
                }
            }
            1->{
                when(list.size){
                    2->{
                        when(pos){
                            0->{
                                if(sp?.getBoolean("curDay_like1",false)!=true){
                                    edit.putBoolean("curDay_like1",true)
                                    return true
                                }else return false
                            }
                            1->{
                                if(sp?.getBoolean("curDay_like2",false)!=true){
                                    edit.putBoolean("curDay_like2",true)
                                    return true
                                }else return false
                            }
                            2->{
                                if(sp?.getBoolean("curDay_like3",false)!=true){
                                    edit.putBoolean("curDay_like3",true)
                                    return true
                                }else return false
                            }
                        }
                    }
                    3->{
                        when(pos){
                            0->{
                                if(sp?.getBoolean("yesterday_like1",false)!=true){
                                    edit.putBoolean("yesterday_like1",true)
                                    return true
                                }else return false
                            }
                            1->{
                                if(sp?.getBoolean("yesterday_like2",false)!=true){
                                    edit.putBoolean("yesterday_like2",true)
                                    return true
                                }else return false
                            }
                            2->{
                                if(sp?.getBoolean("yesterday_like3",false)!=true){
                                    edit.putBoolean("yesterday_like3",true)
                                    return true
                                }else return false
                            }
                        }
                    }
                }
            }
            2->{
                when(list.size){
                    3->{
                        when(pos){
                            0->{
                                if(sp?.getBoolean("curDay_like1",false)!=true){
                                    edit.putBoolean("curDay_like1",true)
                                    return true
                                }else return false
                            }
                            1->{
                                if(sp?.getBoolean("curDay_like2",false)!=true){
                                    edit.putBoolean("curDay_like2",true)
                                    return true
                                }else return false
                            }
                            2->{
                                if(sp?.getBoolean("curDay_like3",false)!=true){
                                    edit.putBoolean("curDay_like3",true)
                                    return true
                                }else return false
                            }
                        }
                    }
                }
            }
        }
        return false
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}