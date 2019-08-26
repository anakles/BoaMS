package ai_projekt.boams

import ai_projekt.boams.ai_project.boams.entities.*
import ai_projekt.boams.ai_project.boams.utils.*
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import org.json.JSONArray
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast


class Fragment_Chats : Fragment(){

    //var arrayAdapter : ArrayAdapter<Any> ?= null
    var arrayAdapter : BoaMS_ListAdapter ?= null
    var listChats : ListView ?= null
    var arrayChats : ArrayList<Chatroom> ?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chats, container, false)

        //Getting the list of chatrooms for this user:
        arrayChats = getChatrooms()
        val temp_array = arrayChats
        if(temp_array!!.isEmpty()){
            Log.d("DEBUG", "There are no chatrooms to be displayed")
        }

        listChats = view!!.findViewById<ListView>(R.id.list_chats)
        arrayAdapter = BoaMS_ListAdapter(context, R.layout.row_main_boa_ms, temp_array)
        listChats?.adapter = arrayAdapter


        listChats?.setOnItemClickListener(
            OnItemClickListener {
                parent, view, position, id ->
                    val chat = listChats?.getItemAtPosition(position) as Chatroom
                    val fragment_messages = Fragment_Message()
                    fragment_messages.corresponding_chatroom = chat
                    activity!!.supportFragmentManager.beginTransaction().replace(R.id.layout_fragment, fragment_messages).commit()

        })

        view.findViewById<FloatingActionButton>(R.id.fab_createChatroom).setOnClickListener{
            onClick_createChatroom()
        }

        return view
    }



    private fun getChatrooms () : java.util.ArrayList<Chatroom> {

        //Check if local userfile exists:
        //val userprofile = File(R.string.path_userprofile.toString())
        //val valid_file = userprofile.exists()
        var current_user : User

        /*if(valid_file)
            return ArrayList()

          Log.d("DEBUG", "Found an existing userprofile")
        */
        val user_json = ApiController().parseJson(readFromFile(R.string.path_userprofile.toString(), activity!!.baseContext))
                if(user_json != null) {
                    //Create user from file content
                    current_user = User(user_json)

                    //Special user for tests
                    if (current_user.username == "admin"){
                        Log.d("DEBUG", "Registered the admin test account as user")
                        return getExampleChatrooms()
                    }

                    //Scenario 1: Check online connection for valid users:
                    if (ApiController().isNetworkOnline(activity!!.baseContext)) {
                        val temp = getChatroomsForUser(current_user)
                        if (temp != null){
                            Log.d("DEBUG", "Registered the ${current_user.displayName} account as user")
                            return parseChatroomsFromJSON(temp)
                        }
            }
        }
        //Scenario 2: Network offline: read chatrooms from file:
        else{
            /*val chatroom_file = File(R.string.path_chatrooms.toString())
            val valid_chatroom_file = chatroom_file.exists()

            if(!valid_chatroom_file) {
                Log.d("DEBUG", "Registered NO account as user")
                return ArrayList()
            }

            else{*/
                Log.d("DEBUG", "Relying on the saved chatrooms as source")

                val json = readFromFile(R.string.path_chatrooms.toString(), activity!!.baseContext)
                return parseChatroomsFromJSON(ApiController().parseJson(json) as JSONArray)
            //}
        }

        return ArrayList()
    }


    /**React to FAB - createChatroom */
    fun onClick_createChatroom () {

        val new_fragment = Fragment_ChatCreation()
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.layout_fragment, new_fragment).commit()

    }
}