package com.example.sudoku.viewModel


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sudoku.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.example.sudoku.view.UserProfileActivity

class MyAdapter(options: FirebaseRecyclerOptions<User>) :
    FirebaseRecyclerAdapter<User, MyAdapter.MyViewHolder>(options) {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, user: User) {
        holder.nom.text = user.nom
        holder.nomdutilisateur.text = user.nomdutilisateur
        holder.score.text = user.score.toString()
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, UserProfileActivity::class.java)
            intent.putExtra("userId", getRef(position).key!!)
            val bundle = Bundle()
            bundle.putString("searchBy", "userId")
            bundle.putString("searchValue", getRef(position).key!!)
            intent.putExtra("searchParams", bundle)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.searchview, parent, false)
        return MyViewHolder(view)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nom: TextView = itemView.findViewById(R.id.nom)
        var nomdutilisateur: TextView = itemView.findViewById(R.id.nomdutilisateur)
        var score: TextView = itemView.findViewById(R.id.score)
    }
}