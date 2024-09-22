package com.example.balancebite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// UserAdapter.kt
class UserAdapter(private val userList: List<User>, private val listener: OnUserClickListener) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    interface OnUserClickListener {
        fun onEditClick(user: User)
        fun onDeleteClick(userId: String)
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.textViewUserName)
        val editButton: Button = itemView.findViewById(R.id.buttonEdit)
        val deleteButton: Button = itemView.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.userName.text = user.name

        holder.editButton.setOnClickListener {
            listener.onEditClick(user)
        }

        holder.deleteButton.setOnClickListener {
            listener.onDeleteClick(user.userId!!)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}
