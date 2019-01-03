package ai_projekt.boams

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_login.setOnClickListener {
            retrieveLoginVariables()
        }

        progressBar_test.visibility = View.INVISIBLE

    }

    fun retrieveLoginVariables(){
        var emptyFields = false

        if(txt_username.text.isBlank()){
            showWrongUsername()
            emptyFields = true
        }
        else
            txt_warningUsername.text = ""

        if(txt_password.text.isBlank()){
            showWrongPassword()
            emptyFields = true
        }
        else
            txt_warningPassword.text = ""

        if(!emptyFields){
            val username = txt_username.text
            val pwd = txt_password.text

            //Show progress bar while logging in
            progressBar_test.visibility = View.VISIBLE
            txt_loadingNotification.text = "Wird angemeldet..."

            //Try to login
            val wasAuthenticated = authenticateLDAP("test", 12345)


            //Remove bar once an authorized connection was created
            if(!wasAuthenticated)
                showWrongCredentials()

            else
                txt_loadingNotification.text = ""

            progressBar_test.visibility = View.INVISIBLE

        }
    }


    fun showWrongUsername(){
        txt_warningUsername.text = "Dieses Feld ist ein Pflichtfeld"
    }

    fun showWrongPassword(){
        txt_warningPassword.text = "Dieses Feld ist ein Pflichtfeld"
    }

    fun showWrongCredentials(){
        txt_loadingNotification.setTextColor(Color.RED)
        txt_loadingNotification.text = "Falsche Zugangsdaten"
    }


}

fun authenticateLDAP(host : String, port : Int): Boolean {
    println("Logging in to server $host at port $port ...")
    var success : Boolean

    try {
        //LDAP stuff


        success = true;
    }
    catch (e : Exception){
        e.printStackTrace()
        println("ERROR >>> Could not connect to server.")
        success = false;
    }

    return success
}

