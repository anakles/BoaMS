package ai_projekt.boams

import ai_projekt.boams.ai_project.boams.FtpActivity
import ai_projekt.boams.ai_project.boams.entities.*
import ai_projekt.boams.ai_project.boams.utils.*

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.app_bar_menu.*
import kotlinx.android.synthetic.main.nav_header_menu.*
import org.json.JSONObject


class MenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var USERNAME = ""
    var DISPLAYNAME = ""


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)


        USERNAME = intent.getStringExtra("USERNAME")
        DISPLAYNAME = intent.getStringExtra("DISPLAYNAME")

        println("The submitted username is: $USERNAME ($DISPLAYNAME)")

        println("Creating current user instance...")
        val currentUser = getUserByLoginName(USERNAME)
        if(currentUser == null) {
            println("ERROR @ Requesting this user")
            return
        }
        else
            println("Current user instance logged in as ${currentUser.printUser()}")


        //ToDo: Get chatrooms of this user
        val chatrooms_json = getChatroomsForUser(currentUser)

        if(chatrooms_json == null)
            return

        //ToDo: Save chatrooms in a file:
        writeToFile("chatrooms.json", chatrooms_json.toString(), false, this.baseContext)
        //println("The chatroom file has been created and written")

        //ToDo: Parse json chatrooms into a ArrayList of chatrooms
        val chatrooms_list = parseChatroomsFromJSON(chatrooms_json)



        //Checking content of chatroom file
        val file_content = readFromFile("chatrooms.json", this.baseContext)

        //ToDo: Draw the chatrooms as elements on the activity:

    }

override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        initInterface()
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {
                //ToDo: Open FTP activity
                var intent = Intent(this@MenuActivity, FtpActivity::class.java)
                //intent.putExtra("USERNAME", USER)
                //intent.putExtra("DISPLAYNAME", DISPLAYNAME)
                runOnUiThread { startActivity(intent)}


            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initInterface(){
        txt_username.text = DISPLAYNAME
        txt_subUsername.text = USERNAME
        //Later setting individual user img
        //img_profilePicture.setImageResource()
    }


    fun writeChatroomsToFile(chats : JSONObject){

    }

}
