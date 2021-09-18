package com.example.plan.db

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.plan.R

class PlanRecyclerAdapter(var mContext:Context,var list:ArrayList<PlanClass>,var listener: TransitListener,var listener2: DeleteOrSuccessListener) : RecyclerView.Adapter<PlanRecyclerAdapter.ViewHolder>(){

    private val viewList= arrayListOf<PlanRecyclerItemView>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView=view.findViewById<TextView>(R.id.recycler_item_text)
        val recyclerItemView=view.findViewById<PlanRecyclerItemView>(R.id.recycler_item_view)
        val successLayout=view.findViewById<FrameLayout>(R.id.recycler_item_success)
        val deleteLayout=view.findViewById<FrameLayout>(R.id.recycler_item_delete)
        val background=view.findViewById<FrameLayout>(R.id.recycler_item_background)
        val isSuccessImage=view.findViewById<ImageView>(R.id.is_success_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.plan_recycer_item,parent,false)
        val viewHolder=ViewHolder(view)
        //viewHolder.setIsRecyclable(false)  //所有ViewHolder永不复用，即每次都是全新的ViewHolder
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(list[position].status){
            "运动"->{
                holder.textView.setTextColor(Color.parseColor("#000000"))
                holder.background.setBackgroundResource(R.drawable.plan_sports)
            }
            "学习"->{
                holder.background.setBackgroundResource(R.drawable.plan_study)
                //holder.isSuccess.setImageResource(R.drawable.plan_success_white)
                holder.textView.setTextColor(Color.parseColor("#FFFFFF"))
            }
            "娱乐"->{
                holder.textView.setTextColor(Color.parseColor("#000000"))
                holder.background.setBackgroundResource(R.drawable.plan_casual)
            }
            "其他"->{
                holder.textView.setTextColor(Color.parseColor("#000000"))
                holder.background.setBackgroundResource(R.drawable.plan_other)
            }
        }
        holder.textView.text=list[position].theme
        holder.recyclerItemView.setMyItemListener(object : PlanRecyclerItemViewListener{
            override fun onClick() {
                if(list[position].text=="该计划无正文内容"){
                    Toast.makeText(mContext,list[position].text,Toast.LENGTH_SHORT).show()
                }else {
                    val dialog=PlanTextDialog(mContext)
                    dialog.setTextView(list[position].text)
                    dialog.show()
                }


                Log.e("TAG", "onClick: "+list[position].text)
            }

            override fun slideOut() {
                listener.slideOut()
                for(i in viewList){
                    if(i!=viewList[position]){
                        i.flag=false
                    }
                }
            }

            override fun slideIn() {
                for(i in viewList){
                    if(i!=viewList[position]){
                        i.flag=true
                    }
                }
                listener.slideIn()
            }
        })
        holder.deleteLayout.setOnClickListener {
            listener2.deleteListener(position)
            holder.recyclerItemView.recover()
        }
        holder.successLayout.setOnClickListener {
            holder.recyclerItemView.recover()
            listener2.successListener(position)
        }
        if(holder.recyclerItemView !in viewList){
            viewList.add(holder.recyclerItemView)
        }
        if(list[position].isSuccess){
            holder.isSuccessImage.visibility=View.VISIBLE
            holder.background.alpha=0.5f
        }else {
            holder.isSuccessImage.visibility=View.GONE
            holder.background.alpha=1f
        }
    }

    override fun getItemCount()=list.size

    override fun getItemViewType(position: Int): Int {//保证每次创建ViewHolder时都要调用onBindViewHolder方法，即允许复用但不会存在两个一模一样的ViewHolder
        return position
    }
}