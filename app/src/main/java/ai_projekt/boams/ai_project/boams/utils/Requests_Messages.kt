package ai_projekt.boams.ai_project.boams.utils


import ai_projekt.boams.ai_project.boams.entities.Message
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.view.*
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


//ToDo: Implement a method to react to the HTTP Return code: (like when the message wasn't sent)
fun postMessage(message : Message) : Boolean {
    val controller = ApiController()

    Log.d("BOAMS", "Trying to POST message: ${message.toJson().toString()}")

    val responseCode = controller.sendPostCommand("/messages", message.toJson())

    println("CONTROL: Sending message: response code: $responseCode")

    if(responseCode > 400)
        return false


    return true
}
