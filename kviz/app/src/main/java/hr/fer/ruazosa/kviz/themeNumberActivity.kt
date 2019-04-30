package hr.fer.ruazosa.kviz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.*

class themeNumberActivity : AppCompatActivity(){

    private var gridView: GridView? = null
    private var categoryData: String?=null
    private var numsAdapter: ThemesNumberAdapter? = null
    private var stages: Int = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.theme_in_list)

        val extras = intent.extras
        if (extras != null) {
            categoryData = extras.getString("categoryJSON")
        }

        gridView=findViewById(R.id.grid_view)

        numsAdapter = ThemesNumberAdapter(this)
        gridView?.adapter= numsAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        for (i in 1..stages) {
            if (requestCode == i - 1) {
                if (resultCode == Activity.RESULT_OK) {
                    numsAdapter?.setColor(i - 1, gridView)
                }
            }
        }
    }

    inner class ThemesNumberAdapter() : BaseAdapter() {

        private var context: Context? = null
        private var numbersList= mutableListOf<String>()
        ////super()
        constructor(context: Context) : this() {
            this.context = context

            for (i in 1..stages){
                numbersList.add(i.toString())
            }
        }

        override fun getItem(position: Int): Any {
            return numbersList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return numbersList.size
        }

        fun setColor(position: Int, parent: ViewGroup?) {
            getView(position, null, parent)?.setBackgroundColor(Color.parseColor("#399b0c"))
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view: View?
            val vh: ViewHolder

            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.question_in_number_list, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }

            vh.numberTitle.text=numbersList[position]

            view?.setOnClickListener {

                val startSelectNumberActivity = Intent(context, questionActivity::class.java)
                val questionData=categoryData+"-"+numbersList[position]
                startSelectNumberActivity.putExtra("questionJSON", questionData)
                startSelectNumberActivity.putExtra("stage", position)
                startActivityForResult(startSelectNumberActivity, position)
            }
            return view

        }


    }
    private class ViewHolder(view: View?) {
        val numberTitle: TextView = view?.findViewById(R.id.number) as TextView


    }
}