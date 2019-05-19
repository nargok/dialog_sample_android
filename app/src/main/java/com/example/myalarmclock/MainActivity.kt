package com.example.myalarmclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), TimeAlartDialog.Listener
    , DatePickerFragment.OnDateSelectedListener
    , TimePickerFragment.OnTimeSelectedListener {

    override fun onSelected(year: Int, month: Int, date: Int) {
        val c = Calendar.getInstance()
        c.set(year, month, date)
        dateText.text = DateFormat.format("yyyy/MM/dd", c)
    }

    override fun onSelected(hourOfDay: Int, minute: Int) {
        timeText.text = "%1$02d:%2$02d".format(hourOfDay, minute)
    }

    override fun getUp() {
        finish()
    }

    override fun finish() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.MINUTE, 5)
        setAlarmManager(calendar)
        finish()
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
            val date = "${dateText.text} ${timeText.text}".toDate()
            when {
                date != null -> {
                    val calendar = Calendar.getInstance()
                    calendar.time = date
                    setAlarmManager(calendar)
                    Toast.makeText(
                        this, "アラームをセットしました",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    Toast.makeText(
                        this, "日付の形式が正しくありません",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        cancelAlarm.setOnClickListener {
            cancelAlarmManager()
            Toast.makeText(this, "アラームをキャンセルしました", Toast.LENGTH_SHORT).show()
        }

        dateText.setOnClickListener{
            val dialog = DatePickerFragment()
            dialog.show(supportFragmentManager, "date_dialog")
        }

        timeText.setOnClickListener{
            val dialog = TimePickerFragment()
            dialog.show(supportFragmentManager, "time_dialog")
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

    private fun String.toDate(pattern: String = "yyyy/MM/dd HH:mm"): Date? {
        return try {
            SimpleDateFormat(pattern).parse(this)
        } catch (e: IllegalArgumentException) {
            return null
        }
        catch (e: ParseException) {
            return null
        }
    }
}
