package ai_projekt.boams.ai_project.boams.entities

import org.json.JSONArray
import org.json.JSONObject

class Chatroom {
    var chatroomId : Int ?= null
    var chatroomName : String ?= null
    var chatroomOwner : Int ?= null

    fun createChatroom(id : Int, name : String, owner : Int){
        this.chatroomId = id
        this.chatroomName = name
        this.chatroomOwner = owner

        println("Chatroom created")
    }

}

fun parseChatroomsFromJSON(chatrooms_json : JSONArray) : ArrayList<Chatroom>{
    val chatrooms = ArrayList<Chatroom>()

    if(chatrooms_json.isNull(0))
        return chatrooms

/*
       //ToDo: repeat for each chatroom
    var chat = Chatroom().createChatroom(
        //this.chatroomId = json_chatroom.getInt()
        //this.chatroomName = json_chatroom.getString()
        //this.chatroomOwner = json_chatroom.getInt()
    )

    chatrooms.add(chat)
*/

    return chatrooms
}