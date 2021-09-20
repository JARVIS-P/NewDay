package com.example.today.db

import cn.bmob.v3.BmobObject

data class RecyclerItem(var moon:String,var day:String,var week:String,var talk:List<String>,var isLike:ArrayList<Boolean>) : BmobObject()