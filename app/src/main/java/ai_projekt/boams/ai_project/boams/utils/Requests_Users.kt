package ai_projekt.boams.ai_project.boams.utils

import ai_projekt.boams.ai_project.boams.entities.Chatroom
import ai_projekt.boams.ai_project.boams.entities.User

/**Command: /users
 * Returns all users */
fun getUsers(){
    val controller = ApiController()
    val json = controller.sendGetCommand("/users")
}


/**
 * Command: users/byLogin/{loginName} */
fun getUserByLoginName (loginname: String) : User? {
    val controller = ApiController()
    val json =  controller.sendGetCommand("/users/byLogin/$loginname")

    if(json == null)
        return null

    var user = User()
    user.createUser(
        json.getInt("user_id"),
        json.getString("login_name"),
        json.getString("display_name")
    )

    return user
}

fun getChatroomsForUser(user: User) : ArrayList<Chatroom>{
    val chatrooms = ArrayList<Chatroom>()
    val controller = ApiController();
    val json = controller.sendGetCommand("/users/${user.userId}/chatrooms")

    if(json == null)
        return chatrooms

    //Test
    println("CONTROL: The user (${user.printUser()}) is in the following chatrooms: $json")

    //ToDo: parse JSON into Array of chatroom objects



    return chatrooms
}