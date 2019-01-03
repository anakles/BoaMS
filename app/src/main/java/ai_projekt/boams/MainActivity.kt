package ai_projekt.boams

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar.visibility = ProgressBar.GONE

        btn_login.setOnClickListener {
            retrieveLoginVariables()
        }

    }

    fun retrieveLoginVariables(){
        var emptyFields = false

        if(txt_username.text.isBlank()){
            txt_warningUsername.text = "Dieses Feld ist ein Pflichtfeld"
            emptyFields = true
        }
        else
            txt_warningUsername.text = ""

        if(txt_password.text.isBlank()){
            txt_warningPassword.text = "Dieses Feld ist ein Pflichtfeld"
            emptyFields = true
        }
        else
            txt_warningPassword.text = ""

        if(!emptyFields){
            val username = txt_username.text
            val pwd = txt_password.text

            //Animate progressbar while loading
            progressBar.max = 100
            progressBar.progress = 0
            progressBar.visibility = ProgressBar.VISIBLE
            while (progressBar.progress < progressBar.max){
                progressBar.progress += 10
                Thread.sleep(1000)
            }
            //Try to login


        }
    }
}
