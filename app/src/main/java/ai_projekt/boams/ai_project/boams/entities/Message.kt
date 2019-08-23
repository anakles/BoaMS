package ai_projekt.boams.ai_project.boams.entities

import org.json.JSONObject

class Message (var message_id : Int, var message_author : Int, var message_chatroom_id : Int, var message_text : String) {

    constructor(json : JSONObject):this(
        json.getInt("message_id"),
        json.getInt("message_author_id"),
        json.getInt("message_chatroom_id"),
        json.getString("message_txt")
    )

    //Create a message with no message id since the message id is created by the API:
    constructor(message_author: Int, message_chatroom_id: Int, message_text: String):this(
        0,
        message_author,
        message_chatroom_id,
        message_text
    )

    override fun toString(): String {
        return "$message_author: << $message_text >>"
    }

}

fun getExampleMessages() : ArrayList<Message>{
    val list = ArrayList<Message>()

    for(i in 0 until 100)
        list.add(Message(i, 1, 1,  "Das ist die Nachricht Nummer $i"))


    return list
}