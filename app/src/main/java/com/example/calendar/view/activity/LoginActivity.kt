package com.example.calendar.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import cn.bmob.v3.Bmob
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.baselibs.User
import com.example.baselibs.Utility
import com.example.calendar.R

@Route(path="/app/app_login")
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Bmob.initialize(this,"0c7cfbe9890b4999af11c9110d39b2fb")

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)//取消默认标题栏
        if(getSharedPreferences("MainActivity",Context.MODE_PRIVATE).getString("isLogin","")=="true"){
            startActivity(Intent(this,MainActivity::class.java))
            overridePendingTransition(0, 0)
            this.finish()
        }

        setContentView(R.layout.activity_login)
        if(Build.VERSION.SDK_INT>=21){
            getWindow().setStatusBarColor(Color.TRANSPARENT);//将通知栏设置为透明色。
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//将标题栏内容设置为黑色
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val fragment=supportFragmentManager.findFragmentById(R.id.root_login_fragment)
        return NavHostFragment.findNavController(fragment!!).navigateUp()
    }
}