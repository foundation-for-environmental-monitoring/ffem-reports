package io.ffem.reports.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.annotation.StringRes
import io.ffem.reports.R

/**
 * Utility functions to show alert messages.
 */
object AlertUtil {
    @JvmStatic
    fun showAlert(context: Context, @StringRes title: Int, message: String,
                  @StringRes okButtonText: Int,
                  positiveListener: DialogInterface.OnClickListener?,
                  negativeListener: DialogInterface.OnClickListener?,
                  cancelListener: DialogInterface.OnCancelListener): AlertDialog {
        return showAlert(context, context.getString(title), message, okButtonText, R.string.cancel,
                true, false, positiveListener, negativeListener, cancelListener)
    }

    /**
     * Displays an alert dialog.
     *
     * @param context          the context
     * @param title            the title
     * @param message          the message
     * @param okButtonText     ok button text
     * @param positiveListener ok button listener
     * @param negativeListener cancel button listener
     * @return the alert dialog
     */
    private fun showAlert(context: Context, title: String, message: String,
                          @StringRes okButtonText: Int, @StringRes cancelButtonText: Int,
                          cancelable: Boolean, isDestructive: Boolean,
                          positiveListener: DialogInterface.OnClickListener?,
                          negativeListener: DialogInterface.OnClickListener?,
                          cancelListener: DialogInterface.OnCancelListener): AlertDialog {
        val builder: AlertDialog.Builder
        builder = if (isDestructive) {
            val a = context.obtainStyledAttributes(R.styleable.BaseActivity)
            val style = a.getResourceId(R.styleable.BaseActivity_dialogDestructiveButton, 0)
            a.recycle()
            AlertDialog.Builder(context, style)
        } else {
            AlertDialog.Builder(context)
        }
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable)
        if (positiveListener != null) {
            builder.setPositiveButton(okButtonText, positiveListener)
        } else if (negativeListener == null) {
            builder.setNegativeButton(okButtonText) { dialogInterface: DialogInterface, _: Int -> dialogInterface.dismiss() }
        }
        if (negativeListener != null) {
            builder.setNegativeButton(cancelButtonText, negativeListener)
        }
        builder.setOnCancelListener(cancelListener)
        val alertDialog = builder.create()
        alertDialog.show()
        return alertDialog
    }
}