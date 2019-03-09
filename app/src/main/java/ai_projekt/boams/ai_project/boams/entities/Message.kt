package ai_projekt.boams.ai_project.boams.entities

import java.sql.Connection

class Message {
    var message_text: String? = null
    var message_author: Int? = null
    var message_chatroom_id: Int? = null

    fun createMessage(conn: Connection, text: String, name: String, chatroom: String) {
        message_text = text

        message_author = executeStatement_requestAuthorId(conn, name)
        if (message_author == 0) {
            println("There is no author with this name")
            return
        }

        message_chatroom_id = executeStatement_requestChatroomId(conn, chatroom)
        if (message_chatroom_id == 0) {
            println("There is no chatroom with this name")
            return
        }

        val updatedRows = executeStatement_createMessage(conn)
        if (updatedRows == 0)
            println("ERROR >>> Message could not be created")
    }


    fun executeStatement_requestAuthorId(conn: Connection, name: String): Int {

        return 0
    }


    fun executeStatement_requestChatroomId(conn: Connection, chatroom_name: String): Int {

        return 0
    }


    fun executeStatement_createMessage(conn: Connection): Int {

        return 0
    }
}