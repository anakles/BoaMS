package ai_projekt.boams

import ai_projekt.boams.ai_project.boams.entities.Chatroom
import ai_projekt.boams.ai_project.boams.entities.User
import ai_projekt.boams.ai_project.boams.entities.getExampleChatrooms
import ai_projekt.boams.ai_project.boams.entities.parseChatroomsFromJSON
import ai_projekt.boams.ai_project.boams.utils.ApiController
import ai_projekt.boams.ai_project.boams.utils.getChatroomsForUser
import ai_projekt.boams.ai_project.boams.utils.readFromFile
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class Fragment_Chats : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chats, container, false)



        //Getting the list of chatrooms for this user:
        val arrayChats : ArrayList<Chatroom> = getChatrooms()
        if(arrayChats.isEmpty()){
            Log.d("DEBUG", "There are no chatrooms to be displayed")
        }

        val listChats = view.findViewById<ListView>(R.id.list_chats)
        val arrayAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, arrayChats.toArray())
        listChats.adapter = arrayAdapter

        return view
    }


    private fun getChatrooms () : ArrayList<Chatroom> {

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

}