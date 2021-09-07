package com.example.calendar.view.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import cn.bmob.v3.BmobSMS
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import com.example.calendar.R


class PhoneLoginFragment :Fragment() {

    private lateinit var back:ImageView
    private lateinit var edit:EditText
    private lateinit var button: Button
    private lateinit var text:TextView
    private var flag=false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_phone_login,container,false)

        back=view.findViewById(R.id.phone_login_back)
        edit=view.findViewById(R.id.phone_login_edit)
        button=view.findViewById(R.id.phone_login_button)
        text=view.findViewById(R.id.phone_login_textView)

        if(context?.getSharedPreferences("LoginActivity", Context.MODE_PRIVATE)?.getInt("FindOrPhone",1)==3){
            text.text="用手机号注册"
        }

        back.setOnClickListener {
            activity?.onBackPressed()
        }

        edit.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(edit.text.length==11){
                    button.setBackgroundColor(Color.parseColor("#03A9F4"))
                    flag=true
                }else {
                    flag=false
                    button.setBackgroundColor(Color.parseColor("#CDD0D1"))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        button.setOnClickListener {
            if(flag){
                BmobSMS.requestSMSCode(edit.text.toString(), null, object : QueryListener<Int>() {
                    override fun done(t: Int?, e: BmobException?) {
                        if(e==null){
                            Toast.makeText(context,"验证码已发送",Toast.LENGTH_SHORT).show()
                        }
                    }
                })
                val editor=context?.getSharedPreferences("LoginActivity",Context.MODE_PRIVATE)?.edit()
                editor?.putString("phone",edit.text.toString())
                editor?.apply()
                Navigation.findNavController(view).navigate(R.id.action_phone_login_fragment_to_phone_login_fragment_2)
            }
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val editor=context?.getSharedPreferences("LoginActivity",Context.MODE_PRIVATE)?.edit()
        editor?.putInt("FindOrPhone",1)
        editor?.apply()
    }
}