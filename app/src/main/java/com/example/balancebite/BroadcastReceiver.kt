package com.example.balancebite

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Toast.makeText(context, "Time to enter your next day's progress!", Toast.LENGTH_SHORT).show()
        // You can also trigger a notification here
    }
}
