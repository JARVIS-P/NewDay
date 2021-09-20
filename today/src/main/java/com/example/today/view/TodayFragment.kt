package com.example.today.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.baselibs.MyLoginInterceptor
import com.example.baselibs.Utility
import com.example.today.R
import com.example.today.db.TodayRetrofitInterface
import com.example.today.db.MyRecyclerAdapter
import com.example.today.db.RecyclerItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient

@Route(path = "/today/today_fragment")
class TodayFragment : Fragment(){

    private lateinit var recyclerView: RecyclerView
    private var list= arrayListOf<RecyclerItem>()
    private var talkList= arrayListOf<String>()
    private var recyclerItem=RecyclerItem("","","", arrayListOf(), arrayListOf(false,false,false))
    private var flag=false
    private var y=0f
    private var mLastY=0f
    private var dy=0f
    private var mapWeek=HashMap<String,String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_today,container,false)
        val sp=context?.getSharedPreferences("Today",Context.MODE_PRIVATE)
        val time=android.text.format.Time("GMT+8")
        time.setToNow()

        recyclerView=view.findViewById(R.id.today_recycler)

        val temp=StringBuilder("")
        if(time.monthDay<10) temp.append(0)
        temp.append(time.monthDay)

        if(sp?.getString("curDay_day","")==temp.toString()){
            init()
            initCur()
            setAdapter()
        }else {
            dayDataRetrofit()
        }

        //自定义RecyclerView的Item的滑动事件
        recyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener{
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
            }

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {//该方法会不停循环因此对变量的初始化不能放在这里
                when(e.action){
                    MotionEvent.ACTION_DOWN->{
                        y=e.y
                    }

                    MotionEvent.ACTION_MOVE->{//只有经历过move，y的值才能被正确的保留下来否则将会为0
                        flag=true
                        mLastY=e.y
                    }

                    MotionEvent.ACTION_UP->{
                        dy=mLastY-y
                        if(flag&&!recyclerView.canScrollVertically(1)&&dy<-400){
                            Toast.makeText(context,"明天怎么翻，也翻不过去", Toast.LENGTH_SHORT).show()
                        }
                        if(flag&&!recyclerView.canScrollVertically(-1)&&dy>400){
                            Toast.makeText(context,"可以回头看，但千万别往回走哦", Toast.LENGTH_SHORT).show()
                        }
                        flag=false
                        dy=0f
                        y=0f
                        mLastY=0f
                    }
                }

                return false
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
            }
        })

        return view
    }

    private fun setAdapter(){
        val manager=LinearLayoutManager(context)
        moveToPosition(manager,3)
        recyclerView.layoutManager=manager

        //RecyclerView实现ViewPager的效果
        recyclerView.isNestedScrollingEnabled=false
        val snapHelper=PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)


        val myRecyclerAdapter=MyRecyclerAdapter(context,list)
        recyclerView.adapter=myRecyclerAdapter
    }

    private fun moveToPosition(manager: LinearLayoutManager,n:Int){
        manager.scrollToPositionWithOffset(n,0)
        manager.stackFromEnd=true
    }

    private fun dateChange(){
        val editor=context?.getSharedPreferences("Today",Context.MODE_PRIVATE)?.edit()
        val sp=context?.getSharedPreferences("Today",Context.MODE_PRIVATE)

        val beforeDayYear=sp?.getString("yesterday_year","")
        val beforeDayDay=sp?.getString("yesterday_day","")
        val beforeDayWeek=sp?.getString("yesterday_week","")
        val beforeDayTalk1=sp?.getString("yesterday_talk1","")
        val beforeDayTalk2=sp?.getString("yesterday_talk2","")
        val beforeDayTalk3=sp?.getString("yesterday_talk3","")
        val beforeDayLike1=sp?.getBoolean("yesterday_like1",false)
        val beforeDayLike2=sp?.getBoolean("yesterday_like2",false)
        val beforeDayLike3=sp?.getBoolean("yesterday_like3",false)

        val yesterdayYear=sp?.getString("curDay_year","")
        val yesterdayDay=sp?.getString("curDay_day","")
        val yesterdayWeek=sp?.getString("curDay_week","")
        val yesterdayTalk1=sp?.getString("curDay_talk1","")
        val yesterdayTalk2=sp?.getString("curDay_talk2","")
        val yesterdayTalk3=sp?.getString("curDay_talk3","")
        val yesterdayLike1=sp?.getBoolean("curDay_like1",false)
        val yesterdayLike2=sp?.getBoolean("curDay_like2",false)
        val yesterdayLike3=sp?.getBoolean("curDay_like3",false)

        editor?.putString("beforeDay_year",beforeDayYear)
        editor?.putString("beforeDay_day",beforeDayDay)
        editor?.putString("beforeDay_week",beforeDayWeek)
        editor?.putString("beforeDay_talk1",beforeDayTalk1)
        editor?.putString("beforeDay_talk2",beforeDayTalk2)
        editor?.putString("beforeDay_talk3",beforeDayTalk3)
        editor?.putBoolean("beforeDay_like1",beforeDayLike1!!)
        editor?.putBoolean("beforeDay_like2",beforeDayLike2!!)
        editor?.putBoolean("beforeDay_like3",beforeDayLike3!!)

        editor?.putString("yesterday_year",yesterdayYear)
        editor?.putString("yesterday_day",yesterdayDay)
        editor?.putString("yesterday_week",yesterdayWeek)
        editor?.putString("yesterday_talk1",yesterdayTalk1)
        editor?.putString("yesterday_talk2",yesterdayTalk2)
        editor?.putString("yesterday_talk3",yesterdayTalk3)
        editor?.putBoolean("yesterday_like1",yesterdayLike1!!)
        editor?.putBoolean("yesterday_like2",yesterdayLike2!!)
        editor?.putBoolean("yesterday_like3",yesterdayLike3!!)
        editor?.apply()
    }

    private fun init(){
        val sp=context?.getSharedPreferences("Today",Context.MODE_PRIVATE)
        val edit=sp?.edit()

        var l= arrayListOf<String>()
        var ll= arrayListOf<Boolean>()
        var moon=sp?.getString("beforeDay_year","")
        var day=sp?.getString("beforeDay_day","")
        var week=sp?.getString("beforeDay_week","")
        var talk1=sp?.getString("beforeDay_talk1","")
        var talk2=sp?.getString("beforeDay_talk2","")
        var talk3=sp?.getString("beforeDay_talk3","")
        var like1=sp?.getBoolean("beforeDay_like1",false)
        var like2=sp?.getBoolean("beforeDay_like2",false)
        var like3=sp?.getBoolean("beforeDay_like3",false)
        l.add(talk1.toString())
        l.add(talk2.toString())
        l.add(talk3.toString())
        ll.add(like1!!)
        ll.add(like2!!)
        ll.add(like3!!)
        if(day!=""){
            list.add(RecyclerItem(moon.toString(),day.toString(),week.toString(),l, ll))
        }


        l= arrayListOf<String>()
        ll= arrayListOf<Boolean>()
        moon=sp?.getString("yesterday_year","")
        day=sp?.getString("yesterday_day","")
        week=sp?.getString("yesterday_week","")
        talk1=sp?.getString("yesterday_talk1","")
        talk2=sp?.getString("yesterday_talk2","")
        talk3=sp?.getString("yesterday_talk3","")
        like1=sp?.getBoolean("yesterday_like1",false)
        like2=sp?.getBoolean("yesterday_like2",false)
        like3=sp?.getBoolean("yesterday_like3",false)
        l.add(talk1.toString())
        l.add(talk2.toString())
        l.add(talk3.toString())
        ll.add(like1!!)
        ll.add(like2!!)
        ll.add(like3!!)
        if(day!=""){
            list.add(RecyclerItem(moon.toString(),day.toString(),week.toString(),l, ll))
        }

        mapWeek.put("星期一","Monday")
        mapWeek.put("星期二","Tuesday")
        mapWeek.put("星期三","Wednesday")
        mapWeek.put("星期四","Thursday")
        mapWeek.put("星期五","Friday")
        mapWeek.put("星期六","Saturday")
        mapWeek.put("星期日","Sunday")
    }

    private fun initCur(){
        val sp=context?.getSharedPreferences("Today",Context.MODE_PRIVATE)

        val l= arrayListOf<String>()
        val ll= arrayListOf<Boolean>()
        val moon=sp?.getString("curDay_year","")
        val day=sp?.getString("curDay_day","")
        val week=sp?.getString("curDay_week","")
        val talk1=sp?.getString("curDay_talk1","")
        val talk2=sp?.getString("curDay_talk2","")
        val talk3=sp?.getString("curDay_talk3","")
        val like1=sp?.getBoolean("curDay_like1",false)
        val like2=sp?.getBoolean("curDay_like2",false)
        val like3=sp?.getBoolean("curDay_like3",false)

        l.add(talk1.toString())
        l.add(talk2.toString())
        l.add(talk3.toString())
        ll.add(like1!!)
        ll.add(like2!!)
        ll.add(like3!!)
        list.add(RecyclerItem(moon.toString(),day.toString(),week.toString(),l, ll))
    }

    private fun dayDataRetrofit(){
        dateChange()
        init()
        val client=OkHttpClient.Builder().addInterceptor(MyLoginInterceptor()).build()

        val retrofit=Utility.getUtility().getRetrofit(client,"http://v.juhe.cn")
        val request=retrofit.create(TodayRetrofitInterface::class.java)

        val data=StringBuilder("")
        val time=android.text.format.Time("GMT+8")
        time.setToNow()
        data.append(time.year).append("-").append((time.month.toString().toInt()+1).toString()).append("-").append(time.monthDay)

        val map=HashMap<String,Any>()
        map.put("key","6e2da9edf94041558f95c0c1b88f6647")
        map.put("date",data)

        val single=request.getDayData(map.toMap())

        single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeBy(
                onSuccess = {
                    var moon=""
                    if(it.result.data.holiday==""){
                        moon=""+time.year+"年"+(time.month.toString().toInt()+1).toString()+"月  农历"+it.result.data.lunar
                    }else {
                        moon=""+time.year+"年"+(time.month.toString().toInt()+1).toString()+"月  农历"+it.result.data.holiday
                    }

                    val day=StringBuilder("")
                    if(time.monthDay<10) day.append("0")
                    day.append(time.monthDay)
                    val week=it.result.data.weekday+"  |  "+mapWeek[it.result.data.weekday]

                    recyclerItem.day=day.toString()
                    recyclerItem.moon=moon
                    recyclerItem.week=week
                    val editor=context?.getSharedPreferences("Today",Context.MODE_PRIVATE)?.edit()
                    editor?.putString("curDay_year",moon)
                    editor?.putString("curDay_day",day.toString())
                    editor?.putString("curDay_week",week)
                    editor?.apply()
                    talkRetrofit()
                    talkRetrofit()
                    talkRetrofit()
                },
                onError={ Log.e("TAG", "onCreate: "+it) }
        )
    }

    private fun talkRetrofit(){
        val client=OkHttpClient.Builder().addInterceptor(MyLoginInterceptor()).build()
        val editor=context?.getSharedPreferences("Today",Context.MODE_PRIVATE)?.edit()
        val retrofit=Utility.getUtility().getRetrofit(client,"http://api.lkblog.net")
        val request=retrofit.create(TodayRetrofitInterface::class.java)

        val single=request.getTalk()

        single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeBy(
                onSuccess = {
                    var temp=""
                    for(i in it.data){
                        if(i=='，'||i=='。'||i=='？'||i=='！'||i=='、') {
                            temp += i
                            temp += "\n"
                        } else temp+=i
                    }
                    talkList.add(temp.toString())
                    if(talkList.size==3) {
                        recyclerItem.talk=talkList
                        list.add(recyclerItem)
                        setAdapter()

                        editor?.putString("curDay_talk1",talkList[0])
                        editor?.putString("curDay_talk2",talkList[1])
                        editor?.putString("curDay_talk3",talkList[2])
                        editor?.putBoolean("curDay_like1",false)
                        editor?.putBoolean("curDay_like2",false)
                        editor?.putBoolean("curDay_like3",false)
                        editor?.apply()
                    }
                },
                onError={ Log.e("TAG", "onCreate: "+it) }
        )
    }



}