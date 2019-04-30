package hr.fer.ruazosa.kviz

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import hr.fer.ruazosa.kviz.Config.Config
import java.util.*


class themesActivity : AppCompatActivity() {

    private var listView: ListView? = null
    private var listOfCategories: MutableList<String> = LinkedList()
    private var categories: String?=null
    private var b1: Button? = null


    private var mRegistrationBroadcastReceiver:BroadcastReceiver?=null

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mRegistrationBroadcastReceiver!!)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mRegistrationBroadcastReceiver!!)
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mRegistrationBroadcastReceiver!!, IntentFilter(Config.STR_PUSH))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_themes)

        val extras = intent.extras
        if (extras != null) {
            categories = extras.getString("categories")
            listOfCategories= categories?.split(Regex(","))!!.toMutableList()
        }

        b1 = findViewById(R.id.button1)
        b1?.setOnClickListener {
            val intent = Intent(this@themesActivity, invitationActivity::class.java)
            startActivity(intent)
        }

        mRegistrationBroadcastReceiver= object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent != null) {
                    if(intent.action==Config.STR_PUSH){
                        val message=intent.getStringExtra("message")
                        showNotification("Admin",message)
                    }
                }
            }
        }

        listView=findViewById(R.id.list_view)
        listView?.adapter= ThemesAdapter(this)

    }

    private fun showNotification(title: String, message: String?) {
        val intent=Intent(applicationContext,questionActivity::class.java)
        val contentIntent=PendingIntent.getActivity(applicationContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        val builder =NotificationCompat.Builder(applicationContext)
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(contentIntent)
        val notificationManager=baseContext.getSystemService(Context.NOTIFICATION_SERVICE)as NotificationManager
        notificationManager.notify(1,builder.build())

    }


    inner class ThemesAdapter() : BaseAdapter() {

        private var context: Context? = null
        private var themesList= mutableListOf<String>()
        ////super()
        constructor(context: Context) : this() {
            this.context = context
            themesList=listOfCategories
        }

        override fun getItem(position: Int): Any {
            return themesList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return themesList.size
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view:View?
            val vh: ViewHolder

            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.theme_display, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }

            vh.themeTitle.text=themesList[position]

            view?.setOnClickListener {

                val startSelectNumberActivity = Intent(context, themeNumberActivity::class.java)
                val categoryData=themesList[position]
                startSelectNumberActivity.putExtra("categoryJSON", categoryData)
                startActivity(startSelectNumberActivity)
            }
            return view

        }


    }
    private class ViewHolder(view: View?) {
        val themeTitle: TextView = view?.findViewById(R.id.theme_title) as TextView


    }

}
