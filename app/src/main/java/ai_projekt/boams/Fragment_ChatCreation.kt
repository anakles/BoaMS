package ai_projekt.boams

import ai_projekt.boams.ai_project.boams.entities.Chatroom
import ai_projekt.boams.ai_project.boams.entities.User
import ai_projekt.boams.ai_project.boams.entities.parseUsersFromJSON
import ai_projekt.boams.ai_project.boams.utils.*
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import org.json.JSONObject

class Fragment_ChatCreation : Fragment() {

    var contacts = ArrayList<User>()
    var selected_contacts = ArrayList<User>()

    var list_contacts : ListView? = null
    var arrayAdapter : BoaMS_ListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //ToDo: Parse the possible users into an Array:
        val jsonArray = getUsers()
        contacts = parseUsersFromJSON(jsonArray)

    }

    override fun onCreateView(inflater: LayoutInflater,        container: ViewGroup?,        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chatcreation, container, false)

        arrayAdapter = BoaMS_ListAdapter(context, R.layout.row_main_boa_ms, contacts)
        list_contacts = view.findViewById(R.id.list_contacts)
        list_contacts?.adapter = arrayAdapter

        view.findViewById<FloatingActionButton>(R.id.fab_cancel).setOnClickListener{
            onClick_abort()
        }

        view.findViewById<FloatingActionButton>(R.id.fab_create).setOnClickListener{
            onClick_create()
        }

        list_contacts?.setOnItemClickListener{
            parent, view, position, id ->
                val temp = contacts.get(position)
                selected_contacts.add(temp)
                temp.displayName = "X  " + temp.displayName
            //ToDo: unselect if clicked again!!

                refreshView()
        }

        return view
    }


    fun onClick_abort() {
        activity?.onBackPressed()
    }

    fun onClick_create() {

        //Get chatroom name:
        val chatroom_name = view?.findViewById<TextView>(R.id.txt_chatroomName)?.text.toString()

        //Case: No valid chatroom name inserted!
        if(chatroom_name.replace(" ", "") == ""){
            Toast.makeText(context, "Ungültiger Gruppenname", Toast.LENGTH_LONG)
            return
        }

        if(selected_contacts.isEmpty()){
            Toast.makeText(context, "Wähle einen Benutzer aus", Toast.LENGTH_LONG)
            return
        }

        //Get owner id:
        val owner = User(JSONObject(readFromFile(R.string.path_userprofile.toString(), context!!)))

        //Create new chatroom object:
        var newChat = Chatroom(0, chatroom_name, owner.userId)
        //newChat.users = selected_contacts

        //POST chatroom to API:
        val responseChatroom_json = postChatroom(newChat)
        if(responseChatroom_json != null){
            //Update chatroom entity with response Entity:
            newChat = Chatroom(responseChatroom_json)

            //Add selected users to chatroom:
            var full_upload = true

            for(i in 0 until selected_contacts.size){
                val temp_contact = selected_contacts.get(i)
                if(temp_contact.userId == newChat.chatroomOwner)
                    continue

                val responseEntity = addUserToChatroom(newChat, selected_contacts.get(i))
                if(responseEntity == null)
                    full_upload = false
            }

            Toast.makeText(context, "Gruppe erstellt", Toast.LENGTH_SHORT)

            if(!full_upload)
                Toast.makeText(context, "Es konnten nicht alle Nutzer hinzugefügt werden", Toast.LENGTH_SHORT)

            activity?.onBackPressed()
            return
        }
        else{
            Toast.makeText(context, "Gruppe konnte nicht erstellt werden", Toast.LENGTH_LONG)
        }
    }

    fun refreshView() {
        activity?.runOnUiThread(Runnable {
            arrayAdapter?.notifyDataSetChanged()
            arrayAdapter = BoaMS_ListAdapter(context, R.layout.row_main_boa_ms, contacts)
            list_contacts?.adapter = arrayAdapter

            view?.invalidate()
        })
    }
}