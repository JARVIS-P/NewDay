package com.example.more.view.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.RotateAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.baselibs.LikeClass
import com.example.baselibs.Utility
import com.example.more.R
import com.example.more.db.TinderCardView
import com.example.more.viewmoudle.MoreViewModel

@Route(path = "/more/more_fragment")
class MoreFragment :Fragment() {

    private lateinit var myCardView1: TinderCardView
    private lateinit var myCardView2: TinderCardView
    private lateinit var myCardView3: TinderCardView
    private lateinit var myCardView4: TinderCardView
    private lateinit var conLayout1: ConstraintLayout
    private lateinit var conLayout2: ConstraintLayout
    private lateinit var conLayout3: ConstraintLayout
    private lateinit var conLayout4: ConstraintLayout
    private lateinit var frameLayout1: FrameLayout
    private lateinit var frameLayout2: FrameLayout
    private lateinit var frameLayout3: FrameLayout
    private lateinit var frameLayout4: FrameLayout
    private lateinit var textView1: TextView
    private lateinit var textView2: TextView
    private lateinit var textView3: TextView
    private lateinit var textView4: TextView
    private lateinit var like1:ImageView
    private lateinit var like2:ImageView
    private lateinit var like3:ImageView
    private lateinit var like4:ImageView
    private lateinit var oneToZero:AlphaAnimation
    private lateinit var moreViewModel: MoreViewModel
    private lateinit var sp:SharedPreferences
    private lateinit var edit:SharedPreferences.Editor

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_more,container,false)

        myCardView1=view.findViewById(R.id.my_card_view_1)
        myCardView2=view.findViewById(R.id.my_card_view_2)
        myCardView3=view.findViewById(R.id.my_card_view_3)
        myCardView4=view.findViewById(R.id.my_card_view_4)
        conLayout1=view.findViewById(R.id.con_layout_1)
        conLayout2=view.findViewById(R.id.con_layout_2)
        conLayout3=view.findViewById(R.id.con_layout_3)
        conLayout4=view.findViewById(R.id.con_layout_4)
        frameLayout1=view.findViewById(R.id.fragment_1)
        frameLayout2=view.findViewById(R.id.fragment_2)
        frameLayout3=view.findViewById(R.id.fragment_3)
        frameLayout4=view.findViewById(R.id.fragment_4)
        textView1=view.findViewById(R.id.textView_1)
        textView2=view.findViewById(R.id.textView_2)
        textView3=view.findViewById(R.id.textView_3)
        textView4=view.findViewById(R.id.textView_4)
        like1=view.findViewById(R.id.like_1)
        like2=view.findViewById(R.id.like_2)
        like3=view.findViewById(R.id.like_3)
        like4=view.findViewById(R.id.like_4)
        sp=context?.getSharedPreferences("More",Context.MODE_PRIVATE)!!
        edit=sp.edit()
        moreViewModel=ViewModelProvider(this).get(MoreViewModel::class.java)

        moreViewModel.init(requireContext())
        moreViewModel.getLuckyRetrofit()
        moreViewModel.getDogRetrofit()
        moreViewModel.getLoveRetrofit()
        moreViewModel.getPoetryRetrofit()

        oneToZero= AlphaAnimation(1f,0f)
        oneToZero.duration=625

        /*edit.putBoolean("dog_like",false)
        edit.putBoolean("poetry_like",false)
        edit.putBoolean("love_like",false)
        edit.apply()*/

        val mGestureDetector1=GestureDetector(object : GestureDetector.SimpleOnGestureListener(){
            override fun onDoubleTap(e: MotionEvent?): Boolean {
                if(frameLayout1.visibility==View.VISIBLE){
                    like1.visibility=View.VISIBLE
                    edit.putBoolean("poetry_like",true)
                    edit.apply()
                    Toast.makeText(context,"已添加至我的收藏",Toast.LENGTH_SHORT).show()
                    val temp=LikeClass(Utility.getUser().phone,textView1.text.toString(),"每日一诗")
                    temp.save(object : SaveListener<String>(){
                        override fun done(t: String?, e: BmobException?) {
                        }
                    })
                }
                return super.onDoubleTap(e)
            }
        })

        val mGestureDetector2=GestureDetector(object : GestureDetector.SimpleOnGestureListener(){
            override fun onDoubleTap(e: MotionEvent?): Boolean {
                if(frameLayout2.visibility==View.VISIBLE){
                    like2.visibility=View.VISIBLE
                    edit.putBoolean("dog_like",true)
                    edit.apply()
                    Toast.makeText(context,"已添加至我的收藏",Toast.LENGTH_SHORT).show()
                    val temp=LikeClass(Utility.getUser().phone,textView2.text.toString(),"舔狗日记")
                    temp.save(object : SaveListener<String>(){
                        override fun done(t: String?, e: BmobException?) {
                        }
                    })
                }
                return super.onDoubleTap(e)
            }
        })

        val mGestureDetector4=GestureDetector(object : GestureDetector.SimpleOnGestureListener(){
            override fun onDoubleTap(e: MotionEvent?): Boolean {
                if(frameLayout4.visibility==View.VISIBLE){
                    like4.visibility=View.VISIBLE
                    edit.putBoolean("love_like",true)
                    edit.apply()
                    Toast.makeText(context,"已添加至我的收藏",Toast.LENGTH_SHORT).show()
                    val temp=LikeClass(Utility.getUser().phone,textView4.text.toString(),"今日情话")
                    temp.save(object : SaveListener<String>(){
                        override fun done(t: String?, e: BmobException?) {
                        }
                    })
                }
                return super.onDoubleTap(e)
            }
        })

        myCardView1.setOnTouchListener (object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return mGestureDetector1.onTouchEvent(event)
            }
        })

        myCardView1.setOnClickListener {
            if(conLayout1.visibility==View.VISIBLE){

                conLayout1.startAnimation(oneToZero)
                rotate(myCardView1)

                Handler().postDelayed({
                    if(sp.getBoolean("poetry_like",false)){
                        like1.visibility=View.VISIBLE
                    }
                    conLayout1.visibility=View.GONE
                    frameLayout1.visibility=View.VISIBLE
                    frameLayout1.animate().alpha(1f).setDuration(625).start()//View动画AlphaAnimation可能有失效现象，因此建议使用属性动画
                },625)
            }
        }

        myCardView2.setOnTouchListener (object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return mGestureDetector2.onTouchEvent(event)
            }
        })

        myCardView2.setOnClickListener {
            if(conLayout2.visibility==View.VISIBLE){

                conLayout2.startAnimation(oneToZero)
                rotate(myCardView2)

                Handler().postDelayed({
                    if(sp.getBoolean("dog_like",false)){
                        like2.visibility=View.VISIBLE
                    }
                    conLayout2.visibility=View.GONE
                    frameLayout2.visibility=View.VISIBLE
                    frameLayout2.animate().alpha(1f).setDuration(625).start()//View动画AlphaAnimation可能有失效现象，因此建议使用属性动画
                },625)

            }
        }

        myCardView3.setOnClickListener {
            if(conLayout3.visibility==View.VISIBLE){

                conLayout3.startAnimation(oneToZero)
                rotate(myCardView3)

                Handler().postDelayed({
                    conLayout3.visibility=View.GONE
                    frameLayout3.visibility=View.VISIBLE
                    frameLayout3.animate().alpha(1f).setDuration(625).start()//View动画AlphaAnimation可能有失效现象，因此建议使用属性动画
                },625)

            }
        }

        myCardView4.setOnTouchListener (object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return mGestureDetector4.onTouchEvent(event)
            }
        })

        myCardView4.setOnClickListener {
            if(conLayout4.visibility==View.VISIBLE){

                conLayout4.startAnimation(oneToZero)
                rotate(myCardView4)

                Handler().postDelayed({
                    if(sp.getBoolean("love_like",false)){
                        like4.visibility=View.VISIBLE
                    }
                    conLayout4.visibility=View.GONE
                    frameLayout4.visibility=View.VISIBLE
                    frameLayout4.animate().alpha(1f).setDuration(625).start()//View动画AlphaAnimation可能有失效现象，因此建议使用属性动画
                },625)

            }
        }

        myCardView1.setListener {
            Handler().postDelayed({
                like1.visibility=View.GONE
                frameLayout2.alpha=0f
                frameLayout2.visibility=View.GONE
                conLayout2.visibility=View.VISIBLE
                myCardView2.visibility=View.VISIBLE

                myCardView1.visibility=View.GONE
                myCardView1.recover()
            },200)
        }

        myCardView2.setListener {
            Handler().postDelayed({
                like2.visibility=View.GONE
                frameLayout3.alpha=0f
                frameLayout3.visibility=View.GONE
                conLayout3.visibility=View.VISIBLE
                myCardView3.visibility=View.VISIBLE

                myCardView2.visibility=View.GONE
                myCardView2.recover()
            },200)
        }

        myCardView3.setListener {
            Handler().postDelayed({
                frameLayout4.alpha=0f
                frameLayout4.visibility=View.GONE
                conLayout4.visibility=View.VISIBLE
                myCardView4.visibility=View.VISIBLE

                myCardView3.visibility=View.GONE
                myCardView3.recover()
            },200)
        }

        myCardView4.setListener {
            Handler().postDelayed({
                like4.visibility=View.GONE
                frameLayout1.alpha=0f
                frameLayout1.visibility=View.GONE
                conLayout1.visibility=View.VISIBLE
                myCardView1.visibility=View.VISIBLE

                myCardView4.visibility=View.GONE
                myCardView4.recover()
            },200)
        }

        this.activity?.let {
            moreViewModel.poetryTalk.observe(it, Observer {
                textView1.text=it.toString()
            })
        }

        this.activity?.let {
            moreViewModel.dogTalk.observe(it, Observer {
                textView2.text=it.toString()
            })
        }

        this.activity?.let {
            moreViewModel.luckyTalk.observe(it, Observer {
                textView3.text=it.toString()
            })
        }

        this.activity?.let {
            moreViewModel.loveTalk.observe(it, Observer {
                textView4.text=it.toString()
            })
        }

        return view
    }

    private fun rotate(view: View){
        val rotateAnimation=RotateAnimation(0f,360f,view.width/2f,view.height/2f)
        rotateAnimation.duration=1250
        rotateAnimation.setInterpolator(AccelerateDecelerateInterpolator())
        view.startAnimation(rotateAnimation)
    }
}