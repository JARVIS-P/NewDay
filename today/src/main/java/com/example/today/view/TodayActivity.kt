package com.example.today.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import cn.bmob.v3.Bmob
import com.example.today.R

class TodayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)//取消默认标题栏
        setContentView(R.layout.activity_today)

        Bmob.initialize(this,"0c7cfbe9890b4999af11c9110d39b2fb")

        if(Build.VERSION.SDK_INT>=21){
            getWindow().setStatusBarColor(Color.TRANSPARENT);//将通知栏设置为透明色。
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//将标题栏内容设置为黑色
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val fragment=supportFragmentManager.findFragmentById(R.id.root_today_fragment)
        return NavHostFragment.findNavController(fragment!!).navigateUp()
    }

}