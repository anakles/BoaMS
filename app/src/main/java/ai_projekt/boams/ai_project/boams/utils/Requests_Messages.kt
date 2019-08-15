package ai_projekt.boams.ai_project.boams.utils

import org.json.JSONArray
import org.json.JSONObject


/**Command: /messages
 * Returns all messages */
fun getMessages(){
    val controller = ApiController()
    val json = controller.sendGetCommand("/messages")
}

/**Command:  */
fun getMessagesOfChatroom(chatroom_id : Int) : JSONArray? {
    val controller = ApiController()
    val json = controller.sendGetCommand("/messages/byChatroom/$chatroom_id")

    if(json == null)
        return null

    val messages_array = json.getJSONArray("data")
    println("CONTROL: Found these messages for the chatroom ($chatroom_id): ${json}")

    return messages_array

}
