package ai_projekt.boams.ai_project.boams.entities

import ai_projekt.boams.ai_project.boams.utils.readFromFile
import android.content.Context
import org.json.JSONObject
import java.time.LocalDateTime
import java.util.*

class User(var userId: Int, var username : String, var displayName : String){
    var sessionStarted : LocalDateTime ?= null

    constructor(json : JSONObject):this(
        userId = json.getInt("user_id"),
        username = json.getString("login_name"),
        displayName = json.getString("display_name"))

    fun printUser() : String {
        return "User: ID:${this.userId} | Login:${this.username} | Display: ${this.displayName}"
    }

    fun toJson() : JSONObject {
        val userJson = JSONObject()
        userJson.put("login_name", username)
        userJson.put("display_name", displayName)
        userJson.put("user_id", userId)

        return  userJson
    }

    fun getCurrentUserFromFile(context : Context) : User{
        val file_content = readFromFile("userprofile.json", context)
        val json = JSONObject(file_content)

        return User(json)
    }

}
