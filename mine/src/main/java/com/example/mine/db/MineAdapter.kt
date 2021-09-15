package com.example.mine.db

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mine.R

class MineAdapter(var mContext:Context,var list:ArrayList<MineRecyclerItem>,var listener: MineAdapterListener) : RecyclerView.Adapter<MineAdapter.ViewHolder>(){

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val imageView=view.findViewById<ImageView>(R.id.mine_like_image)
        val textView=view.findViewById<TextView>(R.id.mine_like_text)
        val layout=view.findViewById<ConstraintLayout>(R.id.mine_item_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.mine_like_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val temp=list[position]
        when(temp.status){
            "毒鸡汤"->{
                holder.imageView.setImageResource(R.drawable.mine_chicken)
            }
            "舔狗日记"->{
                holder.imageView.setImageResource(R.drawable.mine_dog)
            }
            "每日一诗"->{
                holder.imageView.setImageResource(R.drawable.mine_dufu)
            }
            "今日情话"->{
                holder.imageView.setImageResource(R.drawable.mine_love)
            }
        }
        val str=temp.text.substring(0,5)+"..."
        holder.textView.setText(str)
        holder.layout.setOnClickListener {
            val dialog=MineDialog(mContext)
            if(list[position].text.length>100) dialog.textView.textSize=19f
            dialog.textView.text=list[position].text
            dialog.show()
        }
        holder.layout.setOnLongClickListener {
            listener.longListener(position)
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount()=list.size
}