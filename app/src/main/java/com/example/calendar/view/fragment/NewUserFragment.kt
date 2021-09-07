package com.example.calendar.view.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
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
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.example.baselibs.User
import com.example.calendar.R

class NewUserFragment : Fragment() {

    private lateinit var back:ImageView
    private lateinit var editName:EditText
    private lateinit var editPassword:EditText
    private lateinit var button: Button
    private var flag1=false
    private var flag2=false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_new_user,container,false)

        back=view.findViewById(R.id.new_user_back)
        editName=view.findViewById(R.id.new_user_name_edit)
        editPassword=view.findViewById(R.id.new_user_password_edit)
        button=view.findViewById(R.id.new_user_button)

        back.setOnClickListener {
            activity?.onBackPressed()
        }

        editName.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(editName.text.toString().length>=1){
                    flag1=true
                    if(flag1&&flag2){
                        button.setBackgroundColor(Color.parseColor("#03A9F4"))
                    }
                }else {
                    flag1=false
                    button.setBackgroundColor(Color.parseColor("#A8DEF6"))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        editPassword.inputType=129
        editPassword.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(editPassword.text.toString().length>=1){
                    flag2=true
                    if(flag1&&flag2){
                        button.setBackgroundColor(Color.parseColor("#03A9F4"))
                    }
                }else {
                    flag2=false
                    button.setBackgroundColor(Color.parseColor("#A8DEF6"))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        button.setOnClickListener {
            val user=User()
            user.name=editName.text.toString()
            user.phone=context?.getSharedPreferences("LoginActivity",Context.MODE_PRIVATE)?.getString("phone","")
            user.password=editPassword.text.toString()
            user.constellation="射手座"
            user.save(object : SaveListener<String>(){
                override fun done(t: String?, e: BmobException?) {
                    if(e==null){
                        Toast.makeText(activity?.baseContext,"注册成功",Toast.LENGTH_SHORT).show()
                        activity?.onBackPressed()
                    }else {
                        Toast.makeText(activity?.baseContext,"当前手机号已有账户",Toast.LENGTH_SHORT).show()
                        activity?.onBackPressed()
                    }
                }
            })

        }

        return view
    }
}