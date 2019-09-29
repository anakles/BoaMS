package ai_projekt.boams.ai_project.boams.utils

import android.content.Context
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.IndexOutOfBoundsException
import java.net.HttpURLConnection
import java.net.URL
import android.net.ConnectivityManager
import android.widget.Toast
import java.io.*
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*


class ApiController {
    val API_URL = "https://9ntfn2zneyc83qlo.myfritz.net"
    val API_PORT = 3306


    fun sendGetCommand (command : String) : JSONObject? {
        val url = URL("${this.API_URL}:${this.API_PORT}$command")
        var jsonResponse : JSONObject ?= null

        val apiRunnable = Runnable {
            with(url.openConnection() as HttpsURLConnection) {
                //Setting the trustManagerSSLContext sc;
                //https://stackoverflow.com/questions/16504527/how-to-do-an-https-post-from-android
                //https://stackoverflow.com/questions/49032463/kotlin-connect-to-self-signed-https-server
                sslSocketFactory = createSocketFactory(listOf("TLSv1.2"))
                hostnameVerifier = HostnameVerifier { _, _ -> true }

                //optional, default is GET
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


    fun sendPostCommand (command : String, json_body : JSONObject) : JSONObject? {
        val url = URL("${this.API_URL}:${this.API_PORT}$command")
        var apiResponseCode = 0
        var jsonResponse : JSONObject? = null

        val apiRunnable = Runnable {
            with(url.openConnection() as HttpsURLConnection) {

                sslSocketFactory = createSocketFactory(listOf("TLSv1.2"))
                hostnameVerifier = HostnameVerifier { _, _ -> true }

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
                println("Response Message : $responseMessage")

                if (apiResponseCode === HttpURLConnection.HTTP_CREATED) { //success
                    val reader = BufferedReader(
                        InputStreamReader(
                            inputStream
                        )
                    )

                    val response = StringBuffer()
                    var inputLine = reader.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = reader.readLine()
                    }
                    reader.close()
                    // print result
                    println(response.toString())
                    jsonResponse = JSONObject(response.toString())
                }
            }
        }

        val apiThread = Thread(apiRunnable)
        apiThread.start()
        apiThread.join()

        return jsonResponse
    }

    fun sendPutCommand (command : String, json_body: JSONObject) : JSONObject? {
        val url = URL("${this.API_URL}:${this.API_PORT}$command")
        var apiResponseCode = 0
        var jsonResponse : JSONObject? = null

        val apiRunnable = Runnable {
            with(url.openConnection() as HttpsURLConnection) {
                //setRequestProperty("charset", "utf-8")
                setRequestProperty("Content-Type", "application/json")

                sslSocketFactory = createSocketFactory(listOf("TLSv1.2"))
                hostnameVerifier = HostnameVerifier { _, _ -> true }

                doOutput = true
                requestMethod = "PUT"

                println("\nSending 'PUT' request to URL : $url")


                BufferedWriter(OutputStreamWriter(outputStream)).use {
                    outputStream.write(json_body.toString().toByteArray())
                    outputStream.flush()
                }

                apiResponseCode = responseCode

                println("Response Code : $apiResponseCode")
                println("Response Message : $responseMessage")

                if (apiResponseCode === HttpURLConnection.HTTP_CREATED) { //success
                    val reader = BufferedReader(
                        InputStreamReader(
                            inputStream
                        )
                    )

                    val response = StringBuffer()
                    var inputLine = reader.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = reader.readLine()
                    }
                    reader.close()
                    // print result
                    println(response.toString())
                    jsonResponse = JSONObject(response.toString())
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
            with(url.openConnection() as HttpsURLConnection) {

                sslSocketFactory = createSocketFactory(listOf("TLSv1.2"))
                hostnameVerifier = HostnameVerifier { _, _ -> true }

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

    private fun createSocketFactory(protocols: List<String>) =
        SSLContext.getInstance(protocols[0]).apply {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) = Unit
                override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) = Unit
            })
            init(null, trustAllCerts, SecureRandom())
        }.socketFactory

}
