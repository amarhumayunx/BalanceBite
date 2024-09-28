package com.example.balancebite

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// AdminDashboardActivity.kt
class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        recyclerView = findViewById(R.id.recyclerViewforadmin)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch users and setup RecyclerView
        fetchUsers()
    }

    private fun setupRecyclerView(userList: List<User>) {
        userAdapter = UserAdapter(userList)
        recyclerView.adapter = userAdapter
    }

    private fun fetchUsers() {
        val database = FirebaseDatabase.getInstance().getReference("Users")

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = mutableListOf<User>()

                for (userSnapshot in snapshot.children) {
                    val userId = userSnapshot.key ?: ""
                    val user = userSnapshot.getValue(User::class.java)!!.copy(userId = userId)
                    userList.add(user)
                }

                // Pass this list to your RecyclerView Adapter
                setupRecyclerView(userList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error fetching data", error.toException())
            }
        })
    }


}
