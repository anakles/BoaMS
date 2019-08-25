package ai_projekt.boams.ai_project.boams.utils

import android.content.Context
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.IndexOutOfBoundsException
import java.net.HttpURLConnection
import java.net.URL
import android.net.ConnectivityManager
import android.widget.Toast
import java.io.BufferedWriter
import java.io.OutputStreamWriter


class ApiController {
    val API_URL = "http://9ntfn2zneyc83qlo.myfritz.net"
    //val API_URL = "http://192.168.178.24"
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


    fun sendPostCommand (command : String, json_body : JSONObject) : Int {
        val url = URL("${this.API_URL}:${this.API_PORT}$command")
        var apiResponseCode = 0

        val apiRunnable = Runnable {
            with(url.openConnection() as HttpURLConnection) {


                //setRequestProperty("charset", "utf-8")
                setRequestProperty("Content-Type", "application/json")

                //requestMethod = "POST"
                doOutput = true

                println("\nSending 'POST' request to URL : $url")


                BufferedWriter(OutputStreamWriter(outputStream)).use {
                    outputStream.write(json_body.toString().toByteArray())
                    outputStream.flush()
                }


                apiResponseCode = responseCode
                println("Response Code : $apiResponseCode")
            }
        }

        val apiThread = Thread(apiRunnable)
        apiThread.start()


        apiThread.join()

        return apiResponseCode
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

    fun parseJson(rawJson : String) : JSONObject? {
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

    fun isNetworkOnline(context : Context): Boolean {
        var status = false

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo

        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
            Toast.makeText(context, "Connected", Toast.LENGTH_LONG).show()
            status = true
        } else {
            Toast.makeText(context, "Not Connected", Toast.LENGTH_LONG).show()
            status = false
        }

        return status
    }
}
