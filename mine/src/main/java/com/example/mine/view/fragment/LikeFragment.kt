package com.example.mine.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.UpdateListener
import com.example.baselibs.LikeClass
import com.example.baselibs.Utility
import com.example.mine.R
import com.example.mine.db.MineAdapter
import com.example.mine.db.MineAdapterListener
import com.example.mine.db.MineRecyclerItem


class LikeFragment : Fragment(){

    private lateinit var adapter:MineAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var quit:ImageView
    private val list= arrayListOf<MineRecyclerItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.mine_fragment_like,container,false)
        quit=view.findViewById(R.id.mine_recycler_quit)

        init()
        recyclerView=view.findViewById(R.id.mine_like_recycler)
        val manager=GridLayoutManager(requireContext(),2)
        recyclerView.layoutManager=manager
        adapter=MineAdapter(requireContext(),list,object : MineAdapterListener{
            override fun longListener(position: Int) {
                val bmobQuery: BmobQuery<LikeClass> = BmobQuery()
                bmobQuery.addWhereEqualTo("content",list[position].text)
                bmobQuery.findObjects(object : FindListener<LikeClass>(){
                    override fun done(`object`: MutableList<LikeClass>?, e: BmobException?) {
                        if(`object`!=null){
                            val temp=LikeClass()
                            temp.status="123"
                            temp.delete(`object`[0].objectId,object : UpdateListener(){
                                override fun done(e: BmobException?) {
                                }
                            })
                        }
                    }
                })
                val spToday=context?.getSharedPreferences("Today",Context.MODE_PRIVATE)
                val editToday=spToday?.edit()
                val spMore=context?.getSharedPreferences("More",Context.MODE_PRIVATE)
                val editMore=spMore?.edit()
                if(spToday?.getString("beforeDay_talk1","")==list[position].text) editToday?.putBoolean("beforeDay_like1",false)
                if(spToday?.getString("beforeDay_talk2","")==list[position].text) editToday?.putBoolean("beforeDay_like2",false)
                if(spToday?.getString("beforeDay_talk3","")==list[position].text) editToday?.putBoolean("beforeDay_like3",false)
                if(spToday?.getString("curDay_talk1","")==list[position].text) editToday?.putBoolean("curDay_like1",false)
                if(spToday?.getString("curDay_talk2","")==list[position].text) editToday?.putBoolean("curDay_like2",false)
                if(spToday?.getString("curDay_talk3","")==list[position].text) editToday?.putBoolean("curDay_like3",false)
                if(spToday?.getString("yesterday_talk1","")==list[position].text) editToday?.putBoolean("yesterday_like1",false)
                if(spToday?.getString("yesterday_talk2","")==list[position].text) editToday?.putBoolean("yesterday_like2",false)
                if(spToday?.getString("yesterday_talk3","")==list[position].text) editToday?.putBoolean("yesterday_like3",false)
                editToday?.apply()

                if(spMore?.getString("dog_talk","")==list[position].text) editMore?.putBoolean("dog_like",false)
                if(spMore?.getString("love_talk","")==list[position].text) editMore?.putBoolean("love_like",false)
                if(spMore?.getString("poetry_talk","")==list[position].text) editMore?.putBoolean("poetry_like",false)
                editMore?.apply()
                list.removeAt(position)
                adapter.notifyDataSetChanged()
            }
        })
        recyclerView.adapter=adapter

        quit.setOnClickListener {
            activity?.onBackPressed()
        }

        return view
    }

    private fun init(){
        val bmobQuery: BmobQuery<LikeClass> = BmobQuery<LikeClass>()
        bmobQuery.addWhereEqualTo("phone",Utility.getUser().phone)
        bmobQuery.findObjects(object : FindListener<LikeClass>(){
            override fun done(`object`: MutableList<LikeClass>?, e: BmobException?) {
                if(`object`!=null){
                    for(i in `object`){
                        if(i.status!="123"){
                            val temp=MineRecyclerItem(i.content,i.status)
                            list.add(temp)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        })
    }
}