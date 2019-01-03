package ai_projekt.boams

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.unboundid.ldap.sdk.LDAPConnection
import com.unboundid.util.ssl.SSLUtil
import com.unboundid.util.ssl.TrustAllTrustManager
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
            val wasAuthenticated = authenticateLDAP(username.toString(), pwd.toString())


            //Remove bar once an authorized connection was created
            if(!wasAuthenticated)
                showWrongCredentials()
            else{
                txt_loadingNotification.text = ""
                progressBar_test.visibility = View.INVISIBLE
            }

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



    fun authenticateLDAP(user : String, pwd : String): Boolean {
        var success : Boolean

        val URL = "qj6wy3ivstgbxxcx.myfritz.net"
        val SSLPORT = 636
        val BINDDN = "CN=$user,OU=BENUTZER,DC=AIPROJEKT,DC=LOCAL"
        val PW = pwd

        try{
            // Create an SSLUtil instance that is configured to trust any certificate,
            // and use it to create a socket factory.
            val sslUtil = SSLUtil(TrustAllTrustManager())
            val sslSocketFactory = sslUtil.createSSLSocketFactory()

            // Establish a secure connection using the socket factory.
            val connection = LDAPConnection(sslSocketFactory)
            connection.connect(URL, SSLPORT)

            if(connection.isConnected)
                println("Secure SSL connection to $URL at port $SSLPORT established")
            else{
                println("No connection established")
                return false
            }

            val bindResult = connection.bind(BINDDN, PW)

            println("Closing connection...")
            connection.close()

            if(!connection.isConnected)
                println("Connection to $URL was closed")
            else
                println("Connection could not be closed")

            success = true
        }
        catch (e : Exception){
            e.printStackTrace()
            println("ERROR >>> Could not connect to server.")
            return false
        }

        return success
    }

}


