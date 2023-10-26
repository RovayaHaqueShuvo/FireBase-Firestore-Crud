package com.webcode.firebasefirestorecrud

import java.security.Timestamp

data class Data(
    var id:String?=null,
    val title:String?=null,
    val description:String?=null,
    val timestamp: Timestamp?= null
)
