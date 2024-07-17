package com.example.fridgehelper.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.fridgehelper.Functions
import com.example.fridgehelper.MainActivity
import com.example.fridgehelper.R
import java.util.*

class CheckProductsDate : Service() {

    private val channelId = "check_product"
    private val notificationId = 1
    private val notificationInterval: Long = 12 * 60 * 60 * 1000

    private val timer = Timer()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        scheduleNotification()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        val name = "Fridge Helper"
        val descriptionText = "Fridge Helper Product Notification"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun showNotification(products: String) {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("У вас скоро испортятся продукты:")
            .setContentText(products)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, builder.build())
    }

    private fun scheduleNotification() {
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val allProducts = Functions.getProductListArrayFromDB(applicationContext)
                var flag = false
                var products: String = ""
                for (i in allProducts) {
                    val productDate =
                        Functions.findMinDate(Functions.getProductDB(applicationContext, i.name))
                    if (productDate - 3 * 24 * 60 * 60 * 1000 < GregorianCalendar.getInstance().timeInMillis && productDate != (-1).toLong()) {
                        flag = true
                        if (products == "") {
                            products = i.name
                        } else products += ", ${i.name}"
                    }
                }
                if (flag) showNotification(products)


            }
        }, notificationInterval, notificationInterval)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
}
