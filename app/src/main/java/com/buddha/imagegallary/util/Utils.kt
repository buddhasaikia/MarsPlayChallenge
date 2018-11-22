package com.buddha.imagegallary.util

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Utils @Inject
constructor(private val context: Context) {

    fun showToast(message: String?) {
        if (message == null) return
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showToast(messageId: Int) {
        if (messageId == 0) return
        Toast.makeText(context, context.getString(messageId), Toast.LENGTH_SHORT).show()
    }

    fun showErrorDialog(message: String?) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("OOPS")
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
    }
}
