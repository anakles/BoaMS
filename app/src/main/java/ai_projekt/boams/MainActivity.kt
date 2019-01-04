package ai_projekt.boams

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.unboundid.ldap.sdk.LDAPConnection
import com.unboundid.util.ssl.SSLUtil
import com.unboundid.util.ssl.TrustAllTrustManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    var ldapConnection : LDAPConnection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_login.setOnClickListener { retrieveLoginVariables() }

        progressBar_test.visibility = View.INVISIBLE
    }

    private fun retrieveLoginVariables(){
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


            var authenticated = false
            GlobalScope.launch {
                //Try to login
                val threadLdap = GlobalScope.launch {
                    authenticated = authenticateLDAP(username.toString(), pwd.toString())}


                delay(1000)
                threadLdap.join()

                //Remove bar once an authorized connection was created
                if(!authenticated)
                    showWrongCredentials()
                else {
                    runOnUiThread{
                        txt_loadingNotification.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.colorTextRight))
                        txt_loadingNotification.text = "Einen Augenblick Geduld. Sie werden weitergeleitet"
                    }
                    delay(1000)
                    runOnUiThread { startActivity(Intent(this@MainActivity, MenuActivity::class.java))}
                }

                runOnUiThread { progressBar_test.visibility = View.INVISIBLE }

                println("Saved session is: ${ldapConnection?.isConnected}")
            }
        }
    }

    private fun showWrongUsername(){
        runOnUiThread { txt_warningUsername.text = "Dieses Feld ist ein Pflichtfeld"}
    }

    private fun showWrongPassword(){
        runOnUiThread { txt_warningPassword.text = "Dieses Feld ist ein Pflichtfeld" }
    }

    private fun showWrongCredentials(){
        runOnUiThread {
            txt_loadingNotification.setTextColor(ContextCompat.getColor(this, R.color.colorTextWrong))
            txt_loadingNotification.text = "Falsche Zugangsdaten"
        }
    }



    private fun authenticateLDAP(user : String, pwd : String): Boolean {

        val URL = "qj6wy3ivstgbxxcx.myfritz.net"
        val SSLPORT = 636
        val BINDDN = "CN=$user,OU=BENUTZER,DC=AIPROJEKT,DC=LOCAL"
        val PW = pwd

        try {
            // Create an SSLUtil instance that is configured to trust any certificate,
            // and use it to create a socket factory.
            val sslUtil = SSLUtil(TrustAllTrustManager())
            val sslSocketFactory = sslUtil.createSSLSocketFactory()

            // Establish a secure connection using the socket factory.
            val connection = LDAPConnection(sslSocketFactory)
            connection.connect(URL, SSLPORT)

            if (connection.isConnected)
                println("Secure SSL connection to $URL at port $SSLPORT established")
            else {
                println("No connection established")
                return false
            }

            //Binding = Creating "user session"
            connection.bind(BINDDN, PW)

            //Globally safe connection:
            ldapConnection = connection

            //ToDo: Remove later:
            println("Closing connection...")
            connection.close()

            if (!connection.isConnected)
                println("Connection to $URL was closed")
            else
                println("Connection could not be closed")

            return true
        } catch (e: Exception) {
            e.printStackTrace()
            println("ERROR >>> Could not connect to server.")
            return false
        }
    }
}


