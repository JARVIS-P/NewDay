package com.example.mine.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.example.baselibs.Utility
import com.example.mine.R

@Route(path = "/mine/mine_fragment")
class MineFragment : Fragment(){

    private lateinit var user:ConstraintLayout
    private lateinit var like:ConstraintLayout
    private lateinit var safety:ConstraintLayout
    private lateinit var chat:ConstraintLayout
    private lateinit var our:ConstraintLayout
    private lateinit var product:ConstraintLayout
    private lateinit var back:ConstraintLayout
    private lateinit var avatar:ImageView
    private lateinit var number:TextView
    private lateinit var name:TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_mine,container,false)

        user=view.findViewById(R.id.mine_root_user)
        like=view.findViewById(R.id.mine_root_like)
        safety=view.findViewById(R.id.mine_root_safety)
        chat=view.findViewById(R.id.mine_root_chat)
        our=view.findViewById(R.id.mine_root_our)
        product=view.findViewById(R.id.mine_root_product)
        back=view.findViewById(R.id.mine_root_back)
        avatar=view.findViewById(R.id.mine_user_avatar)
        number=view.findViewById(R.id.mine_user_number)
        name=view.findViewById(R.id.mine_user_name)

        user.setOnClickListener {
            val transaction=activity?.supportFragmentManager?.beginTransaction();
            transaction?.replace(R.id.mine_root_layout,UserFragment())
            transaction?.addToBackStack(null)
            transaction?.commit()
        }

        like.setOnClickListener {
            val transaction=activity?.supportFragmentManager?.beginTransaction();
            transaction?.replace(R.id.mine_root_layout,LikeFragment())
            transaction?.addToBackStack(null)
            transaction?.commit()
        }

        safety.setOnClickListener {
            val transaction=activity?.supportFragmentManager?.beginTransaction();
            transaction?.replace(R.id.mine_root_layout,SafetyFragment())
            transaction?.addToBackStack(null)
            transaction?.commit()
        }

        chat.setOnClickListener {
            Toast.makeText(context,"意见反馈",Toast.LENGTH_SHORT).show()
        }

        our.setOnClickListener {
            Toast.makeText(context,"关于我们",Toast.LENGTH_SHORT).show()
        }

        product.setOnClickListener {
            Toast.makeText(context,"产品初衷",Toast.LENGTH_SHORT).show()
        }

        back.setOnClickListener {
            val edit=context?.getSharedPreferences("MainActivity",Context.MODE_PRIVATE)?.edit()
            edit?.putString("isLogin","false")
            edit?.apply()
            ARouter.getInstance().build("/app/app_login").navigation()
            activity?.finish()
        }

        name.text=Utility.getUser().name
        number.text="手机号 :  "+Utility.getUser().phone

        return view
    }

}