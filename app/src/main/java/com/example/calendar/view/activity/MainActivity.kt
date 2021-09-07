package com.example.calendar.view.activity

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.alibaba.android.arouter.launcher.ARouter
import com.example.baselibs.User
import com.example.baselibs.Utility
import com.example.calendar.BuildConfig
import com.example.calendar.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navigation: BottomNavigationView
    private lateinit var rootFragment: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)//取消默认标题栏
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//防止软键盘破坏布局
        val view=setContentView(R.layout.activity_main)

        if(Build.VERSION.SDK_INT>=21){
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)//将通知栏设置为透明色。
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//将标题栏内容设置为黑色
        }

        if(isDebug()){
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.init(this.application)

        val bmobQuery= BmobQuery<User>()
        bmobQuery.addWhereEqualTo("phone", PreferenceManager.getDefaultSharedPreferences(this).getString("phone",""))
        bmobQuery.findObjects(object : FindListener<User>(){
            override fun done(`object`: MutableList<User>?, e: BmobException?) {
                Utility.getUser().phone=`object`!![0].phone
                Utility.getUser().password=`object`[0].password
                Utility.getUser().name=`object`[0].name
                Utility.getUser().image=`object`[0].image
                Utility.getUser().constellation=`object`[0].constellation
            }
        })

        val editor=getSharedPreferences("MainActivity",Context.MODE_PRIVATE).edit()
        editor.putString("isLogin","true")
        editor.apply()

        initView()
        /*activityImageView.setOnClickListener {
            val apk = File("$cacheDir/plug-debug.apk")
            try {
                assets.open("apk/plug-debug.apk").source().use({ source -> apk.sink().buffer().use({ sink -> sink.writeAll(source) }) })
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val dexClassLoader = DexClassLoader(apk.path, cacheDir.path, null, null) //构建DexClassLoader

            try {
                val oldClass = dexClassLoader.loadClass("com.example.plug.Utils") //通过DexClassLoader找到目标类
                val constructor = oldClass.declaredConstructors[0]
                constructor.isAccessible = true
                val method = oldClass.getDeclaredMethod("exit")
                method.isAccessible = true
                val util = constructor.newInstance()
                method.invoke(util)
                val transaction=supportFragmentManager.beginTransaction()
                transaction.replace(R.id.main_layout,util as Fragment)
                transaction.commit()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }*/
    }

    private fun initView(){
        rootFragment=Navigation.findNavController(this,
            R.id.root_fragment
        )
        navigation=findViewById(R.id.bottom_navigation_view)
        NavigationUI.setupWithNavController(navigation,rootFragment)
    }

    private fun isDebug():Boolean{
        return BuildConfig.DEBUG
    }
}