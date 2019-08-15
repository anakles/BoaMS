package ai_projekt.boams.ai_project.boams.entities

import org.json.JSONObject

class Message (var message_id : Int, var message_author : Int, var message_chatroom_id : Int, var message_text : String) {

    constructor(json : JSONObject):this(
        json.getInt("message_id"),
        json.getInt("message_author_id"),
        json.getInt("message_chatroom_id"),
        json.getString("message_txt")
    )

    override fun toString(): String {
        return "$message_author: << $message_text >>"
    }

}