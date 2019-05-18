package com.example.myalarmclock

import androidx.appcompat.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment

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
