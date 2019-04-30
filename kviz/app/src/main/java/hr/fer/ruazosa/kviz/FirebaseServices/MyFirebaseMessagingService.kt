package hr.fer.ruazosa.kviz.FirebaseServices

import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import hr.fer.ruazosa.kviz.Config.Config

class MyFirebaseMessagingService:FirebaseMessagingService(){
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        handleNotification(remoteMessage!!.notification.body)
    }

    private fun handleNotification(body: String?) {
        val pushNotification= Intent(Config.STR_PUSH)
        pushNotification.putExtra("message",body)
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)

    }

}