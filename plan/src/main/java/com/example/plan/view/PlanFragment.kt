package com.example.plan.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.baselibs.Utility
import com.example.plan.R
import com.example.plan.db.*
import com.google.android.material.floatingactionbutton.FloatingActionButton


@Route(path = "/plan/plan_fragment")
class PlanFragment : Fragment(){

    private val list= arrayListOf<PlanClass>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var fab:FloatingActionButton
    private lateinit var adapter: PlanRecyclerAdapter
    private var downX:Float=0f
    private var downY:Float=0f
    private var lastX:Float=0f
    private var lastY:Float=0f
    private var dy:Float=0f
    private var dx:Float=0f
    private var flag=true
    private var index=0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_plan,container,false)

        recyclerView=view.findViewById(R.id.plan_fragment_recycler_view)
        fab=view.findViewById(R.id.plan_fab)

        init()
        val listener=object : TransitListener {
            override fun slideOut() {
                flag=false
            }

            override fun slideIn() {
                Handler().postDelayed({
                    flag=true
                },100)

            }
        }

        val listener2=object : DeleteOrSuccessListener{
            override fun successListener(position: Int) {
                list[position].isSuccess=true
                list.add(list[position])

                val temp=PlanClass(list[position].theme,list[position].status,list[position].text,true)
                temp.update(list[position].objectId,object : UpdateListener(){
                    override fun done(e: BmobException?) {
                    }
                })

                list.removeAt(position)
                updateIndex()
                adapter.notifyDataSetChanged()
            }

            override fun deleteListener(position: Int) {
                val temp=PlanClass("","123","",false)
                temp.update(list[position].objectId,object : UpdateListener(){
                    override fun done(e: BmobException?) {
                    }
                })

                list[position].isSuccess=false
                list.removeAt(position)
                updateIndex()
                adapter.notifyDataSetChanged()
            }
        }

        val manager=object : LinearLayoutManager(context){
            override fun canScrollVertically(): Boolean {
                return flag//是否禁止RecyclerView的点击事件
            }
        }

        recyclerView.layoutManager=manager
        adapter=PlanRecyclerAdapter(requireContext(),list,listener,listener2)
        recyclerView.adapter=adapter

        fab.setOnTouchListener { v, event ->
            val x=event.x
            val y=event.y
            when(event.action){
                MotionEvent.ACTION_DOWN->{
                    downX=x
                    downY=y
                }
                MotionEvent.ACTION_MOVE->{
                    lastX=x
                    lastY=y
                    dy=lastY-downY
                    dx=lastX-downX
                    if(flag==true){
                        v.x=dx+v.x
                        v.y=dy+v.y
                    }
                }
                MotionEvent.ACTION_UP->{
                    if(Math.abs(dx)<0.1&&Math.abs(dy)<0.1&&flag==true){
                        val myDialog= PlanMyDialog(requireContext())
                        myDialog.setCancelable(false)
                        myDialog.setOnKeyListener { dialog, keyCode, event ->
                            return@setOnKeyListener keyCode==KeyEvent.KEYCODE_SEARCH
                        }
                        myDialog.setYesListener {
                            val temp=myDialog.planClass

                            list.add(index,temp)

                            temp.save(object : SaveListener<String>(){
                                override fun done(t: String?, e: BmobException?) {

                                }
                            })

                            updateIndex()
                            adapter.notifyDataSetChanged()
                        }
                        myDialog.show()
                    }
                    downX=0f
                    downY=0f
                    lastY=0f
                    lastX=0f
                    dx=0f
                    dy=0f
                }
            }

            return@setOnTouchListener !flag//判断是否要拦截本次事件(即本次触摸事件是否要传递给setOnClickListener方法)
        }

        return view
    }

    private fun init(){
        if(context?.getSharedPreferences("Plan",Context.MODE_PRIVATE)?.getString("day","")!=Utility.getUtility().time.monthDay.toString()){
            val bmobQuery: BmobQuery<PlanClass> = BmobQuery<PlanClass>()
            bmobQuery.addWhereNotEqualTo("status","123456789")
            bmobQuery.findObjects(object : FindListener<PlanClass>(){
                override fun done(`object`: MutableList<PlanClass>?, e: BmobException?) {
                    if(`object`!=null){
                        for(i in `object`){
                            val temp=PlanClass("","123","",false)
                            temp.update(i.objectId,object : UpdateListener(){
                                override fun done(e: BmobException?) {
                                }
                            })
                        }
                    }
                }
            })
            val edit=context?.getSharedPreferences("Plan",Context.MODE_PRIVATE)?.edit()
            edit?.putString("day",Utility.getUtility().time.monthDay.toString())
            edit?.apply()
        }

        val bmobQuery: BmobQuery<PlanClass> = BmobQuery<PlanClass>()
        bmobQuery.addWhereNotEqualTo("status","123")
        bmobQuery.findObjects(object : FindListener<PlanClass>(){
            override fun done(`object`: MutableList<PlanClass>?, e: BmobException?) {
                if(`object`!=null){
                    for(i in `object`){
                        if(i.status!="123"&&i.isSuccess==false){
                            list.add(i)
                        }
                    }
                    for(i in `object`){
                        if(i.status!="123"&&i.isSuccess==true){
                            list.add(i)
                        }
                    }
                    updateIndex()
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

    fun updateIndex(){
        if(list.lastIndex==0){
            index=0
        }else for(i in 0..list.lastIndex){
            if(list[i].isSuccess==true){
                index=i
                break
            }
        }
    }
}