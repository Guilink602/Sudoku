package com.example.sudoku.view

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.sudoku.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity: AppCompatActivity() {
    private lateinit var nom: TextView
    private lateinit var age: TextView
    private lateinit var nomdutilisateur: TextView
    private lateinit var score: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else {
            val userId = currentUser!!.uid


            // Read from the database
            val userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId)
            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val Nom = dataSnapshot.child("nom").getValue(String::class.java)
                    val Age = dataSnapshot.child("age").getValue(Int::class.java)
                    val Score = dataSnapshot.child("score").getValue(Int::class.java)
                    val Nomdutilisateur =
                        dataSnapshot.child("nomdutilisateur").getValue(String::class.java)
                    nom.text = Nom
                    nomdutilisateur.text = Nomdutilisateur
                    age.text = Age.toString()
                    score.text = Score.toString()

                }


                override fun onCancelled(error: DatabaseError) {
                    // Handle errors
                }
            })
        }
    }
}