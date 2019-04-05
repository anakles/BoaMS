package ai_projekt.boams.ai_project.boams.utils

import org.json.JSONObject

/**Command: /chatrooms
 * Returns all chatrooms */
fun getChatrooms() {
    val controller = ApiController()
    val json = controller.sendGetCommand("/chatrooms")
}





fun getChatroomsFromJson(json : JSONObject){
    //Get array of chatrooms
    var json_chatrooms = json.getJSONArray("data")
    println("JSON Array of chatrooms is: $json_chatrooms")

    //ToDo: Loop over the chatroom array and create a chatroom object for each chatroom:
    for (i in 0 until json_chatrooms.length()) {
        val json_chat = json_chatrooms.getJSONObject(i)

        //parse json chatroom object into kotlin object:




    }

}