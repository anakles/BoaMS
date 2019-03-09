package ai_projekt.boams.ai_project.boams.entities

import java.time.LocalDateTime
import java.util.*

class User {
    var username : String ?= null
    var displayName : String ?= null
    var sessionStarted : LocalDateTime ?= null
    var userId : Int ?= null


    fun createUser(userId: Int, username : String, displayName : String){
        this.userId = userId
        this.username = username
        this.displayName = displayName
        //this.sessionStarted = LocalDateTime.now()

        println("User created: ${printUser()}")
    }

    fun printUser() : String {
        return "User: ID:${this.userId} | Login:${this.username} | Display: ${this.displayName}"
    }



}