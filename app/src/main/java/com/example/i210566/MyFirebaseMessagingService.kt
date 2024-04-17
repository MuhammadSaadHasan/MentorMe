package com.example.i210566

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            // Handle FCM messages here.
            // If the application is in the foreground handle both data and notification messages here.
            val title = remoteMessage.notification!!.title
            val body = remoteMessage.notification!!.body
            // TODO: Handle the notification received
        }
    }
}
