package ai_projekt.boams.ai_project.boams.utils

import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ApiController {
    val API_URL = "http://qj6wy3ivstgbxxcx.myfritz.net"
    val API_PORT = 3306


    fun sendGetCommand (command : String) : JSONObject? {
        val url = URL("${this.API_URL}:${this.API_PORT}$command")
        var jsonResponse : JSONObject ?= null

        val apiRunnable = Runnable {
            with(url.openConnection() as HttpURLConnection) {
                // optional default is GET
                requestMethod = "GET"

                println("\nSending 'GET' request to URL : $url")
                println("Response Code : $responseCode")

                BufferedReader(InputStreamReader(inputStream)).use {
                    val response = StringBuffer()

                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    //ToDo: Parse JSON:
                    val responseString = response.toString()
                    //println("Raw response: $responseString")

                    jsonResponse = JSONObject(
                            responseString.substring(
                            responseString.indexOf("{"),
                            responseString.lastIndexOf("}") + 1
                        )
                    )
                    println("Parsed Json: ${jsonResponse}")
                }
            }
        }

        val apiThread = Thread(apiRunnable)
        apiThread.start()


        apiThread.join()

        return jsonResponse
    }


    fun sendPostCommand (command : String) : JSONObject? {
        val url = URL("${this.API_URL}:${this.API_PORT}$command")
        var jsonResponse : JSONObject ?= null

        val apiRunnable = Runnable {
            with(url.openConnection() as HttpURLConnection) {

                requestMethod = "POST"

                println("\nSending 'POST' request to URL : $url")
                println("Response Code : $responseCode")

                BufferedReader(InputStreamReader(inputStream)).use {
                    val response = StringBuffer()

                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    //ToDo: Parse JSON:
                    val responseString = response.toString()
                    //println("Raw response: $responseString")

                    jsonResponse = JSONObject(responseString.substring(responseString.indexOf("{"), responseString.lastIndexOf("}")+1))
                    println("Parsed Json: ${jsonResponse}")
                }
            }
        }

        val apiThread = Thread(apiRunnable)
        apiThread.start()


        apiThread.join()

        return jsonResponse
    }


    fun sendPutCommand (command : String) : JSONObject? {
        val url = URL("${this.API_URL}:${this.API_PORT}$command")
        var jsonResponse : JSONObject ?= null

        val apiRunnable = Runnable {
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "PUT"

                println("\nSending 'PUT' request to URL : $url")
                println("Response Code : $responseCode")

                BufferedReader(InputStreamReader(inputStream)).use {
                    val response = StringBuffer()

                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    //ToDo: Parse JSON:
                    val responseString = response.toString()
                    //println("Raw response: $responseString")

                    jsonResponse = JSONObject(responseString.substring(responseString.indexOf("{"), responseString.lastIndexOf("}")+1))
                    println("Parsed Json: ${jsonResponse}")
                }
            }
        }

        val apiThread = Thread(apiRunnable)
        apiThread.start()
        apiThread.join()

        return jsonResponse
    }


    fun sendDeleteCommand (command : String) : JSONObject? {
        val url = URL("${this.API_URL}:${this.API_PORT}$command")
        var jsonResponse : JSONObject ?= null

        val apiRunnable = Runnable {
            with(url.openConnection() as HttpURLConnection) {

                requestMethod = "DELETE"

                println("\nSending 'DELETE' request to URL : $url")
                println("Response Code : $responseCode")

                BufferedReader(InputStreamReader(inputStream)).use {
                    val response = StringBuffer()

                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    //ToDo: Parse JSON:
                    val responseString = response.toString()
                    //println("Raw response: $responseString")

                    jsonResponse = JSONObject(responseString.substring(responseString.indexOf("{"), responseString.lastIndexOf("}")+1))
                    println("Parsed Json: ${jsonResponse}")
                }
            }
        }

        val apiThread = Thread(apiRunnable)
        apiThread.start()
        apiThread.join()

        return jsonResponse
    }

}
