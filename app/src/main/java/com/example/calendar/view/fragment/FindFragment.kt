package com.example.calendar.view.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.UpdateListener
import com.example.baselibs.User
import com.example.calendar.R
import com.example.calendar.view.activity.MainActivity

class FindFragment : Fragment() {

    private lateinit var back:ImageView
    private lateinit var button: Button
    private lateinit var edit:EditText
    private var flag=false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_find,container,false)

        back=view.findViewById(R.id.find_back)
        button=view.findViewById(R.id.find_button)
        edit=view.findViewById(R.id.find_edit)

        edit.inputType=129
        edit.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(edit.text.toString().length>=1){
                    flag=true
                    button.setBackgroundColor(Color.parseColor("#03A9F4"))
                }else {
                    flag=false
                    button.setBackgroundColor(Color.parseColor("#A8DEF6"))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        back.setOnClickListener {
            activity?.onBackPressed()
        }

        button.setOnClickListener {
            var id=""
            val bmobQuery= BmobQuery<User>()
            bmobQuery.addWhereEqualTo("phone",context?.getSharedPreferences("LoginActivity",Context.MODE_PRIVATE)?.getString("phone",""))
            bmobQuery.findObjects(object : FindListener<User>(){
                override fun done(`object`: MutableList<User>?, e: BmobException?) {
                    if(`object`?.size!!.toInt()>0){
                        `object`[0].password=edit.text.toString()
                        `object`[0].update(object : UpdateListener(){
                            override fun done(e: BmobException?) {
                                if(e==null){
                                    Toast.makeText(context,"密码修改成功", Toast.LENGTH_SHORT).show()
                                    activity?.onBackPressed()
                                }
                            }
                        })
                    } else {
                        Toast.makeText(context,"当前手机号尚未注册", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }

        return view
    }
}