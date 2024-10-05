package com.example.balancebite

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class ProgressNotificationWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    /*override fun doWork(): Result {
        // Assuming 'ProgressEntry' is a Map or List that holds the progress for each day
        val daysOfWeek = listOf("Day 1", "Day 2", "Day 3", "Day 4", "Day 5", "Day 6", "Day 7")

        // Check which day's progress has been entered
        //val lastDayEntered = ProgressEntry.size ?: 0 // Get the number of days entered, or 0 if null

        /*/return if (lastDayEntered < daysOfWeek.size) {
            // Notify for the next day's entry
            val nextDay = daysOfWeek[lastDayEntered] // Get the next day to be entered
            sendNotification("BalanceBite Progress Reminder", "It's time to update your progress for $nextDay!")
            Result.success()
        } else {
            // If all 7 days have been entered, perhaps notify the user that the week is complete
            sendNotification("BalanceBite Progress Reminder", "Great job! You have completed all your weekly progress entries.")
            Result.success()
        }*/
    }*/


    private fun sendNotification(title: String, message: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "balancebite_channel_id"

        // Create notification channel for Android O and above
        val channel = NotificationChannel(
            channelId,
            "BalanceBite Progress Notifications",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.appimage)  // Replace with your app's icon

            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }

    override fun doWork(): Result {
        TODO("Not yet implemented")
    }
}
