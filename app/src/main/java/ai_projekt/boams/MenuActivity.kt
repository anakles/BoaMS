package ai_projekt.boams

import ai_projekt.boams.ai_project.boams.entities.*
import ai_projekt.boams.ai_project.boams.utils.*
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.app_bar_menu.*
import kotlinx.android.synthetic.main.nav_header_menu.*
import org.json.JSONObject


class MenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var USERNAME = ""
    var DISPLAYNAME = ""

    var current_fragment : Fragment ?= null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
        //navView.bringToFront()

        //-------------------------------------------------------------------

        USERNAME = intent.getStringExtra("USERNAME")
        DISPLAYNAME = intent.getStringExtra("DISPLAYNAME")

        println("The submitted username is: $USERNAME ($DISPLAYNAME)")

        if(USERNAME == "admin") {
            writeToFile(R.string.path_userprofile.toString(), User(0, "admin", "admin").toJson().toString(), false, baseContext)
            return
        }


        println("Creating current user instance...")
        val currentUser = getUserByLoginName(USERNAME)
        if(currentUser == null) {
            println("ERROR @ Requesting this user")
            return
        }
        else {
            println("Current user instance logged in as ${currentUser.printUser()}")
            writeToFile(R.string.path_userprofile.toString(), currentUser.toJson().toString(), false, this.baseContext)
        }

        //ToDo: Get chatrooms of this user
        val chatrooms_json = getChatroomsForUser(currentUser)

        if(chatrooms_json == null)
            return

        //ToDo: Save chatrooms in a file:
        writeToFile(R.string.path_chatrooms.toString(), chatrooms_json.toString(), false, this.baseContext)
        //println("The chatroom file has been created and written")

        //ToDo: Parse json chatrooms into a ArrayList of chatrooms
        val chatrooms_list = parseChatroomsFromJSON(chatrooms_json)


        //Checking content of chatroom file
        val file_content = readFromFile(R.string.path_chatrooms.toString(), this.baseContext)

        //Draw the chatrooms as elements on the activity:
        //Initially start the chat fragment:

        val temp_fragment = Fragment_Chats()
        current_fragment = temp_fragment
        supportFragmentManager.beginTransaction().add(R.id.layout_fragment, temp_fragment).commit()

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
            R.id.action_settings -> {

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val temp_fragment = current_fragment

        when (item.itemId) {
            R.id.nav_chat -> {

                val new_fragment = Fragment_Chats()
                println(">>>>>> LOG: Selected chat fragment")

                if(temp_fragment == null) {
                    supportFragmentManager.beginTransaction().add(R.id.layout_fragment, new_fragment).commit()
                }
                else {
                    supportFragmentManager.beginTransaction().replace(R.id.layout_fragment, new_fragment).commit()
                }
                current_fragment = new_fragment

            }
            R.id.nav_ftp -> {

                val new_fragment = Fragment_FTP()
                println(">>>>>>> LOG: Selected FTP fragment")

                if(temp_fragment == null) {
                    supportFragmentManager.beginTransaction().add(R.id.layout_fragment, new_fragment).commit()
                }
                else {
                    supportFragmentManager.beginTransaction().replace(R.id.layout_fragment, new_fragment).commit()
                }
                current_fragment = new_fragment

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
        //ToDo: Later setting individual user img
        //img_profilePicture.setImageResource()
    }

}
