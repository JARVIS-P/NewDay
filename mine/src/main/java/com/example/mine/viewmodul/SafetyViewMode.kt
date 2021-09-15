package com.example.mine.viewmodul

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.baselibs.Utility

class SafetyViewMode : ViewModel()  {

    val number= MutableLiveData<String>()

    fun initNumber(){
        number.value=Utility.getUser().phone
    }

}