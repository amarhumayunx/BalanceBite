package com.example.balancebite

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class UserAdapter(private val userList: List<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.profileImage)
        val userName: TextView = itemView.findViewById(R.id.userName)
        val userDetails: TextView = itemView.findViewById(R.id.userDetails)
        val editButton: Button = itemView.findViewById(R.id.editButton)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        val profile = user.profile

        // Load profile picture with Picasso
        Picasso.get().load(profile.profilePictureUrl).into(holder.profileImage)

        // Set user details
        holder.userName.text = userList[position].profile.name
        holder.userDetails.text = "Age: ${profile.age}, \nHeight: ${profile.height} cm, \nWeight: ${profile.weight} kg, \nHealth Info: ${profile.healthInfo}"

        // Handle edit button click
        holder.editButton.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, EditUserActivity::class.java)
            intent.putExtra("userId", user.userId)
            context.startActivity(intent)
        }

        // Handle delete button click
        holder.deleteButton.setOnClickListener {
            deleteUser(user.userId, holder)
        }
    }

    override fun getItemCount(): Int = userList.size


    // Function to delete the user profile from Firebase
    private fun deleteUser(userId: String, holder: UserViewHolder) {
        val database = FirebaseDatabase.getInstance().reference.child("Users").child(userId)

        // Delete the user from Firebase
        database.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(holder.itemView.context, "User profile deleted.", Toast.LENGTH_SHORT).show()
                notifyItemRemoved(holder.absoluteAdapterPosition)
            }
            else {
                Toast.makeText(holder.itemView.context, "Failed to delete user profile.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}