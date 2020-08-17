package com.fmohammadi.whatsapp.model

class Users() {
    var userName: String? = null
    var userEmail: String? = null
    var userStatus: String? = null
    var userImage: String? = null
    var userThumbImage: String? = null

    constructor(
        userName: String,
        userEmail: String,
        userStatus: String,
        userImage: String,
        userThumbImage: String
    ) : this() {
        this.userName = userName
        this.userEmail = userEmail
        this.userStatus = userStatus
        this.userImage = userImage
        this.userThumbImage = userThumbImage
    }

}