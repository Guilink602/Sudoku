package com.example.sudoku.view

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.sudoku.R
import com.example.sudoku.databinding.ActivityLeaderboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class LeaderboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLeaderboardBinding
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database.reference

        val listLeaderboard = findViewById<ListView>(R.id.ListViewLeaderboard)

        val isNotEmpty = intent.getBooleanExtra("notEmpty", false)

        val arrayAdapterLocal = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)

        if(isNotEmpty) {

            val getName = intent.getStringArrayListExtra("dataName")
            val getTime = intent.getStringArrayListExtra("dataTime")
            val getErrors = intent.getIntegerArrayListExtra("dataErrors")
            val getScore = intent.getIntegerArrayListExtra("dataScore")

            val getAllData = ArrayList<String>()
            var index = 0

            while ((index < getName!!.size) and (index < getTime!!.size) and (index < getErrors!!.size)) {
                getAllData.add("${index + 1} " + getName[index] + " Time: " + getTime[index] + " Errors: " + getErrors[index] + " Total Score: " + (getScore?.get(index)))
                index++
            }

            arrayAdapterLocal.addAll(getAllData)

        }
        listLeaderboard.adapter = arrayAdapterLocal

        val getOnlineData = ArrayList<String>()

        val userRef = FirebaseDatabase.getInstance().getReference("Users")
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var x = 0
                for (postSnapshot in dataSnapshot.children) {
                    x++
                    val Username = postSnapshot.child("nomdutilisateur").getValue(String::class.java)
                    val Time = postSnapshot.child("temps").getValue(String::class.java)
                    val Errors = postSnapshot.child("erreurs").getValue(String::class.java)
                    val Score = postSnapshot.child("score").getValue(Int::class.java)
                    getOnlineData.add("$x Nom: $Username Time: $Time Errors: $Errors Total Score: $Score")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        val arrayAdapterOnline = ArrayAdapter(this, android.R.layout.simple_list_item_1, getOnlineData)



        binding.BackButton.setOnClickListener {
            super.finish()
        }

        binding.OnlineButton.setOnClickListener{

            if(binding.OnlineButton.text == "Online Leaderboard") {



                listLeaderboard.adapter = arrayAdapterOnline
                binding.OnlineButton.text = "Personnal Leaderboard"
                binding.TextViewLeaderboard.text = "Online Leaderboard"
            }
            else {

                listLeaderboard.adapter = arrayAdapterLocal

                binding.OnlineButton.text = "Online Leaderboard"
                binding.TextViewLeaderboard.text = "Personnal Leaderboard"
            }

        }

    }

}
