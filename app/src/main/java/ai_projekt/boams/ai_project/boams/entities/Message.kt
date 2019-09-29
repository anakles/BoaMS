package ai_projekt.boams.ai_project.boams.entities

import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class Message (var message_id : Int, var message_author : Int, var message_chatroom_id : Int, var message_text : String) {

    var message_creation_date : Date ? = null
    var author_alias = "Dummy"

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
        return "$author_alias: ($message_creation_date) \n<< $message_text >>"
    }

    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("message_id", message_id)
        json.put("message_author_id", message_author)
        json.put("message_chatroom_id", message_chatroom_id)
        json.put("message_txt", message_text)

        return json
    }

    fun setMessage_creation_date(json_date: JSONArray){
        if (json_date == null)
            message_creation_date = Date()

        val date_int = IntArray(6)
        for (i in 0 until date_int.size) {
            date_int[i] = json_date[i] as Int
        }

        message_creation_date = Date(
            date_int[0],    //year
            date_int[1],    //month
            date_int[2],    //day
            date_int[3],    //hrs
            date_int[4],    //min
            date_int[5]     //sec
        )
    }

}

fun getExampleMessages() : ArrayList<Message>{
    val list = ArrayList<Message>()

    for(i in 0 until 100)
        list.add(Message(i, 1, 1,  "Das ist die Nachricht Nummer $i"))


    return list
}