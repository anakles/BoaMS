package ai_projekt.boams.ai_project.boams.utils


/**Command: /messages
 * Returns all messages */
fun getMessages(){
    val controller = ApiController()
    val json = controller.sendGetCommand("/messages")
}
