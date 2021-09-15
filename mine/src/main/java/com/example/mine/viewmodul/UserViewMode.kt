package com.example.mine.viewmodul

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.baselibs.Utility

class UserViewMode : ViewModel() {
    var userName= MutableLiveData<String>()
    var userConstellation= MutableLiveData<String>()

    fun initUserName(){
        userName.value=Utility.getUser().name
    }

    fun initConstellation(){
        userConstellation.value=Utility.getUser().constellation
    }
}