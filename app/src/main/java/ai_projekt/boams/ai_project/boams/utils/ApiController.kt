package ai_projekt.boams.ai_project.boams.utils

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.IndexOutOfBoundsException
import java.net.HttpURLConnection
import java.net.URL

class ApiController {
    val API_URL = "http://9ntfn2zneyc83qlo.myfritz.net"
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
                    //Parse JSON string to Object:
                    jsonResponse = parseJson(response.toString())

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
                    //Parse JSON string to Object:
                    jsonResponse = parseJson(response.toString())

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
                    //Parse JSON string to Object:
                    jsonResponse = parseJson(response.toString())

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
                    //Parse JSON string to Object:
                    jsonResponse = parseJson(response.toString())

                }
            }
        }

        val apiThread = Thread(apiRunnable)
        apiThread.start()
        apiThread.join()

        return jsonResponse
    }

    private fun parseJson(rawJson : String) : JSONObject? {
        //Catch exception for "empty" JSON strings (IndexOutOfBounds):

        try {
            var jsonObject = JSONObject(
                //rawJson.substring(
                //    rawJson.indexOf("{"),
                //   rawJson.lastIndexOf("}") + 1
                //)
                rawJson
            )
            println("Parsed Json: ${jsonObject}")

            return jsonObject

        } catch (e: IndexOutOfBoundsException) {
            //e.printStackTrace()
            println("ERROR @ API-CONTROLLER: The received JSON string is to short to be parsed into an object, probably empty!")
        } catch (e_json : JSONException){
            //e_json.printStackTrace()
            println("ERROR @ API-CONTROLLER: The received JSON string cannot be parsed into JSONObject, trying JSONArray instead:")

            var jsonArray = JSONArray(
                rawJson
            )
            println("Parsed Json (Array) is: $jsonArray")

            //"data" is the key to extract the array from the JSONObject
            return JSONObject( "{ \"data\" : " +jsonArray.toString()+ "}")
        }

        return null
    }
}
