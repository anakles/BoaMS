package ai_projekt.boams.ai_project.boams.entities

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