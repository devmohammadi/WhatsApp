package com.fmohammadi.whatsapp.model

import java.text.SimpleDateFormat
import java.util.*

class FriendlyMessage() {
    var idMessage: String? = null
    var nameMessage: String? = null
    var textMessage: String? = null
    var timeMessage: Long? = null



    constructor(
        idMessage:String,
        nameMessage:String,
        textMessage:String,
        timeMessage:Long
    ):this(){
        this.idMessage = idMessage
        this.nameMessage = nameMessage
        this.textMessage = textMessage
        this.timeMessage = timeMessage
    }

    fun showTime(friendlyMessage:FriendlyMessage): String {
        val simple = SimpleDateFormat("mm/ss")
        var result = Date(friendlyMessage.timeMessage!!)
        return simple.format(result)
    }
}