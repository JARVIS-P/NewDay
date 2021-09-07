package com.example.calendar.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.QueryListener
import com.example.baselibs.User
import com.example.baselibs.Utility
import com.example.calendar.R
import com.example.calendar.view.activity.MainActivity

class LoginFragment : Fragment(){

    private lateinit var nameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginImageView: ImageView
    private lateinit var phoneLogin:TextView
    private lateinit var findPassword:TextView
    private lateinit var newUser:TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_login,container,false)

        nameEditText=view.findViewById(R.id.user_name)
        passwordEditText=view.findViewById(R.id.user_password)
        loginImageView=view.findViewById(R.id.login_image)
        phoneLogin=view.findViewById(R.id.phone_login)
        findPassword=view.findViewById(R.id.find_password)
        newUser=view.findViewById(R.id.new_user)

        passwordEditText.inputType=129

        loginImageView.setOnClickListener {
            val bmobQuery=BmobQuery<User>()
            bmobQuery.addWhereEqualTo("phone",nameEditText.text.toString())
            bmobQuery.findObjects(object : FindListener<User>(){
                override fun done(`object`: MutableList<User>?, e: BmobException?) {
                    if(`object`?.size!!.toInt()>0){
                        Log.e("TAG", "done: "+`object`[0].name)
                        if(`object`[0].password==passwordEditText.text.toString()){
                            val editor=PreferenceManager.getDefaultSharedPreferences(activity).edit()
                            editor.putString("phone",`object`[0].phone)
                            editor.apply()
                            startActivity(Intent(context,
                                MainActivity::class.java))
                            activity?.finish()
                        }else {
                            Toast.makeText(context,"密码错误",Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context,"账号错误",Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }

        phoneLogin.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_login_fragment_to_phone_login_fragment)
        }

        findPassword.setOnClickListener {
            val sp=context?.getSharedPreferences("LoginActivity", Context.MODE_PRIVATE)
            val editor=sp?.edit()
            editor?.putInt("FindOrPhone",2)
            editor?.apply()
            Navigation.findNavController(view).navigate(R.id.action_login_fragment_to_phone_login_fragment)
        }

        newUser.setOnClickListener {
            val sp=context?.getSharedPreferences("LoginActivity", Context.MODE_PRIVATE)
            val editor=sp?.edit()
            editor?.putInt("FindOrPhone",3)
            editor?.apply()
            Navigation.findNavController(view).navigate(R.id.action_login_fragment_to_phone_login_fragment)
        }

        return view
    }
}