package ai_projekt.boams.ai_project.boams.entities

import org.json.JSONArray
import org.json.JSONObject
import java.sql.Date
import kotlin.collections.ArrayList

class Chatroom(var chatroomId : Int, val chatroomName : String, val chatroomOwner : Int) {

    var chatroom_creation_date : Date ?= null

    override fun toString() : String {
        return "${chatroomName}"
    }

    fun setChatroomCreationDate(date : java.sql.Date) {
        chatroom_creation_date = date
    }

}

fun parseChatroomsFromJSON(chatrooms_json : JSONArray) : ArrayList<Chatroom>{
    val chatrooms = ArrayList<Chatroom>()

    if(chatrooms_json.isNull(0))
        return chatrooms

    for (i in 0..chatrooms_json.length()-1){
        val json = chatrooms_json.get(i) as JSONObject

        //ToDo: repeat for each chatroom
        var chat = Chatroom(
            json.getInt("chatroom_id"),
            json.getString("chatroom_name"),
            json.getInt("chatroom_owner_id")
        )
        //ToDo: chat.setChatroomCreationDate(json.get("chatroom_creation_date") as Date)

        chatrooms.add(chat)
    }



    return chatrooms
}


fun getExampleChatrooms() : ArrayList<Chatroom> {
    val chatrooms = ArrayList<Chatroom>()

    //Example chatrooms
    chatrooms.add(Chatroom(1, "We Were Here #19", 1))
    chatrooms.add(Chatroom(2, "Das Einhorngemetzel", 1))
    chatrooms.add(Chatroom(3, "#Abi19 - KohlrABI, wir machen uns vom Acker!", 2))
    chatrooms.add(Chatroom(4, "#Abi19 - KohlrABI, wir machen uns vom Acker!", 2))
    chatrooms.add(Chatroom(5, "#Abi19 - KohlrABI, wir machen uns vom Acker!", 2))
    chatrooms.add(Chatroom(6, "#Abi19 - KohlrABI, wir machen uns vom Acker!", 2))
    chatrooms.add(Chatroom(7, "#Abi19 - KohlrABI, wir machen uns vom Acker!", 2))
    chatrooms.add(Chatroom(8, "#Abi19 - KohlrABI, wir machen uns vom Acker!", 2))

    return chatrooms
}