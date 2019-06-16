package ai_projekt.boams.ai_project.boams.utils
import ai_projekt.boams.R
import ai_projekt.boams.ai_project.boams.ftpFiles
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import org.apache.commons.net.ftp.FTPFile


class MyListAdapter(var mCtx:Context , var resource:Int,var items:List<FTPFile>) :ArrayAdapter<FTPFile>( mCtx , resource , items )
{


    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View
    {

        val layoutInflator :LayoutInflater = LayoutInflater.from(mCtx)

        val rowMain :View = layoutInflator.inflate(resource, null)


        //Grey Background
        if (position % 2 == 0)
        {
            rowMain.setBackgroundColor(Color.parseColor("#DDDDDD"))
        }

        val nameTextView = rowMain.findViewById<TextView>(R.id.name_textview)

        nameTextView.text = ftpFiles.get(position).getName()


        return rowMain
    }



}
