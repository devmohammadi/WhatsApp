package com.fmohammadi.whatsapp.model

import java.text.SimpleDateFormat
import java.util.*

class FriendlyMessage() {
    var idMessage: String? = null
    var nameMessageSend: String? = null
    var nameMessageReceive: String? = null
    var textMessage: String? = null
    var timeMessage: Long? = null


    constructor(
        idMessage: String,
        nameMessageSend: String,
        nameMessageReceive: String,
        textMessage: String,
        timeMessage: Long
    ) : this() {
        this.idMessage = idMessage
        this.nameMessageSend = nameMessageSend
        this.nameMessageReceive = nameMessageReceive
        this.textMessage = textMessage
        this.timeMessage = timeMessage
    }

    fun showTime(time: Long): String {
        val simple = SimpleDateFormat("h:mm a")
        var result = Date(time)
        return simple.format(result)
    }
}