package com.example.mine.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mine.R

class MineRootFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.root_fragment,container,false)
        val transition=activity?.supportFragmentManager?.beginTransaction()
        transition?.replace(R.id.mine_root_layout,MineFragment())
        transition?.commit()
        return view
    }
}