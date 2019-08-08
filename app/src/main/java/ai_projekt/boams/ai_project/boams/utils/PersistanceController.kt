package ai_projekt.boams.ai_project.boams.utils

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader


fun writeToFile(filename : String, content : String, append : Boolean, context : Context){

    var temp_content : String

    try {

        if(append){
            temp_content = (readFromFile(filename, context) + content)
        }
        else{
            temp_content = content
        }

        val outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
        outputStream.write(temp_content.toByteArray())

        outputStream.flush()
        outputStream.close()

        Log.d("DEBUG", "Created file $filename with the content: $content")

    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun readFromFile(filename: String, context : Context) : String{
    val total = StringBuilder()

    try {
        val inputStream = context.openFileInput(filename)
        val r = BufferedReader(InputStreamReader(inputStream))

        var line = r.readLine()

        while (line != null) {
            println("Read from file: ($line)")
            total.append(line)
            line = r.readLine()
        }

        r.close()
        inputStream.close()
        Log.d("File", "File contents: $total")

    } catch (e: Exception) {
        e.printStackTrace()
    }

    return total.toString()
}