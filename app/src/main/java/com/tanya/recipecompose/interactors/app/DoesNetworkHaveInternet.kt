package com.tanya.recipecompose.interactors.app

import android.util.Log
import com.tanya.recipecompose.util.TAG
import java.io.IOException
import java.net.InetSocketAddress
import javax.net.SocketFactory

object DoesNetworkHaveInternet {

    fun execute(socketFactory: SocketFactory): Boolean {
        return try {
            Log.d(TAG, "Pinging Google")
            val socket = socketFactory.createSocket() ?: throw IOException("socket is null")
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            Log.d(TAG, "Ping success")
            true
        } catch (e: IOException) {
            Log.e(TAG, "no internet connection: $e")
            false
        }
    }

}