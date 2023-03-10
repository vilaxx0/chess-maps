package com.viliusbucinskas.chessmaps

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.viliusbucinskas.chessmaps.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class LoginActivity:AppCompatActivity() {

    val ref = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_login)

        val emailLogin = findViewById<TextInputEditText>(R.id.emailLogin)
        val passwordLogin = findViewById<TextInputEditText>(R.id.passLogin)
        val loginBtn = findViewById<Button>(R.id.btnLogin)

        loginBtn.setOnClickListener {
            if (emailLogin.text.toString() == "")
            {
                Toast.makeText(this, "Please provide an Email", Toast.LENGTH_LONG).show()
                emailLogin.setTextColor(Color.RED)
            }
            else if(passwordLogin.length() < 6) {
                Toast.makeText(this, "Password is too short to be correct", Toast.LENGTH_LONG).show()
                passwordLogin.setTextColor(Color.RED)
            } else
            {
                ref.signInWithEmailAndPassword(
                    emailLogin.text.toString(),
                    passwordLogin.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                        val intentMain = Intent(this, MapActivity::class.java)
                        startActivity(intentMain)
                        finish()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    fun goToRegister(view: View) {
        val intentRegister= Intent(this, RegisterActivity::class.java)
        startActivity(intentRegister)
        finish()
    }
}