package com.example.mine.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.UpdateListener
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.baselibs.User
import com.example.baselibs.Utility
import com.example.mine.R
import com.example.mine.viewmodul.SafetyViewMode
import com.google.android.material.bottomnavigation.BottomNavigationView


@Route(path = "/mine/mine_safety")
class SafetyFragment : Fragment() {

    private lateinit var quit:ImageView
    private lateinit var success:FrameLayout
    private lateinit var number:TextView
    private lateinit var edit1:EditText
    private lateinit var edit2:EditText
    private lateinit var edit3:EditText
    private lateinit var viewModel: SafetyViewMode

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.mine_fragment_safety,container,false)

        quit=view.findViewById(R.id.mine_safety_quite)
        success=view.findViewById(R.id.mine_safety_success)
        number=view.findViewById(R.id.mine_safety_number)
        edit1=view.findViewById(R.id.mine_safety_edit1)
        edit2=view.findViewById(R.id.mine_safety_edit2)
        edit3=view.findViewById(R.id.mine_safety_edit3)

        viewModel=ViewModelProvider(this).get(SafetyViewMode::class.java)
        viewModel.initNumber()
        edit1.inputType=129
        edit2.inputType=129
        edit3.inputType=129

        quit.setOnClickListener {
            activity?.onBackPressed()
        }

        success.setOnClickListener {
            val bmobQuery: BmobQuery<User> = BmobQuery<User>()
            bmobQuery.addWhereEqualTo("phone",number.text)
            bmobQuery.findObjects(object : FindListener<User>(){
                override fun done(`object`: MutableList<User>?, e: BmobException?) {
                    if(`object`!=null){
                        Log.e("TAG", "done: "+`object`[0].password)
                        if(`object`[0].password==edit1.text.toString()){
                            if(edit2.text.toString()==edit3.text.toString()){
                                val temp=User()
                                temp.password=edit2.text.toString()
                                temp.update(`object`[0].objectId,object : UpdateListener(){
                                    override fun done(e: BmobException?) {
                                        if(e==null){
                                            Toast.makeText(context,"修改密码成功",Toast.LENGTH_SHORT).show()
                                            Utility.getUser().password=edit2.text.toString()
                                            activity?.onBackPressed()
                                        }else {
                                            Log.e("TAG", "done: "+e)
                                        }
                                    }
                                })
                            }else {
                                edit1.setText("")
                                edit2.setText("")
                                edit3.setText("")
                                Toast.makeText(context,"两次新密码的填写不一致",Toast.LENGTH_SHORT).show()
                            }
                        }else {
                            edit1.setText("")
                            edit2.setText("")
                            edit3.setText("")
                            Toast.makeText(context,"原密码错误",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }

        this.activity?.let {
            viewModel.number.observe(it, Observer {
                number.text=it.toString()
            })
        }

        return view
    }



}