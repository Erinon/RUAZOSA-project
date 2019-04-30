package hr.fer.ruazosa.kviz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase

class registerActivity : AppCompatActivity() {

    var fAuth = FirebaseAuth.getInstance()
    private var categories:String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val extras = intent.extras
        if (extras != null) {
            categories = extras.getString("categories")
        }

        var remail = findViewById<EditText>(R.id.rEmailTextField)
        var rPassword = findViewById<EditText>(R.id.rPasswordEditText)
        var rUsername = findViewById<EditText>(R.id.usernameTextField)

        var btnRegister = findViewById<Button>(R.id.registerButton)
        btnRegister.setOnClickListener {view ->
            var imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)

            createAccount(view, remail?.text.toString(), rPassword?.text.toString(), rUsername?.text.toString())
        }

        var loginTabButton2 = findViewById<Button>(R.id.loginButton2)
        loginTabButton2.setOnClickListener { view ->
            finish()
        }
    }

    fun createAccount (view: View, email: String, password: String, username: String) {
        showMessage(view,"Creating Account...")

        var usersRef = FirebaseDatabase.getInstance().getReference("users")

        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
            if(task.isSuccessful){
                var user = this.fAuth.currentUser
                val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(username).build()
                user?.updateProfile(profileUpdates)
                System.out.println("Username: " + user?.email + " " + user?.displayName)
                usersRef?.push()?.setValue(username)
                var intent = Intent(this, themesActivity::class.java)
                intent.putExtra("categories", categories)
                startActivity(intent)
            }else{
                showMessage(view,"Error: ${task.exception?.message}")
            }
        })
    }

    fun showMessage(view:View, message: String){
        Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show()
    }

}
