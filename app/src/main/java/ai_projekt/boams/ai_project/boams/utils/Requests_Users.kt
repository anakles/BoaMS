package ai_projekt.boams.ai_project.boams.utils

import ai_projekt.boams.MenuActivity
import ai_projekt.boams.ai_project.boams.entities.Chatroom
import ai_projekt.boams.ai_project.boams.entities.User
import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.OutputStreamWriter

/**Command: /users
 * Returns all users */
fun getUsers(){
    val controller = ApiController()
    val json = controller.sendGetCommand("/users")
}


/**
 * Command: users/byLogin/{loginName} */
fun getUserByLoginName (loginname: String) : User? {
    val controller = ApiController()
    val json =  controller.sendGetCommand("/users/byLogin/$loginname")

    if(json == null)
        return null

    var user = User(
        json.getInt("user_id"),
        json.getString("login_name"),
        json.getString("display_name")
    )

    //ToDo: Save user.json locally

    return user
}

fun getChatroomsForUser(user: User) : JSONArray? {
    val chatrooms = ArrayList<Chatroom>()
    val controller = ApiController();
    val json = controller.sendGetCommand("/users/${user.userId}/chatrooms")

    if(json == null)
        return null

    val chatroom_array = json.getJSONArray("data")
    println("CONTROL: The user (${user.printUser()}) is in the following chatrooms: $chatroom_array")


    return chatroom_array
}
