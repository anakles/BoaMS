package ai_projekt.boams

import ai_projekt.boams.ai_project.boams.utils.BoaMS_ListAdapter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ai_projekt.boams.ai_project.boams.utils.FTP_ListAdapter
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.os.StrictMode
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.*
import kotlinx.android.synthetic.main.fragment_ftp.*
import org.apache.commons.net.ftp.FTPSClient
import org.apache.commons.net.ftp.FTP
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.commons.net.ftp.FTPFile
import java.io.BufferedOutputStream
import java.io.FileOutputStream





class Fragment_FTP : Fragment()
{

    var ftpFiles = arrayListOf<FTPFile>()
    val mFtpClient = FTPSClient(true)      //Implicit for connection type


    val TAG = "FTP-Fragment"

    override fun onAttach(context: Context?)
    {
        Log.d(TAG, "onAttach")
        super.onAttach(context)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        setupPermissions()


        ftpFiles = arrayListOf<FTPFile>()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        Log.d(TAG, "onCreateView")

        val view = inflater.inflate(R.layout.fragment_ftp, container, false)

        //Paint the ListView
        val listView = view.findViewById<ListView>(R.id.ftp_listview)
        Log.d(TAG, "Starting to fill FTP-List#1")


        //init the ftp file list:
        val runnable = Runnable {
            Log.d("DEBUG", "Starting to receive the FTP files... !")
            getFtpFiles()
        }

        val t = Thread(runnable)
        t.start()
        t.join()


        if(ftpFiles.isEmpty()){
            Log.d("DEBUG", "There are no FTP files in the list")
            return view
        }


        val arrayAdapter = BoaMS_ListAdapter(context, R.layout.row_main_boa_ms, ftpFiles)
        listView.adapter = arrayAdapter


        //Download after Klick #1
        listView.setOnItemClickListener {
                parent, view, position, id ->

                    var remoteFilePath = ftpFiles.get(position).name
                    var localFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
                    System.out.println("Download Path is: " + localFilePath)
                    var fileName = localFilePath + "/" + ftpFiles.get(position).name
                    var fos = BufferedOutputStream(FileOutputStream(fileName))
                    val success = mFtpClient.retrieveFile(remoteFilePath, fos)
                    Toast.makeText(requireContext(),
                        "The file " + ftpFiles.get(position).name + " was downloaded.",
                        Toast.LENGTH_LONG
                    ).show()
                    System.out.println("Download erfolgreich? " + success)
                    System.out.println("Reply Code: " + mFtpClient.replyString)
                    fos.close()
        }

        return view
    }



    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        Log.d(TAG, "onActivityCreated")
        super.onActivityCreated(savedInstanceState)
    }


    override fun onStart()
    {
        Log.d(TAG, "onStart")
        super.onStart()
    }



    override fun onResume()
    {
        Log.d(TAG, "onResume")
        super.onResume()
    }


    override fun onStop()
    {
        Log.d(TAG, "onStop")
        super.onStop()
    }


    override fun onDetach()
    {
        Log.d(TAG, "onDetach")
        super.onDetach()

        //ftpFiles.clear()
    }




    private fun setupPermissions()
    {
        val permission = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED)
        {
            System.out.println("Permission to write denied.")
            makeRequest()
        }
    }

    private fun makeRequest()
    {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 101)
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

    private fun getFtpFiles(){

        val user = activity?.intent?.getStringExtra("USERNAME")
        val pwd = activity?.intent?.getStringExtra("PWD")

        //val ftpHost = "192.168.50.14"
        val ftpHost = "9ntfn2zneyc83qlo.myfritz.net"
        val ftpPort = 990
        val ftpUser = user
        val ftpPW = pwd

        //GlobalScope.launch {

            try {
                println("Starting connection attempt to FTP server...")
                mFtpClient.connect(ftpHost)
                println("I'm connected!!!!11!!!")


                mFtpClient.enterLocalPassiveMode()
                mFtpClient.login(ftpUser, ftpPW)

                val reply = mFtpClient.replyString

                println("Logged in to FTP server " + ftpHost + " using FTPS(implizit). Reply Code: " + reply)



                mFtpClient.changeWorkingDirectory("/" + ftpUser + "/")

                mFtpClient.setFileType(FTP.BINARY_FILE_TYPE)

                mFtpClient.execPBSZ(0)      // 0 - Protection Buffer size
                mFtpClient.execPROT("P")    //P = Private for Data Channel Protection Level

                val loginDir = mFtpClient.printWorkingDirectory()
                println("Wir sind hier: " + loginDir + ". Reply Code: " + reply)


                val files = mFtpClient.listFiles()


                if (files != null && files.size > 0)
                {
                    //loop thru files
                    var i = 0
                    for (file in files)
                    {
                        if (file.isFile())
                        {
                            ftpFiles.add(file)
                            Log.d(TAG, ftpFiles.get(i).name + " wurde hinzugefügt")
                            i = i+1
                            println("File is " + file.getName())
                        } else if (file.isDirectory())
                        {
                            println("Directory is " + file.getName())
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println("ERROR >>> Could not upload/download file to FTP server ($ftpHost)")
            }

       // }

    }
}