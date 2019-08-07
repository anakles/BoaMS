package ai_projekt.boams

import ai_projekt.boams.ai_project.boams.utils.MyListAdapter
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import org.apache.commons.net.ftp.FTPSClient
import org.apache.commons.net.ftp.FTP
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.app_bar_menu.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.commons.net.ftp.FTPFile
import java.io.BufferedOutputStream
import java.io.FileOutputStream


val ftpFiles = arrayListOf<FTPFile>()


class FtpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ftp)
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()



        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        setupPermissions()



//+++++++++++++++++++++++++++LOGIN++++++++++++++++++++++++++++++++++++

        lateinit var listView : ListView

        val ftpHost ="9ntfn2zneyc83qlo.myfritz.net"
        val ftpPort = 990
        val ftpUser = "jballnar"                        //TODO: Hier eingegebenen Anmeldename einfügen
        val ftpPW = "Leverkusen19+"                     //TODO: Hier eigegebenes Passwort einfügen
        val mFtpClient = FTPSClient(true)      //Implicit for connection type

        //Don't log into the system with not exisiting admin credentials. The admin-admin access is only for offline testing purpose
        if(intent.getStringExtra("USERNAME") == "admin") return

        GlobalScope.launch{

            try {
                mFtpClient.connect(ftpHost)
                println("I'm connected!!!!11!!!")


                mFtpClient.enterLocalPassiveMode()
                mFtpClient.login(ftpUser, ftpPW)

                val reply = mFtpClient.replyString

                println("Logged in to FTP server " + ftpHost + " using FTPS(implizit). Reply Code: " + reply)



                mFtpClient.changeWorkingDirectory("/Jan/")  //TODO: Hier Usernamen eingeben (Ordner werden noch umbenannt)

                mFtpClient.setFileType(FTP.BINARY_FILE_TYPE)

                mFtpClient.execPBSZ(0)      // 0 - Protection Buffer size
                mFtpClient.execPROT("P")    //P = Private for Data Channel Protection Level

                val loginDir = mFtpClient.printWorkingDirectory()
                println("Wir sind hier: " + loginDir + ". Reply Code: " + reply)


                val files = mFtpClient.listFiles()


                if (files != null && files.size > 0) {
                    //loop thru files
                    for (file in files) {
                        if (file.isFile()) {
                            ftpFiles.add(file)
                            println("File is " + file.getName())
                        } else if (file.isDirectory()) {
                            println("Directory is " + file.getName())

                        }
                    }
                }
            }
            catch (e : Exception) {
                e.printStackTrace()
                println("ERROR >>> Could not upload/download file to FTP server ($ftpHost)")
            }
        }


        //Paint the ListView
        listView = findViewById(R.id.ftp_listview)
        var list = mutableListOf<FTPFile>()

        for (file in ftpFiles)
        {
            list.add(file)
        }

        listView.adapter = MyListAdapter(baseContext,R.layout.row_main, list)




        //Download after Klick #1
        listView.setOnItemClickListener { parent, view, position, id ->

            var remoteFilePath = list.get(position).name
            var localFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
            System.out.println("Download Path is: " + localFilePath)
            var fileName = localFilePath + "/" + list.get(position).name
            var fos = BufferedOutputStream(FileOutputStream(fileName))
            val success = mFtpClient.retrieveFile(remoteFilePath, fos)
            Toast.makeText(this, "The file " + list.get(position).name + " was downloaded.", Toast.LENGTH_LONG).show()
            System.out.println("Download erfolgreich? " + success)
            System.out.println("Reply Code: " + mFtpClient.replyString)
            fos.close()


        }


        //Download after Klick #2

/*        listView.setOnItemClickListener { parent, view, position, id ->

            val url = "https://"+ ftpHost + "/Jan/" + list.get(position).name.toString()
            val request = DownloadManager.Request(Uri.parse(url))
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setTitle("Download")
            request.setDescription("File is downloading...")

            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "BoaMS")

            val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)

        }
*/


        /*
            val listView = findViewById<ListView>(R.id.ftp_listview)
            listView.adapter = MyAdapter(this)





            listView.onItemClickListener = object : AdapterView.OnItemClickListener {
                override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Toast.makeText(applicationContext, "hi", Toast.LENGTH_SHORT).show()

                    val itemValue = listView.getItemAtPosition(position) as String
                    Toast.makeText(applicationContext, "Position : $position\n Item Value: $itemValue", Toast.LENGTH_SHORT).show()
                }
            }

*/


    }

    private fun setupPermissions()
    {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED)
        {
            System.out.println("Permission to write denied.")
            makeRequest()
        }
    }

    private fun makeRequest()
    {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 101)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        when (requestCode)
        {
            101 ->
            {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)
                {
                    System.out.println("Permission has been denied by user")
                }
                else
                {
                    System.out.println("Permission has been granted by user")
                }
            }
        }
    }

}