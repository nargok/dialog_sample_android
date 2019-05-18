package com.example.myalarmclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AlarmBroadCastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val mainIntent = Intent(context, MainActivity::class.java)
                .putExtra("onReceive", true)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(mainIntent)
    }
}
