package com.example.sudoku.view

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import com.example.sudoku.R
import com.example.sudoku.viewModel.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity: AppCompatActivity() {
    private lateinit var nom: TextView
    private lateinit var nomdutilisateur: TextView
    private lateinit var score: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        nom = findViewById(R.id.nom)
        nomdutilisateur = findViewById(R.id.nomdutilisateur)
        score = findViewById(R.id.score)
        val shareButton = findViewById<Button>(R.id.partage)
        val search = findViewById<EditText>(R.id.search)


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

                    val Prenom = dataSnapshot.child("prenom").getValue(String::class.java)
                    val Score = dataSnapshot.child("score").getValue(Int::class.java)
                    val Nomdutilisateur =
                        dataSnapshot.child("nomdutilisateur").getValue(String::class.java)
                    if (Nom != null&& Score!=null) {
                        nom.text = Nom+" "+Prenom

                    }
                    nomdutilisateur.text=nom.text
                    score.text = Score.toString()

                }


                override fun onCancelled(error: DatabaseError) {
                    // Handle errors
                }
            })
        }

        shareButton.setOnClickListener {

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Qui fait Mieux que moi  ${score.text} !")
            startActivity(Intent.createChooser(shareIntent, "Partager mon score via :").apply {

                putExtra(
                    Intent.EXTRA_EXCLUDE_COMPONENTS, arrayOf(
                        ComponentName(
                            "com.android.bluetooth",
                            "com.android.bluetooth.opp.BluetoothOppLauncherActivity"
                        ),
                        ComponentName(
                            "com.google.android.apps.docs",
                            "com.google.android.apps.docs.app.SendTextToClipboardActivity"
                        )
                    )
                )
            })
        }
/*
search.addTextChangedListener(object : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        val searchQuery = s.toString()
        searchUsers(searchQuery)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
})
}
private fun searchUsers(query: String) {
val userRef = FirebaseDatabase.getInstance().getReference("Users")
val queryRef = userRef.orderByChild("uid").startAt(query).endAt(query + "\uf8ff")
queryRef.addValueEventListener(object : ValueEventListener {
    override fun onDataChange(snapshot: DataSnapshot) {
        val usersLayout = findViewById<LinearLayout>(R.id.user_search)

        usersLayout.removeAllViews()

        // Boucler sur les résultats et les ajouter à l'affichage
        for (userSnapshot in snapshot.children) {
            val user = userSnapshot.getValue(User::class.java)
            if (user != null) {
                val userView = LayoutInflater.from(this@ProfileActivity)
                    .inflate(R.layout.user_view, null, false)
                userView.findViewById<TextView>(R.id. nomdutilisateur).text =
                    user.nomdutilisateur
                userView.setOnClickListener {
                    openUserProfile(userSnapshot.key!!)
                }
                usersLayout.addView(userView)
            }
        }
    }

    override fun onCancelled(error: DatabaseError) {

    }
})
}
private fun openUserProfile(userId: String) {
val intent = Intent(this, ProfileActivity::class.java)
intent.putExtra("userId", userId)
startActivity(intent)
}
*/
    }
}