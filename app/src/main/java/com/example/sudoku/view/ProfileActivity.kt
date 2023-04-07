package com.example.sudoku.view

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sudoku.R
import com.example.sudoku.viewModel.MyAdapter
import com.example.sudoku.viewModel.User
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileActivity: AppCompatActivity() {
    private lateinit var nom: TextView
    private lateinit var nomdutilisateur: TextView
    private lateinit var score: TextView
    var recview: RecyclerView? = null
    var adapter: MyAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        nom = findViewById(R.id.nom)
        nomdutilisateur = findViewById(R.id.nomdutilisateur)
        score = findViewById(R.id.score)
        val shareButton = findViewById<Button>(R.id.partage)



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
        recview = findViewById(R.id.recview)
        recview?.setLayoutManager(LinearLayoutManager(this))

        val options: FirebaseRecyclerOptions<User> = FirebaseRecyclerOptions.Builder<User>()

            .setQuery(FirebaseDatabase.getInstance().reference.child("Users"), User::class.java)
            .build()

        adapter = MyAdapter(options)
        (recview as? RecyclerView)?.setAdapter(adapter)

    }override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.searchmenu, menu)
        val searchItem: MenuItem = menu.findItem(R.id.search)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.queryHint = "Rechercher un utilisateur"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter!!.stopListening()
                if (newText != null && newText.isNotBlank()) {
                    val searchQuery = newText.trim()
                    val query = FirebaseDatabase.getInstance().reference.child("Users")
                        .orderByChild("nomdutilisateur").startAt(searchQuery).endAt(searchQuery + "\uf8ff")
                    val options: FirebaseRecyclerOptions<User> = FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(query, User::class.java)
                        .build()
                    adapter = MyAdapter(options)
                    recview!!.adapter = adapter
                    adapter!!.startListening()
                } else {
                    adapter!!.stopListening()
                    val options: FirebaseRecyclerOptions<User> = FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(FirebaseDatabase.getInstance().reference.child("Users"), User::class.java)
                        .build()
                    adapter = MyAdapter(options)
                    recview!!.adapter = adapter
                    adapter!!.startListening()
                }
                return true
            }
        })

        val options: FirebaseRecyclerOptions<User> = FirebaseRecyclerOptions.Builder<User>()
            .setQuery(FirebaseDatabase.getInstance().reference.child("Users").orderByChild("nomdutilisateur").equalTo(""), User::class.java)
            .build()

        adapter = MyAdapter(options)
        recview!!.adapter = adapter
        adapter!!.startListening()

        return true
    }
    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

    private fun processSearch(s: String) {
        val query = FirebaseDatabase.getInstance().reference.child("Users")
            .orderByChild("nom").startAt(s).endAt(s + "\uf8ff")
        val options: FirebaseRecyclerOptions<User> = FirebaseRecyclerOptions.Builder<User>()
            .setQuery(query, User::class.java)
            .build()
        adapter = MyAdapter(options)
        adapter!!.startListening()
        recview!!.adapter = adapter
    }
}