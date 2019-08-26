package ai_projekt.boams.ai_project.boams.utils

import ai_projekt.boams.ai_project.boams.entities.Chatroom
import ai_projekt.boams.ai_project.boams.entities.Message
import ai_projekt.boams.ai_project.boams.entities.User
import android.util.Log
import org.json.JSONObject

/**Command: /chatrooms
 * Returns all chatrooms */
fun getChatrooms() {
    val controller = ApiController()
    val json = controller.sendGetCommand("/chatrooms")
}

//ToDo: Implement a method to react to the HTTP Return code: (like when the message wasn't sent)
fun postChatroom(chatroom : Chatroom) : JSONObject? {
    val controller = ApiController()

    Log.d("BOAMS", "Trying to POST chatroom: ${chatroom.toJson().toString()}")

    val responseEntity = controller.sendPostCommand("/chatrooms", chatroom.toJson())

    println("CONTROL: Creating chatroom: $responseEntity")

    return responseEntity
}

fun addUserToChatroom(chatroom : Chatroom, user : User) : JSONObject?  {

    val controller = ApiController()
    Log.d("BOAMS", "Trying to PUT user ${user.toJson().toString()} to chatroom: ${chatroom.toJson().toString()}")

    val responseEntity = controller.sendPutCommand("/chatroom/${chatroom.chatroomId}/addUser/${user.userId}", chatroom.toJson())

    println("CONTROL: Creating chatroom: $responseEntity")

    return responseEntity
}



fun getChatroomsFromJson(json : JSONObject){
    //Get array of chatrooms
    var json_chatrooms = json.getJSONArray("data")
    println("JSON Array of chatrooms is: $json_chatrooms")

    //ToDo: Loop over the chatroom array and create a chatroom object for each chatroom:
    for (i in 0 until json_chatrooms.length()) {
        val json_chat = json_chatrooms.getJSONObject(i)

        //parse json chatroom object into kotlin object:
    }
}