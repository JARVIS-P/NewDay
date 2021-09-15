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
import com.example.baselibs.User
import com.example.baselibs.Utility
import com.example.mine.R
import com.example.mine.viewmodul.UserViewMode

class UserFragment : Fragment() {

    private lateinit var quit:ImageView
    private lateinit var success:FrameLayout
    private lateinit var image:ImageView
    private lateinit var name:TextView
    private lateinit var constellation:TextView
    private lateinit var edit1:EditText
    private lateinit var edit2:EditText
    private lateinit var userViewMode: UserViewMode

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.mine_fragment_user,container,false)

        quit=view.findViewById(R.id.mine_user_quite)
        success=view.findViewById(R.id.mine_user_success)
        image=view.findViewById(R.id.mine_user_image)
        edit1=view.findViewById(R.id.mine_user_rename)
        edit2=view.findViewById(R.id.mine_user_constellation)
        name=view.findViewById(R.id.mine_user_fragment_name)
        constellation=view.findViewById(R.id.mine_user_fragment_constellation)

        userViewMode=ViewModelProvider(this).get(UserViewMode::class.java)
        userViewMode.initConstellation()
        userViewMode.initUserName()

        quit.setOnClickListener {
            activity?.onBackPressed()
        }

        success.setOnClickListener {

            val bmobQuery: BmobQuery<User> = BmobQuery<User>()
            bmobQuery.addWhereEqualTo("phone",Utility.getUser().phone)
            bmobQuery.findObjects(object : FindListener<User>(){
                override fun done(`object`: MutableList<User>?, e: BmobException?) {
                    if(`object`!=null){
                        val temp= User()
                        if(edit1.text.toString()=="") temp.name=Utility.getUser().name
                        else temp.name=edit1.text.toString()
                        if(edit2.text.toString()=="") temp.constellation=Utility.getUser().constellation
                        else temp.constellation=edit2.text.toString()
                        temp.update(`object`[0].objectId,object : UpdateListener(){
                            override fun done(e: BmobException?) {
                                if(e==null){
                                    Toast.makeText(context,"修改成功", Toast.LENGTH_SHORT).show()
                                    Utility.getUser().name=temp.name.toString()
                                    Utility.getUser().constellation=temp.constellation.toString()
                                    activity?.onBackPressed()
                                }else {
                                    Log.e("TAG", "done: "+e)
                                }
                            }
                        })
                    }
                }
            })
        }

        this.activity?.let {
            userViewMode.userName.observe(it, Observer {
                name.text=it.toString()
            })
        }

        this.activity?.let {
            userViewMode.userConstellation.observe(it, Observer {
                constellation.text="星座 :  "+it.toString()
            })
        }

        return view
    }
}