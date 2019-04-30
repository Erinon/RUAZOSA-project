package hr.fer.ruazosa.kviz.FirebaseServices

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class MyFirebaseIdService:FirebaseInstanceIdService(){
    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val refreshedToken= FirebaseInstanceId.getInstance().token
        sendRegistrationToServer(refreshedToken!!)
    }

    private fun sendRegistrationToServer(refreshedToken: String) {
        Log.d("DEBUG",refreshedToken)
    }
}