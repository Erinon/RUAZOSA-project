package hr.fer.ruazosa.kviz

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.database.*
import java.util.*

class logoActivity:AppCompatActivity() {

    private var database: FirebaseDatabase? = null
    private var dataRef: DatabaseReference? = null
    private var listOfCategories: MutableList<String> = LinkedList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logo)

        database = FirebaseDatabase.getInstance()
        dataRef = database?.getReference("data")

        dataRef!!.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.children
                for (d in data) {
                    listOfCategories.add(d.key)
                }

                val startLoginActivity = Intent(this@logoActivity, LoginActivity::class.java)
                var categories: String? = null
                for (i in 0 until listOfCategories.size) {
                    if(i==0) {
                        categories = listOfCategories[i]
                    }
                    else{
                        categories += ","+listOfCategories[i]
                    }
                }
                startLoginActivity.putExtra("categories", categories)
                startActivity(startLoginActivity)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })


    }
}