package ai_projekt.boams.ai_project.boams.utils
import ai_projekt.boams.R
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import org.apache.commons.net.ftp.FTPFile


class BoaMS_ListAdapter(var mCtx:Context?, var resource:Int, var items:List<Any>) : ArrayAdapter<Any>( mCtx , resource , items )
{

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View
    {

        val layoutInflator :LayoutInflater = LayoutInflater.from(mCtx)

        val rowMain :View = layoutInflator.inflate(resource, null)

      //  val rowMain :View = layoutInflator.inflate(resource,viewGroup,false)

        //Grey Background
        if (position % 2 == 0)
        {
            rowMain.setBackgroundColor(Color.parseColor("#DDDDDD"))
        }

        val nameTextView = rowMain.findViewById<TextView>(R.id.name_textview)


        System.out.println("Filling the TextViews with the Names...")
        System.out.println("It's: " + items.get(position).toString())
        nameTextView.text = items.get(position).toString()


        return rowMain
    }



}
