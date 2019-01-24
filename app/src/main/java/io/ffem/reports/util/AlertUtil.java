package io.ffem.reports.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import io.ffem.reports.R;

/**
 * Utility functions to show alert messages.
 */
@SuppressWarnings({"SameParameterValue", "UnusedReturnValue"})
public final class AlertUtil {

    private AlertUtil() {
    }

    public static AlertDialog showAlert(@NonNull Context context, @StringRes int title, String message,
                                        @StringRes int okButtonText,
                                        DialogInterface.OnClickListener positiveListener,
                                        DialogInterface.OnClickListener negativeListener,
                                        DialogInterface.OnCancelListener cancelListener) {
        return showAlert(context, context.getString(title), message, okButtonText, R.string.cancel,
                true, false, positiveListener, negativeListener, cancelListener);
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
    private static AlertDialog showAlert(@NonNull final Context context, String title, String message,
                                         @StringRes int okButtonText, @StringRes int cancelButtonText,
                                         boolean cancelable, boolean isDestructive,
                                         @Nullable DialogInterface.OnClickListener positiveListener,
                                         @Nullable DialogInterface.OnClickListener negativeListener,
                                         DialogInterface.OnCancelListener cancelListener) {

        AlertDialog.Builder builder;
        if (isDestructive) {

            TypedArray a = context.obtainStyledAttributes(R.styleable.BaseActivity);
            int style = a.getResourceId(R.styleable.BaseActivity_dialogDestructiveButton, 0);
            a.recycle();

            builder = new AlertDialog.Builder(context, style);
        } else {
            builder = new AlertDialog.Builder(context);
        }

        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable);

        if (positiveListener != null) {
            builder.setPositiveButton(okButtonText, positiveListener);
        } else if (negativeListener == null) {
            builder.setNegativeButton(okButtonText, (dialogInterface, i) -> dialogInterface.dismiss());
        }

        if (negativeListener != null) {
            builder.setNegativeButton(cancelButtonText, negativeListener);
        }

        builder.setOnCancelListener(cancelListener);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        return alertDialog;
    }
}
