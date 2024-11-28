package com.example.mybird

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class RankRecyclerViewActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var firebaseManager: FirebaseManager
    private lateinit var searchTxt: EditText
    private lateinit var searchBtn: Button
    private lateinit var refreshBtn: Button

    private lateinit var sharedPrefManager: SharedPreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPrefManager = SharedPreferenceManager(this)
        val language = sharedPrefManager.getLanguageConfig()
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)


        setContentView(R.layout.recycler_view_main)

        searchTxt = findViewById(R.id.searchTxt)
        searchBtn = findViewById(R.id.searchBtn)
        refreshBtn = findViewById(R.id.refreshBtn)

        firebaseManager = FirebaseManager()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        firebaseManager.rankQuery { list ->
//            println("ARR: -V: " + list.size)
            recyclerView.adapter = RankAdapter(list)
        }

        searchBtn.setOnClickListener {
            firebaseManager.rankQuery { list ->
                val nameToFind = searchTxt.text.toString()
                val matchedUsers = list.filter { it.name == nameToFind }
                Toast.makeText(this, matchedUsers.toString(), Toast.LENGTH_SHORT).show()
                recyclerView.adapter = RankAdapter(matchedUsers)
            }
        }
        refreshBtn.setOnClickListener {
            firebaseManager.rankQuery { list ->
                searchTxt.setText("")
                recyclerView.adapter = RankAdapter(list)
            }
        }

    }
}