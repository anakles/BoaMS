package ai_projekt.boams

import ai_projekt.boams.ai_project.boams.entities.Chatroom
import ai_projekt.boams.ai_project.boams.entities.Message
import ai_projekt.boams.ai_project.boams.entities.User
import ai_projekt.boams.ai_project.boams.entities.getExampleMessages
import ai_projekt.boams.ai_project.boams.utils.getMessagesOfChatroom
import ai_projekt.boams.ai_project.boams.utils.readFromFile
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.json.JSONObject

class Fragment_Message : Fragment(){

    var messages : ArrayList<Message> = ArrayList()
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


        //Setting on CLick Listener for the button:
        val button = view.findViewById<Button>(R.id.button_send_message)
        button.setOnClickListener {
            onClick_sendMessage()
        }


        return view
    }


    private fun getMessages(chatroom_id : Int) : ArrayList<Message> {
        val json = getMessagesOfChatroom(corresponding_chatroom!!.chatroomId)

        for(i in 0 until json!!.length()){
            val json_message = json!!.get(i) as JSONObject
            messages.add(Message(json_message))
        }

        if(messages.isEmpty())
            messages = getExampleMessages()

        return messages
    }

    fun onClick_sendMessage(){
        val message_txt = view!!.findViewById<TextView>(R.id.txt_message).text
        Log.d("DEBUG", "The entered message is: $message_txt")

        if(message_txt == "")
            return

        //Get the needed information about the message:
        val user = User(JSONObject(readFromFile("userprofile.json", context!!)))

        //Create message with the retrieved information:
        val new_message = Message(
            user.userId,
            corresponding_chatroom!!.chatroomId,
            message_txt.toString())


        Toast.makeText(context, "Nachricht gesendet", Toast.LENGTH_SHORT)

    }





}