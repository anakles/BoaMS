package ai_projekt.boams.ai_project.boams.utils

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

    if(json == null){
        return null
    }

    var user = User()
    user.createUser(
        json.getInt("user_id"),
        json.getString("login_name"),
        json.getString("display_name")
    )

    return user
}