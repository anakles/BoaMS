package ai_projekt.boams

import ai_projekt.boams.ai_project.boams.entities.Chatroom
import ai_projekt.boams.ai_project.boams.entities.Message
import ai_projekt.boams.ai_project.boams.utils.getMessagesOfChatroom
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import kotlinx.android.synthetic.main.fragment_message.*
import org.json.JSONObject
import java.io.FileDescriptor
import java.io.PrintWriter


class Fragment_Message : Fragment(){

    val messages : ArrayList<Message> = ArrayList()
    var corresponding_chatroom : Chatroom? = null

    var arrayAdapter : ArrayAdapter<Any> ?= null
    var listMessages : ListView ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Check for directory
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_message, container, false)

        //Initialize the list of messages:
        getMessages(corresponding_chatroom!!.chatroomId)


        val temp_array = messages
        if(temp_array.isEmpty()){
            Log.d("DEBUG", "There are no messages to be displayed")
        }

        listMessages = view!!.findViewById<ListView>(R.id.list_messages)
        arrayAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, temp_array?.toArray())
        listMessages?.adapter = arrayAdapter

        return view
    }


    private fun getMessages(chatroom_id : Int) : ArrayList<Message> {
        val json = getMessagesOfChatroom(corresponding_chatroom!!.chatroomId)

        if(json == null) {
            Log.d("DEBUG", "There are no messages to be displayed!")
            return messages
        }

        for(i in 0 until json.length()){
            val json_message = json.get(i) as JSONObject
            messages.add(Message(json_message))
        }

        return messages
    }





}