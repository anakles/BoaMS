package ai_projekt.boams.ai_project.boams

import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import ai_projekt.boams.R
import ai_projekt.boams.ai_project.boams.utils.MyListAdapter
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.apache.commons.net.ftp.FTPSClient
import org.apache.commons.net.ftp.FTP

import kotlinx.android.synthetic.main.activity_ftp.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.apache.commons.net.ftp.FTPFile
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.jar.Manifest


val ftpFiles = arrayListOf<FTPFile>()


class FtpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ftp)
        setSupportActionBar(toolbar)



        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        setupPermissions()

/*       fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
*/

//+++++++++++++++++++++++++++LOGIN++++++++++++++++++++++++++++++++++++

        lateinit var listView : ListView

        val ftpHost = "192.168.50.14"
        val ftpPort = 990
        val ftpUser = "jballnar"            //TODO: Hier eingegebenen Anmeldename einfügen
        val ftpPW = "Leverkusen19+"         //TODO: Hier eigegebenes Passwort einfügen
        val mFtpClient = FTPSClient(true)   //Implicit for connection type


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




            } catch (e: Exception) {
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


/*    private class MyAdapter(context: Context): BaseAdapter()
    {
        private val mContext: Context

        init
        {
            this.mContext = context
        }



        // responsible for rendering out each row
        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View
        {
            notifyDataSetChanged()
            val layoutInflator = LayoutInflater.from(mContext)
            val rowMain = layoutInflator.inflate(R.layout.row_main, viewGroup, false)


            if (position % 2 == 0)
            {
                rowMain.setBackgroundColor(Color.parseColor("#DDDDDD"))
            }


            val nameTextView = rowMain.findViewById<TextView>(R.id.name_textview)
            nameTextView.text = ftpFiles.get(position).getName()

           notifyDataSetChanged()

            return rowMain
        }


        override fun notifyDataSetChanged() {
            super.notifyDataSetChanged()
        }



        // responsible for how many rows in my list
        override fun getCount(): Int
        {
            return ftpFiles.size
        }


        override fun getItemId(position: Int): Long
        {
            return position.toLong()
        }



        override fun getItem(position: Int): Any
        {
            System.out.println("2")
            return 2
        }
    }*/


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