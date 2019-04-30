package hr.fer.ruazosa.kviz

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class invitationActivity : AppCompatActivity()  {

    private var listView: ListView? = null

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.invitation)
         listView=findViewById(R.id.list_view1)
         listView?.adapter= PeopleAdapter(this)

         findViewById<Button>(R.id.pozovi1).setOnClickListener {
             finish()
         }

     }

    inner class PeopleAdapter() : BaseAdapter() {

        private var context: Context? = null
        private var peopleList= mutableListOf<String>()
        ////super()
        constructor(context: Context) : this() {
            this.context = context
            val peopleRef = FirebaseDatabase.getInstance().getReference("users")
            peopleRef!!.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val data = dataSnapshot.children
                    if (data != null) {

                        for (zadnjiIspis in data){
                            val list = zadnjiIspis.value.toString().split("=")
                            peopleList.add(list[0])
                        }

//                        for (ispis in data){
//                            var list = ispis.value.toString().split("=")
//                            messageList.add(list.get(0))
//                        }
                        notifyDataSetChanged()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

        override fun getItem(position: Int): Any {
            return peopleList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return peopleList.size
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view:View?
            val vh: ViewHolder

            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.invitation2, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }

            vh.peopleTitle.text=peopleList[position]

            vh.box.setOnClickListener {

                Toast.makeText(applicationContext,
                        "Clicked on Row: " + peopleList[position],
                        Toast.LENGTH_LONG).show()
            }


            return view

        }


    }
    private class ViewHolder(view: View?) {
        val peopleTitle: TextView = view?.findViewById(R.id.invitation_title) as TextView
        val box: CheckBox = view?.findViewById(R.id.checked) as CheckBox


    }
}