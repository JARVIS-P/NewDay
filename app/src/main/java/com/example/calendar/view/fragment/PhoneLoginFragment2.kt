package com.example.calendar.view.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobSMS
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.UpdateListener
import com.example.baselibs.User
import com.example.baselibs.Utility
import com.example.calendar.R
import com.example.calendar.db.SMSMessage
import com.example.calendar.moudle.bMobSMSVerify
import com.example.calendar.view.activity.MainActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class PhoneLoginFragment2 : Fragment() {

    private lateinit var back:ImageView
    private lateinit var edit:EditText
    private lateinit var button: Button
    private var sms=false
    private var judge=false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_phone_login_2,container,false)
        val flag=context?.getSharedPreferences("LoginActivity",Context.MODE_PRIVATE)?.getInt("FindOrPhone",1)


        back=view.findViewById(R.id.phone_login2_back)
        edit=view.findViewById(R.id.phone_login2_edit)
        button=view.findViewById(R.id.phone_login2_button)
        EventBus.getDefault().register(this)

        back.setOnClickListener {
            activity?.onBackPressed()
        }

        edit.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(edit.text.length==6){
                    judge=true
                    button.setBackgroundColor(Color.parseColor("#03A9F4"))
                }else{
                    judge=false
                    button.setBackgroundColor(Color.parseColor("#CDD0D1"))
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        button.setOnClickListener {
            bMobSMSVerify(context?.getSharedPreferences("LoginActivity",Context.MODE_PRIVATE)?.getString("phone","").toString(),edit.text.toString())
            Handler().postDelayed({
                if(!sms){
                    if(judge){
                        when(flag){
                            1-> {
                                val bmobQuery= BmobQuery<User>()
                                bmobQuery.addWhereEqualTo("phone",context?.getSharedPreferences("LoginActivity",Context.MODE_PRIVATE)?.getString("phone",""))
                                bmobQuery.findObjects(object : FindListener<User>(){
                                    override fun done(`object`: MutableList<User>?, e: BmobException?) {
                                        if(`object`?.size!!.toInt()>0){
                                            val editor= PreferenceManager.getDefaultSharedPreferences(activity).edit()
                                            editor.putString("phone",`object`[0].phone)
                                            editor.apply()
                                            startActivity(Intent(context, MainActivity::class.java))
                                            activity?.finish()
                                        } else {
                                            Toast.makeText(context,"该手机号未注册",Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                })
                            }
                            2->{
                                Navigation.findNavController(view).navigate(R.id.action_phone_login_fragment_2_to_find_password_fragment)
                            }
                            3->{
                                Navigation.findNavController(view).navigate(R.id.action_phone_login_fragment_2_to_new_user_fragment)
                            }
                        }
                    }
                }else {
                    Toast.makeText(activity,"验证码错误",Toast.LENGTH_SHORT).show()
                }
            },1000)
        }
        return view
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public fun judgeSMS(smsMessage: SMSMessage){
        sms=smsMessage.flag
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val editor=context?.getSharedPreferences("LoginActivity",Context.MODE_PRIVATE)?.edit()
        editor?.putInt("FindOrPhone",1)
        editor?.apply()
    }
}