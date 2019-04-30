package hr.fer.ruazosa.kviz

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth



class LoginActivity : AppCompatActivity() {

    var fAuth = FirebaseAuth.getInstance()
    private var email : EditText? = null
    private var password : EditText?=null
    private var categories:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (fAuth.currentUser != null) {
            fAuth.signOut()
        }

        val extras = intent.extras
        if (extras != null) {
            categories = extras.getString("categories")
        }

        password = findViewById(R.id.passwordEditText)
        email = findViewById(R.id.emailEditText)

        var registerTabButton2 = findViewById<Button>(R.id.registerButton)
        registerTabButton2.setOnClickListener {
            var intent = Intent(this, registerActivity::class.java)
            intent.putExtra("categories",categories)
            startActivity(intent)
        }

        var btnLogin = findViewById<Button>(R.id.loginButton)
        btnLogin.setOnClickListener {view ->
            var imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)

            signIn(view, email?.text.toString(), password?.text.toString())
        }
    }


    override fun onBackPressed() {
        AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit")
                .setMessage("Želite li izaći iz aplikacije?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->  moveTaskToBack(true) })
                .setNegativeButton("No", null)
                .show()
    }

    fun signIn(view: View, email: String, password: String){
        showMessage(view,"Authenticating...")

        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
            if(task.isSuccessful){
                var intent = Intent(this, themesActivity::class.java)
                intent.putExtra("categories",categories)
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
