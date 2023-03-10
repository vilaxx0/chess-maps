package com.viliusbucinskas.chessmaps

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.Toast
import com.viliusbucinskas.chessmaps.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    val ref = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_register)


        val email=findViewById<TextInputEditText>(R.id.login_email)
        val password=findViewById<TextInputEditText>(R.id.login_password)
        val confirmpass = findViewById<TextInputEditText>(R.id.confirm_login_password)
        val registerBtn=findViewById<Button>(R.id.register_button)

        registerBtn.setOnClickListener {
            if (email.text.toString() == "")
            {
                Toast.makeText(this, "Please provide an Email", Toast.LENGTH_LONG).show()
                email.setTextColor(Color.RED)
            }
             else if(password.length() < 6) {
                Toast.makeText(this, "Password is too short!", Toast.LENGTH_LONG).show()
                password.setTextColor(Color.RED)
                confirmpass.setTextColor(Color.RED)
            }else if(password.text.toString() != confirmpass.text.toString()){
                 Toast.makeText(this, "Do not match", Toast.LENGTH_LONG).show()
                 password.setTextColor(Color.RED)
                 confirmpass.setTextColor(Color.RED)
            } else
            {
                ref.createUserWithEmailAndPassword(
                    email.text.toString().trim(),
                    password.text.toString().trim()
                ).addOnCompleteListener{
                    if(it.isSuccessful)
                    {
                        Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show()
                        val intent= Intent(this, LoginActivity::class.java)
                        startActivity(intent)

                    }
                }
            }


        }

    }

    fun goToLogin(view: View) {

        val intent= Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }




}