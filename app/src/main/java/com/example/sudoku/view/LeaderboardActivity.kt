package com.example.sudoku.view

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.sudoku.R
import com.example.sudoku.databinding.ActivityLeaderboardBinding

class LeaderboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLeaderboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getIntent = intent
        val listLeaderboard = findViewById<ListView>(R.id.ListViewLeaderboard)

        val getName = intent.getStringArrayListExtra("dataName")
        val getTime = intent.getStringArrayListExtra("dataTime")
        val getErrors = intent.getStringArrayListExtra("dataErrors")
        val getAllData = ArrayList<String>()
        var index = 0

        while ((index < getName!!.size) and (index < getTime!!.size) and (index < getErrors!!.size)) {
            getAllData.add("${index+1} " + getName[index] + " " + getTime[index] + " " + getErrors[index])
            index++
        }

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, getAllData)
        listLeaderboard.adapter = arrayAdapter
        binding.BackButton.setOnClickListener { v: View? -> super.finish() }

    }

}