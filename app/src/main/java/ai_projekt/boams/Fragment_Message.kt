package ai_projekt.boams

import ai_projekt.boams.ai_project.boams.entities.Chatroom
import ai_projekt.boams.ai_project.boams.entities.Message
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class Fragment_Message : Fragment(){

    val messages : ArrayList<Message> = ArrayList()
    val corresponding_chatroom : Chatroom? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_message, container, false)

        //Initialize the list of messages:
        val temp_list = getMessages(corresponding_chatroom!!.chatroomId)


        return view
    }


    private fun getMessages(chatroom_id : Int) : ArrayList<Message> {


        return ArrayList()
    }





}