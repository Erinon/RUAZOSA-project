package hr.fer.ruazosa.kviz

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class questionActivity() : AppCompatActivity(){

    private var questionText: TextView?=null
    private var answer: EditText?=null
    private var listView: ListView? = null
    private var chatInput: EditText?=null
    private var b1:Button?=null
    private var b2:Button?=null
    private var b3:Button?=null
    private var b4:Button?=null
    private var sendBtn:Button?=null
    private var answerBtn:Button?=null
    private var questionData:MutableList<String>?=null
    private var questions:HashMap<Int,String> = HashMap()
    private var answers:HashMap<Int,String> = HashMap()
    private var currentQuestion=1

    private var database : FirebaseDatabase?=null
    private var dataRef  : DatabaseReference?=null
    private var fAuth = FirebaseAuth.getInstance()
    private var chatRef : DatabaseReference? = null

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.question)



        database = FirebaseDatabase.getInstance()
        dataRef = database?.getReference("data")
        chatRef = FirebaseDatabase.getInstance().getReference("chat")

        //chatRef?.push()?.setValue("")
        val extras = intent.extras
        if (extras != null) {
            questionData = extras.getString("questionJSON").split(Regex("-")).toMutableList()

            dataRef!!.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val data = dataSnapshot.child(questionData!![0]).child(questionData!![1])
                    if (data != null) {
                        for (i in 1..4) {
                            System.out.println(data.child("questions").child(i.toString()).value)
                            questions[i] = data.child("questions").child(i.toString()).value.toString()
                            answers[i] = data.child("answers").child(i.toString()).value.toString()
                            questionText?.text= questions[currentQuestion]
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

        chatRef!!.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (i in 1..4) {
                    val list = dataSnapshot.value.toString().split("=")
                    System.out.println(list[1])
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        questionText=findViewById(R.id.question_text)
        questionText?.text= questions[currentQuestion]
        answer=findViewById(R.id.answer)
        listView=findViewById(R.id.list_view)
        listView?.adapter= ChatAdapter(this)
        chatInput=findViewById(R.id.chat_input)
        b1=findViewById(R.id.first_answer)
        b2=findViewById(R.id.second_answer)
        b3=findViewById(R.id.third_answer)
        b4=findViewById(R.id.fourth_answer)
        sendBtn=findViewById(R.id.sendBtn)
        answerBtn=findViewById(R.id.answerBtn)

        var toAnswer = 1

        b1?.setOnClickListener {
            currentQuestion=1
            questionText?.text= questions[currentQuestion]
        }
        b2?.setOnClickListener {
            if (toAnswer >= 2) {
                currentQuestion = 2
                questionText?.text = questions[currentQuestion]
            }
            else{
                Toast.makeText(this,"solve "+toAnswer.toString()+". question",Toast.LENGTH_SHORT).show()
            }
        }
        b3?.setOnClickListener {
            if (toAnswer >= 3) {
                currentQuestion = 3
                questionText?.text = questions[currentQuestion]
            }
            else{
                Toast.makeText(this,"solve "+toAnswer.toString()+". question",Toast.LENGTH_SHORT).show()
            }
        }
        b4?.setOnClickListener {
            if (toAnswer >= 4) {
                currentQuestion = 4
                questionText?.text = questions[currentQuestion]
            }
            else{
                Toast.makeText(this,"solve "+toAnswer.toString()+". question",Toast.LENGTH_SHORT).show()
            }
        }

        sendBtn?.setOnClickListener {

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

            val sendMessage=chatInput?.text.toString()
            //posalti poruku
            //refresh poruka
            System.out.print(fAuth.currentUser?.displayName)
                val user = (fAuth.currentUser)
                var name = ""
                if (user != null) {
                    val displayName = user.displayName
                    if (displayName != null)
                        name = displayName
                }
                val message = "$name: $sendMessage"
                chatRef?.push()?.setValue(message)


            chatInput?.setText("")
            Toast.makeText(this,"Message sent!",Toast.LENGTH_SHORT).show()
        }
        answerBtn?.setOnClickListener {

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

            val answerText=answer?.text.toString()
            if(answerText.toLowerCase()== answers[currentQuestion]?.toLowerCase()){
                //spremiti u bazu score
                answer?.setText("")
                Toast.makeText(this,"Correct!",Toast.LENGTH_SHORT).show()
                if (currentQuestion==1){
                    b1?.setBackgroundColor(Color.parseColor("#399b0c"))
                    if (toAnswer == 1) toAnswer++
                }
                if (currentQuestion==2){
                    b2?.setBackgroundColor(Color.parseColor("#399b0c"))
                    if (toAnswer == 2) toAnswer++
                }
                if (currentQuestion==3){
                    b3?.setBackgroundColor(Color.parseColor("#399b0c"))
                    if (toAnswer == 3) toAnswer++
                }
                if (currentQuestion==4){
                    b4?.setBackgroundColor(Color.parseColor("#acb70e"))

                    setResult(extras.getInt("stage"))

                    finish()
                }
            }
            else{
                answer?.setText("")
                Toast.makeText(this,"Wrong, try again!",Toast.LENGTH_SHORT).show()
            }
        }
    }


    inner class ChatMessage (messageTextinput :String ,messageUserinput: String) {
        var messageText = messageTextinput
        var messageUser = messageUserinput

        init {

        }
    }

    inner class ChatAdapter() : BaseAdapter() {

        private var context: Context? = null
        private var messageList= mutableListOf<String>()
        ////super()
        constructor(context: Context) : this() {
            this.context = context
            //dohvatiti poruke
            chatRef!!.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val data = dataSnapshot.children
                    if (data != null) {

                        val zadnjiIspis = data.last()
                        val list = zadnjiIspis.value.toString().split("=")
                        

                        messageList.add(list[0])
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
            return messageList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return messageList.size
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view: View?
            val vh: ViewHolder

            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.message, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }

            vh.message.text=messageList[position]

            return view

        }


    }
    private class ViewHolder(view: View?) {
        val message: TextView = view?.findViewById(R.id.message) as TextView


    }


}
