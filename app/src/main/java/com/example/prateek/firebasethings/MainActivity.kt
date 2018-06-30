package com.example.prateek.firebasethings

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var database = FirebaseDatabase.getInstance()
        var dbReference = database.getReference("messages").push()

        var person = Person("Pru", 20, true)
        dbReference.setValue(person)

        auth = FirebaseAuth.getInstance()


        createBtn.setOnClickListener {
            var email = emailTxt.text.toString()
            var password = passwordTxt.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener{task ->
                        if(task.isSuccessful) {
                            var user = auth.currentUser
                            Log.d("CREATE", "$user created")
                        } else {
                            Log.d("CREATE", "errorrrrrrrrrrr")
                        }

                    }

        }



        auth.signInWithEmailAndPassword("pru@gmail.com", "123456")
                .addOnCompleteListener {task ->

                    if(task.isSuccessful) {
                        Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                    }

                }


        //Read
        dbReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                var value = dataSnapshot.value as HashMap<String, Any>
                Log.d("DB", value.get("isstudent").toString())

            }

        })


    }

    override fun onStart() {
        super.onStart()

        currentUser = auth.currentUser

        if(currentUser != null) {
            Toast.makeText(this, "User is logged in", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "User is logged out", Toast.LENGTH_SHORT).show()
        }
    }

    data class Person(var name: String, var age: Int, var isstudent: Boolean)
}
