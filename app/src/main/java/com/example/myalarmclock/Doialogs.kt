package com.example.myalarmclock

import android.app.DatePickerDialog
import androidx.appcompat.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimeAlartDialog: DialogFragment() {
    interface Listener {
        fun getUp()
        fun snooze()
    }

    private var listener: Listener? = null

    // フラグメントが呼ばれ最初にコンテキストにアタッチされたときに呼ばれる
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        when (context) {
            // Listener interfaceを持っているactivityなら呼び出し元activityを設定する
            is Listener -> listener = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("時間になりました！")
        builder.setPositiveButton("起きる") { dialog, which ->
            listener?.getUp()
        }
        builder.setNegativeButton("あと5分") { dialog, which ->
            listener?.snooze()
        }
        return builder.create()
    }
}


class DatePickerDialog: DialogFragment(), DatePickerDialog.OnDateSetListener {

    interface OnDateSelectedListener {
        fun onSelected(year: Int, month: Int, date: Int)
    }

    private var listener: OnDateSelectedListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        when (context) {
            // 呼んでるActivityをセットする
            is OnDateSelectedListener -> listener = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // 現在の日付を初期値として設定する
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val date = c.get(Calendar.DAY_OF_MONTH)
        // DatePickerDialogのインスタンスを返す
        return DatePickerDialog(requireActivity(), this, year, month, date)
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        listener?.onSelected(year, month, dayOfMonth)
    }
}