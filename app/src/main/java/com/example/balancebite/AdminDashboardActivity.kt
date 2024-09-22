package com.example.balancebite

import android.annotation.SuppressLint
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// AdminDashboardActivity.kt
class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var recyclerViewUsers: RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var userAdapter: UserAdapter
    private val userList = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        recyclerViewUsers = findViewById(R.id.recyclerViewUsers)
        recyclerViewUsers.layoutManager = LinearLayoutManager(this)

        database = FirebaseDatabase.getInstance().getReference("Users")

        // Initialize RecyclerView Adapter
        userAdapter = UserAdapter(userList, object : UserAdapter.OnUserClickListener {
            override fun onEditClick(user: User) {
                // Edit user information
                editUser(user)
            }

            override fun onDeleteClick(userId: String) {
                // Delete user profile
                deleteUser(userId)
            }
        })

        recyclerViewUsers.adapter = userAdapter

        // Fetch users from Firebase
        fetchUsersFromDatabase()
    }

    private fun fetchUsersFromDatabase() {
        database.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    if (user != null) {
                        userList.add(user)
                    }
                }
                userAdapter.notifyDataSetChanged()  // Notify the adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AdminDashboardActivity, "Failed to fetch users", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun editUser(user: User) {
        // Open dialog or activity to edit user data and update it in Firebase
        val intent = Intent(this, EditUserActivity::class.java)
        intent.putExtra("userId", user.userId)
        startActivity(intent)
    }

    private fun deleteUser(userId: String) {
        database.child(userId).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "User deleted successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to delete user", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
