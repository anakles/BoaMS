package ai_projekt.boams

import android.content.Intent
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
import org.apache.commons.net.ftp.FTPSClient
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    var ldapConnection = LDAPConnection()
    val URL = "9ntfn2zneyc83qlo.myfritz.net"
    val SSLPORT = 636
    var USER : String? = null
    var DISPLAYNAME = ""

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
                //ToDo: Remove the admin credentials before productive use!!!
                if(txt_username.text.toString() == "admin" &&
                   txt_password.text.toString() == "admin"){
                    authenticated = true
                    USER = txt_username.text.toString()
                    DISPLAYNAME = txt_username.text.toString()
                }
                else {
                    val threadLdap = GlobalScope.launch {
                        authenticated = authenticateLDAP(username.toString(), pwd.toString())}


                    delay(1000)
                    threadLdap.join()
                }

                //Remove bar once an authorized connection was created
                if(!authenticated)
                    showWrongCredentials()
                else {
                    runOnUiThread{
                        txt_loadingNotification.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.colorTextRight))
                        txt_loadingNotification.text = "Einen Augenblick Geduld. Sie werden weitergeleitet"
                    }
                    delay(1000)
                    var intent = Intent(this@MainActivity, MenuActivity::class.java)
                    intent.putExtra("USERNAME", USER)
                    intent.putExtra("DISPLAYNAME", DISPLAYNAME)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    runOnUiThread { startActivity(intent)}
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
        USER = user
        val BINDDN = "CN=$USER,OU=BENUTZER,DC=AIPROJEKT,DC=LOCAL"
        val PW = pwd

        try {
            // Create an SSLUtil instance that is configured to trust any certificate,
            // and use it to create a socket factory.
            val sslUtil = SSLUtil(TrustAllTrustManager())
            val sslSocketFactory = sslUtil.createSSLSocketFactory()

            // Establish a secure connection using the socket factory.
            val connection = LDAPConnection(sslSocketFactory)
            connection.connect(URL, SSLPORT)

            if (!connection.isConnected){
                println("No connection established")
                return false
            }
            println("Secure SSL connection to $URL at port $SSLPORT established")

            //Binding = Creating "user session"
            connection.bind(BINDDN, PW)

            //Globally safe connection:
            ldapConnection = connection


            //Query the user entry from the directory
            val entry = ldapConnection.getEntry(BINDDN)
            //Get the display name from the queried entry
            DISPLAYNAME = entry.getAttributeValue("DisplayName")

            //ToDo: Remove later:
            println("Closing connection...")
            connection.close()

            if (connection.isConnected)
                println("Connection could not be closed")
            else
                println("Connection to $URL was closed")

            return true

        } catch (e: Exception) {
            e.printStackTrace()
            println("ERROR >>> Could not connect to server.")
            return false
        }
    }

}


