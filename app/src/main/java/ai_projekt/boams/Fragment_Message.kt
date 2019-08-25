package ai_projekt.boams

import ai_projekt.boams.ai_project.boams.entities.Chatroom
import ai_projekt.boams.ai_project.boams.entities.Message
import ai_projekt.boams.ai_project.boams.entities.User
import ai_projekt.boams.ai_project.boams.entities.getExampleMessages
import ai_projekt.boams.ai_project.boams.utils.getMessagesOfChatroom
import ai_projekt.boams.ai_project.boams.utils.postMessage
import ai_projekt.boams.ai_project.boams.utils.readFromFile
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import android.app.Activity
import android.view.inputmethod.InputMethodManager


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


        listMessages = view!!.findViewById(R.id.list_messages)
        arrayAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, messages?.toArray())
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

        if(message_txt.replace(Regex(" "), "") == ""){
            return
        }

        //Get the needed information about the message:
        val user = User(JSONObject(readFromFile(R.string.path_userprofile.toString(), context!!)))

        //Create message with the retrieved information:
        val new_message = Message(
            0,
            user.userId,
            corresponding_chatroom!!.chatroomId,
            message_txt.toString())

        //ToDo: Send message to api, then redraw the activity
        val success = postMessage(new_message)

        //If the message was successfully sent, clear the textbox and lose the focus
        if(success){
            Toast.makeText(context, "Nachricht gesendet", Toast.LENGTH_LONG)
            Log.d("BOAMS", "Nachricht gesendet")
            view!!.findViewById<TextView>(R.id.txt_message).text = ""
            view!!.findViewById<TextView>(R.id.txt_message).clearFocus()

            //Refreshing the UI to show the new messages
            messages.add(new_message)
            activity?.runOnUiThread(Runnable {
                arrayAdapter?.notifyDataSetChanged()
                arrayAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, messages?.toArray())
                listMessages?.adapter = arrayAdapter

                view?.invalidate()
            })

            hideKeyboard(activity!!)
        }

        else {
            Toast.makeText(context, "Nachricht konnte nicht gesendet werden", Toast.LENGTH_LONG)
            Log.d("BOAMS", "Nachricht konnte nicht gesendet werden")
        }
    }


    // From : https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}