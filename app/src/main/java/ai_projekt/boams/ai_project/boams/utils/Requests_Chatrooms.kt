package ai_projekt.boams.ai_project.boams.utils

/**Command: /chatrooms
 * Returns all chatrooms */
fun getChatrooms() {
    val controller = ApiController()
    val json = controller.sendGetCommand("/chatrooms")
}
