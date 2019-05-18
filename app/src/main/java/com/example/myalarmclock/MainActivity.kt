package com.example.myalarmclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), TimeAlartDialog.Listener {
    override fun getUp() {
        Toast.makeText(this, "起きるがクリックされました", Toast.LENGTH_SHORT).show()
    }

    override fun snooze() {
        Toast.makeText(this, "あと5分がクリックされました", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent?.getBooleanExtra("onReceive", false) == true) {
            val dialog = TimeAlartDialog()
            dialog.show(supportFragmentManager, "alert_dialog")
        }

        setContentView(R.layout.activity_main)

        // AlarmManagerに時刻と実施するintentを登録する
        setAlarm.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            // 5秒後にアラーム処理を実行する
            calendar.add(Calendar.SECOND, 5)
            setAlarmManager(calendar)
        }

        cancelAlarm.setOnClickListener {
            cancelAlarmManager()
            Toast.makeText(this, "アラームをキャンセルしました", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAlarmManager(calendar: Calendar) {
        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmBroadCastReceiver::class.java)
        // 指定したタイミングでintentを実施する場合は、PendingIntentを使う
        val pending = PendingIntent.getBroadcast(this, 0, intent, 0)
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                val info = AlarmManager.AlarmClockInfo(calendar.timeInMillis, null)
                am.setAlarmClock(info, pending)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> {
                am.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pending)

            }
            else -> {
                am.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pending)
            }
        }
    }

    private fun cancelAlarmManager() {
        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmBroadCastReceiver::class.java)
        val pending = PendingIntent.getBroadcast(this, 0, intent, 0)
        am.cancel(pending)
    }
}
